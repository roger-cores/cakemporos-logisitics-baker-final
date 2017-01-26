package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by roger on 10/8/16.
 */
public class Locality extends EntityBase{

    public String name;

    public String placeId;

    private Double lat;

    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
