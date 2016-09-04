package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import in.cakemporos.logistics.cakemporoslogistics.R;

/**
 * Created by roger on 10/8/16.
 */
public class AuthRequest {

    @SerializedName("grant_type")
    private String grantType;

    private String username;

    private String password;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;

    public AuthRequest(String username, String password, Context context) {
        this.username = username;
        this.password = password;
        clientId = context.getString(R.string.client_id);
        clientSecret = context.getString(R.string.client_secret);
        grantType = context.getString(R.string.grant_password);
    }

    public AuthRequest(String refreshToken, Context context) {
        this.refreshToken = refreshToken;
        clientId = context.getString(R.string.client_id);
        clientSecret = context.getString(R.string.client_secret);
        grantType = context.getString(R.string.grant_refresh);
    }



    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
