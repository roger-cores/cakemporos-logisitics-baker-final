package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.GoogleDistanceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by roger on 11/27/2016.
 */
public interface GoogleAPIEndPoint {
    @POST("distancematrix/json")
    Call<GoogleDistanceResponse> getDistanceBetweenPlaces(@Query("origins") String origin,
                                                          @Query("destinations") String destination,
                                                          @Query("apiKey") String apiKey,
                                                          @Query("units") String units);
}
