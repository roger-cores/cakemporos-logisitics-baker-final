package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.EnumFormatter;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderType;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;

/**
 * Created by Maitreya on 28-Aug-16.
 */
public class SingleOrderActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    private static final int REQUEST_CALL_RIDER_PHONE = 0;
    private static final int REQUEST_CALL_CUSTOMER_PHONE = 1;
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
    private TextView rider_travel_cost;
    private TextView order_id_so;
    private TextView order_status_so;
    private TextView pickupDate_so;
    private TextView dropDate_so;
    private TextView bookingDate_so;
    private TextView riderTravelCost;
    private TextView cake_val_wt;
    private TextView cake_val_cost;
    private TextView estimatedCost, serviceType;
    private Button button;
    private ProgressBar progressBar;
    private ScrollView scrollView;


    Order order;

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
        cake_val_so=(TextView)findViewById(R.id.cake_val_type);
        cake_val_wt=(TextView)findViewById(R.id.cake_val_wt);
        cake_val_cost=(TextView)findViewById(R.id.cake_val_cost);

        pickup_val_so=(TextView)findViewById(R.id.pickup_val_so);
        customer_val_so=(TextView)findViewById(R.id.customer_val_so);
        phone_val_so=(TextView)findViewById(R.id.phone_val_so);
        address_val_so=(TextView)findViewById(R.id.address_val_so);
        drop_val_so=(TextView)findViewById(R.id.drop_val_so);
        rider_name = (TextView) findViewById(R.id.rider_name_txt);
        rider_phone = (TextView) findViewById(R.id.rider_phone_txt);
        rider_travel_cost = (TextView) findViewById(R.id.rider_travel_cost);
        order_id_so=(TextView) findViewById(R.id.order_id_order_history_detailed);
        order_status_so=(TextView) findViewById(R.id.order_status_oh_detailed);
        pickupDate_so=(TextView) findViewById(R.id.pickup_date_oh_detailed);
        dropDate_so=(TextView) findViewById(R.id.drop_date_oh_detailed);
        bookingDate_so=(TextView) findViewById(R.id.booking_date_oh_detailed);
        riderTravelCost = (TextView) findViewById(R.id.rider_travel_cost);
        progressBar = (ProgressBar) findViewById(R.id.baker_specific_order_progress);
        scrollView = (ScrollView) findViewById(R.id.baker_specific_order_scroll_view);
        button = (Button) findViewById(R.id.calll_rider_order_details);
        estimatedCost = (TextView) findViewById(R.id.estimatedFare);
        serviceType = (TextView) findViewById(R.id.serviceType);
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

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {

        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        if(args.length!=0 && args[0]!=null && args[0] instanceof Order){
            order = (Order) args[0];

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy @ hh:mm a");
            SimpleDateFormat formatter1 =new SimpleDateFormat("dd-MMM-yyyy");
            //Set values on text views
            String cake_values=order.getCost()+" "+order.getCakeType()+" "+order.getWeight();
            String pickupdate_value=formatter.format(order.getPickUpDate().getTime());
            String pickupdate_head=formatter1.format(order.getPickUpDate().getTime());
            String dropdate_head=formatter1.format(order.getDropDate().getTime());
            String bookingdate_head=formatter1.format(order.getBookingDate().getTime());
            String customer_value=order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName();
            String phone_values=order.getCustomer().getPhone()+" / "+order.getDropAltPhone();
            String dropdate_value=formatter.format(order.getDropDate().getTime());
            cake_val_so.setText(order.getCakeType().toString());
            cake_val_wt.setText(Float.toString(EnumFormatter.getWeight(order.getWeight())) + " Kg.");
            cake_val_cost.setText("Rs. " + order.getCost());
            pickup_val_so.setText(pickupdate_value);
            customer_val_so.setText(customer_value);
            phone_val_so.setText(phone_values);
            address_val_so.setText(order.getCustomer().getAddress());
            drop_val_so.setText(dropdate_value);
            if(order.getRider() == null){
                rider_name.setText("Rider isn't assigned yet!");
                rider_phone.setText("");
                button.setEnabled(false);
            }
            else {
                rider_name.setText(order.getRider().getUser().getName());
                rider_phone.setText(Long.toString(order.getRider().getUser().getPhone()));
                button.setEnabled(true);
            }
            order_id_so.setText(order.getOrderCode());
            order_status_so.setText(order.getStatus().toString());
            pickupDate_so.setText("Pick Up Date\n"+pickupdate_head);
            dropDate_so.setText("Drop Date\n"+dropdate_head);
            bookingDate_so.setText("Booking Date\n"+bookingdate_head);
            if(order.getTotalCost() != null && order.getDistance() != null){
                riderTravelCost.setText("Rs. " + Double.toString(order.getTotalCost()) + " for " + order.getDistance()/1000 + " Km.");
            } else riderTravelCost.setText("Info will be available after order is delivered");
            //
            if (order.getStatus().equals(OrderStatus.CANCELLED)){
                order_status_so.setBackgroundColor(Color.RED);
            }
            else if (order.getStatus().equals(OrderStatus.DISPATCHED)){
                order_status_so.setBackgroundColor(Color.rgb(0,100,0));
            }
            else if(order.getStatus().equals(OrderStatus.DELIVERED)){
                order_status_so.setBackgroundColor(Color.BLUE);
            }
            estimatedCost.setText("Rs. " + order.getEstimatedCost() + "/-");
            if(order.getOrderType().equals(OrderType.SUPER)){
                serviceType.setText("SUPER JET");
            } else serviceType.setText(order.getOrderType().toString());
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


    public void callRider(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        if(order.getRider() == null) return;
        callIntent.setData(Uri.parse("tel:" + order.getRider().getUser().getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(SingleOrderActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_RIDER_PHONE);


            return;
        }
        startActivity(callIntent);
    }

    public void callCustomer(View view){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + order.getCustomer().getPhone()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(SingleOrderActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_CUSTOMER_PHONE);


            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_RIDER_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + order.getRider().getUser().getPhone()));
                    try {
                        startActivity(callIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_CALL_CUSTOMER_PHONE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + order.getCustomer().getPhone()));
                    try {
                        startActivity(callIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
