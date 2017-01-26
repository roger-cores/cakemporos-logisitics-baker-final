package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

/**
 * Created by roger on 11/27/2016.
 */
public class GoogleDistanceElement {

    private Value distance;

    private Value duration;

    private String status;

    public Value getDistance() {
        return distance;
    }

    public void setDistance(Value distance) {
        this.distance = distance;
    }

    public Value getDuration() {
        return duration;
    }

    public void setDuration(Value duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
