package com.zakaria.dynamiclink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SplashActivity extends AppCompatActivity {

    TextView textViewWelcome;
    String TAG = "splash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //for do something first time
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if(firstStart){
            showStartDialog();
        }
        //end for do something first time




        textViewWelcome = findViewById(R.id.textView_welcome);
        Button Btn_Reffer = (Button) findViewById(R.id.button_open_activity_id);
        Btn_Reffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //for detect dynamic link
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            Log.e(TAG, "my referLink = "+deepLink.toString());


                            //we will get this link
                            // https://www.bncodeing.com/cust_id=cust123-prod345"

                            String refferlinkGet = deepLink.toString();
                            try {
                                refferlinkGet = refferlinkGet.substring(refferlinkGet.lastIndexOf("=")+1);
                                Log.e(TAG, "===substring = "+refferlinkGet);//cust123-prod345

                                String custid = refferlinkGet.substring(0, refferlinkGet.indexOf("-"));
                                String prodid = refferlinkGet.substring(refferlinkGet.indexOf("-")+1);

                                Log.e(TAG, "===cust_id = "+custid + "--------- ProductId ==== "+prodid );

                                Toast.makeText(SplashActivity.this, "cust id = "+custid+"podduct id = "+prodid, Toast.LENGTH_LONG).show();

                                //shareprefarace for save data

                                SharedPreferences prefs = getSharedPreferences("prefss", MODE_PRIVATE);
                                String customarid = prefs.getString("custid", custid);
                                String productdid = prefs.getString("prodid", prodid);


                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("custid", custid);
                                textViewWelcome.setText(custid);






                            }catch (Exception e){
                                Log.e(TAG, ""+e.getMessage());
                            }
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "getDynamicLink:onFailure", e);
                    }
                });

    }


    //only first time show use this function for show somethis
    private void showStartDialog() {

        //layout pass Alertdialog

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        // ...Irrelevant code for customizing the buttons and title
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
//        dialogBuilder.setView(dialogView);
//        EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
//        editText.setText("test label");
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();


        //end layout pass Alertdialog

        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("One Time Dialog")
                .setMessage("This should only be shown once ")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();

    }
    //only first time show use this function for show somethis
}