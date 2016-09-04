package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import java.io.Serializable;

/**
 * Created by roger on 10/8/16.
 */
public class Customer extends EntityBase {

    public String firstName;

    public String lastName;

    public Locality locality;

    public String address;

    public Long phone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
