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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "menu")
public class Menu {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="menu_id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="icon")
	private String icon;
	
	@Column(name="action")
	private String action;
	
	@Column(name="parent_id")
	private Long parentId;
	
	@Column(name="active")
	private String active;
	
	@Column(name="priority")
	private String priority;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "menu_menu_privilege", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "menu_privilege_id"))
    private List<MenuPrivilege> menuPrivilegeList;
	
	@Transient
	private List<Menu> menuList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public List<MenuPrivilege> getMenuPrivilegeList() {
		return menuPrivilegeList;
	}

	public void setMenuPrivilegeList(List<MenuPrivilege> menuPrivilegeList) {
		this.menuPrivilegeList = menuPrivilegeList;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	
}