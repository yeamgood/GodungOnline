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

import com.yeamgood.godungonline.model.template.ModelTemplate;

@Entity
@Table(name = "role")
public class Role extends ModelTemplate{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long id;
	
	@Column(name = "name")
	private String name;

	@Column(name = "system")
	private int system;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "role_menu", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "menu_id"))
	//@Where(clause = "parent_id is null")
	@OrderBy("priority ASC")
    private List<Menu> menuList;
	
	@OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GodungUserRole> godungUserRoleList;
	
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

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
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
	
}
