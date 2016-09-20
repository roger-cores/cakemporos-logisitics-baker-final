package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.CakeType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;

/**
 * Created by Maitreya on 28-Aug-16.
 */
public class SingleOrderActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    private Retrofit retrofit;
    private ImageButton home;
    private TextView cake_val_so;
    private TextView pickup_val_so;
    private TextView customer_val_so;
    private TextView phone_val_so;
    private TextView address_val_so;
    private TextView drop_val_so;
    private TextView rider_name;
    private TextView rider_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baker_specific_order);

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //find views
        home=(ImageButton)findViewById(R.id.single_order_img_button_app_version);
        cake_val_so=(TextView)findViewById(R.id.cake_val_so);
        pickup_val_so=(TextView)findViewById(R.id.pickup_val_so);
        customer_val_so=(TextView)findViewById(R.id.customer_val_so);
        phone_val_so=(TextView)findViewById(R.id.phone_val_so);
        address_val_so=(TextView)findViewById(R.id.address_val_so);
        drop_val_so=(TextView)findViewById(R.id.drop_val_so);
        rider_name = (TextView) findViewById(R.id.rider_name_txt);
        rider_phone = (TextView) findViewById(R.id.rider_phone_txt);

        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        Intent past_Intent=getIntent();
        Bundle bundle=past_Intent.getExtras();
        String orderId = bundle.getString("orderId");
        OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
        OrderService.getMyOrder(this, retrofit, endPoint, orderId, this);

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        if(args.length!=0 && args[0]!=null && args[0] instanceof Order){
            Order order = (Order) args[0];

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy @ hh:mm a");
            //Set values on text views
            String cake_values=order.getCost()+" "+order.getCakeType()+" "+order.getWeight();
            String pickupdate_value=formatter.format(order.getPickUpDate().getTime());
            String customer_value=order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName();
            String phone_values=order.getCustomer().getPhone()+" / "+order.getDropAltPhone();
            String dropdate_value=formatter.format(order.getDropDate().getTime());
            cake_val_so.setText(cake_values);
            pickup_val_so.setText(pickupdate_value);
            customer_val_so.setText(customer_value);
            phone_val_so.setText(phone_values);
            address_val_so.setText(order.getCustomer().getAddress());
            drop_val_so.setText(dropdate_value);
            rider_name.setText(order.getRider().getUser().getName());
            rider_phone.setText(Long.toString(order.getRider().getUser().getPhone()));



        } else displayContingencyError(this, 0);
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
