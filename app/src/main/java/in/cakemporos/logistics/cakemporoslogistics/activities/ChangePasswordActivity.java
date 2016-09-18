package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by Maitreya on 04-Sep-16.
 */
public class ChangePasswordActivity extends BaseActivity implements OnWebServiceCallDoneEventListener{
    private ImageButton home;
    private EditText oldPassword,newPassword;
    private String oldPass,newPass;

    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_change_password);
        oldPassword=(EditText) findViewById(R.id.old_password_cp);
        newPassword=(EditText) findViewById(R.id.new_password_cp);
        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //
    }
    public void submitChangePassword(View view)
    {
        //Get old and new values in strings
        //
        oldPass=oldPassword.getText().toString();
        newPass=newPassword.getText().toString();
        //checking
        //
        //Do webservice validations on submit
        //
        AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);
        AuthenticationService.changePassword(this, retrofit, endPoint, this.getIntent().getExtras().getString("email_baker"), oldPass, newPass, this);


    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        displayMessage(this, "Success", Snackbar.LENGTH_LONG);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, Snackbar.LENGTH_LONG);
    }


    @Override
    public void onError(int message_id, int code, String args[]) {
        displayError(this, message_id, Snackbar.LENGTH_LONG, args);
    }
}