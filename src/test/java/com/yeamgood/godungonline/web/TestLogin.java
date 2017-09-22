package com.yeamgood.godungonline.web;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TestLogin extends TestSetup{
	

	
	@Test
	public void pageLogin() {
		driver.navigate().to(url);
		assertTrue(driver.getPageSource().contains("Login Form"));
	}
	
	@Test
	public void pageLoginErrorCaseEmailAndPasswordNull() {
		driver.navigate().to(url);
		
		WebElement elementInputEmail = driver.findElement(By.id("login_email"));
		WebElement elementInputPassword = driver.findElement(By.id("login_password"));
		WebElement elementInputBtSubmit = driver.findElement(By.id("bt_login"));
		
		elementInputEmail.sendKeys("");
		elementInputPassword.sendKeys("");
		elementInputBtSubmit.click();
		assertTrue(driver.getPageSource().contains("Invalid username and password!"));
	}
	
	@Test
	public void pageLoginErrorCaseEmailAndPasswordError() {
		driver.navigate().to(url);
		
		WebElement elementInputEmail = driver.findElement(By.id("login_email"));
		WebElement elementInputPassword = driver.findElement(By.id("login_password"));
		WebElement elementInputBtSubmit = driver.findElement(By.id("bt_login"));
		
		elementInputEmail.sendKeys("test1");
		elementInputPassword.sendKeys("test1");
		elementInputBtSubmit.click();
		assertTrue(driver.getPageSource().contains("Invalid username and password!"));
	}
	
	@Test
	public void pageLoginErrorCasePasswordWrong() {
		driver.navigate().to(url);
		
		WebElement elementInputEmail = driver.findElement(By.id("login_email"));
		WebElement elementInputPassword = driver.findElement(By.id("login_password"));
		WebElement elementInputBtSubmit = driver.findElement(By.id("bt_login"));
		
		elementInputEmail.sendKeys("user@gmail.com");
		elementInputPassword.sendKeys("test1");
		elementInputBtSubmit.click();
		assertTrue(driver.getPageSource().contains("Invalid username and password!"));
	}
	
	@Test
	public void pageLoginSuccess() {
		driver.navigate().to(url);
		
		WebElement elementInputEmail = driver.findElement(By.id("login_email"));
		WebElement elementInputPassword = driver.findElement(By.id("login_password"));
		WebElement elementInputBtSubmit = driver.findElement(By.id("bt_login"));
		
		elementInputEmail.sendKeys("user@gmail.com");
		elementInputPassword.sendKeys("123456");
		elementInputBtSubmit.click();
		assertTrue(driver.getPageSource().contains("Home"));
		
		WebElement elementInputBtHeaderProfile = driver.findElement(By.id("header_profile"));
		elementInputBtHeaderProfile.click();
		
		WebElement elementInputBtLogout = driver.findElement(By.id("bt_logout"));
		elementInputBtLogout.click();
	}
	
	
}
