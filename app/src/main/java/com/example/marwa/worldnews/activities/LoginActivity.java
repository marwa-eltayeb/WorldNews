package com.example.marwa.worldnews.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.marwa.worldnews.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class LoginActivity extends AppCompatActivity {

    //Declaring Twitter loginButton object
    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing twitter instance
        Twitter.initialize(this);
        setContentView(R.layout.activity_login);

        //Instantiating loginButton
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setBackgroundColor(Color.RED);

          /*
          Adding a callback to loginButton
          These statements will execute when loginButton is clicked
         */
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                /*
                  This provides TwitterSession as a result
                  This will execute when the authentication is successful
                 */
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                // Calling login method and passing twitter session
                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // Displaying Toast message
                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();
            }
        });


        // If the user logged in, disable going back to the loginActivity
        try{
            TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
            String username = session.getUserName();
            if(!username.isEmpty()){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }catch (NullPointerException e){
            Log.v("LoginActivity","No Username");
        }
    }

    /**
     * @param session
     * This method will get username using session and start a new activity where username will be displayed
     */
    public void login(TwitterSession session)
    {
        String username = session.getUserName();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * @param requestCode - we'll set it to REQUEST_CAMERA
     * @param resultCode - this will store the result code
     * @param data - data will store an intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}
