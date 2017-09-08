package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "province")
public class Province {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "province_id")
	private Long provinceId;
	
	@Column(name = "province_code")
	private String provinceCode;

	@Column(name = "province_name")
	private String provinceName;

	@Column(name = "geo_id")
	private String geoId;

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getGeoId() {
		return geoId;
	}

	public void setGeoId(String geoId) {
		this.geoId = geoId;
	}

	@Override
	public String toString() {
		return "Province [provinceId=" + provinceId + ", provinceCode=" + provinceCode + ", provinceName="
				+ provinceName + ", geoId=" + geoId + "]";
	}
	
	
}
