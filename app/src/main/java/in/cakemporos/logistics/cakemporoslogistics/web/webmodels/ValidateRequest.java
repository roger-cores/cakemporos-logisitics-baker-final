package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roger on 10/8/16.
 */
public class ValidateRequest {


    @SerializedName("access_token")
    private String accessToken;

    public ValidateRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
