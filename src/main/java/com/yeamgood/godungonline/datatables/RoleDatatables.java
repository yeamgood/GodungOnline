package com.yeamgood.godungonline.datatables;

import com.yeamgood.godungonline.model.Role;

public class RoleDatatables {

	private String roleIdEncrypt;
	
	private String roleName;
	
	private String description;
	
	
	public void setDatatableByModel(Role role) {
		this.roleIdEncrypt = role.getRoleIdEncrypt();
 		this.roleName = role.getRoleName();
		this.description = role.getDescription();
	}

	public String getRoleIdEncrypt() {
		return roleIdEncrypt;
	}


	public void setRoleIdEncrypt(String roleIdEncrypt) {
		this.roleIdEncrypt = roleIdEncrypt;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
