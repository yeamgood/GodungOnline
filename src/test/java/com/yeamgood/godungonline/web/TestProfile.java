package com.yeamgood.godungonline.web;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class TestProfile extends TestSetup{
	
	private void openMainPage() {
		driver.findElement(By.id("menu.name.setting")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu.name.profile"))).click();
	}
	
	@Test
	public void mainPageInputNameNull() {
		openMainPage();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.id("bt_save")).click();
		assertTrue(driver.getPageSource().contains("*Please provide your name."));
	}
	
	@Test
	public void mainPageInputNameMaxLenght() {
		openMainPage();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("123456789012345678901234567890123456789012345678901");
		driver.findElement(By.id("bt_save")).click();
		assertTrue(driver.getPageSource().contains("The field must be less than 50 characters."));
	}
	
	@Test
	public void mainPageInputGodungNameNull() {
		openMainPage();
		driver.findElement(By.name("godungName")).clear();
		driver.findElement(By.id("bt_save")).click();
		assertTrue(driver.getPageSource().contains("*Please provide your Godung name."));
	}

	@Test
	public void mainPageInputGodungNameMaxLenght() {
		openMainPage();
		driver.findElement(By.name("godungName")).clear();
		driver.findElement(By.name("godungName")).sendKeys("12345678901234567890123456");
		driver.findElement(By.id("bt_save")).click();
		assertTrue(driver.getPageSource().contains("The field must be less than 25 characters."));
	}
	
	@Test
	public void mainPageSave() {
		openMainPage();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("YeamGood");
		driver.findElement(By.name("godungName")).clear();
		driver.findElement(By.name("godungName")).sendKeys("yeam godung");
		driver.findElement(By.id("bt_save")).click();
		assertTrue(driver.getPageSource().contains("Save success."));
	}
	
}
