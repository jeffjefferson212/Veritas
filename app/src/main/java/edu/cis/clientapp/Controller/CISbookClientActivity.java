package edu.cis.clientapp.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private static  int SPLASHSCREENTIMER=5000;
    /**
     * The address of the server that should be contacted when sending
     * any Requests.
     */

        //Home page buttons
    WebView webView;
    Button scanButtonHome;
    Button shoesTabButton;

    //Login page buttons
    Button returnHomeX;
    //scan page buttons
    Button testFake;
    Button testReal;
    TextView randoNum;
    EditText sigInput;
    EditText adminPassword;
    ImageView verifiedTick;
    ImageView FakeX;
    Button returnHomeXScan;
    Button shoesScan;
    //Splash screen
    ImageView topimage;
    ImageView bottomImage;
    Animation topAnim;
    Animation bottomAnim;

    @Override
    // splash screen code adapted from https://www.youtube.com/watch?v=JLIFqqnSNmg
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        setupAnim();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                setContentView(R.layout.content_main);
                setUpHomepage();

            }
        },SPLASHSCREENTIMER);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        System.out.println("about to ping!");
         pingTheServer();







         // showHideEditProfile();
    }

    private void setupAnim(){
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animantion);
        topimage = findViewById(R.id.splashtop);
        bottomImage=findViewById(R.id.splashbottom);
        topimage.setAnimation(topAnim);
        bottomImage.setAnimation(bottomAnim);
    }
    private void setupLogin()
    {
        returnHomeX = findViewById(R.id.returnHomeXButton);

        returnHomeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                setUpHomepage();
            }
        });
    }
    private void setupScan()
    {
    randoNum = findViewById(R.id.randomNumberDisplay);
    sigInput = findViewById(R.id.siginput);
    adminPassword = findViewById(R.id.adminpassword);
    testFake = findViewById(R.id.testerFake);
    shoesScan=findViewById(R.id.shoesButtonScan);
    returnHomeXScan = findViewById(R.id.homebuttonScan);
    testReal = findViewById(R.id.testerReal);
    verifiedTick = findViewById(R.id.VerifiedImage);
    FakeX = findViewById(R.id.FakeImage);
    scanClicks();
    scanHideShowAdmin();
        verifiedTick.setVisibility(View.GONE);
        FakeX.setVisibility(View.GONE);

    }

    private void scanHideShowAdmin()
    {
        if (adminPassword.getVisibility()== View.VISIBLE){
            adminPassword.setVisibility(View.GONE);
        }
        else
            {
                adminPassword.setVisibility(View.VISIBLE);
            }

        if (testFake.getVisibility()== View.VISIBLE){
            testFake.setVisibility(View.GONE);
        }
        else
        {
            testFake.setVisibility(View.VISIBLE);
        }

        if (testReal.getVisibility()== View.VISIBLE){
            testReal.setVisibility(View.GONE);
        }
        else
        {
            testReal.setVisibility(View.VISIBLE);
        }
    }
    private void scanClicks()
    {
        testFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        testReal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        shoesScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.login);
                setupLogin();
            }
        });
        returnHomeXScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                setUpHomepage();
            }
        });

    }

    private void setUpHomepage() {


        shoesTabButton = findViewById(R.id.shoesButton);
        webView=findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://sneakernews.com/");
        scanButtonHome = findViewById(R.id.scanButton);
        shoesTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.login);
                setupLogin();

            }
        });
        scanButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.scan);
                setupScan();

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
    private boolean verifySigMessage (byte[] signature, String message,String serialNum) throws GeneralSecurityException, IOException {
        char[] Array =message.toCharArray();



        Signature sig2 = Signature.getInstance("SHA256withRSA");
        PublicKey publickey = loadPublicKey(getpublickey(serialNum));

        sig2.initVerify(publickey);
        for (char c : message.toCharArray())
        {
            byte b = (byte) c;
            sig2.update(b);
        }

       boolean valid = sig2.verify(signature);
        System.out.println("valid? " + valid);
        if (valid)
        {
            return true;
        }
        return false;
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
