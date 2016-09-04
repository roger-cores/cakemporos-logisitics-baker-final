package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.RiderStatus;

/**
 * Created by roger on 10/8/16.
 */
public class Rider extends EntityBase {
    private Login user;

    private RiderStatus status;

    private Order order1;

    private Order order2;

    public Order getOrder2() {
        return order2;
    }

    public void setOrder2(Order order2) {
        this.order2 = order2;
    }

    public RiderStatus getStatus() {
        return status;
    }

    public void setStatus(RiderStatus status) {
        this.status = status;
    }

    public Order getOrder1() {
        return order1;
    }

    public void setOrder1(Order order1) {
        this.order1 = order1;
    }

    public Login getUser() {
        return user;
    }

    public void setUser(Login user) {
        this.user = user;
    }
}
