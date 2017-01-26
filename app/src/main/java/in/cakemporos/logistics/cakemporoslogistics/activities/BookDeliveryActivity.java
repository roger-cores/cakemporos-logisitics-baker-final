package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.GoogleAPIEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.LocalityEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.GoogleAPIService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.LocalityService;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.GoogleDistanceResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.CakeType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.PaymentType;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by maitr on 31-Jul-16.
 */
public class BookDeliveryActivity extends BaseActivity implements OnWebServiceCallDoneEventListener {
    public static final int CUSTOMER_REQUEST = 2;
    private Spinner cake_type,weight_of_cake;
    private Button confirm_booking,select_existing_customer;
    private Button pickupDate,pickupTime, dropDate, dropTime;
    private ImageButton home;
    private EditText firstName,lastName,phone,altPhone,address,pick_address,cost,dropAltPhone, instructions;
    private Button sublocality;
    private SimpleDateFormat simpleDateFormatForDate = new SimpleDateFormat("dd-MMM");
    private SimpleDateFormat simpleDateFormatForTime = new SimpleDateFormat("hh:mm a");
    private Calendar pickupDateTime, dropDateTime;
    private Retrofit retrofit, googleRetrofit;
    private ArrayAdapter<Locality> adapter;
    private Locality selectedLocality;
    private Customer selectedCustomer;
    private Double lat, lon;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private RadioGroup radioGroup;

    private Context ctx_book_delivery=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_delivery);

        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);

        radioGroup = (RadioGroup) this.findViewById(R.id.book_delivery_radiogroup);

        pickupDateTime = Calendar.getInstance();
        pickupDateTime.add(Calendar.HOUR_OF_DAY, 1);

        dropDateTime = (Calendar) pickupDateTime.clone();
        dropDateTime.add(Calendar.HOUR_OF_DAY, 2);

        //find views and init
        loadViews();
        setUpViews();
        //Web Services
        initWebService();

    }

    public boolean validateOrder(){
        boolean result = true;
        if(cost.getText().toString().equals("")){
            cost.setError("Cost is required");
            result = false;
        }
        if(pick_address.getText().toString().equals("")){
            pick_address.setError("Address is required");
            result = false;
        }

        if(selectedLocality == null){
            sublocality.setError("Locality is required");
            result = false;
        }

        if(firstName.getText().toString().equals("")){
            firstName.setError("First Name is required");
            result = false;
        }
        if(lastName.getText().toString().equals("")){
            lastName.setError("Last Name is required");
            result = false;
        }
        if(address.getText().toString().equals("")){
            address.setError("Address is required");
            result = false;
        }
        if(phone.getText().toString().equals("")){
            phone.setError("Phone is required");
            result = false;
        }

        return result;
    }


    public Order getOrder(){


        if(selectedLocality == null) return null;


        Order order = new Order();

        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        order.setPaymentType(PaymentType.valueOf(radioButton.getText().toString()));


        order.setStatus(OrderStatus.PENDING);
        order.setCakeType(CakeType.valueOf(cake_type.getSelectedItem().toString().toUpperCase()));
        order.setCost(Long.parseLong(cost.getText().toString()));
        order.setInstructions(instructions.getText().toString());
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
        } else {
            if(selectedLocality!=null){
                selectedCustomer.setLocality(selectedLocality);
            }
            order.setCustomer(selectedCustomer);
        }




        return order;
    }

    public void book(){


        if(!validateOrder()) return;

        final Order order = getOrder();


        GoogleAPIEndPoint googleAPIEndPoint = googleRetrofit.create(GoogleAPIEndPoint.class);
        GoogleAPIService.getAllCustomers(this, googleRetrofit, googleAPIEndPoint, "" + lat + "," + lon, order.getCustomer().getLocality().getLat() + "," + order.getCustomer().getLocality().getLon(), new OnWebServiceCallDoneEventListener() {
            @Override
            public void onDone(int message_id, final int code, Object... args) {
                Log.d("GoogleDone", "Success");
                GoogleDistanceResponse response = (GoogleDistanceResponse) args[0];

                if(response.getRows()!=null && response.getRows().size()>0 &&
                        response.getRows().get(0).getElements()!=null &&
                        response.getRows().get(0).getElements().size()>0 &&
                        response.getRows().get(0).getElements().get(0).getDistance()!=null){
                    //in meters
                    int distance = Integer.parseInt(response.getRows().get(0).getElements().get(0).getDistance().getValue());
                    int duration = Integer.parseInt(response.getRows().get(0).getElements().get(0).getDuration().getValue());
                    String distanceString = response.getRows().get(0).getElements().get(0).getDistance().getText();
                    String durationString = response.getRows().get(0).getElements().get(0).getDuration().getText();
                    float distanceInKm = distance/1000;
                    float cost = 0;
                    final int jetCost = 250;
                    if(distance <= 10000){
                        order.setOrderType(OrderType.NORMAL);
                        //normal
                        if(distanceInKm<=3){
                            cost = 60;
                        } else {
                            cost = 60;
                            cost += (distanceInKm-3) * 12;
                        }
                    } else {
                        //jet or super jet
                        order.setOrderType(OrderType.JET);
                        if(distanceInKm<=5){
                            cost = 250;
                        } else {
                            cost = 250;
                            cost += (distanceInKm-5) * 15;
                        }
                    }

                    if(cost < 60){
                        cost = 60;
                    }

                    View view = getLayoutInflater().inflate(R.layout.fare_estimation, null);
                    final Switch serviceSwitch = (Switch) view.findViewById(R.id.service_switch);
                    final TextView fareEstimate, durationTxt, distanceTxt;
                    TextView normalTxt = (TextView) view.findViewById(R.id.normal_text);
                    fareEstimate = (TextView) view.findViewById(R.id.fare_estimate);
                    durationTxt = (TextView) view.findViewById(R.id.fare_estimate_duration);
                    distanceTxt = (TextView) view.findViewById(R.id.fare_estimate_distance);
                    durationTxt.setText(durationString);
                    distanceTxt.setText(distanceString);


                    if(order.getOrderType() == OrderType.NORMAL){
                        serviceSwitch.setVisibility(View.GONE);
                        normalTxt.setVisibility(View.VISIBLE);
                        fareEstimate.setText("Rs. " + cost + "/-");
                        order.setEstimatedCost(cost);
                    } else {
                        serviceSwitch.setVisibility(View.VISIBLE);
                        normalTxt.setVisibility(View.GONE);
                        fareEstimate.setText("Rs. " + jetCost + "/-");
                        order.setEstimatedCost(jetCost);
                    }
                    final float finalCost = cost;
                    serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                order.setOrderType(OrderType.SUPER);
                                order.setEstimatedCost(finalCost);
                                fareEstimate.setText("Rs. " + finalCost + "/-");
                                serviceSwitch.setText("Super JET  ");
                            } else {
                                order.setOrderType(OrderType.JET);
                                order.setEstimatedCost(jetCost);
                                fareEstimate.setText("Rs. " + jetCost + "/-");
                                serviceSwitch.setText("JET  ");
                            }
                        }
                    });

                    new AlertDialog.Builder(BookDeliveryActivity.this)
                            .setTitle("Review your order")
                            .setView(view)
                            .setPositiveButton("Book", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                                    OrderService.createOrder(order, BookDeliveryActivity.this, retrofit, endPoint, BookDeliveryActivity.this);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                } else displayContingencyError(BookDeliveryActivity.this, 0);

            }

            @Override
            public void onContingencyError(int code) {
                displayContingencyError(BookDeliveryActivity.this, 0);
            }

            @Override
            public void onError(int message_id, int code, String... args) {
                displayError(BookDeliveryActivity.this, message_id, Snackbar.LENGTH_LONG);
            }
        });


//        View view = getLayoutInflater().inflate(R.layout.select_order_type, null);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                .setCancelable(true)
//                .setTitle("Select Type of Service")
//                .setIcon(android.R.drawable.ic_dialog_info)
//                .setView(view);
//
//        Button hyperLocal, jet, superJet;
//
//        hyperLocal = (Button) view.findViewById(R.id.hyper_select_order);
//        jet = (Button) view.findViewById(R.id.jet_select_order);
//        superJet = (Button) view.findViewById(R.id.super_select_order);
//
//        final AlertDialog dialog = builder.show();
//        hyperLocal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                order.setOrderType(OrderType.NORMAL);
//
//                if(order != null){
//                    OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
//                    OrderService.createOrder(order, BookDeliveryActivity.this, retrofit, endPoint, BookDeliveryActivity.this);
//
//                } else {
//                    displayContingencyError(BookDeliveryActivity.this, 0);
//                }
//            }
//        });
//
//        jet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                order.setOrderType(OrderType.JET);
//
//                if(order != null){
//                    OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
//                    OrderService.createOrder(order, BookDeliveryActivity.this, retrofit, endPoint, BookDeliveryActivity.this);
//
//                } else {
//                    displayContingencyError(BookDeliveryActivity.this, 0);
//                }
//            }
//        });
//
//        superJet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                order.setOrderType(OrderType.SUPER);
//
//                if(order != null){
//                    OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
//                    OrderService.createOrder(order, BookDeliveryActivity.this, retrofit, endPoint, BookDeliveryActivity.this);
//
//                } else {
//                    displayContingencyError(BookDeliveryActivity.this, 0);
//                }
//            }
//        });




    }

    public void setTextWatcher(final EditText editText, final String name){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText.getText().toString().equals("")){
                    editText.setError(name + " is required");
                } else editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadViews(){
        //find views
        cake_type= (Spinner) findViewById(R.id.spinner_cake_type);
        weight_of_cake=(Spinner) findViewById(R.id.spinner_weight_of_cake);
        select_existing_customer=(Button)findViewById(R.id.button_select_existing_customer);
        confirm_booking=(Button)findViewById(R.id.button_confirm_booking);
        home=(ImageButton)findViewById(R.id.home_img_button_book_delivery);
        firstName=(EditText)findViewById(R.id.et_first_name_drop_details);
        setTextWatcher(firstName, "first name");
        lastName=(EditText)findViewById(R.id.et_last_name_drop_details);
        setTextWatcher(lastName, "last name");
        phone=(EditText)findViewById(R.id.et_phone_no_drop_details);
        setTextWatcher(phone, "phone");
        altPhone=(EditText)findViewById(R.id.et_alternative_phone_pickup_details);
        sublocality=(Button) findViewById(R.id.til3_sub_locality);
        address=(EditText)findViewById(R.id.et_address_drop_details);
        setTextWatcher(address, "address");
        pick_address=(EditText)findViewById(R.id.et_address_pickup_details);
        setTextWatcher(pick_address, "address");
        cost=(EditText)findViewById(R.id.et_cost_of_cake_product_details);
        setTextWatcher(cost, "cost");
        instructions=(EditText)findViewById(R.id.et_instructions_product_details);
        pickupDate =(Button) findViewById(R.id.til2_pickup_date);
        pickupTime=(Button) findViewById(R.id.til2_pickup_time);
        dropAltPhone = (EditText) findViewById(R.id.et_alternative_phone_drop_details);
        dropDate = (Button) findViewById(R.id.til2_drop_date);
        dropTime = (Button) findViewById(R.id.til2_drop_time);
    }

    private void setUpViews(){

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
                startActivityForResult(intent_select_existing_customer,CUSTOMER_REQUEST);
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

        cost.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        instructions.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);

        firstName.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        lastName.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        phone.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        altPhone.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        address.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        pick_address.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        dropAltPhone.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        sublocality.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);

        altPhone.setText(getIntent().getStringExtra("phone"));
        pick_address.setText(getIntent().getStringExtra("address"));

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
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleRetrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.google_api_base_url))
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

                Calendar calendar2 = Calendar.getInstance();
                calendar2.add(Calendar.MINUTE, 59);


                if(calendar.after(calendar2)){
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

        bundle.putInt("hour", pickupDateTime.get(Calendar.HOUR_OF_DAY));
        bundle.putInt("minute", pickupDateTime.get(Calendar.MINUTE));

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

                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(pickupDateTime.getTime());
                calendar2.add(Calendar.MINUTE, 59);

                if(calendar.after(calendar2)){
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


        bundle.putInt("hour", dropDateTime.get(Calendar.HOUR_OF_DAY));
        bundle.putInt("minute", dropDateTime.get(Calendar.MINUTE));

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


    public void showCakeTypeDropDown(View view){
        cake_type.performClick();
    }

    public void showWeightDropDown(View view){
        weight_of_cake.performClick();
    }

    public void selectLocality(View view){
        openAutocompleteActivity();
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setBoundsBias(new LatLngBounds(new LatLng(18.847812, 72.747345), new LatLng(19.385, 73.168945)))
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(BookDeliveryActivity.class.getName(), message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == CUSTOMER_REQUEST) {
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
            sublocality.setError(null);
            Toast.makeText(ctx_book_delivery,customerValues.getId(),Toast.LENGTH_SHORT).show();
        }

        // Check that the result was from the autocomplete widget.
        else if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(BookDeliveryActivity.class.getName(), "Place Selected: " + place.getName());
                sublocality.setText(place.getName());
                selectedLocality = new Locality();
                selectedLocality.setPlaceId(place.getId());
                selectedLocality.setLat(place.getLatLng().latitude);
                selectedLocality.setLon(place.getLatLng().longitude);
                selectedLocality.setName(place.getName().toString());
                sublocality.setError(null);


                //get place here
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(BookDeliveryActivity.class.getName(), "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }
}
