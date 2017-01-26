package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.adapters.OrderAdapter;
import in.cakemporos.logistics.cakemporoslogistics.events.OnFilterDoneListener;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.Factory;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import retrofit2.Retrofit;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;

/**
 * Created by maitr on 14/8/16.
 */
public class OrderHistoryActivity extends BaseActivity implements OnWebServiceCallDoneEventListener, OnFilterDoneListener {
    private List<Order> orders;
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout orderHistoryContainer;
    private RelativeLayout progressBarContainer;
    private Context ctx=this;
    private ImageButton home;
    private int item_clicked;

    private SearchView searchView;
    private String order_clicked_status;
    Retrofit retrofit;

    private Toolbar toolbar;
    private TextView emptyMessage;

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        {
//            if (resultCode == 2) {
//                // TODO Extract the data returned from the child Activity.
//                // Customer customerValues= (Customer) data.getSerializableExtra("customer");
//                int val=data.getIntExtra("status",-1);
//                Toast.makeText(ctx,val+"",Toast.LENGTH_SHORT).show();
//                final OrderStatus orderStatus;
//                switch (val)
//                {
//                    case 0:
//                        orderStatus=OrderStatus.READY;
//
//                        OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
//                        OrderService.readyOrder(OrderHistoryActivity.this, retrofit, endPoint, new OnWebServiceCallDoneEventListener() {
//                            @Override
//                            public void onDone(int message_id, int code, Object... args) {
//                                //successful
//                                orders[item_clicked].setStatus(orderStatus);
//                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onContingencyError(int code) {
//                                displayContingencyError(OrderHistoryActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onError(int message_id, int code, String... args) {
//                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
//                            }
//                        }, orders[item_clicked].getId());
//                        break;
//                    case 1:
//                        orderStatus=OrderStatus.DISPATCHED;
//
//                        OrderEndPoint endPoint1 = retrofit.create(OrderEndPoint.class);
//                        OrderService.shipOrder(OrderHistoryActivity.this, retrofit, endPoint1, new OnWebServiceCallDoneEventListener() {
//                            @Override
//                            public void onDone(int message_id, int code, Object... args) {
//                                //successful
//                                orders[item_clicked].setStatus(orderStatus);
//                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onContingencyError(int code) {
//                                displayContingencyError(OrderHistoryActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onError(int message_id, int code, String... args) {
//                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
//                            }
//                        }, orders[item_clicked].getId());
//                        break;
//                    case 2:
//                        orderStatus=OrderStatus.CANCELLED;
//
//                        OrderEndPoint endPoint2 = retrofit.create(OrderEndPoint.class);
//                        OrderService.cancelOrder(OrderHistoryActivity.this, retrofit, endPoint2, new OnWebServiceCallDoneEventListener() {
//                            @Override
//                            public void onDone(int message_id, int code, Object... args) {
//                                //successful
//                                orders[item_clicked].setStatus(orderStatus);
//                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onContingencyError(int code) {
//                                displayContingencyError(OrderHistoryActivity.this, 0);
//                            }
//
//                            @Override
//                            public void onError(int message_id, int code, String... args) {
//                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
//                            }
//                        }, orders[item_clicked].getId());
//                        break;
//                    default:
//                        orderStatus=OrderStatus.PENDING;
//                }
//                orders[item_clicked].setStatus(orderStatus);
//                mAdapter.notifyDataSetChanged();
//                //
//
//
//                //
//            }
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.order_history_activity, menu);

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
                    mAdapter.filter(query, OrderHistoryActivity.this);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);

                //filter
                if(mAdapter != null){
                    mAdapter.filter(s, OrderHistoryActivity.this);
                }

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        //

        orders = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.order_history_toolbar);
        emptyMessage = (TextView) findViewById(R.id.empty_message_order_history);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_order_history);
        orderHistoryContainer = (LinearLayout) findViewById(R.id.linear_layout_order_history);
        progressBarContainer = (RelativeLayout) findViewById(R.id.progressBar);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new OrderAdapter(orders,this);
        mRecyclerView.setAdapter(mAdapter);

        retrofit = Factory.createClient(getString(R.string.base_url));

        OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
        OrderService.getMyOrders(this, retrofit, endPoint, this);
        //
        showProgress();

        home= (ImageButton) findViewById(R.id.home_img_button_order_history);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        //
        //
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, final int position) {
                // TODO Handle item click
                item_clicked=position;

            }
        }));
        //
        //jhol
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -200);
        animation.setDuration(0);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());
        mRecyclerView.startAnimation(animation);

    }

    private void hideProgress(){
        orderHistoryContainer.setVisibility(View.VISIBLE);
        progressBarContainer.setVisibility(View.GONE);
    }

    private void showProgress(){
        orderHistoryContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void filterDone(boolean isEmpty) {
        if(!isEmpty){
            emptyMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyMessage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            mRecyclerView.clearAnimation();
            mRecyclerView.setPadding(0, 50, 0, 0);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }
    @Override
    public void onDone(int message_id, int code, Object... args) {
        //displayMessage(this, "Success", Snackbar.LENGTH_LONG);
        hideProgress();
        List<Order> orderlist = ((List<Order>) args[0]);
        if(orderlist!=null){
            //here goes orders  \o/
            //                   |
            //                  / \
            Collections.reverse(orderlist);
            orders=orderlist;
            mAdapter.setmDataset(orderlist);
            mAdapter.notifyDataSetChanged();

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
}
