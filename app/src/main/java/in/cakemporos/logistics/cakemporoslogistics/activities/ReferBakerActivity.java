package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
public class ReferBakerActivity extends BaseActivity implements OnWebServiceCallDoneEventListener{
    private ImageButton home;

    private Retrofit retrofit;

    private LinearLayout referalContainer;
    private RelativeLayout progressBarContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_baker);

        referalContainer = (LinearLayout) findViewById(R.id.referalContainer);
        progressBarContainer = (RelativeLayout) findViewById(R.id.progressBar);

        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_refer_baker);
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
            //here is baker info with referal

            ((TextView) findViewById(R.id.refer_code_rb)).setText(baker.getReferal());


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

    private void hideProgress(){
        referalContainer.setVisibility(View.VISIBLE);
        progressBarContainer.setVisibility(View.GONE);
    }

    private void showProgress(){
        referalContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }
}
