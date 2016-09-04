package in.cakemporos.logistics.cakemporoslogistics.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;

/**
 * Created by bloss on 13/8/16.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private Customer[] mDataset;

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
    public CustomerAdapter(Customer[] myDataset) {
        mDataset = myDataset;
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
        String customer_name=mDataset[position].getFirstName()+" "+mDataset[position].getLastName();
        String customer_phone=mDataset[position].getPhone()+"";
        holder.customer_name_cv.setText(customer_name);
        holder.customer_phone_cv.setText(customer_phone);
        holder.customer_address_cv.setText(mDataset[position].getAddress());
        holder.customer_locality_cv.setText(mDataset[position].getLocality().getName());
        //onclick

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

        public void setmDataset(Customer[] mDataset1)
        {
            this.mDataset=mDataset1;
        }
}