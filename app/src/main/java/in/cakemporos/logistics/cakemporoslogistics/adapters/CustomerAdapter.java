package in.cakemporos.logistics.cakemporoslogistics.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.activities.SelectCustomerActivity;
import in.cakemporos.logistics.cakemporoslogistics.events.OnFilterDoneListener;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;

/**
 * Created by bloss on 13/8/16.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    //private Customer[] mDataset;

    private List<Customer> listItems, filterList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView customer_name_cv,customer_phone_cv,customer_address_cv,customer_locality_cv;
        public ViewHolder(View v) {
            super(v);
            customer_name_cv = (TextView) v.findViewById(R.id.customer_first_name_cv);
            customer_phone_cv = (TextView) v.findViewById(R.id.customer_phone_cv);
            customer_address_cv = (TextView) v.findViewById(R.id.customer_address_cv);
            customer_locality_cv = (TextView) v.findViewById(R.id.customer_locality_cv);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomerAdapter(Context context, List<Customer> listItems) {
        this.context = context;
        this.listItems = listItems;
        filterList = new ArrayList<>();
        filterList.addAll(listItems);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_select_customer, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String customer_name=filterList.get(position).getFirstName()+" "+filterList.get(position).getLastName();
        String customer_phone=filterList.get(position).getPhone()+"";
        holder.customer_name_cv.setText(customer_name);
        holder.customer_phone_cv.setText(customer_phone);
        holder.customer_address_cv.setText(filterList.get(position).getAddress());
        holder.customer_locality_cv.setText(filterList.get(position).getLocality().getName());
        //onclick

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(filterList != null) return filterList.size();
        else return 0;
    }

    public void setmDataset(List<Customer> listItems) {
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
                    for (Customer item : listItems) {
                        if (item.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getLastName().toLowerCase().contains(text.toLowerCase()) ||
                                text.toLowerCase().contains(item.getFirstName().toLowerCase())||
                                text.toLowerCase().contains(item.getLastName().toLowerCase()) ||
                                (item.getFirstName() + " " + item.getLastName()).toLowerCase().contains(text.toLowerCase())||
                                (text).toLowerCase().contains((item.getFirstName() + " " + item.getLastName()).toLowerCase())) {
                            // Adding Matched items
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
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