package com.yeamgood.godungonline.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class TestSetup {
	
	protected static WebDriver driver;
	protected static WebDriverWait wait;
	protected static String url = "http://localhost:8080/godung/logout"; 
	
	@BeforeClass
	public static void setUp() {
		System.setProperty("webdriver.chrome.driver","chromedriver");
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
	}
	
	@AfterClass
	public static void tearDown() {
		driver.close();
	}
	
	@BeforeMethod
	public void login() {
		driver.navigate().to(url);
		WebElement elementInputEmail = driver.findElement(By.id("login_email"));
		WebElement elementInputPassword = driver.findElement(By.id("login_password"));
		WebElement elementInputBtSubmit = driver.findElement(By.id("bt_login"));
		elementInputEmail.sendKeys("user2@gmail.com");
		elementInputPassword.sendKeys("123456");
		elementInputBtSubmit.click();
	}
	
//	@AfterMethod
//	public void logout() {
//		WebElement elementInputBtHeaderProfile = driver.findElement(By.id("header_profile"));
//		elementInputBtHeaderProfile.click();
//		
//		WebElement elementInputBtLogout = driver.findElement(By.id("bt_logout"));
//		elementInputBtLogout.click();
//	}
	
}
