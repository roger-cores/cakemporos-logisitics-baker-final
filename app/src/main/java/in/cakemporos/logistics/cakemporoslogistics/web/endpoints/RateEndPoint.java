package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Rate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Maitreya on 03-Sep-16.
 */
public interface RateEndPoint {

    @GET("user/baker/rate")
    public Call<List<Rate>> getAllRates(@Header("x-access-token") String accessToken);

}
