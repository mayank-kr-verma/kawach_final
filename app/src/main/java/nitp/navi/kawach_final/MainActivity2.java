package nitp.navi.kawach_final;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;


public class MainActivity2 extends AppCompatActivity {

    Intent intent;
    SharedPreferences Pref;
    private BoomMenuButton bmb;
    private int[] options = new int[5];
    private int[] drawRes = new int[5];
    String txt = "Help! \n I'm stuck and need assistance at \n ";
    String listOfTrackers;
    String[] userInfo;
    boolean showHelp;
    ImageView alert_h_btn, alert_m_btn, alert_l_btn, alert_h_bg, alert_m_bg, alert_l_bg, dismiss_btn;
    RelativeLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        UserData userData = new UserData(this);
        showHelp = userData.isFirstTimeLoad();

        parentLayout = findViewById(R.id.parentLayout);

        if (Build.VERSION.SDK_INT >= 23){
            askForPermissions();
        }

        //sos button code below

        dismiss_btn = findViewById(R.id.dismiss_iv);

        alert_l_btn = (ImageView)findViewById(R.id.alert_l_iv);
        alert_m_btn = (ImageView)findViewById(R.id.alert_m_iv);
        alert_h_btn = (ImageView)findViewById(R.id.alert_h_iv);

        alert_l_bg = (ImageView)findViewById(R.id.alert_l_iv_b);
        alert_m_bg = (ImageView)findViewById(R.id.alert_m_iv_b);
        alert_h_bg = (ImageView)findViewById(R.id.alert_h_iv_b);

        alert_h_btn.setVisibility(View.VISIBLE);
        alert_m_btn.setVisibility(View.GONE);
        alert_l_btn.setVisibility(View.GONE);
        dismiss_btn.setVisibility(View.GONE);

        alert_h_bg.setVisibility(View.VISIBLE);
        alert_m_bg.setVisibility(View.GONE);
        alert_l_bg.setVisibility(View.GONE);

        if (showHelp){

            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(findViewById(R.id.alert_l_iv))
                    .setPrimaryText("Tap on bell icon to send SOS message")
                    .setSecondaryText("Message will be sent to listed emergency contacts")
                    .setPromptBackground(new RectanglePromptBackground())
                    .setPromptFocal(new RectanglePromptFocal())
                    .show();

        }

        setback();


        alert_l_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                magicFunction();

            }
        });

        alert_m_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                magicFunction();

            }
        });

        alert_h_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                magicFunction();

            }
        });

        dismiss_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss_btn.setVisibility(View.GONE);
                Toast.makeText(MainActivity2.this,"Help dismissal message sent",Toast.LENGTH_SHORT).show();
                String message = "I am safe now. Thanks for your concern";

                UserData userData= new UserData(MainActivity2.this);
                Pref = userData.getPref();

                listOfTrackers =Pref.getString("listOfTrackers","empty");

                if (!listOfTrackers.equals("empty")){
                    userInfo = listOfTrackers.split("%");
                    int i=0;
                    while(i<userInfo.length){
                        Log.e("dismissTxtCheck","sending message to : "+ userInfo[i+1]);
                        sendSMS(userInfo[i], "", message);
                        i += 2;
                    }
                }

            }
        });


        //boom button code below

        options[0]=R.string.title_help_nearby;
        options[1]=R.string.title_child_tracker;
        options[2]=R.string.title_emergency_contacts;
        options[3]=R.string.title_report_abuse;
        options[4]=R.string.title_about;

        drawRes[0]=R.drawable.ic_menu_help_nearby;
        drawRes[1]=R.drawable.ic_menu_child_tracker;
        drawRes[2]=R.drawable.ic_menu_emergency_contacts;
        drawRes[3]=R.drawable.ic_menu_report_abuse;
        drawRes[4]=R.drawable.ic_menu_features;

        bmb = findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_5);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder()
                    .normalTextRes(options[i])
                    .normalImageRes(drawRes[i])
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textSize(20)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if (index==0){
                                intent = new Intent(MainActivity2.this, HelpNearby.class);
                                startActivity(intent);
                            };
                            if (index==1){
                                intent = new Intent(MainActivity2.this, MainActivity.class);
                                startActivity(intent);
                            };
                            if (index==2){
                                intent = new Intent(MainActivity2.this, MyTrackers.class);
                                startActivity(intent);
                            };
                            if (index==3){
                                intent = new Intent(MainActivity2.this, ReportAbuse.class);
                                startActivity(intent);
                            };
                            if (index==4){
                                intent = new Intent(MainActivity2.this, About.class);
                                startActivity(intent);
                            }
                        }
                    });

            bmb.addBuilder(builder);
        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private void magicFunction(){

    			double latitude = 0;
                double longitude = 0;
                for(int i=0;i<2;i++) {

                    GPSTracker gpsTracker = new GPSTracker(MainActivity2.this);

                    if (gpsTracker.canGetLocation()) {
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();

                    } else {
                        gpsTracker.showSettingsAlert();
                    }
                }
                Log.d("location","longitude: " + longitude + "\nlatitude: " + latitude);

                String message = String.format("https://www.google.com/maps/?q=%1$s,%2$s", latitude, longitude);

                UserData userData= new UserData(MainActivity2.this);
                Pref = userData.getPref();

                listOfTrackers =Pref.getString("listOfTrackers","empty");

                if (!listOfTrackers.equals("empty")){

                    dismiss_btn.setVisibility(View.VISIBLE);
                    userInfo = listOfTrackers.split("%");
                    int i=0;
                    while(i<userInfo.length){
                        Log.e("txtCheck","sending message to : "+ userInfo[i+1]);
                        Toast.makeText(MainActivity2.this,  "sending message to : "+ userInfo[i+1], Toast.LENGTH_SHORT).show();
                        sendSMS(userInfo[i], txt, message);
                        i += 2;
                    }
                }
                else{
                    Toast.makeText(MainActivity2.this,  "No Emergency Contact Found", Toast.LENGTH_SHORT).show();

                }
    }

    private void sendSMS(String phoneNumber, String txt ,String message) {
        SmsManager sms = SmsManager.getDefault();
        StringBuffer smsBody = new StringBuffer();
        smsBody.append(txt);
        smsBody.append(message);
        sms.sendTextMessage(phoneNumber, null, smsBody.toString(), null, null);
        Log.e("sms",smsBody.toString() + " " + phoneNumber);
    }

    private void setback(){
        double latitude = 0;
        double longitude = 0;
        for(int i=0;i<2;i++) {

            GPSTracker gpsTracker = new GPSTracker(MainActivity2.this);

            if (gpsTracker.canGetLocation()) {
                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                //   Snackbar.make(view, "longitude: "+longitude+"\nlatitude: "+latitude, Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gpsTracker.showSettingsAlert();
            }
        }


        int crime = findCrime(getAddress(this, longitude, latitude));
        if (crime<268){
            Log.e("crime","low");
            alert_h_btn.setVisibility(View.GONE);
            alert_m_btn.setVisibility(View.GONE);
            alert_l_btn.setVisibility(View.VISIBLE);

            alert_h_bg.setVisibility(View.GONE);
            alert_m_bg.setVisibility(View.GONE);
            alert_l_bg.setVisibility(View.VISIBLE);

        }
        else if (crime<800){
            Log.e("crime","mid");
            alert_h_btn.setVisibility(View.GONE);
            alert_m_btn.setVisibility(View.VISIBLE);
            alert_l_btn.setVisibility(View.GONE);

            alert_h_bg.setVisibility(View.GONE);
            alert_m_bg.setVisibility(View.VISIBLE);
            alert_l_bg.setVisibility(View.GONE);

        }
        else {
            Log.e("crime", "high");
            alert_h_btn.setVisibility(View.VISIBLE);
            alert_m_btn.setVisibility(View.GONE);
            alert_l_btn.setVisibility(View.GONE);

            alert_h_bg.setVisibility(View.VISIBLE);
            alert_m_bg.setVisibility(View.GONE);
            alert_l_bg.setVisibility(View.GONE);

        }
    }

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {


                String city = addresses.get(0).getLocality();


                Log.e("TAG", "getAddress:  city " + city);

                return city;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Agra";
    }

    private int findCrime(String testdistrict) {

        int temp = 1000;
        JsonHandler con = new JsonHandler(this);
        JSONArray jsonArray = con.reads("crime_record.json",this);
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject jobject = null;
            try {
                jobject = jsonArray.getJSONObject(j);
                String  district = jobject.getString("District");

                if(district.contains(testdistrict)){
                    Log.e("dist", district);
                    temp = jobject.getInt("Crimes");
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("catch3","c");
            }
        }
        Log.e("temp",temp+"");
        return temp;
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void askForPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Contacts");
        if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("SMS");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                Toast.makeText(MainActivity2.this, message, Toast.LENGTH_SHORT).show();
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for permissions
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity2.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
