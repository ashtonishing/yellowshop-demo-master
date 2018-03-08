package com.buyopicadmin.admin.models;


public class OrderList {

	String orderTime;
	String orderName;
	String orderStatus;

	String requestedDelivery;
	String state;
	String expectedDelivery;
	String itemPrice;

	String addressLine3;
	String addressLine2;
	String addressLine1;

	String totalOrderPrice;
	String consumerId;
	String city;
	String offerItemName;

	String consumerName;
	String pincode;
	String statusId;

	String statusDesc;

	String offerItemId;
	String orderQuantity;
	String orderId;
	String status_desc;
	String status_id;
	String status_list;
	String mConsumerPhoneNo;
	String mConsumerMailId;
	private StatusList statusListObj;

	public String getmConsumerPhoneNo() {
		return mConsumerPhoneNo;
	}

	public void setmConsumerPhoneNo(String mConsumerPhoneNo) {
		this.mConsumerPhoneNo = mConsumerPhoneNo;
	}

	public String getmConsumerMailId() {
		return mConsumerMailId;
	}

	public void setmConsumerMailId(String mconsumerMailId) {
		this.mConsumerMailId = mconsumerMailId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRequestedDelivery() {
	/*	String newFormart = "MMM dd yyyy hh:mm a";
		String oldFormart = "yyyy-mm-dd hh:mm a";
		SimpleDateFormat df1 = new SimpleDateFormat(oldFormart);
		SimpleDateFormat df2 = new SimpleDateFormat(newFormart);
		String value = null;
		try {
			value = df2.format(df1.parse(requestedDelivery));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return requestedDelivery;
	}

	public void setRequestedDelivery(String requestedDelivery) {
		this.requestedDelivery = requestedDelivery;
	}

	public String getExpectedDelivery() {
		return expectedDelivery;
	}

	public void setExpectedDelivery(String expectedDelivery) {
		this.expectedDelivery = expectedDelivery;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getTotalOrderPrice() {
		return totalOrderPrice;
	}

	public void setTotalOrderPrice(String totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOfferItemName() {
		return offerItemName;
	}

	public void setOfferItemName(String offerItemName) {
		this.offerItemName = offerItemName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getOfferItemId() {
		return offerItemId;
	}

	public void setOfferItemId(String offerItemId) {
		this.offerItemId = offerItemId;
	}

	public String getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus_desc() {
		return status_desc;
	}

	public void setStatus_desc(String status_desc) {
		this.status_desc = status_desc;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}

	public String getStatus_list() {
		return status_list;
	}

	public void setStatus_list(String status_list) {
		this.status_list = status_list;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setStatusListObj(StatusList statusListObj) {
		// TODO Auto-generated method stub
		this.statusListObj = statusListObj;
	}

	public StatusList getStatusListObj() {
		// TODO Auto-generated method stub
		return statusListObj;
	}
}
