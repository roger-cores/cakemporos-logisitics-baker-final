package in.cakemporos.logistics.cakemporoslogistics.web.services;

import android.app.Activity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.CustomerEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.LocalityEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Error;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by bloss on 13/8/16.
 */
public class CustomerService {
    public static void getAllCustomers(final Activity activity,
                                        final Retrofit retrofit,
                                        final CustomerEndPoint customerEndPoint,
                                        final OnWebServiceCallDoneEventListener event){
        Call<List<Customer>> callForGetAllLocalities = customerEndPoint.getAllCustomers(Utility.getKey(activity).getAccess());
        callForGetAllLocalities.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
                    Converter<ResponseBody, Error> errorConverter =
                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                    try {
                        Error error = errorConverter.convert(response.errorBody());
                        switch (error.getError()) {
                            case "Unauthorized":
                                event.onError(R.string.unauthorized, 0);
                                return;
                            default:
                                event.onContingencyError(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if(response != null && response.body() != null){
                    List<Customer> customers = null;
                    customers = response.body();

                    event.onDone(R.string.success, 1, customers);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }
}
