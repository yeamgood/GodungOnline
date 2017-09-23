package com.yeamgood.godungonline.web;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestSupplier extends TestSetup{

	
	private void openMain() {
		driver.findElement(By.id("menu.name.person.management")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu.name.supplier"))).click();
	}
	
	private void openCreatePerson() {
		openMain();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_create"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_create_person"))).click();
	}
	
	private void openCreateCompany() {
		openMain();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_create"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_create_company"))).click();
	}
	
	@Test
	public void main() {
		openMain();
		Assert.assertTrue(driver.getPageSource().contains("Supplier List"));
	}
	
	@Test
	public void managePerson() {
		openCreatePerson();
		Assert.assertTrue(
				driver.getPageSource().contains("Person") &&
				driver.getPageSource().contains("Supplier Code") &&
				driver.getPageSource().contains("Address") &&
				driver.getPageSource().contains("Address Send") &&
				driver.findElement(By.id("bt_save")).isDisplayed() &&
				driver.findElement(By.id("bt_back")).isDisplayed() &&
				driver.findElements(By.id("bt_delete")).isEmpty()
		);
	}
	
	@Test
	public void manageCompany() {
		openCreateCompany();
		Assert.assertTrue(
				driver.getPageSource().contains("Company") &&
				driver.getPageSource().contains("Supplier Code") &&
				driver.getPageSource().contains("Address") &&
				driver.getPageSource().contains("Address Send") &&
				driver.findElement(By.id("bt_save")).isDisplayed() &&
				driver.findElement(By.id("bt_back")).isDisplayed() &&
				driver.findElements(By.id("bt_delete")).isEmpty()
		);
	}
	
}
