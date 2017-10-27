package com.yeamgood.godungonline.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.yeamgood.godungonline.model.template.ModelTemplate;
import com.yeamgood.godungonline.utils.AESencrpUtils;

@Entity
@Table(name = "menu")
public class Menu extends ModelTemplate{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="menu_id")
	private Long menuId;
	
	@Transient
	private String menuIdEncrypt;
	
	@Column(name="menu_code")
	@NotEmpty(message = "{form.menu.valid.code}")
	private String menuCode;
	
	@Column(name="menu_name")
	private String menuName;
	
	@Column(name="message_code")
	private String messageCode;
	
	@Column(name="description")
	private String description;
	
	@Column(name="icon")
	private String icon;
	
	@Column(name="action")
	private String action;
	
	@Column(name="sequence")
	private Long sequence;
	
	@Column(name="parent_code")
	private String parentCode;
	
	@Column(name="active")
	private Long active;

	//@ManyToMany(cascade = CascadeType.ALL)
	//@JoinTable(name = "menu_menu_privilege", joinColumns = @JoinColumn(name = "menu_id"), inverseJoinColumns = @JoinColumn(name = "menu_privilege_id"))
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "menu_id")
	private List<MenuPrivilege> menuPrivilegeList;
	
	@Transient
	private List<Menu> menuList;
	
	public void setObject(Menu menu) {
		this.menuName = menu.getMenuName();
		this.menuCode = menu.getMenuCode();
		this.description = menu.getDescription();
		this.messageCode = menu.getMessageCode();
		this.icon = menu.getIcon();
		this.action = menu.getAction();
		this.sequence = menu.getSequence();
		this.parentCode = menu.getParentCode();
		this.active = menu.getActive();
	}
	
	public void encryptData() {
		this.menuIdEncrypt = AESencrpUtils.encryptLong(this.menuId);
		this.menuId = null;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getActive() {
		return active;
	}

	public void setActive(Long active) {
		this.active = active;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public List<MenuPrivilege> getMenuPrivilegeList() {
		return menuPrivilegeList;
	}

	public void setMenuPrivilegeList(List<MenuPrivilege> menuPrivilegeList) {
		this.menuPrivilegeList = menuPrivilegeList;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public String getMenuIdEncrypt() {
		return menuIdEncrypt;
	}

	public void setMenuIdEncrypt(String menuIdEncrypt) {
		this.menuIdEncrypt = menuIdEncrypt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	@Override
	public String toString() {
		return "Menu [menuId=" + menuId + ", menuIdEncrypt=" + menuIdEncrypt + ", menuCode=" + menuCode + ", menuName="
				+ menuName + ", messageCode=" + messageCode + ", description=" + description + ", icon=" + icon
				+ ", action=" + action + ", sequence=" + sequence + ", parentCode=" + parentCode + ", active=" + active
				+ "]";
	}
	
	
	
}