package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.adapters.CustomerAdapter;
import in.cakemporos.logistics.cakemporoslogistics.events.OnFilterDoneListener;
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
public class SelectCustomerActivity extends BaseActivity implements OnWebServiceCallDoneEventListener, OnFilterDoneListener {
    private List<Customer> customers;
    private RecyclerView mRecyclerView;
    private CustomerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context ctx=this;
    Retrofit retrofit;

    private TextView textView;

    RelativeLayout progressBarContainer;
    LinearLayout customerListContainer;
    private SearchView searchView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);

        progressBarContainer = (RelativeLayout) findViewById(R.id.progressBar);
        customerListContainer = (LinearLayout) findViewById(R.id.linear_layout_customer);
        textView = (TextView) findViewById(R.id.empty_message_select_customer);
        toolbar = (Toolbar) findViewById(R.id.toolbar_select_customer);
        setSupportActionBar(toolbar);

        retrofit = Factory.createClient(getResources().getString(R.string.base_url));

        //Test Order
        //
        //Dynamic values for Recycler Data
        CustomerEndPoint endPoint = retrofit.create(CustomerEndPoint.class);
        CustomerService.getAllCustomers(this, retrofit, endPoint, this);
        showProgress();
        //
        //Convert List to Array
        //customers= (Customer[]) list.toArray(new Customer[list.size()]);
        customers = new ArrayList<>();
        //
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new CustomerAdapter(this, customers);
        mRecyclerView.setAdapter(mAdapter);
        //
        mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            Intent resultIntent=new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("customer",customers.get(position));
                            resultIntent.putExtras(bundle);
                            setResult(BookDeliveryActivity.CUSTOMER_REQUEST,resultIntent);
                            finish();
                        }
                    })
        );

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate( R.menu.select_customer, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);

                }

                //filter
                if(mAdapter != null){
                    mAdapter.filter(query, SelectCustomerActivity.this);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);

                //filter
                if(mAdapter != null){
                    mAdapter.filter(s, SelectCustomerActivity.this);
                }

                return true;
            }
        });
        return true;
    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        List<Customer> customerslist = (List<Customer>) args[0];
        if(customers != null){
            customers=customerslist;
            ((CustomerAdapter)mAdapter).setmDataset(customers);
            mAdapter.notifyDataSetChanged();

        }
        hideProgress();
    }

    @Override
    public void onContingencyError(int code) {
        displayContingencyError(this, Snackbar.LENGTH_LONG);
        hideProgress();
    }

    @Override
    public void onError(int message_id, int code, String... args) {
        displayMessage(this, getString(message_id), Snackbar.LENGTH_LONG);
        hideProgress();
    }

    private void hideProgress(){
        customerListContainer.setVisibility(View.VISIBLE);
        progressBarContainer.setVisibility(View.GONE);
    }

    private void showProgress(){
        customerListContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void filterDone(boolean isEmpty) {
        if(!isEmpty){
            textView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

}

