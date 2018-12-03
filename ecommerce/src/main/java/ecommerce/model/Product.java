package ecommerce.model;

import java.math.BigDecimal;

public class Product {
	private String productId;
	private String productName;
	private String description;
	private String longDescription;
	private String partNumber;
	private String sTDPartNumber;
	private BigDecimal quantiryIncrementOrder;
	private BigDecimal maximumOrderQuantity;
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getsTDPartNumber() {
		return sTDPartNumber;
	}
	public void setsTDPartNumber(String sTDPartNumber) {
		this.sTDPartNumber = sTDPartNumber;
	}
	public BigDecimal getQuantiryIncrementOrder() {
		return quantiryIncrementOrder;
	}
	public void setQuantiryIncrementOrder(BigDecimal quantiryIncrementOrder) {
		this.quantiryIncrementOrder = quantiryIncrementOrder;
	}
	public BigDecimal getMaximumOrderQuantity() {
		return maximumOrderQuantity;
	}
	public void setMaximumOrderQuantity(BigDecimal maximumOrderQuantity) {
		this.maximumOrderQuantity = maximumOrderQuantity;
	}
	
}
