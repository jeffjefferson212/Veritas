package edu.cis.clientapp.Controller;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import edu.cis.clientapp.Model.Constants;
import edu.cis.clientapp.Model.Request;
import edu.cis.clientapp.Model.SimpleClient;
import edu.cis.clientapp.R;

public class CISbookClientActivity extends AppCompatActivity {
    /**
     * The address of the server that should be contacted when sending
     * any Requests.
     */
    Button addButton;

    Button deleteButton;

    ImageButton accountsButton;
    Button saveProfileButton;
    Button editProfileButton;
    WebView webView;
    Button shoesTabButton;
    Button returnHomeX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        System.out.println("about to ping!");
        // pingTheServer();
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        accountsButton = findViewById(R.id.accountsButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        shoesTabButton = findViewById(R.id.shoesButton);
        webView=findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://sneakernews.com/");

        setUpButtons();
         // showHideEditProfile();
    }


    private void setupLogin()
    {
        returnHomeX= findViewById(R.id.returnHomeXButton);
        returnHomeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });
    }
    private void setUpButtons()
    {

        shoesTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.login);
                setupLogin();

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }


        });


    }



    private void pingTheServer() {
        try {
            // Lets prepare ourselves a new request with command "ping".
            Request example = new Request("ping");
            // We are ready to send our request
            String result = SimpleClient.makeRequest(Constants.HOST, example);
            System.out.println("ping!");
            System.out.println(("result is: " + result));
            String text = result;
            //Toast code taken and adapted from: https://developer.android.com/guide/topics/ui/notifiers/toasts
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    private String getpublickey(String serialnumber) {
        try {
            String serialtag =  "";
            for (int i=0; i<10 ;i ++ ){
               String num=Integer.toString(i);
                 serialtag =serialnumber.replace(num ,"");
            }
            System.out.println(serialtag);
            String num = serialnumber.replace(serialtag, "");
            System.out.println(num);


            // Lets prepare ourselves a new request with command "ping".
            Request getPublicKey = new Request("getPublicKey");
            getPublicKey.addParam("serialtag",serialtag);
            getPublicKey.addParam("serialnum",num);
            // We are ready to send our request
            String result = SimpleClient.makeRequest(Constants.HOST, getPublicKey);

            System.out.println(("result is: " + result));
            String text = "public key received";
            //Toast code taken and adapted from: https://developer.android.com/guide/topics/ui/notifiers/toasts
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return result;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
    //Check replaced Key with public key line 155
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException, IOException
    {
        byte[] data = Base64.getDecoder().decode((stored.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

    }
    private boolean verifySigMessage (String signedMessage, String serialNum) throws GeneralSecurityException, IOException {
        char[] Array =signedMessage.toCharArray();



        Signature sig2 = Signature.getInstance("SHA256withRSA");
        PublicKey publickey = loadPublicKey(getpublickey(serialNum));
        String message = signedMessage;
        sig2.initVerify(publickey);
        for (char c : message.toCharArray())
        {
            byte b = (byte) c;
            sig2.update(b);
        }
       // boolean valid = sig2.verify(messageSignature);
    //    System.out.println("valid? " + valid);
        return false;
    }


    private void addProf(String name1) {
        String result= "";
        try {
            Request addProfile = new Request("addProfile");
            addProfile.addParam("name", name1);
            result = SimpleClient.makeRequest(Constants.HOST, addProfile);
            System.out.println("Add Profile!");
            System.out.println(("result is: " + result));
            TextView textView = findViewById(R.id.accountName);
            textView.setText(name1);


            if (result.equals("success")){
                showHideAccounts();


            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            result=e.getMessage();

        }
        String text = result;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    private void deleteProf(String name1)
    {
        String result= "";
        try {
            Request deleteProfile = new Request("deleteProfile");
            deleteProfile.addParam("name", name1);
             result = SimpleClient.makeRequest(Constants.HOST, deleteProfile);
            System.out.println("DeleteProfile!");
            System.out.println(("result is: " + result));

        } catch (IOException e) {
            System.out.println(e.getMessage());
            result=e.getMessage();
        }
        TextView textView = findViewById(R.id.accountName);
        textView.setText(name1);
        String text = result;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();




    }











    private void showHideAccounts(){
        if(addButton.getVisibility()==View.VISIBLE)
        {
        addButton.setVisibility(View.GONE);
        }
        else if(addButton.getVisibility()==View.GONE)
        {
            addButton.setVisibility(View.VISIBLE);
        }
        if(deleteButton.getVisibility()==View.VISIBLE)
        {
            deleteButton.setVisibility(View.GONE);
        }
        else if(deleteButton.getVisibility()==View.GONE)
        {
            deleteButton.setVisibility(View.VISIBLE);
        }
        if(editProfileButton.getVisibility()==View.VISIBLE)
        {
            editProfileButton.setVisibility(View.GONE);
        }
        else if(editProfileButton.getVisibility()==View.GONE)
        {
            editProfileButton.setVisibility(View.VISIBLE);
        }
    }
    private void showHideEditProfile()

    {

        if(saveProfileButton.getVisibility()==View.VISIBLE)
        {
            saveProfileButton.setVisibility(View.GONE);
        }
        else if(saveProfileButton.getVisibility()==View.GONE)
        {
            saveProfileButton.setVisibility(View.VISIBLE);
        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }
