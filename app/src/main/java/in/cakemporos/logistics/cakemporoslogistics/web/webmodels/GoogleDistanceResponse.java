package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by roger on 11/27/2016.
 */
public class GoogleDistanceResponse {

    @SerializedName("destination_address")
    private String destinationAddress;

    @SerializedName("origin_address")
    private String originAddress;

    private List<GoogleDistanceElementWrapper> rows;

    private String status;

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public List<GoogleDistanceElementWrapper> getRows() {
        return rows;
    }

    public void setRows(List<GoogleDistanceElementWrapper> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
