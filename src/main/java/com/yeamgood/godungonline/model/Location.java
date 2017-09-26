package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "location")
public class Location extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "location_Id")
	private Long locationId;
	
	@Column(name = "location_code")
	@NotEmpty(message = "{form.location.valid.code}")
	@Length(max = 20, message = "{validation.max.lenght}")
	private String locationCode;
	
	@Column(name = "aisle")
	@Length(max = 3, message = "{validation.max.lenght}")
	private String aisle;
	
	@Column(name = "unit")
	@Length(max = 3, message = "{validation.max.lenght}")
	private String unit;
	
	@Column(name = "shelf")
	@Length(max = 3, message = "{validation.max.lenght}")
	private String shelf;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@Transient
	private String warehouseIdEncrypt;
	
	@Transient
	private String locationIdEncrypt;
	
	public void setObject(Location location) {
		this.locationCode = location.getLocationCode();
		this.aisle = location.getAisle();
		this.unit = location.getUnit();
		this.shelf = location.getShelf();
		this.description = location.getDescription();
	}
	
	public void encryptData() {
		this.locationIdEncrypt = AESencrpUtils.encryptLong(this.locationId);
		this.locationId = null;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getAisle() {
		return aisle;
	}

	public void setAisle(String aisle) {
		this.aisle = aisle;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWarehouseIdEncrypt() {
		return warehouseIdEncrypt;
	}

	public void setWarehouseIdEncrypt(String warehouseIdEncrypt) {
		this.warehouseIdEncrypt = warehouseIdEncrypt;
	}

	public String getLocationIdEncrypt() {
		return locationIdEncrypt;
	}

	public void setLocationIdEncrypt(String locationIdEncrypt) {
		this.locationIdEncrypt = locationIdEncrypt;
	}

	@Override
	public String toString() {
		return "Location [locationId=" + locationId + ", locationCode=" + locationCode + ", aisle=" + aisle + ", unit="
				+ unit + ", shelf=" + shelf + ", description=" + description + ", warehouseIdEncrypt="
				+ warehouseIdEncrypt + ", locationIdEncrypt=" + locationIdEncrypt + "]";
	}

}
