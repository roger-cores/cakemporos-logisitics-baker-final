package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.RateEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.RateService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Rate;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by maitr on 31-Jul-16.
 */
public class RateCardActivity extends BaseActivity implements OnWebServiceCallDoneEventListener{
    private ImageButton home;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card);
        //find views
        home=(ImageButton)findViewById(R.id.home_img_button_rate_card);
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

        RateEndPoint endPoint = retrofit.create(RateEndPoint.class);

        RateService.getAllRates(this, retrofit, endPoint, this);

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        displayMessage(this, "Success", Snackbar.LENGTH_LONG);

        if(args.length>0) {
            List<Rate> rates = (List<Rate>) args[0];

            for(Rate rate: rates){
                switch(rate.getName()){
                    case "NORMAL 5":
                        ((TextView) findViewById(R.id.normal_5km_rate_tv)).setText("Rs. "+rate.getValue());
                        break;
                    case "NORMAL MORE":
                        ((TextView) findViewById(R.id.normal_10km_rate_tv)).setText("Rs. "+rate.getValue());
                        break;
                    case "EXPRESS 5":
                        ((TextView) findViewById(R.id.express_5km_rate_tv)).setText("Rs. "+rate.getValue());
                        break;
                    case "EXPRESS MORE":
                        ((TextView) findViewById(R.id.express_10km_rate_tv)).setText("Rs. "+rate.getValue());
                        break;
                }
            }
            //Rates are here

        }

    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, 0);
    }

    @Override
    public void onError(int message_id, int code, String... args) {
        displayError(this, message_id, Snackbar.LENGTH_LONG);
    }
}
