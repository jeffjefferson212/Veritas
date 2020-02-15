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
    EditText ageDisplay;
    TextView ageTag;
    EditText genderDisplay;
    TextView genderTag;
    TextView friendsTag;
    Button saveProfileButton;
    Button editProfileButton;

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
        editProfileButton = findViewById(R.id.editProfileButton);
        addFriendButton = findViewById(R.id.addFriendButton);
        friendListText =findViewById(R.id.friendListText);
        addFriendButton.setVisibility(View.GONE);
        ageDisplay=findViewById(R.id.ageDisplay);
        ageTag=findViewById(R.id.ageTag);
        genderDisplay=findViewById(R.id.genderDisplay);
        genderTag=findViewById(R.id.genderTag);
        saveProfileButton =findViewById(R.id.saveProfileButton);
        friendsTag=findViewById(R.id.friendsTag);
        setUpButtons();
        showHideEditProfile();
    }

    private void setUpButtons()
    {
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
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
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
            TextView textView = findViewById(R.id.accountName);
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
    private String getAge(String name)
    {
        String result ="";
        try
        {
            Request getAge = new Request("getAge");

            getAge.addParam("name", name);

            result = SimpleClient.makeRequest(Constants.HOST, getAge);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            result=e.getMessage();
        }
        return result;
    }
    private boolean setAge(String name,String age)
    {
        String result= "";

        try {
            TextView textView = findViewById(R.id.accountName);


            Request setAge= new Request("addFriend");
            setAge.addParam("age", age);
            setAge.addParam("name", name);
            result = SimpleClient.makeRequest(Constants.HOST, setAge);
            return true;
        }
        catch (IOException e){
            result=e.getMessage();
            return false;
        }




    }
    private boolean setGender(String name, String gender)
    {
        String result= "";

        try
        {
            Request setGender= new Request("setGender");
            setGender.addParam("gender", gender);
            setGender.addParam("name", name);
            result = SimpleClient.makeRequest(Constants.HOST, setGender);
            return true;
        }
        catch (IOException e){
            result=e.getMessage();
            return false;
        }




    }

    private String getGender(String name)
    {
        String result ="";
        try
        {
            Request getGender = new Request("getGender");
            getGender.addParam("name", name);
            result = SimpleClient.makeRequest(Constants.HOST, getGender);

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            result=e.getMessage();
        }
        return result;

    }


    private void addFriend(String name1)
    {
        String result= "";
        String name2="";

        try {
            TextView textView = findViewById(R.id.accountName);

            if (!textView.getText().equals("Account Name")){
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
    private void saveProfile(){
        String text = "Profile saved";





            TextView accountName = findViewById(R.id.accountName);
            String name=accountName.toString();
            TextView age=findViewById(R.id.ageDisplay);
            String age2=age.getText().toString();
            TextView gender=findViewById(R.id.genderDisplay);
            String gender2=gender.getText().toString();
            if (setAge(name,age2)&& setGender(name,gender2)){
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                showHideEditProfile();

            }



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

                EditText age = findViewById(R.id.ageDisplay);
                EditText gender = findViewById(R.id.genderDisplay);
                age.setText(getGender(name1));
                gender.setText(getGender(name1));
                showHideEditProfile();
                saveProfileButton.setVisibility(View.GONE);

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
    public void editProfile()
    {
        EditText age = findViewById(R.id.ageDisplay);
        EditText gender = findViewById(R.id.genderDisplay);
        TextView name = findViewById(R.id.accountName);
        String name2=name.getText().toString();
        age.setText(getAge(name2));
        gender.setText(getGender(name2));
        showHideEditProfile();
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
        if(ageTag.getVisibility()==View.VISIBLE)
        {
            ageTag.setVisibility(View.GONE);
        }
        else if(ageTag.getVisibility()==View.GONE)
        {
            ageTag.setVisibility(View.VISIBLE);
        }

        if(ageDisplay.getVisibility()==View.VISIBLE)
        {
            ageDisplay.setVisibility(View.GONE);
        }
        else if(ageDisplay.getVisibility()==View.GONE)
        {
            ageDisplay.setVisibility(View.VISIBLE);
        }

        if(genderDisplay.getVisibility()==View.VISIBLE)
        {
            genderDisplay.setVisibility(View.GONE);
        }
        else if(genderDisplay.getVisibility()==View.GONE)
        {
            genderDisplay.setVisibility(View.VISIBLE);
        }

        if(genderTag.getVisibility()==View.VISIBLE)
        {
            genderTag.setVisibility(View.GONE);
        }
        else if(genderTag.getVisibility()==View.GONE)
        {
            genderTag.setVisibility(View.VISIBLE);
        }
        if(friendsTag.getVisibility()==View.VISIBLE)
        {
            friendsTag.setVisibility(View.GONE);
        }
        else if(friendsTag.getVisibility()==View.GONE)
        {
            friendsTag.setVisibility(View.VISIBLE);
        }
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
