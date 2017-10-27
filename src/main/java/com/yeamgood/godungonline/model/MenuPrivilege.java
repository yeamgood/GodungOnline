package com.yeamgood.godungonline.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "menu_privilege")
public class MenuPrivilege extends ModelTemplate{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="menu_privilege_id")
	private Long menuPrivilegeId;
	
	@Transient
	private String menuPrivilegeIdEncrypt;
	
	@Column(name="menu_id")
	private Long menuId;
	
	@Transient
	private String menuIdEncrypt;
	
	@Column(name="menu_privilege_code")
	@NotEmpty(message = "{form.menuPrivilege.valid.code}")
	private String menuPrivilegeCode;
	
	
	@Column(name="menu_privilege_name")
	private String menuPrivilegeName;
	
	@Column(name="description")
	private String description;
	
	@Column(name="active")
	private String active;
	
	public void encryptData() {
		this.menuPrivilegeIdEncrypt = AESencrpUtils.encryptLong(this.menuPrivilegeId);
		this.menuPrivilegeId = null;
		this.menuIdEncrypt = AESencrpUtils.encryptLong(this.menuId);
		this.menuId = null;
	}

	public Long getMenuPrivilegeId() {
		return menuPrivilegeId;
	}

	public void setMenuPrivilegeId(Long menuPrivilegeId) {
		this.menuPrivilegeId = menuPrivilegeId;
	}

	public String getMenuPrivilegeIdEncrypt() {
		return menuPrivilegeIdEncrypt;
	}

	public void setMenuPrivilegeIdEncrypt(String menuPrivilegeIdEncrypt) {
		this.menuPrivilegeIdEncrypt = menuPrivilegeIdEncrypt;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuPrivilegeCode() {
		return menuPrivilegeCode;
	}

	public void setMenuPrivilegeCode(String menuPrivilegeCode) {
		this.menuPrivilegeCode = menuPrivilegeCode;
	}

	public String getMenuPrivilegeName() {
		return menuPrivilegeName;
	}

	public void setMenuPrivilegeName(String menuPrivilegeName) {
		this.menuPrivilegeName = menuPrivilegeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getMenuIdEncrypt() {
		return menuIdEncrypt;
	}

	public void setMenuIdEncrypt(String menuIdEncrypt) {
		this.menuIdEncrypt = menuIdEncrypt;
	}

	@Override
	public String toString() {
		return "MenuPrivilege [menuPrivilegeId=" + menuPrivilegeId + ", menuPrivilegeIdEncrypt="
				+ menuPrivilegeIdEncrypt + ", menuId=" + menuId + ", menuIdEncrypt=" + menuIdEncrypt
				+ ", menuPrivilegeCode=" + menuPrivilegeCode + ", menuPrivilegeName=" + menuPrivilegeName
				+ ", description=" + description + ", active=" + active + "]";
	}

	
}