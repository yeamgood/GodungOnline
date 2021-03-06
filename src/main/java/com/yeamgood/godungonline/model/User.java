package com.yeamgood.godungonline.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;
	
	@Transient
	private String userIdEncrypt;
	
	@Column(name = "email")
	@Email(message = "{validation.required.email.valid}")
	@NotEmpty(message = "{validation.required.email}")
	private String email;
	
	@Column(name = "password")
	@Length(min = 5, message = "{validation.required.password.valid}")
	@NotEmpty(message = "{validation.required.password}")
	private String password;
	
	@Transient
	private String confirmPassword;
	
	@Column(name = "name")
	@NotEmpty(message = "{validation.required.name}")
	private String name;
	
	@Column(name = "language")
	private String language;
	
	@Column(name = "active")
	private int active;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "user_rolelogin", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleLogin> roles;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
    private List<GodungUserRole> godungUserRoleList;
	
	@Transient
	@Valid
	Godung godung;
	
	@Transient
	Role role;
	
	public void encryptData() {
		this.userIdEncrypt = AESencrpUtils.encryptLong(this.userId);
		this.userId = null;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Set<RoleLogin> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleLogin> roles) {
		this.roles = roles;
	}


	public Godung getGodung() {
		return godung;
	}

	public void setGodung(Godung godung) {
		this.godung = godung;
	}

	public List<GodungUserRole> getGodungUserRoleList() {
		return godungUserRoleList;
	}

	public void setGodungUserRoleList(List<GodungUserRole> godungUserRoleList) {
		this.godungUserRoleList = godungUserRoleList;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getUserIdEncrypt() {
		return userIdEncrypt;
	}

	public void setUserIdEncrypt(String userIdEncrypt) {
		this.userIdEncrypt = userIdEncrypt;
	}
	
}
