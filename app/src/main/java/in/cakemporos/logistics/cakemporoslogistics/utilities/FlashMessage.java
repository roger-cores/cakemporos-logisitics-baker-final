package in.cakemporos.logistics.cakemporoslogistics.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import in.cakemporos.logistics.cakemporoslogistics.R;


/**
 * Created by Roger Cores on 2/8/16.
 */
public class FlashMessage {
    public static void displayContingencyError(Activity activity, int duration){

        //TODO Add icon to snackbar

        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), activity.getString(R.string.error_contingency), duration)
                .setActionTextColor(Color.RED);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if(tv!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        //TODO

        snack.show();
    }

    public static void displayError(Activity activity, int id, int duration, String... args){

        //TODO Add icon to snackbar

        String message;
        if(args != null){
            message = String.format(activity.getString(id), (Object[]) args);
        } else message = activity.getString(id);

        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), message, duration)
                .setActionTextColor(Color.RED);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if(tv!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        //TODO

        snack.show();
    }

    public static void displayMessage(Activity activity, String message, int duration){

        //TODO Add icon to snackbar


        Snackbar snack = Snackbar.make(activity.findViewById(android.R.id.content), message, duration)
                .setActionTextColor(Color.RED);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if(tv!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        //TODO

        snack.show();
    }


}
