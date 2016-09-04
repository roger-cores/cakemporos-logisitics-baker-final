package in.cakemporos.logistics.cakemporoslogistics.web.endpoints;

import java.util.List;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Customer;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities.Locality;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by bloss on 13/8/16.
 */
public interface CustomerEndPoint {

    @GET("user/baker/customer")
    public Call<List<Customer>> getAllCustomers(@Header("x-access-token") String accessToken);

}
