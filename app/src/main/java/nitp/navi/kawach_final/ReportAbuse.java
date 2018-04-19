package nitp.navi.kawach_final;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class ReportAbuse extends AppCompatActivity {

    Button ca_btn, wa_btn, loc_btn, img_btn, submit_btn;
    FrameLayout parentLayout;
    EditText det_et;
    Boolean ca, wa, ui;
    String details, location="Not Specified", uploadPath="Not Given";
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abuse);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        ca_btn = findViewById(R.id.ra_ca_btn);
        wa_btn = findViewById(R.id.ra_wa_btn);
        loc_btn = findViewById(R.id.ra_loc_btn);
        img_btn = findViewById(R.id.ra_img_btn);
        submit_btn = findViewById(R.id.ra_sub_btn);
        det_et = findViewById(R.id.ra_det_et);
        parentLayout = findViewById(R.id.parentLayout);
        ui = false;
        ca = true;
        wa = false;
        ca_btn.setBackgroundResource(R.color.turquoise);
        ca_btn.setTextColor(getResources().getColor(R.color.white));
        wa_btn.setBackgroundResource(R.color.gray);
        wa_btn.setTextColor(getResources().getColor(R.color.turquoise));

        ca_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ca = true;
                wa = false;
                ca_btn.setBackgroundResource(R.color.turquoise);
                ca_btn.setTextColor(getResources().getColor(R.color.white));
                wa_btn.setBackgroundResource(R.color.gray);
                wa_btn.setTextColor(getResources().getColor(R.color.turquoise));
            }
        });

        wa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wa = true;
                ca = false;
                wa_btn.setBackgroundResource(R.color.turquoise);
                wa_btn.setTextColor(getResources().getColor(R.color.white));
                ca_btn.setBackgroundResource(R.color.gray);
                ca_btn.setTextColor(getResources().getColor(R.color.turquoise));
            }
        });

        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double latitude = 0;
                double longitude = 0;
                for(int i=0;i<2;i++) {

                    GPSTracker gpsTracker = new GPSTracker(ReportAbuse.this);

                    if (gpsTracker.canGetLocation()) {
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();

                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
                Log.e("location","longitude: " + longitude + "\nlatitude: " + latitude);
                Toast.makeText(ReportAbuse.this,"Added Location",Toast.LENGTH_SHORT).show();

                location = String.format("https://www.google.com/maps/?q=%1$s,%2$s", latitude, longitude);

            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(det_et.getText().toString().trim().length() <= 10){
                    Toast.makeText(ReportAbuse.this, "Include more details please", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isOnline()) {
                        UserData userData = new UserData(ReportAbuse.this);
                        String phone = userData.loadPhoneNumber();
                        if (ui) {
                            uploadPath = uploadImage(phone);
                        }

                        details = det_et.getText().toString();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy/MMM/dd/HH:MM:ss");
                        Date date = new Date();

                        databaseRef = FirebaseDatabase.getInstance().getReference();
                        if (ca) {
                            databaseRef.child("Users").child(phone).child("reports").child(df.format(date)).child("Abuse Type").setValue("Child Abuse");
                            Log.e("fb", "Child Abuse");
                        } else {
                            databaseRef.child("Users").child(phone).child("reports").child(df.format(date)).child("Abuse Type").setValue("Women Abuse");
                            Log.e("fb", "Women Abuse");
                        }
                        databaseRef.child("Users").child(phone).child("reports").child(df.format(date)).child("Details").setValue(details);
                        Log.e("fb", details);
                        databaseRef.child("Users").child(phone).child("reports").child(df.format(date)).child("Location").setValue(location);
                        Log.e("fb", location);
                        databaseRef.child("Users").child(phone).child("reports").child(df.format(date)).child("Attachment").setValue(uploadPath);
                        Log.e("fb", uploadPath);

                        Toast.makeText(ReportAbuse.this, "Thanks for reporting", Toast.LENGTH_SHORT).show();
                        det_et.setText("");
                    }
                    else {
                        Toast.makeText(ReportAbuse.this, "No internet detected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public void activityHelp(View v){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(findViewById(R.id.floatingActionButton2))
                .setSecondaryText("Fill this form and report any crime you come across")
                .show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 71 && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            ui = true;
            Toast.makeText(this,"Image Added",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"No Image Selected",Toast.LENGTH_SHORT).show();

        }
    }

    private String uploadImage(String phone) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            SimpleDateFormat df =new SimpleDateFormat("yyyy/MMM/dd/HH:MM:ss");
            Date date =new Date();
            String uploadPath = "images/"+phone+"/"+df.format(date);
            StorageReference ref = storageReference.child(uploadPath);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ReportAbuse.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ReportAbuse.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            return uploadPath;
        }
        return "Not Given";
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            Log.e("exitValue",exitValue + "");
            return (exitValue == 0);

        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}
