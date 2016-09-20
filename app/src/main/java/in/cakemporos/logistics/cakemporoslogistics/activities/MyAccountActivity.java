package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by maitr on 14-Aug-16.
 */
public class MyAccountActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    private ImageButton home;
    private TextView email_baker,address_baker,phone_baker;
    private Retrofit retrofit;

    LinearLayout myAccountContainer;
    RelativeLayout progressBarContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        myAccountContainer = (LinearLayout) findViewById(R.id.myAccountContainer);
        progressBarContainer = (RelativeLayout) findViewById(R.id.progressBar);

        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_my_account);
        email_baker=(TextView)findViewById(R.id.email_baker_ma);
        address_baker=(TextView)findViewById(R.id.address_baker_ma);
        phone_baker=(TextView)findViewById(R.id.phone_baker_ma);
        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);

        AuthenticationService.getMyInfo(this, retrofit, endPoint, this);

        showProgress();

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        //displayMessage(this, "Success", Snackbar.LENGTH_LONG);
        hideProgress();
        if(args.length>0) {
            Baker baker = (Baker) args[0];

            //here is baker info
            phone_baker.setText(Long.toString(baker.getUser().getPhone()));
            email_baker.setText(baker.getUser().getEmail());
            address_baker.setText(baker.getAddress());
        }

    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, 0);
        hideProgress();
    }

    @Override
    public void onError(int message_id, int code, String... args) {
        displayError(this, message_id, Snackbar.LENGTH_LONG);
        hideProgress();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Toast.makeText(this,"Password successfully updated",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void changePassword(View view)
    {
        Intent intent=new Intent(this,ChangePasswordActivity.class);
        intent.putExtra("email_baker", email_baker.getText());
        startActivityForResult(intent,1);
    }

    public void logout(View view){
        AuthenticationService.logout(this);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void hideProgress(){
        myAccountContainer.setVisibility(View.VISIBLE);
        progressBarContainer.setVisibility(View.GONE);
    }

    private void showProgress(){
        myAccountContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

}
