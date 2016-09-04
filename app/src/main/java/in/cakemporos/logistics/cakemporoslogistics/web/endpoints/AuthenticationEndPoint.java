package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.ValidateRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Roger Cores on 1/8/16.
 */
public interface AuthenticationEndPoint {

    /**
     * Returns accessToken using grantType=password or grantType=refreshToken
     * @param loginRequest
     * @return
     */
    @POST("user/oauth/token")
    public Call<AuthResponse> getToken(@Body AuthRequest loginRequest);

    /**
     * Validates a newly fetched accessToken and starts a session with the server
     * @param validateRequest
     * @return
     */
    @POST("user/validate-token")
    public Call<Response> validateToken(@Body ValidateRequest validateRequest);


    @GET("user/userinfo")
    public Call<Baker> getMyInfo(@Header("x-access-token") String accessToken);



}
