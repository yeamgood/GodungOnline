package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "employee")
public class Employee extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employee_Id")
	private Long employeeId;
	
	@Column(name = "employee_code")
	private String employeeCode;
	
	@Column(name = "employee_name")
	@NotEmpty(message = "{form.employee.valid.name}")
	@Length(max = 100, message = "{validation.max.lenght}")
	private String employeeName;
	
	@Column(name = "description")
	@Length(max = 200, message = "{validation.max.lenght}")
	private String description;
	
	@ManyToOne()
	@JoinColumn(name = "godung_id")
	@JsonIgnore
	private Godung godung;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", employeeCode=" + employeeCode + ", employeeName=" + employeeName + ", description="
				+ description + ", godung=" + godung + "]";
	}
	
	
}
