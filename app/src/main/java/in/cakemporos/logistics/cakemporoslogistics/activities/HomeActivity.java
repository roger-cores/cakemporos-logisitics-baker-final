package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.receivers.DeregisterEventReceiver;
import in.cakemporos.logistics.cakemporoslogistics.staticvals.IntentFilters;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;

/**
 * Created by maitr on 29-Jul-16.
 */
public class HomeActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    private View book_delivery,rate_card,order_history,my_account,refer_baker,app_ver;
    private Context ctx_home=this;
    private TextView username_tv;

    Baker baker;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //get views
        book_delivery= findViewById(R.id.grid_1);
        rate_card= findViewById(R.id.grid_3);
        order_history= findViewById(R.id.grid_2);
        my_account= findViewById(R.id.grid_4);
        refer_baker= findViewById(R.id.grid_5);
        app_ver= findViewById(R.id.grid_6);
        username_tv=(TextView)findViewById(R.id.home_user_tv);

        //set onlClicks
        book_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = "", address = "", placeId = "";

                if(baker != null){
                    phone = Long.toString(baker.getUser().getPhone());
                    address = baker.getLocality().getName() + ", " + baker.getAddress();
                    placeId = baker.getLocality().getPlaceId();
                }

                Intent intent_book=new Intent(ctx_home,BookDeliveryActivity.class);
                intent_book.putExtra("phone", phone);
                intent_book.putExtra("address", address);
                intent_book.putExtra("lat", baker.getLocality().getLat());
                intent_book.putExtra("lon", baker.getLocality().getLon());
                startActivity(intent_book);
            }
        });
        rate_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_book=new Intent(ctx_home,RateCardActivity.class);
                startActivity(intent_book);
            }
        });
        order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_book=new Intent(ctx_home,OrderHistoryActivity.class);
                startActivity(intent_book);
            }
        });
        my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_book=new Intent(ctx_home,MyAccountActivity.class);
                startActivity(intent_book);
            }
        });
        refer_baker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_book=new Intent(ctx_home,ReferBakerActivity.class);
                startActivity(intent_book);
            }
        });
        app_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_book=new Intent(ctx_home,AppVersionActivity.class);
                startActivity(intent_book);
            }
        });

        //Web Services
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AuthenticationEndPoint endPoint = retrofit.create(AuthenticationEndPoint.class);
        AuthenticationService.getMyInfo(this, retrofit, endPoint, this);


    }


    @Override
    public void onDone(int message_id, int code, Object... args) {
        if(args.length>0) {
            baker = (Baker) args[0];

            //TODO here is baker info
            String name = "Welcome "+baker.getUser().getName()+"!";
            username_tv.setText(name);
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
