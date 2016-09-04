package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.CakeType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;

/**
 * Created by Maitreya on 28-Aug-16.
 */
public class SingleOrderActivity extends AppCompatActivity {
    private ImageButton home;
    private TextView cake_val_so,pickup_val_so,customer_val_so,phone_val_so,address_val_so,drop_val_so;
    private String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baker_specific_order);
        //find views
        home=(ImageButton)findViewById(R.id.single_order_img_button_app_version);
        cake_val_so=(TextView)findViewById(R.id.cake_val_so);
        pickup_val_so=(TextView)findViewById(R.id.pickup_date_oh);
        customer_val_so=(TextView)findViewById(R.id.customer_val_so);
        phone_val_so=(TextView)findViewById(R.id.phone_val_so);
        address_val_so=(TextView)findViewById(R.id.address_val_so);
        drop_val_so=(TextView)findViewById(R.id.drop_val_so);

        //onclick
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        Intent past_Intent=getIntent();
        order_id=past_Intent.getStringExtra("order_id");
        //
        Toast.makeText(this,"order id: "+order_id,Toast.LENGTH_SHORT).show();
        //
        //Retrofit code goes here
        Order testOrder=new Order();
        //
        Locality testLocality=new Locality();
        testLocality.setName("Navagaon");
        Customer testCustomer=new Customer();
        testCustomer.setAddress("qwertyuiopas\nsnkjn nkjlkjlkjlkj adansdknklsandksand\nasmdasdsad");
        testCustomer.setFirstName("Maitreya");
        testCustomer.setLastName("Save");
        testCustomer.setLocality(testLocality);
        testCustomer.setPhone(789456123l);
        //
        testOrder.setLocality(testLocality);
        testOrder.setCustomer(testCustomer);
        testOrder.setCakeType(CakeType.CUSTOMIZED);
        testOrder.setCost(300l);
        testOrder.setWeight(OrderWeight.HALF);
        Calendar c=Calendar.getInstance();
        testOrder.setPickUpDate(c.getTime());
        testOrder.setDropDate(c.getTime());
        testOrder.setDropAltPhone(333444555l);
        //
        //Retrofit code ends here
        //
        //Set values on text views
        cake_val_so.setText(testOrder.getCost()+" "+testOrder.getCakeType()+" "+testOrder.getWeight());
        //pickup_val_so.setText(testOrder.getPickUpDate()+"");
        customer_val_so.setText(testOrder.getCustomer().getFirstName()+" "+testOrder.getCustomer().getLastName());
        phone_val_so.setText(testOrder.getCustomer().getPhone()+" / "+testOrder.getDropAltPhone());
        address_val_so.setText(testOrder.getCustomer().getAddress());
        //drop_val_so.setText(testOrder.getDropDate()+"");
        //
    }
}
