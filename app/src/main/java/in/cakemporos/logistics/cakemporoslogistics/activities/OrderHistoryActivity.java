package in.cakemporos.logistics.cakemporoslogistics.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.adapters.OrderAdapter;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.Factory;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import retrofit2.Retrofit;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayMessage;

/**
 * Created by bloss on 14/8/16.
 */
public class OrderHistoryActivity extends AppCompatActivity implements OnWebServiceCallDoneEventListener{
    private Order[] orders;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context ctx=this;
    private ImageButton home;
    private int item_clicked;
    private String order_clicked_status;
    Retrofit retrofit;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (resultCode == 2) {
                // TODO Extract the data returned from the child Activity.
                // Customer customerValues= (Customer) data.getSerializableExtra("customer");
                int val=data.getIntExtra("status",-1);
                Toast.makeText(ctx,val+"",Toast.LENGTH_SHORT).show();
                final OrderStatus orderStatus;
                switch (val)
                {
                    case 0:
                        orderStatus=OrderStatus.READY;

                        OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                        OrderService.readyOrder(OrderHistoryActivity.this, retrofit, endPoint, new OnWebServiceCallDoneEventListener() {
                            @Override
                            public void onDone(int message_id, int code, Object... args) {
                                //successful
                                orders[item_clicked].setStatus(orderStatus);
                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onContingencyError(int code) {
                                displayContingencyError(OrderHistoryActivity.this, 0);
                            }

                            @Override
                            public void onError(int message_id, int code, String... args) {
                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
                            }
                        }, orders[item_clicked].getId());
                        break;
                    case 1:
                        orderStatus=OrderStatus.DISPATCHED;

                        OrderEndPoint endPoint1 = retrofit.create(OrderEndPoint.class);
                        OrderService.shipOrder(OrderHistoryActivity.this, retrofit, endPoint1, new OnWebServiceCallDoneEventListener() {
                            @Override
                            public void onDone(int message_id, int code, Object... args) {
                                //successful
                                orders[item_clicked].setStatus(orderStatus);
                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onContingencyError(int code) {
                                displayContingencyError(OrderHistoryActivity.this, 0);
                            }

                            @Override
                            public void onError(int message_id, int code, String... args) {
                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
                            }
                        }, orders[item_clicked].getId());
                        break;
                    case 2:
                        orderStatus=OrderStatus.CANCELLED;

                        OrderEndPoint endPoint2 = retrofit.create(OrderEndPoint.class);
                        OrderService.cancelOrder(OrderHistoryActivity.this, retrofit, endPoint2, new OnWebServiceCallDoneEventListener() {
                            @Override
                            public void onDone(int message_id, int code, Object... args) {
                                //successful
                                orders[item_clicked].setStatus(orderStatus);
                                Toast.makeText(ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onContingencyError(int code) {
                                displayContingencyError(OrderHistoryActivity.this, 0);
                            }

                            @Override
                            public void onError(int message_id, int code, String... args) {
                                displayError(OrderHistoryActivity.this, message_id, Snackbar.LENGTH_LONG);
                            }
                        }, orders[item_clicked].getId());
                        break;
                    default:
                        orderStatus=OrderStatus.PENDING;
                }
                orders[item_clicked].setStatus(orderStatus);
                mAdapter.notifyDataSetChanged();
                //


                //
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oh, menu);
        //
        //disable if dispatched
        if(orders[item_clicked].getStatus()==OrderStatus.PENDING||orders[item_clicked].getStatus()==OrderStatus.READY)
            menu.getItem(1).setEnabled(true);
        else
            menu.getItem(1).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_order_details_oh) {
            Intent intent=new Intent(ctx,SingleOrderActivity.class);
            //Order order = orders[item_clicked];
            Bundle bundle=new Bundle();
            bundle.putSerializable("current_order",orders[item_clicked]);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_change_status_oh){
            //
            //Toast.makeText(ctx,"Change",Toast.LENGTH_SHORT).show();
            //
            Intent intent=new Intent(ctx,ChangeStatusActivity.class);
            order_clicked_status=orders[item_clicked].getStatus().toString();
            intent.putExtra("status",order_clicked_status);
            startActivityForResult(intent,2);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        //
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_order_history);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

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
                TextView status=(TextView)view.findViewById(R.id.order_status_oh);
                Toolbar toolbar=(Toolbar)view.findViewById(R.id.toolbar_menu_oh);
                setSupportActionBar(toolbar);
                item_clicked=position;

            }
        }));


    }

    @Override
    public void onDone(int message_id, int code, Object... args) {
        displayMessage(this, "Success", Snackbar.LENGTH_LONG);

        List<Order> orderlist = ((List<Order>) args[0]);
        if(orderlist!=null){
            //here goes orders  \o/
            //                   |
            //                  / \
            Collections.reverse(orderlist);
            orders=orderlist.toArray(new Order[orderlist.size()]);
            ((OrderAdapter)mAdapter).setmDataset(orders);
            mAdapter.notifyDataSetChanged();

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
