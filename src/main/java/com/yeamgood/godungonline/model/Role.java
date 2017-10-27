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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "role")
public class Role extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long roleId;
	
	@Transient
	private String roleIdEncrypt;
	
	@Column(name = "role_name")
	@NotEmpty(message = "{form.role.valid.name}")
	private String roleName;
	
	@Column(name = "description")
	private String description;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
	@OrderBy("sequence ASC")
    private List<Menu> menuList;
	
	@OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GodungUserRole> godungUserRoleList;
	
	public void encryptData() {
		this.roleIdEncrypt = AESencrpUtils.encryptLong(this.roleId);
		this.roleId = null;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<GodungUserRole> getGodungUserRoleList() {
		return godungUserRoleList;
	}

	public void setGodungUserRoleList(List<GodungUserRole> godungUserRoleList) {
		this.godungUserRoleList = godungUserRoleList;
	}

	public String getRoleIdEncrypt() {
		return roleIdEncrypt;
	}

	public void setRoleIdEncrypt(String roleIdEncrypt) {
		this.roleIdEncrypt = roleIdEncrypt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
