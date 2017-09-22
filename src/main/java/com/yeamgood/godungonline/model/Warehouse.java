package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "warehouse")
public class Warehouse extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "warehouse_Id")
	private Long warehouseId;
	
	@Transient
	private String warehouseIdEncrypt;
	
	@Column(name = "warehouse_code")
	private String warehouseCode;
	
	@Column(name = "warehouse_name")
	@NotEmpty(message = "{form.warehouse.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String warehouseName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval=true)
	@JoinTable(name = "warehouse_location", joinColumns = @JoinColumn(name = "warehouse_Id"), inverseJoinColumns = @JoinColumn(name = "location_id"))
	private List<Location> locationList;
	
	public void setObject(Warehouse warehouse) {
		this.warehouseName = warehouse.getWarehouseName();
		this.description  = warehouse.getDescription();
	}
	
	public void encryptData(Warehouse warehouse) {
		this.warehouseIdEncrypt = AESencrpUtils.encryptLong(warehouse.getWarehouseId());
		this.warehouseId = null;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public String getWarehouseIdEncrypt() {
		return warehouseIdEncrypt;
	}

	public void setWarehouseIdEncrypt(String warehouseIdEncrypt) {
		this.warehouseIdEncrypt = warehouseIdEncrypt;
	}
	
	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	@Override
	public String toString() {
		return "Warehouse [warehouseId=" + warehouseId + ", warehouseIdEncrypt=" + warehouseIdEncrypt
				+ ", warehouseCode=" + warehouseCode + ", warehouseName=" + warehouseName + ", description="
				+ description + ", godung=" + godung + ", locationList=" + locationList + "]";
	}

}
