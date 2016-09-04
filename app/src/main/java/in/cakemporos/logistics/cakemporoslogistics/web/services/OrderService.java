package in.cakemporos.logistics.cakemporoslogistics.web.services;

import android.app.Activity;
import android.support.design.widget.Snackbar;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.utilities.FlashMessage;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Error;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.EntityBase;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by roger on 10/8/16.
 */
public class OrderService {
    public static void getMyOrders(final Activity activity,
                                   final Retrofit retrofit,
                                   final OrderEndPoint orderEndPoint,
                                   final OnWebServiceCallDoneEventListener event){

        Call<List<Order>> getOrdersCall = orderEndPoint.getMyOrders(Utility.getKey(activity).getAccess());

        getOrdersCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
//                    Converter<ResponseBody, Error> errorConverter =
//                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
//                    try {
//                        Error error = errorConverter.convert(response.errorBody());
//                        switch (error.getError()) {
//                            case "Unauthorized":
//                                event.onError(R.string.unauthorized, 0);
//                                return;
//                            default:
//                                event.onContingencyError(0);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    List<Order> orders = null;
                    orders = response.body();

                    event.onDone(R.string.success, 1, orders);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }

    public static void createOrder(final Order order,
                            final Activity activity,
                            final Retrofit retrofit,
                            final OrderEndPoint orderEndPoint,
                            final OnWebServiceCallDoneEventListener event){
        Call<EntityBase> callForCreateOrder = orderEndPoint.createOrder(Utility.getKey(activity).getAccess(), order);
        callForCreateOrder.enqueue(new Callback<EntityBase>() {
            @Override
            public void onResponse(Call<EntityBase> call, Response<EntityBase> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
//                    Converter<ResponseBody, Error> errorConverter =
//                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
//                    try {
//                        Error error = errorConverter.convert(response.errorBody());
//                        switch (error.getError()) {
//                            case "Validation failed":
//                                event.onError(R.string.bad_input, 0);
//                                break;
//                            case "Unauthorized":
//                                event.onError(R.string.unauthorized, 0);
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    EntityBase order;
                    order = response.body();

                    event.onDone(R.string.success, 1, order);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<EntityBase> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);

                //FlashMessage.displayMessage(activity, , Snackbar.LENGTH_LONG);
            }
        });
    }




    public static void shipOrder(final Activity activity,
                                   final Retrofit retrofit,
                                   final OrderEndPoint orderEndPoint,
                                   final OnWebServiceCallDoneEventListener event,
                                   final String id){
        Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> callForShip = orderEndPoint.shipOrder(Utility.getKey(activity).getAccess(), id);
        callForShip.enqueue(new Callback<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response>() {
            @Override
            public void onResponse(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Response<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
//                    Converter<ResponseBody, Error> errorConverter =
//                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
//                    try {
//                        Error error = errorConverter.convert(response.errorBody());
//                        switch (error.getError()) {
//                            case "Validation failed":
//                                event.onError(R.string.bad_input, 0);
//                                break;
//                            case "Unauthorized":
//                                event.onError(R.string.unauthorized, 0);
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    event.onDone(R.string.success, 1);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });


    }

    public static void readyOrder(final Activity activity,
                                 final Retrofit retrofit,
                                 final OrderEndPoint orderEndPoint,
                                 final OnWebServiceCallDoneEventListener event,
                                 final String id){
        Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> callForShip = orderEndPoint.readyOrder(Utility.getKey(activity).getAccess(), id);
        callForShip.enqueue(new Callback<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response>() {
            @Override
            public void onResponse(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Response<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
//                    Converter<ResponseBody, Error> errorConverter =
//                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
//                    try {
//                        Error error = errorConverter.convert(response.errorBody());
//                        switch (error.getError()) {
//                            case "Validation failed":
//                                event.onError(R.string.bad_input, 0);
//                                break;
//                            case "Unauthorized":
//                                event.onError(R.string.unauthorized, 0);
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    event.onDone(R.string.success, 1);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });


    }

    public static void cancelOrder(final Activity activity,
                                 final Retrofit retrofit,
                                 final OrderEndPoint orderEndPoint,
                                 final OnWebServiceCallDoneEventListener event,
                                 final String id){
        Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> callForShip = orderEndPoint.cancelOrder(Utility.getKey(activity).getAccess(), id);
        callForShip.enqueue(new Callback<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response>() {
            @Override
            public void onResponse(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Response<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
//                    Converter<ResponseBody, Error> errorConverter =
//                            retrofit.responseBodyConverter(Error.class, new Annotation[0]);
//                    try {
//                        Error error = errorConverter.convert(response.errorBody());
//                        switch (error.getError()) {
//                            case "Validation failed":
//                                event.onError(R.string.bad_input, 0);
//                                break;
//                            case "Unauthorized":
//                                event.onError(R.string.unauthorized, 0);
//                                break;
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    event.onDone(R.string.success, 1);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });


    }
}
