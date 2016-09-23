package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnDateTimePickedEventListner;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.fragments.DatePickerFragment;
import in.cakemporos.logistics.cakemporoslogistics.fragments.TimePickerFragment;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.AuthenticationEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.LocalityEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.LocalityService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.CakeType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by maitr on 31-Jul-16.
 */
public class BookDeliveryActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    private Spinner cake_type,weight_of_cake;
    private Button confirm_booking,select_existing_customer;
    private Button pickupDate,pickupTime, dropDate, dropTime;
    private ImageButton home;
    private EditText firstName,lastName,phone,altPhone,address,pick_address,cost,dropAltPhone;
    private AutoCompleteTextView sublocality;
    private SimpleDateFormat simpleDateFormatForDate = new SimpleDateFormat("dd-MMM");
    private SimpleDateFormat simpleDateFormatForTime = new SimpleDateFormat("hh:mm a");
    private Calendar pickupDateTime, dropDateTime;
    private Retrofit retrofit;
    private ArrayAdapter<Locality> adapter;
    private Locality selectedLocality;
    private Customer selectedCustomer;

    private Context ctx_book_delivery=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_delivery);

        pickupDateTime = Calendar.getInstance();
        pickupDateTime.add(Calendar.HOUR_OF_DAY, 1);

        dropDateTime = (Calendar) pickupDateTime.clone();
        dropDateTime.add(Calendar.HOUR_OF_DAY, 1);

        //find views and init
        loadViews();
        setUpViews();
        //Web Services
        initWebService();
        loadAllLocalities();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
                if (resultCode == 1) {
                    // TODO Extract the data returned from the child Activity.
                   // Customer customerValues= (Customer) data.getSerializableExtra("customer");
                    Bundle bundle=data.getExtras();
                    Customer customerValues= (Customer) bundle.getSerializable("customer");
                    firstName.setText(customerValues.getFirstName());
                    lastName.setText(customerValues.getLastName());
                    address.setText(customerValues.getAddress());
                    phone.setText(customerValues.getPhone()+"");
                    sublocality.setText(customerValues.getLocality().getName());
                    selectedLocality = customerValues.getLocality();
                    selectedCustomer=customerValues;
                    Toast.makeText(ctx_book_delivery,customerValues.getId(),Toast.LENGTH_SHORT).show();
                }
        }
    }
    public void loadAllLocalities(){
        LocalityEndPoint endPoint = retrofit.create(LocalityEndPoint.class);
        LocalityService.getAllLocalities(this, retrofit, endPoint, new OnWebServiceCallDoneEventListener() {
            @Override
            public void onDone(int message_id, int code, Object... args) {
                if(args[0] != null){
                    List<Locality> localities = (List<Locality>) args[0];
                    adapter = new ArrayAdapter<Locality>(
                            BookDeliveryActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            localities);
                    sublocality.setAdapter(adapter);


                }
            }

            @Override
            public void onContingencyError(int code) {
                displayContingencyError(BookDeliveryActivity.this, Snackbar.LENGTH_LONG);
            }

            @Override
            public void onError(int message_id, int code, String... args) {
                displayError(BookDeliveryActivity.this, message_id, Snackbar.LENGTH_LONG);
            }
        });
    }

    public Order getOrder(){

        if(selectedLocality == null) return null;


        Order order = new Order();

        order.setStatus(OrderStatus.PENDING);
        order.setCakeType(CakeType.valueOf(cake_type.getSelectedItem().toString().toUpperCase()));
        order.setCost(Long.parseLong(cost.getText().toString()));
        order.setPickUpDate(pickupDateTime.getTime());
        if(!altPhone.getText().toString().equals(""))
        order.setAltPhone(Long.parseLong(altPhone.getText().toString()));
        switch (weight_of_cake.getSelectedItem().toString()){
            case "0.5 Kg":
                order.setWeight(OrderWeight.HALF);
                break;
            case "1 Kg":
                order.setWeight(OrderWeight.ONE);
                break;
            case "1.5 Kg":
                order.setWeight(OrderWeight.ONEANDHALF);
                break;
            case "2 Kg":
                order.setWeight(OrderWeight.TWO);
                break;
        }
        order.setAddress(pick_address.getText().toString());
        if(!dropAltPhone.getText().toString().equals(""))
        order.setDropAltPhone(Long.parseLong(dropAltPhone.getText().toString()));
        order.setLocality(selectedLocality);
        order.setDropDate(dropDateTime.getTime());
        if(selectedCustomer == null){
            Customer customer = new Customer();
            customer.setFirstName(firstName.getText().toString());
            customer.setLastName(lastName.getText().toString());
            customer.setLocality(selectedLocality);
            customer.setAddress(address.getText().toString());
            customer.setPhone(Long.parseLong(phone.getText().toString()));
            order.setCustomer(customer);
        } else order.setCustomer(selectedCustomer);
        return order;
    }

    public void book(){
        Order order = getOrder();

        if(order != null){
            OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
            OrderService.createOrder(order, this, retrofit, endPoint, this);

        } else {
            displayContingencyError(this, 0);
        }

    }

    private void loadViews(){
        //find views
        cake_type= (Spinner) findViewById(R.id.spinner_cake_type);
        weight_of_cake=(Spinner) findViewById(R.id.spinner_weight_of_cake);
        select_existing_customer=(Button)findViewById(R.id.button_select_existing_customer);
        confirm_booking=(Button)findViewById(R.id.button_confirm_booking);
        home=(ImageButton)findViewById(R.id.home_img_button_book_delivery);
        firstName=(EditText)findViewById(R.id.et_first_name_drop_details);
        lastName=(EditText)findViewById(R.id.et_last_name_drop_details);
        phone=(EditText)findViewById(R.id.et_phone_no_drop_details);
        altPhone=(EditText)findViewById(R.id.et_alternative_phone_drop_details);
        sublocality=(AutoCompleteTextView) findViewById(R.id.et_sub_locality_drop_details);
        address=(EditText)findViewById(R.id.et_address_drop_details);
        pick_address=(EditText)findViewById(R.id.et_address_pickup_details);
        cost=(EditText)findViewById(R.id.et_cost_of_cake_product_details);
        pickupDate =(Button) findViewById(R.id.til2_pickup_date);
        pickupTime=(Button) findViewById(R.id.til2_pickup_time);
        dropAltPhone = (EditText) findViewById(R.id.et_alternative_phone_drop_details);
        dropDate = (Button) findViewById(R.id.til2_drop_date);
        dropTime = (Button) findViewById(R.id.til2_drop_time);
    }

    private void setUpViews(){

        sublocality.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                selectedLocality = (Locality) arg0.getAdapter().getItem(arg2);
            }
        });

        String[] items_cake_type = new String[]{"Normal","Photo","Customized"};
        ArrayAdapter<String> adapter_cake_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_cake_type);
        cake_type.setAdapter(adapter_cake_type);
        //
        String[] items_weight = new String[]{"0.5 Kg","1 Kg","1.5 Kg","2 Kg"};
        ArrayAdapter<String> adapter_weight = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_weight);
        weight_of_cake.setAdapter(adapter_weight);

        //button onClicks
        select_existing_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_select_existing_customer=new Intent(ctx_book_delivery,SelectCustomerActivity.class);
                startActivityForResult(intent_select_existing_customer,1);
                //startActivity(intent_select_existing_customer);
            }
        });
        //
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book();
            }
        });

        updatePickupDateTime();
        updateDropDateTime();
    }

    public void defaultPickTime(){
        Calendar calendar = Calendar.getInstance();
        pickupDateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        pickupDateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        pickupDateTime.add(Calendar.HOUR_OF_DAY, 1);
    }

    public void defaultDropDateTime(){
        dropDateTime = (Calendar) pickupDateTime.clone();
        dropDateTime.add(Calendar.HOUR_OF_DAY, 1);
    }

    public void updatePickupDateTime(){
        pickupDate.setText(simpleDateFormatForDate.format(pickupDateTime.getTime()));
        pickupTime.setText(simpleDateFormatForTime.format(pickupDateTime.getTime()));
    }

    public void updateDropDateTime(){
        dropDate.setText(simpleDateFormatForDate.format(dropDateTime.getTime()));
        dropTime.setText(simpleDateFormatForTime.format(dropDateTime.getTime()));
    }

    private void initWebService(){
        retrofit=new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void pickupDate(View view){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateTimePickedEventListner(new OnDateTimePickedEventListner() {
            @Override
            public void onDateTimePicked(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                pickupDateTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                pickupDateTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                pickupDateTime.set(Calendar.DATE, calendar.get(Calendar.DATE));
                defaultPickTime();
                updatePickupDateTime();
                defaultDropDateTime();
                updateDropDateTime();
            }
        });


        Bundle bundle = new Bundle();
        bundle.putInt("d", pickupDateTime.get(Calendar.DATE));
        bundle.putInt("m", pickupDateTime.get(Calendar.MONTH));
        bundle.putInt("y", pickupDateTime.get(Calendar.YEAR));

        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void pickupTime(View view){
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setOnDateTimePickedEventListner(new OnDateTimePickedEventListner() {
            @Override
            public void onDateTimePicked(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, pickupDateTime.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, pickupDateTime.get(Calendar.MONTH));
                calendar.set(Calendar.DATE, pickupDateTime.get(Calendar.DATE));

                if(calendar.after(Calendar.getInstance()) && calendar.after(pickupDateTime)){
                    pickupDateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                    pickupDateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                    updatePickupDateTime();
                    defaultDropDateTime();
                    updateDropDateTime();
                } else {
                    displayError(BookDeliveryActivity.this, R.string.invalid_time, Snackbar.LENGTH_LONG);
                }


            }
        });

        Bundle bundle = new Bundle();
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.YEAR) == pickupDateTime.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == pickupDateTime.get(Calendar.MONTH) &&
                calendar.get(Calendar.DATE) == pickupDateTime.get(Calendar.DATE)){
            bundle.putBoolean("today", true);
        } else bundle.putBoolean("today", false);

        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public void pickDropDate(View view){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateTimePickedEventListner(new OnDateTimePickedEventListner() {
            @Override
            public void onDateTimePicked(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                if(calendar.after(pickupDateTime)){
                    dropDateTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                    dropDateTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                    dropDateTime.set(Calendar.DATE, calendar.get(Calendar.DATE));
                    updateDropDateTime();
                } else {
                    displayError(BookDeliveryActivity.this, R.string.invalid_time, Snackbar.LENGTH_LONG);
                }


            }
        });


        Bundle bundle = new Bundle();
        bundle.putInt("d", dropDateTime.get(Calendar.DATE));
        bundle.putInt("m", dropDateTime.get(Calendar.MONTH));
        bundle.putInt("y", dropDateTime.get(Calendar.YEAR));

        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void pickDropTime(View view){
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setOnDateTimePickedEventListner(new OnDateTimePickedEventListner() {
            @Override
            public void onDateTimePicked(Date date) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, dropDateTime.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, dropDateTime.get(Calendar.MONTH));
                calendar.set(Calendar.DATE, dropDateTime.get(Calendar.DATE));

                if(calendar.after(Calendar.getInstance())){
                    dropDateTime.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                    dropDateTime.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

                    updateDropDateTime();
                } else {
                    displayError(BookDeliveryActivity.this, R.string.invalid_time, Snackbar.LENGTH_LONG);
                }


            }
        });

        Bundle bundle = new Bundle();
        Calendar calendar = Calendar.getInstance();
        if(calendar.get(Calendar.YEAR) == dropDateTime.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == dropDateTime.get(Calendar.MONTH) &&
                calendar.get(Calendar.DATE) == dropDateTime.get(Calendar.DATE)){
            bundle.putBoolean("today", true);
        } else bundle.putBoolean("today", false);

        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    @Override
    public void onDone(int message_id, int code, Object... args) {
        displayMessage(this, "Success", Snackbar.LENGTH_LONG);

        //Go to Order History
        Intent intent_oh=new Intent(ctx_book_delivery,OrderHistoryActivity.class);
        startActivity(intent_oh);
        finish();
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
