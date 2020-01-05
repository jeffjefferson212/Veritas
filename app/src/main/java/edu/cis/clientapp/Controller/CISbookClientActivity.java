package edu.cis.clientapp.Controller;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import edu.cis.clientapp.Model.Constants;
import edu.cis.clientapp.Model.Request;
import edu.cis.clientapp.Model.SimpleClient;
import edu.cis.clientapp.R;

public class CISbookClientActivity extends AppCompatActivity {
    /** The address of the server that should be contacted when sending
     * any Requests. */


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
    }

    private void pingTheServer()
    {
        try {
            // Lets prepare ourselves a new request with command "ping".
            Request example = new Request("ping");
            // We are ready to send our request
            String result = SimpleClient.makeRequest(Constants.HOST, example);
            System.out.println("ping!");
            System.out.println(("result is: " + result));
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
