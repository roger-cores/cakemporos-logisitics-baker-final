package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.AuthResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.FCMRegRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.OTPResponse;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.ValidateRequest;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Baker;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    @PUT("user/change-pass")
    public Call<Response> changePassword(@Header("x-access-token") String accessToken, @Body AuthRequest authRequest);

    @PUT("user/updateReg")
    public Call<Response> updateReg(@Header("x-access-token") String accessToken, @Body FCMRegRequest regRequest);

    @GET("user/forgot-pass/{email}")
    public Call<Response> forgotPassword(@Path("email") String email);

    @GET("user/verify-otp/{email}/{input}")
    public Call<OTPResponse> verifyOtp(@Path("email") String email, @Path("input") String input);

    @PUT("user/change-forgotten-pass/{email}/{sessionId}")
    public Call<Response> changeForgottenPassword(@Path("email") String email, @Path("sessionId") String sessionId, @Body AuthRequest request);

}
