package gr.uom.adroid.mylocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {

    private static TextView cordsTXT,iptxt;
    EditText nameTXT;
    IPhandler ip;
    URLconection urlGetCords;
    private static String ipconection,incomeLNG,incomeLAT;
    String user_id;
    LocationsBD dbLocation;
    String acceess="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        cordsTXT = findViewById(R.id.cordsTXT);
        nameTXT = findViewById(R.id.nameTXT);
        iptxt = findViewById(R.id.ipTXT);

        Intent intent = getIntent();
        acceess = intent.getStringExtra("ACCESS");
        user_id = intent.getStringExtra("ID");

        Log.d("KAPPA", "favoriteLocations on create: "+acceess);
        dbLocation = new LocationsBD(this);
        ip = new IPhandler();
        ip.execute();

    }

    public void myLocationsBTN(View v){
        Log.d("KAPPA", "BUTTON PRESSED");

        Intent intent = new Intent(MainPageActivity.this,LocationsActivity.class);
        intent.putExtra("ACCESS",acceess);
        intent.putExtra("ID",user_id);

        Log.d("KAPPA", "favBTNid " +user_id);
        intent.putExtra("ID",user_id);
        startActivity(intent);
    }

    public void favoriteLocationsBTN(View v){

        if(acceess.equals("true")) {
            Intent LocationsIntent = new Intent(MainPageActivity.this, FavoriteLocationsActivity.class);
            LocationsIntent.putExtra("ID",user_id);
            startActivity(LocationsIntent);
        }
        else {
            Toast.makeText(MainPageActivity.this, "YOU NEED TO CONECT WITH EMAIL",Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocationBTN(View v){
        urlGetCords = new URLconection(ipconection);
        urlGetCords.execute();
    }

    public void SaveLocationBTN(View v){
        //NEED TO GIVE A NAME TO LOCATION.

        String incomeName = nameTXT.getText().toString();
        boolean checkForGabsInName = false;
        int spaceCount = 0;
        for (char c : incomeName.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
            }
        }
        if(spaceCount == 0){
            checkForGabsInName =true;
        }
        if(checkForGabsInName) {
            if(incomeLAT == null || incomeLNG == null){
                Toast.makeText(MainPageActivity.this,"Plaese press the button to get cords",Toast.LENGTH_SHORT).show();
            }
            else {
                if(incomeName.equals(null)|| incomeName.equals("")){
                    Toast.makeText(MainPageActivity.this, "Give a name", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean isInserted = dbLocation.insertData(incomeName, incomeLAT.toString(), incomeLNG.toString());

                    if (isInserted == true) {
                        Toast.makeText(MainPageActivity.this, "Succesfully saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainPageActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else{
            Toast.makeText(MainPageActivity.this,"Please do not put spaces",Toast.LENGTH_SHORT).show();
        }
    }

    public void setIP(String astring){
        ipconection = astring;
        iptxt.setText("IP: " + astring);
    }

    public  void setCords(String aLat,String aLng){
        incomeLAT = aLat;
        incomeLNG = aLng;
        cordsTXT.setText("Latitude: " +aLat+"\nLongitude: "+aLng);
    }

}

