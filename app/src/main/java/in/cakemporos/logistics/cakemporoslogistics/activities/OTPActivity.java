package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import in.cakemporos.logistics.cakemporoslogistics.R;

public class OTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    public void back(View view){
        finish();
    }
}
