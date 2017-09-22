package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "measure")
public class Measure extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "measure_Id")
	private Long measureId;
	
	@Transient
	private String measureIdEncrypt;
	
	@Column(name = "measure_code")
	private String measureCode;
	
	@Column(name = "measure_name")
	@NotEmpty(message = "{form.measure.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String measureName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;
	
	public void encryptData(Measure measure) {
		this.measureIdEncrypt = AESencrpUtils.encryptLong(measure.getMeasureId());
		this.measureId = null;
	}

	public Long getMeasureId() {
		return measureId;
	}

	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}

	public String getMeasureCode() {
		return measureCode;
	}

	public void setMeasureCode(String measureCode) {
		this.measureCode = measureCode;
	}

	public String getMeasureName() {
		return measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
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
	
	public String getMeasureIdEncrypt() {
		return measureIdEncrypt;
	}

	public void setMeasureIdEncrypt(String measureIdEncrypt) {
		this.measureIdEncrypt = measureIdEncrypt;
	}

	@Override
	public String toString() {
		return "Measure [measureId=" + measureId + ", measureIdEncrypt=" + measureIdEncrypt + ", measureCode=" + measureCode
				+ ", measureName=" + measureName + ", description=" + description + ", godung=" + godung + "]";
	}
	
	
}
