package in.cakemporos.logistics.cakemporoslogistics.web.webmodels.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.CakeType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderStatus;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderType;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.OrderWeight;
import in.cakemporos.logistics.cakemporoslogistics.web.webmodels.enums.PaymentType;

/**
 * Created by roger on 10/8/16.
 */
public class Order extends EntityBase {
    private Baker baker;

    private Rider rider;

    private OrderStatus status;

    private CakeType cakeType;

    private Long cost;

    private Double totalCost;

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    private Double distance;


    private Date pickUpDate;

    private Date dropDate;

    @SerializedName("createOrderDate")
    private Date bookingDate;

    private Long altPhone;

    private OrderWeight weight;

    private Locality locality;

    private String address;

    private Customer customer;

    private Long dropAltPhone;

    private String orderCode;

    private String instructions;

    private OrderType orderType;

    private float estimatedCost;

    private PaymentType paymentType;

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public float getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(float estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Baker getBaker() {
        return baker;
    }

    public void setBaker(Baker baker) {
        this.baker = baker;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public CakeType getCakeType() {
        return cakeType;
    }

    public void setCakeType(CakeType cakeType) {
        this.cakeType = cakeType;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getDropDate() {
        return dropDate;
    }

    public void setDropDate(Date dropDate) {
        this.dropDate = dropDate;
    }

    public Long getAltPhone() {
        return altPhone;
    }

    public void setAltPhone(Long altPhone) {
        this.altPhone = altPhone;
    }

    public OrderWeight getWeight() {
        return weight;
    }

    public void setWeight(OrderWeight weight) {
        this.weight = weight;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getDropAltPhone() {
        return dropAltPhone;
    }

    public void setDropAltPhone(Long dropAltPhone) {
        this.dropAltPhone = dropAltPhone;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
