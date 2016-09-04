package in.cakemporos.logistics.cakemporoslogistics.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.cakemporos.logistics.cakemporoslogistics.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bloss on 13/8/16.
 */
public class Factory {
    public static Retrofit createClient(String baseUrl){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd-MM-yyyy HH:mm:ss")
                .create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }
}
