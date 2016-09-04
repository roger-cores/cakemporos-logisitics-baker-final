package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roger on 10/8/16.
 */
public class EntityBase implements Serializable{

    @SerializedName("_id")
    private String id;

    @SerializedName("__v")
    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
