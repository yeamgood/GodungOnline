package com.yeamgood.godungonline.web;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class TestRolegodung extends TestSetup{
	
	private void openMainPage() {
		driver.findElement(By.id("menu.name.setting")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu.name.rolegodung"))).click();
	}
	
	private void openCreatePage() {
		openMainPage();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_rolegodung_create"))).click();
	}
	
	@Test
	public void mainPage() {
		openMainPage();
		assertTrue(driver.getPageSource().contains("Rolegodung List"));
	}
	
	@Test
	public void managePage() {
		openCreatePage();
		assertTrue(
				driver.getPageSource().contains("Rolegodung Code") &&
				driver.getPageSource().contains("Rolegodung Name") &&
				driver.getPageSource().contains("Description") &&
				driver.findElement(By.id("bt_save")).isDisplayed() &&
				driver.findElement(By.id("bt_back")).isDisplayed() &&
				driver.findElements(By.id("bt_delete")).isEmpty()
		);
	}
	
	@Test
	public void managePageClickBack() {
		openCreatePage();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_back"))).click();
		assertTrue(driver.getPageSource().contains("Rolegodung List"));
	}
	

	@Test
	public void managePageSaveInputAllNull() {
		openCreatePage();		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(driver.getPageSource().contains("*Please provide your rolegodung name."));
	}
	

	@Test
	public void managePageSaveInputAllMaxLenght() {
		openCreatePage();
		WebElement inputRolegodungName = driver.findElement(By.name("rolegodungName"));
		WebElement inputDescription = driver.findElement(By.name("description"));
		inputRolegodungName.sendKeys("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
		inputDescription.sendKeys("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("The field must be less than 100 characters.") &&
				driver.getPageSource().contains("The field must be less than 200 characters.")
		);
	}
	
	
	@Test
	public void managePageSave00001() {
		openCreatePage();
		WebElement inputRolegodungName = driver.findElement(By.name("rolegodungName"));
		WebElement inputDescription = driver.findElement(By.name("description"));
		inputRolegodungName.sendKeys("เจ้าของกิจการ ทดสอบ1");
		inputDescription.sendKeys("รายละเอียด ทดสอบ1");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("Save success.") &&
				driver.getPageSource().contains("-00001")
		);
	}
	
	@Test(dependsOnMethods= {"managePageSave00001"})
	public void mainPageDatatables() {
		openMainPage();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datatablesMain_info")));
		assertTrue(
				driver.getPageSource().contains("-00001") &&
				driver.getPageSource().contains("เจ้าของกิจการ ทดสอบ1") &&
				driver.getPageSource().contains("รายละเอียด ทดสอบ1") 
		);
		
	}
	
	@Test(dependsOnMethods= {"managePageSave00001"})
	public void mainPageDatatablesSearchByCode000001() {
		openMainPage();
		driver.findElement(By.id("input_basic_search")).sendKeys("-00001");
		assertTrue(
				driver.getPageSource().contains("-00001") &&
				driver.getPageSource().contains("เจ้าของกิจการ ทดสอบ1") &&
				driver.getPageSource().contains("รายละเอียด ทดสอบ1") 
		);
		
	}
	
	@Test(dependsOnMethods= {"managePageSave00001"})
	public void mainPageDatatablesSearchByName00001() {
		openMainPage();
		driver.findElement(By.id("input_basic_search")).sendKeys("เจ้าของกิจการ");
		assertTrue(
				driver.getPageSource().contains("-00001") &&
				driver.getPageSource().contains("เจ้าของกิจการ ทดสอบ1") &&
				driver.getPageSource().contains("รายละเอียด ทดสอบ1") 
		);
		
	}
	
	@Test(dependsOnMethods= {"managePageSave00001"})
	public void mainPageDatatablesSearchByDescription00001() {
		openMainPage();
		driver.findElement(By.id("input_basic_search")).sendKeys("รายละเอียด");
		assertTrue(
				driver.getPageSource().contains("-00001") &&
				driver.getPageSource().contains("เจ้าของกิจการ ทดสอบ1") &&
				driver.getPageSource().contains("รายละเอียด ทดสอบ1") 
		);
	}
	
	@Test
	public void managePageSave00002() {
		openCreatePage();
		WebElement inputRolegodungName = driver.findElement(By.name("rolegodungName"));
		WebElement inputDescription = driver.findElement(By.name("description"));
		inputRolegodungName.sendKeys("เจ้าของกิจการ ทดสอบ2");
		inputDescription.sendKeys("รายละเอียด ทดสอบ2");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("Save success.") &&
				driver.getPageSource().contains("-00002")
		);
	}
	
	@Test(dependsOnMethods= {"managePageSave00002"})
	public void managePageEdit00002() {
		openMainPage();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@id,'bt_edit_ROL') and contains(@id,'-00002')]"))).click();
		driver.findElement(By.name("rolegodungName")).sendKeys("เจ้าของกิจการ แก้ไขทดสอบ2");
		driver.findElement(By.name("description")).sendKeys("รายละเอียด แก้ไขทดสอบ2");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("Save success.") &&
				driver.getPageSource().contains("-00002")
		);
		
	}
	
	@Test
	public void managePageSave00003() {
		openCreatePage();
		driver.findElement(By.name("rolegodungName")).sendKeys("เจ้าของกิจการ ทดสอบ3");
		driver.findElement(By.name("description")).sendKeys("รายละเอียด ทดสอบ3");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("Save success.") &&
				driver.getPageSource().contains("-00003")
		);
	}
	
	@Test(dependsOnMethods= {"managePageSave00003"})
	public void mainPageDelete00003() {
		openMainPage();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@id,'bt_delete_ROL') and contains(@id,'-00003')]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_delete_main"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-pnotify")));
		assertTrue(
				driver.getPageSource().contains("Delete success.") 
		);
	}
	
	@Test
	public void managePageSave00004() {
		openCreatePage();
		driver.findElement(By.name("rolegodungName")).sendKeys("เจ้าของกิจการ ทดสอบ4");
		driver.findElement(By.name("description")).sendKeys("รายละเอียด ทดสอบ4");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_save"))).click();
		assertTrue(
				driver.getPageSource().contains("Save success.") &&
				driver.getPageSource().contains("-00004")
		);
	}
	
	@Test(dependsOnMethods= {"managePageSave00004"})
	public void managePageDelete00004() {
		openMainPage();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@id,'bt_edit_ROL') and contains(@id,'-00004')]"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_delete"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("bt_delete_main"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ui-pnotify")));
		assertTrue(
				driver.getPageSource().contains("Delete success.") 
		);
	}
	
}
