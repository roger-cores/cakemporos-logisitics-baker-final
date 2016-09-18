package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import in.cakemporos.logistics.cakemporoslogistics.R;

public class AppVersionActivity extends BaseActivity {
    private ImageButton home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_version);
        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_app_version);
        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
