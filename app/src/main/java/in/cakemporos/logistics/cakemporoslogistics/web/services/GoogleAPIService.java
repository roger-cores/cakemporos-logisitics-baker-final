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
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.GoogleAPIEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Error;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.GoogleDistanceResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by roger on 11/27/2016.
 */
public class GoogleAPIService {

    public static void getAllCustomers(final Activity activity,
                                       final Retrofit retrofit,
                                       final GoogleAPIEndPoint googleAPIEndPoint,
                                       final String origin,
                                       final String destination,
                                       final OnWebServiceCallDoneEventListener event){
        Call<GoogleDistanceResponse> callForGetAllLocalities = googleAPIEndPoint.getDistanceBetweenPlaces(origin, destination, activity.getString(R.string.api_key), "metric");
        callForGetAllLocalities.enqueue(new Callback<GoogleDistanceResponse>() {
            @Override
            public void onResponse(Call<GoogleDistanceResponse> call, Response<GoogleDistanceResponse> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null) {
                    //Branch: Error
                    event.onContingencyError(0);
                } else if(response != null && response.body() != null){
                    GoogleDistanceResponse googleDistanceResponse = null;
                    googleDistanceResponse = response.body();

                    event.onDone(R.string.success, 1, googleDistanceResponse);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<GoogleDistanceResponse> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }
}
