package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roger on 10/8/16.
 */
public class Error {

    private String error;

    @SerializedName("error_description")
    private String errorDescription;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
