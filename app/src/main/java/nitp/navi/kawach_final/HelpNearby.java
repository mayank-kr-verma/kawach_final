package nitp.navi.kawach_final;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;


public class HelpNearby extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_nearby);

    }

    public void activityHelp(View v){
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(findViewById(R.id.hn_fire_sl))
                .setPrimaryText("Tap to locate on map")
                .setSecondaryText("Swipe left to access instant call")
                .setPromptBackground(new RectanglePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .show();
    }

    public void layoutPolice(View v) {
        String uri = "https://www.google.co.in/maps/search/nearest+police+station/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void layoutFire(View v) {
        String uri = "https://www.google.co.in/maps/search/nearest+fire+station/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
    public void layoutHospital(View v) {
        String uri = "https://www.google.co.in/maps/search/nearest+hospital/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void callPoliceOnClick(View v) {
        String uri = "tel:100" ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void callFireOnClick(View v) {
        String uri = "tel:101" ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void callHospitalOnClick(View v) {
        String uri = "tel:102" ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

}
