package com.zakaria.dynamiclink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class MainActivity extends AppCompatActivity {
    TextView createLink, shareLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createLink = findViewById(R.id.textView_createlink);
        shareLink = findViewById(R.id.textView_sharlink);


        createLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createReferLink();
            }
        });




        shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharReferLink();

            }
        });

    }




    private void sharReferLink() {
    }



    private void createReferLink() {
        Log.e("Main", "create link ");

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.androwep.com/"))//ami je link ta dite sai

                .setDynamicLinkDomain("zakaria.page.link")//firebase base link
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        Log.e("main", "Long reffer "+dynamicLink.getUri());

        //https://https%3A//zakaria.page.link?apn=com.zakaria.dynamiclink&ibi=com.example.ios&link=https%3A%2F%2Fwww.androwep.com%2F
        //apn ibi link

        createReferallink("zakaria123", "pro123");


    }


    public void createReferallink(String custid, String prodid){

        //menual link
        String shareLinktext = "https://zakaria.page.link/?"+
                "link=https://www.bncodeing.com/cust_id="+custid+"-"+prodid+
                "&apn="+getPackageName()+
                "&st="+"My Refer Link"+ //title
                "&sd="+"Reward Coin 20"+//description
                "&si="+"https://androwep.com/wp-content/uploads/2019/09/androwep-logo-.png";//logo apps



        Log.e("mainactivity", "sharelink - "+shareLinktext);
        //short link code

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(shareLinktext)) //menual link parce for short

                //.setLongLink(dynamicLink.getUri()) //fixed link parce for short
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main", "short link= "+shortLink);
//step-1 = click ->
//step-2 = link ->
//step-3 =  google play store ->
//step-4 = instal or not? open directly:install apps;


                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.setType("text/plain");
                            startActivity(intent);
                        } else {
                            // Error
                            // ...
                            Log.e("main", "error"+task.getException());
                        }
                    }
                });
    }
}