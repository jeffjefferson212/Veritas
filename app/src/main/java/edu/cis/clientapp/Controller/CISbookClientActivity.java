package edu.cis.clientapp.Controller;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

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
    EditText textInput;
    Button deleteButton;
    Button lookUpButton;
    ImageButton accountsButton;
    Button addFriendButton;
    TextView friendListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        System.out.println("about to ping!");
        pingTheServer();
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        lookUpButton= findViewById(R.id.lookUpButton);
        accountsButton= findViewById(R.id.accountsButton);
        addFriendButton = findViewById(R.id.addFriendButton);
        friendListText =findViewById(R.id.friendListText);
        addFriendButton.setVisibility(View.GONE);

        setUpButtons();
    }

    private void setUpButtons() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text =findViewById(R.id.textInput);
                addProf(text.getText().toString());

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text =findViewById(R.id.textInput);
                deleteProf(text.getText().toString());
            }
        });
        lookUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text =findViewById(R.id.textInput);
                lookup(text.getText().toString());
            }
        });
        accountsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideAccounts();
            }
        });
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text =findViewById(R.id.textInput);
                addFriend(text.getText().toString());
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


    private void addProf(String name1) {
        String result= "";
        try {
            Request addProfile = new Request("addProfile");
            addProfile.addParam("name", name1);
            result = SimpleClient.makeRequest(Constants.HOST, addProfile);
            System.out.println("Add Profile!");
            System.out.println(("result is: " + result));
            TextView textView = findViewById(R.id.textView);
            textView.setText(name1);
            EditText Edittext= findViewById(R.id.textInput);
            Edittext.setText("");
            if (result.equals("success")){
                showHideAccounts();
                addFriendButton.setVisibility(View.VISIBLE);

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

    private void deleteProf(String name1) {
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
        TextView textView = findViewById(R.id.textView);
        textView.setText(name1);
        String text = result;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();



    }
    private void addFriend(String name1)
    {
        String result= "";
        String name2="";

        try {
            TextView textView = findViewById(R.id.textView);

            if (!textView.getText().equals("TextView")){
                name2 =textView.getText().toString();
            }
            Request addFriend = new Request("addFriend");
            addFriend.addParam("name1", name1);
            addFriend.addParam("name2", name2);
             result = SimpleClient.makeRequest(Constants.HOST, addFriend);
        }
        catch (IOException e){
            result=e.getMessage();
        }
        String text = result;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();



    }
    private void getFriends(String name){
        String result= "";
        String text = "";

        try {
            Request getFriends = new Request("getFriends");
            getFriends.addParam("name",name);
            result = SimpleClient.makeRequest(Constants.HOST, getFriends);
            TextView friendListText = findViewById(R.id.friendListText);
            friendListText.setText("Friends"+ result);
            text = "Success";
            System.out.println(result);
        }
        catch (IOException e) {
                System.out.println(e.getMessage());
                result=e.getMessage();



            }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }




    private void lookup(String name1) {
        String result= "";
        try {
            Request containsProfile = new Request("containsProfile");
            containsProfile.addParam("name", name1);
            result = SimpleClient.makeRequest(Constants.HOST, containsProfile);
            System.out.println("Contains Profile!");
            System.out.println(("result is: " + result));

            TextView textView = findViewById(R.id.lookUpDisplay);
            if (result.equals("true")) {
                result = "Displaying "+ name1;
                textView.setText(result);
                getFriends(name1);
            }
            else {
                result = "A profile with the name " + name1 + " does not exist";
                textView.setText(result);
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
