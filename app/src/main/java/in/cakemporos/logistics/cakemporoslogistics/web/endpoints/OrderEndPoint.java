package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.Response;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.EntityBase;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Order;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by roger on 10/8/16.
 */
public interface OrderEndPoint {
    @GET("user/baker/order")
    public Call<List<Order>> getMyOrders(@Header("x-access-token") String accessToken);

    @POST("user/baker/order")
    public Call<EntityBase> createOrder(@Header("x-access-token") String accessToken, @Body Order order);

    @PUT("user/baker/order/{orderid}/ship")
    public Call<Response> shipOrder(@Header("x-access-token") String accessToken,@Path("orderid") String id);

    @PUT("user/baker/order/{orderid}/ready")
    public Call<Response> readyOrder(@Header("x-access-token") String accessToken,@Path("orderid") String id);

    @PUT("user/baker/order/{orderid}/cancel")
    public Call<Response> cancelOrder(@Header("x-access-token") String accessToken,@Path("orderid") String id);





}
