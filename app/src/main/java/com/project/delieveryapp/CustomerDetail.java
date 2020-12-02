package com.project.delieveryapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetail {

//    String CustomerId="";
//    String CustomerName="";
//    String Latitude="";
//    String Longitude="";
//    String MobileNo="";
//    String Distance="";
//    String Address="";
//    String Orderid="";
//    String Ordertype="";
//    String AssignID="";
//    String deliveryboyemail;
//    String deliverboyfirebaseid;
//    String deliveryboymobileno;

    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Distance")
    @Expose
    private String distance;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("PurchaseTypeID")
    @Expose
    private Integer purchaseTypeID;
    @SerializedName("OrderStatusID")
    @Expose
    private Integer orderStatusID;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("PaymentTypeID")
    @Expose
    private Integer paymentTypeID;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("paymentstatus")
    @Expose
    private String paymentstatus;
    @SerializedName("ServiceName")
    @Expose
    private String serviceName;
    @SerializedName("ServiceTypeID")
    @Expose
    private Integer serviceTypeID;
    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("DeliveryCharages")
    @Expose
    private Float deliveryCharages;
    @SerializedName("DeliveryAssigned")
    @Expose
    private String deliveryAssigned;
    @SerializedName("AddressLine1")
    @Expose
    private String addressLine1;
    @SerializedName("AddressLine2")
    @Expose
    private String addressLine2;
    @SerializedName("Zipcode")
    @Expose
    private String zipcode;
    @SerializedName("IsDelevered")
    @Expose
    private Boolean isDelevered;
    @SerializedName("AssignID")
    @Expose
    private String assignID;
    @SerializedName("AssignedDate")
    @Expose
    private String assignedDate;
    @SerializedName("FirebaseID")
    @Expose
    private String firebaseID;


    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("MobileNo1")
    @Expose
    private String MobileNo1;

    @SerializedName("Ordertype")
    @Expose
    private String Ordertype;


    public CustomerDetail(String xcustomerId, String xcustomerName, String xlatitude, String xlongitude, String xmobileNo, String xdistance, String xaddress,
                          String xorderid, String xordertype, String xassignid, String xDboyemail, String xDboyfirebaseid, String xDboymobileno) {
        customerID = xcustomerId;
        customerName = xcustomerName;
        latitude = xlatitude;
        longitude = xlongitude;
        mobileNo = xmobileNo;
        distance = xdistance;
        addressLine1 = xaddress;
        orderID = xorderid;
        paymentType = xordertype;
        assignID = xassignid;
        email = xDboyemail;
        firebaseID = xDboyfirebaseid;
        MobileNo1 = xDboymobileno;
    }


    public Integer getPurchaseTypeID() {
        return purchaseTypeID;
    }

    public void setPurchaseTypeID(Integer purchaseTypeID) {
        this.purchaseTypeID = purchaseTypeID;
    }

    public Integer getOrderStatusID() {
        return orderStatusID;
    }

    public void setOrderStatusID(Integer orderStatusID) {
        this.orderStatusID = orderStatusID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(Integer paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getServiceTypeID() {
        return serviceTypeID;
    }

    public void setServiceTypeID(Integer serviceTypeID) {
        this.serviceTypeID = serviceTypeID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Float getDeliveryCharages() {
        return deliveryCharages;
    }

    public void setDeliveryCharages(Float deliveryCharages) {
        this.deliveryCharages = deliveryCharages;
    }

    public String getDeliveryAssigned() {
        return deliveryAssigned;
    }

    public void setDeliveryAssigned(String deliveryAssigned) {
        this.deliveryAssigned = deliveryAssigned;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Object getAddressLine2() {
        return addressLine2;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Boolean getIsDelevered() {
        return isDelevered;
    }

    public void setIsDelevered(Boolean isDelevered) {
        this.isDelevered = isDelevered;
    }

    public void setAssignID(String assignID) {
        this.assignID = assignID;
    }

    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(String firebaseID) {
        this.firebaseID = firebaseID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public Boolean getDelevered() {
        return isDelevered;
    }

    public void setDelevered(Boolean delevered) {
        isDelevered = delevered;
    }

    public String getAssignID() {
        return assignID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo1() {
        return MobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
        MobileNo1 = mobileNo1;
    }
}
