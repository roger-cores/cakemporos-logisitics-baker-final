package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

/**
 * Created by roger on 10/8/16.
 */
public class Baker extends EntityBase{

    private Login user;

    private Locality locality;

    private String address;

    private String referal;

    public String getReferal() {
        return referal;
    }

    public void setReferal(String referal) {
        this.referal = referal;
    }

    public Login getUser() {
        return user;
    }

    public void setUser(Login user) {
        this.user = user;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
