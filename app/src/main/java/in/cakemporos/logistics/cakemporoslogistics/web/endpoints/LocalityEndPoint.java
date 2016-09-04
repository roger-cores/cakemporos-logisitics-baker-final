package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by roger on 10/8/16.
 */
public interface LocalityEndPoint {
    @GET("user/baker/locality")
    public Call<List<Locality>> getAllLocalities(@Header("x-access-token") String accessToken);


    @GET("user/baker/locality/{query}")
    public Call<List<Locality>> getLocalitiesBySearch(@Path("query") String query, @Header("x-access-token") String accessToken);
}
