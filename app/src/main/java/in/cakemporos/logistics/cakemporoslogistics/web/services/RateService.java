package in.cakemporos.logistics.cakemporoslogistics.web.services;

import android.app.Activity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.dbase.Utility;
import in.cakemporos.logistics.cakemporoslogistics.events.OnWebServiceCallDoneEventListener;
import in.cakemporos.logistics.cakemporoslogistics.web.endpoints.RateEndPoint;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Rate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Maitreya on 03-Sep-16.
 */
public class RateService {
    public static void getAllRates(final Activity activity,
                                   final Retrofit retrofit,
                                   final RateEndPoint endPoint,
                                   final OnWebServiceCallDoneEventListener event){
        final Call<List<Rate>> getMyInfoCall = endPoint.getAllRates(Utility.getKey(activity).getAccess());

        getMyInfoCall.enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, retrofit2.Response<List<Rate>> response) {
                if(response != null && !response.isSuccessful() && response.errorBody() != null){
                    event.onContingencyError(0);
                }
                else {
                    List<Rate> rates = response.body();
                    if(rates!=null && rates.size()!=0) {
                        event.onDone(R.string.success, 0, rates);
                    } else {
                        event.onContingencyError(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                if(t instanceof IOException){
                    event.onError(R.string.offline, 2);
                } else if(t instanceof SocketTimeoutException){
                    event.onError(R.string.request_timed_out, 3);
                } else event.onContingencyError(0);
            }
        });
    }
}
