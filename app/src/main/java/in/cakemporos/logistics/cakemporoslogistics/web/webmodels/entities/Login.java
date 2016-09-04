package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.UserType;

/**
 * Created by roger on 10/8/16.
 */
public class Login extends EntityBase{
    private String name;

    private String phone;

    private String email;

    private String password;

    private UserType userType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
