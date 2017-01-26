package in.cakemporos.logistics.cakemporoslogistics.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.activities.SingleOrderActivity;
import in.cakemporos.logistics.cakemporoslogistics.events.OnFilterDoneListener;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.Factory;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.services.OrderService;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import retrofit2.Retrofit;

import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayContingencyError;
import static in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage.displayError;

/**
 * Created by maitr on 31-Jul-16.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> listItems, filterList;


    private static Context orderAdapter_ctx;
    Retrofit retrofit;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView order_id_oh,booking_date_oh,pickup_date_oh,drop_date_oh,order_status_oh,customer_name_oh,phone_no_oh,pickup_locaion_oh,drop_location_oh;
        public Toolbar menu_toolbar_oh;
        public ViewHolder(View v) {
            super(v);
            order_id_oh=(TextView)v.findViewById(R.id.order_id_order_history);
            booking_date_oh=(TextView)v.findViewById(R.id.booking_date_oh);
            pickup_date_oh=(TextView)v.findViewById(R.id.pickup_date_oh);
            drop_date_oh=(TextView)v.findViewById(R.id.drop_date_oh);
            order_status_oh=(TextView)v.findViewById(R.id.order_status_oh);
            customer_name_oh=(TextView)v.findViewById(R.id.customer_name_oh);
            phone_no_oh=(TextView)v.findViewById(R.id.phone_no_oh);
            pickup_locaion_oh=(TextView)v.findViewById(R.id.pickup_location_oh);
            drop_location_oh=(TextView)v.findViewById(R.id.drop_location_oh);
            menu_toolbar_oh=(Toolbar)v.findViewById(R.id.toolbar_menu_oh);

            menu_toolbar_oh.inflateMenu(R.menu.menu_oh);
        }
    }
    //


    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(List<Order> orders,Context ctx) {
        orderAdapter_ctx=ctx;
        this.listItems = orders;
        filterList = new ArrayList<>();
        filterList.addAll(listItems);
        retrofit = Factory.createClient(orderAdapter_ctx.getString(R.string.base_url));
    }
    public OrderAdapter(List<Order> orders) {
        this.listItems = orders;
        filterList = new ArrayList<>();
        filterList.addAll(listItems);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_order_history, parent, false);
        // set the view's size, margins, paddings and layout parameters



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Order order = filterList.get(position);
        String customer_name=filterList.get(position).getCustomer().getFirstName()+" "+filterList.get(position).getCustomer().getLastName();
        String customer_phone=filterList.get(position).getCustomer().getPhone()+"";
        //Dates
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        //
        String date=sdf.format(filterList.get(position).getBookingDate() );
        String booking_date="Booking Date\n"+date;
        //
        date=sdf.format(filterList.get(position).getPickUpDate());
        String pickup_date="Pick Up Date\n"+date;
        //
        date=sdf.format(filterList.get(position).getDropDate());
        String drop_date="Drop Date\n"+date;
        holder.order_id_oh.setText(filterList.get(position).getOrderCode());
        holder.booking_date_oh.setText(booking_date);
        holder.pickup_date_oh.setText(pickup_date);
        holder.order_status_oh.setText(filterList.get(position).getStatus().toString());
        holder.drop_date_oh.setText(drop_date);
        holder.customer_name_oh.setText(customer_name);
        holder.phone_no_oh.setText(customer_phone);
        holder.pickup_locaion_oh.setText(filterList.get(position).getAddress());
        holder.drop_location_oh.setText(filterList.get(position).getCustomer().getAddress());

        if (filterList.get(position).getStatus().equals(OrderStatus.CANCELLED)){
            holder.order_status_oh.setBackgroundColor(Color.RED);
        }
        else if (filterList.get(position).getStatus().equals(OrderStatus.DISPATCHED)){
            holder.order_status_oh.setBackgroundColor(Color.rgb(0,100,0));
        }
        else if(filterList.get(position).getStatus().equals(OrderStatus.DELIVERED)){
            holder.order_status_oh.setBackgroundColor(Color.BLUE);
        }

        else if(filterList.get(position).getStatus().equals(OrderStatus.READY)){
            holder.order_status_oh.setBackgroundColor(Color.BLUE);
        }

        Menu menu = holder.menu_toolbar_oh.getMenu();

        if(filterList.get(position).getStatus()==OrderStatus.APPROVED||filterList.get(position).getStatus()==OrderStatus.READY)
            menu.getItem(1).setEnabled(true);
        else
            menu.getItem(1).setEnabled(false);
        
        holder.menu_toolbar_oh.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement



                if (id == R.id.action_order_details_oh) {
                    Intent intent=new Intent(orderAdapter_ctx,SingleOrderActivity.class);
                    //Order order = order;
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("current_order",order);
                    bundle.putString("orderId", order.getId());
                    intent.putExtras(bundle);
                    orderAdapter_ctx.startActivity(intent);
                    return true;
                }
                else if(id == R.id.action_change_status_oh){
                    //
                    //Toast.makeText(ctx,"Change",Toast.LENGTH_SHORT).show();
                    //
//            Intent intent=new Intent(ctx,ChangeStatusActivity.class);
//            order_clicked_status=order.getStatus().toString();
//            intent.putExtra("status",order_clicked_status);
//            intent.putExtra("id", order.getOrderCode());
//            startActivityForResult(intent,2);

                    final LinearLayout dialogLayout = (LinearLayout) LayoutInflater.from(orderAdapter_ctx).inflate(R.layout.change_status_dialog, null);

                    if(order.getStatus()==OrderStatus.PENDING){
                        //true
                        RadioButton readyStatus = ((RadioButton) dialogLayout.findViewById(R.id.ready_status));
                        RadioButton dispatchStatus = ((RadioButton) dialogLayout.findViewById(R.id.dispatch_status));
                        readyStatus.setEnabled(true);
                        readyStatus.setChecked(true);
                        dispatchStatus.setChecked(false);

                    }
                    else {
                        RadioButton readyStatus = ((RadioButton) dialogLayout.findViewById(R.id.ready_status));
                        RadioButton dispatchStatus = ((RadioButton) dialogLayout.findViewById(R.id.dispatch_status));
                        readyStatus.setEnabled(false);
                        readyStatus.setChecked(false);
                        dispatchStatus.setChecked(true);
                    }
                    LinearLayout dialogHeader = (LinearLayout) LayoutInflater.from(orderAdapter_ctx).inflate(R.layout.change_status_dialog_title, null);
                    ((TextView) dialogHeader.findViewById(R.id.change_status_message)).setText("Change Status for " + order.getOrderCode());
                    new AlertDialog.Builder(orderAdapter_ctx)
                            .setCustomTitle(dialogHeader)
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    RadioGroup radioGroup = ((RadioGroup) dialogLayout.findViewById(R.id.change_status_dialog_radio));
                                    final OrderStatus orderStatus;
                                    switch (radioGroup.getCheckedRadioButtonId())
                                    {
                                        case R.id.ready_status:
                                            orderStatus=OrderStatus.READY;

                                            OrderEndPoint endPoint = retrofit.create(OrderEndPoint.class);
                                            OrderService.readyOrder((Activity) orderAdapter_ctx, retrofit, endPoint, new OnWebServiceCallDoneEventListener() {
                                                @Override
                                                public void onDone(int message_id, int code, Object... args) {
                                                    //successful
                                                    order.setStatus(orderStatus);
                                                    Toast.makeText(orderAdapter_ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onContingencyError(int code) {
                                                    displayContingencyError((Activity) orderAdapter_ctx, 0);
                                                }

                                                @Override
                                                public void onError(int message_id, int code, String... args) {
                                                    displayError((Activity) orderAdapter_ctx, message_id, Snackbar.LENGTH_LONG);
                                                }
                                            }, order.getId());
                                            break;
                                        case R.id.dispatch_status:
                                            orderStatus=OrderStatus.DISPATCHED;

                                            OrderEndPoint endPoint1 = retrofit.create(OrderEndPoint.class);
                                            OrderService.shipOrder((Activity) orderAdapter_ctx, retrofit, endPoint1, new OnWebServiceCallDoneEventListener() {
                                                @Override
                                                public void onDone(int message_id, int code, Object... args) {
                                                    //successful
                                                    order.setStatus(orderStatus);
                                                    Toast.makeText(orderAdapter_ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onContingencyError(int code) {
                                                    displayContingencyError((Activity) orderAdapter_ctx, 0);
                                                }

                                                @Override
                                                public void onError(int message_id, int code, String... args) {
                                                    displayError((Activity) orderAdapter_ctx, message_id, Snackbar.LENGTH_LONG);
                                                }
                                            }, order.getId());
                                            break;
                                        case R.id.cancel_status:
                                            orderStatus=OrderStatus.CANCELLED;

                                            OrderEndPoint endPoint2 = retrofit.create(OrderEndPoint.class);
                                            OrderService.cancelOrder((Activity) orderAdapter_ctx, retrofit, endPoint2, new OnWebServiceCallDoneEventListener() {
                                                @Override
                                                public void onDone(int message_id, int code, Object... args) {
                                                    //successful
                                                    order.setStatus(orderStatus);
                                                    Toast.makeText(orderAdapter_ctx,"os: "+orderStatus,Toast.LENGTH_SHORT).show();
                                                    notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onContingencyError(int code) {
                                                    displayContingencyError((Activity) orderAdapter_ctx, 0);
                                                }

                                                @Override
                                                public void onError(int message_id, int code, String... args) {
                                                    displayError((Activity) orderAdapter_ctx, message_id, Snackbar.LENGTH_LONG);
                                                }
                                            }, order.getId());
                                            break;
                                        default:
                                            orderStatus=OrderStatus.PENDING;
                                    }
                                    order.setStatus(orderStatus);
                                    notifyDataSetChanged();

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setView(dialogLayout)
                            .show();



                    return true;
                }
                return false;
            }
        });
        
        

        //

    }
    
    

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(filterList != null) return filterList.size();
        else return 0;
    }

    public void setmDataset(List<Order> listItems) {
        this.listItems = listItems;
        this.filterList.clear();
        this.filterList.addAll(listItems);
    }

    public void filter(final String text, final OnFilterDoneListener filterEvent) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterList.addAll(listItems);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (Order item : listItems) {
                        if (item.getOrderCode().toLowerCase().contains(text.toLowerCase()) ||
                                text.toLowerCase().contains(item.getOrderCode().toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) orderAdapter_ctx).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                        filterEvent.filterDone(filterList.isEmpty());
                    }
                });

            }
        }).start();

    }
}