package in.cakemporos.logistics.cakemporoslogistics.web.services;

import android.app.Activity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.LocalityEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.OrderEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Error;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
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
public class LocalityService {
    public static void getAllLocalities(final Activity activity,
                                 final Retrofit retrofit,
                                 final LocalityEndPoint localityEndPoint,
                                 final OnWebServiceCallDoneEventListener event){
        Call<List<Locality>> callForGetAllLocalities = localityEndPoint.getAllLocalities(Utility.getKey(activity).getAccess());
        callForGetAllLocalities.enqueue(new Callback<List<Locality>>() {
            @Override
            public void onResponse(Call<List<Locality>> call, Response<List<Locality>> response) {
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
                    List<Locality> localities = null;
                    localities = response.body();

                    event.onDone(R.string.success, 1, localities);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<List<Locality>> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }

    public static void getLocalitiesBySearch(final Activity activity,
                                      final Retrofit retrofit,
                                      final LocalityEndPoint localityEndPoint,
                                      final OnWebServiceCallDoneEventListener event,
                                      final String query){
        Call<List<Locality>> callForSearchLocalities = localityEndPoint.getLocalitiesBySearch(query, Utility.getKey(activity).getAccess());
        callForSearchLocalities.enqueue(new Callback<List<Locality>>() {
            @Override
            public void onResponse(Call<List<Locality>> call, Response<List<Locality>> response) {
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
                    List<Locality> localities = null;
                    localities = response.body();

                    event.onDone(R.string.success, 1, localities);
                } else {
                    event.onContingencyError(0);
                }
            }

            @Override
            public void onFailure(Call<List<Locality>> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }
}
