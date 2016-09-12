package com.veggkart.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.veggkart.android.R;
import com.veggkart.android.util.APIHelper;
import com.veggkart.android.util.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

  private AppCompatEditText editTextUsername;
  private AppCompatEditText editTextPassword;
  private AppCompatTextView textViewForgotPassword;
  private AppCompatButton buttonSignIn;
  private AppCompatButton buttonSignUp;
  private TextView textViewSkip;

  private ProgressDialog progressDialog;
  /**
   * ATTENTION: This was auto-generated to implement the App Indexing API.
   * See https://g.co/AppIndexing/AndroidStudio for more information.
   */
  private GoogleApiClient client;

  public static void launchActivity(AppCompatActivity currentActivity) {
    Intent intent = new Intent(currentActivity, SignInActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    currentActivity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.initialize();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
  }

  private void initialize() {
    if (UserHelper.getUserId(this) != null) {
      CatalogueActivity.launchActivity(this);
    } else {
      setContentView(R.layout.activity_sign_in);

      ActionBar actionBar = this.getSupportActionBar();

      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(this.getResources().getString(R.string.app_name));
      }

      this.editTextUsername = (AppCompatEditText) this.findViewById(R.id.editText_signIn_username);
      this.editTextPassword = (AppCompatEditText) this.findViewById(R.id.editText_signIn_password);
      this.textViewForgotPassword = (AppCompatTextView) this.findViewById(R.id.textView_signIn_forgotPassword);
      this.buttonSignIn = (AppCompatButton) this.findViewById(R.id.button_signIn_signIn);
      this.buttonSignUp = (AppCompatButton) this.findViewById(R.id.button_signIn_signUp);
      this.textViewSkip = (TextView) this.findViewById(R.id.button_skip);

      this.textViewSkip.setOnClickListener(this);
      this.textViewForgotPassword.setOnClickListener(this);
      this.buttonSignIn.setOnClickListener(this);
      this.buttonSignUp.setOnClickListener(this);
    }
  }

  @Override
  public void onClick(View view) {
    int viewId = view.getId();

    switch (viewId) {
      case R.id.textView_signIn_forgotPassword:
        this.forgotPassword();
        break;
      case R.id.button_signIn_signIn:
        this.signIn();
        break;
      case R.id.button_signIn_signUp:
        this.signUp();
        break;
      case R.id.button_skip:
        this.skip_listener();
        break;
    }
  }

  private void skip_listener(){CatalogueActivity.launchActivity(this);}

  private void forgotPassword() {
    //ToDo: Implement actual logic
    Toast.makeText(SignInActivity.this, "The functionality is yet to come", Toast.LENGTH_SHORT).show();
  }

  private void signIn() {
    String username = this.editTextUsername.getText().toString().trim();
    String password = this.editTextPassword.getText().toString().trim();

    boolean isInputValid = true;

    if (!username.matches("^[a-zA-Z0-9]+$")) {
      isInputValid = false;
      this.editTextUsername.setError("Invalid username");
    }

    if (!password.matches("^.{6,}$")) {
      isInputValid = false;
      this.editTextPassword.setError("Invalid password");
    }

    if (isInputValid) {
      if (this.progressDialog != null && this.progressDialog.isShowing()) {
        this.progressDialog.dismiss();
      }
      this.progressDialog = new ProgressDialog(this);
      this.progressDialog.setIndeterminate(true);
      this.progressDialog.setTitle("VegGKart");
      this.progressDialog.setMessage("Signing In...");
      this.progressDialog.show();

      APIHelper.userSignIn(username, password, this, this, this);
    }
  }

  private void signUp() {
    SignUpActivity.launchActivity(this);
  }

  @Override
  public void onResponse(JSONObject response) {
    if (this.progressDialog != null && this.progressDialog.isShowing()) {
      this.progressDialog.dismiss();
    }

    try {
      String userId = response.getString("key");

      if (userId != null && !userId.equals("null")) {
        String username = this.editTextUsername.getText().toString().trim();

        UserHelper.storeUserId(userId, this);
        UserHelper.storeUsername(username, this);

        CatalogueActivity.launchActivity(this);
      } else {
        this.editTextPassword.setText("");
        this.editTextUsername.setError("Username or password invalid");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      this.onErrorResponse(new VolleyError("Corrupt network response"));
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    Log.e("SIGN-IN", (new String(error.networkResponse.data, Charset.defaultCharset())));
    Snackbar.make(this.editTextUsername, "Some error occurred\nTry again after some time", Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void onStart() throws IllegalArgumentException{
    super.onStart();

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client.connect();
    Action viewAction = Action.newAction(
            Action.TYPE_VIEW, // TODO: choose an action type.
            "SignIn Page", // TODO: Define a title for the content shown.
            // TODO: If you have web page content that matches this app activity's content,
            // make sure this auto-generated web page URL is correct.
            // Otherwise, set the URL to null.
            Uri.parse("http://host/path"),
            // TODO: Make sure this auto-generated app URL is correct.
            Uri.parse("android-app://com.veggkart.android.activity/http/host/path")
    );
//    AppIndex.AppIndexApi.start(client, viewAction);
  }

  @Override
  public void onStop() {
    super.onStop();

    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    Action viewAction = Action.newAction(
            Action.TYPE_VIEW, // TODO: choose an action type.
            "SignIn Page", // TODO: Define a title for the content shown.
            // TODO: If you have web page content that matches this app activity's content,
            // make sure this auto-generated web page URL is correct.
            // Otherwise, set the URL to null.
            Uri.parse("http://host/path"),
            // TODO: Make sure this auto-generated app URL is correct.
            Uri.parse("android-app://com.veggkart.android.activity/http/host/path")
    );
//    AppIndex.AppIndexApi.end(client, viewAction);
    client.disconnect();
  }
}
