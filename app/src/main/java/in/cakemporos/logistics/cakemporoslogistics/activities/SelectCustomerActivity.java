package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.adapters.CustomerAdapter;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.Factory;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.CustomerEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.CustomerService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import retrofit2.Retrofit;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by maitr on 31-Jul-16.
 */
public class SelectCustomerActivity extends BaseActivity implements OnWebServiceCallDoneEventListener{
    private Customer[] customers;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context ctx=this;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);

        retrofit = Factory.createClient(getResources().getString(R.string.base_url));

        //Test Order
        //
        //Dynamic values for Recycler Data
        CustomerEndPoint endPoint = retrofit.create(CustomerEndPoint.class);
        CustomerService.getAllCustomers(this, retrofit, endPoint, this);
        //
        //Convert List to Array
        //customers= (Customer[]) list.toArray(new Customer[list.size()]);
        customers=new Customer[0];
        //
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CustomerAdapter(customers);
        mRecyclerView.setAdapter(mAdapter);
        //
        mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            Intent resultIntent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("customer",customers[position]);
                            resultIntent.putExtras(bundle);
                            setResult(1,resultIntent);
                            finish();
                        }
                    })
        );

    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        List<Customer> customerslist = (List<Customer>) args[0];
        if(customers != null){
            customers=customerslist.toArray(new Customer[customerslist.size()]);
            ((CustomerAdapter)mAdapter).setmDataset(customers);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onError(int message_id, int code, String... args) {
        displayMessage(this, getString(message_id), Snackbar.LENGTH_LONG);
    }
}
