package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import in.cakemporos.logistics.cakemporoslogistics.R;

/**
 * Created by Maitreya on 04-Sep-16.
 */
public class ChangePasswordActivity extends AppCompatActivity{
    private ImageButton home;
    private EditText oldPassword,newPassword;
    private String oldPass,newPass;
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

        //
    }
    public void submitChangePassword(View view)
    {
        //Get old and new values in strings
        //
        oldPass=oldPassword.getText().toString();
        newPass=newPassword.getText().toString();
        //checking
        Toast.makeText(this,oldPass+" "+newPass,Toast.LENGTH_SHORT).show();
        //
        //Do webservice validations on submit
        //

        //
        //Use below code when successful and go back to previous activity
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}