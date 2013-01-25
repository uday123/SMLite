package com.lbo.sml.entity;

import org.sakaiproject.entitybus.entityprovider.annotations.EntityFieldRequired;
import org.sakaiproject.entitybus.entityprovider.annotations.EntityId;

/**
 * 
 * @author ukola
 * 
 * Vendor entity has following attributes mapped with corresponding persistent entity columns. 
 *
 */
public class VendorEntity {

	@EntityId
	private String id;
	public static String ID = "id";

	@EntityFieldRequired
	private String name;
	public static String NAME = "name";
	
	private boolean poCreated;
	public static String PO_CREATED = "poCreated";
	
	private long poNumber;
	public static String PO_NUMBER = "poNumber";
	
	private String poDetails;
	public static String PO_DETAILS = "poDetails";	
	
	public String type; 
	public static String TYPE = "type";

	public VendorEntity() {
	}

	public VendorEntity(String id, String name, boolean poCreated, long poNumber, String poDetails, String type) {
		super();
		this.id = id;
		this.setName(name);
		this.setPoCreated(poCreated);
		this.setPoNumber(poNumber);
		this.setPoDetails(poDetails);
		this.setType(type);
	}

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPoCreated() {
		return poCreated;
	}

	public void setPoCreated(boolean poCreated) {
		this.poCreated = poCreated;
	}

	public long getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(long poNumber) {
		this.poNumber = poNumber;
	}

	public String getPoDetails() {
		return poDetails;
	}

	public void setPoDetails(String poDetails) {
		this.poDetails = poDetails;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ID+":" + this.id + ", "+NAME+":" + this.name + ", "+PO_CREATED+":"
				+ this.poCreated + ", "+PO_NUMBER+":" + this.poNumber
				+ ", "+PO_DETAILS+":" + this.poDetails + ", "+TYPE+":" + this.type;
	}
}
