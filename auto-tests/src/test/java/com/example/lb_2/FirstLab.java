package com.example.lb_2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import org.openqa.selenium.Keys;

public class FirstLab {

    private WebDriver chromeDriver;
    private static final String baseUrl = "https://www.nmu.org.ua/ua/";

    @BeforeClass(alwaysRun = true)
    public void setup() {
       ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        
        this.chromeDriver = new ChromeDriver(chromeOptions);
        this.chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @BeforeMethod 
    public void preconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() { 
        chromeDriver.quit();
    }

    @Test
    public void testHeaderExists() {
        WebElement header = chromeDriver.findElement(By.id("heder"));
        Assert.assertNotNull(header);
    }

    @Test
    public void testClickOnForStudent() {
        WebElement forStudentButton = chromeDriver
        .findElement(By.xpath("/html/body/div[1]/footer/div[1]/div/div/div[1]/div/div/nav/div/ul/li[2]/a"));
        Assert.assertNotNull(forStudentButton);
        forStudentButton.click();
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    public void testSearchFieldOnForStudentPage() {
        String studentPageUrl = baseUrl + "content/students/";
        chromeDriver.get(studentPageUrl);

        WebElement searchField = chromeDriver.findElement(By.tagName("input"));
        Assert.assertNotNull(searchField);

        System.out.println(String.format("Name attribute: %s", searchField.getAttribute("name")) +
                String.format("\nId attribute: %s", searchField.getAttribute("id")) +
                String.format("\nType attribute: %s", searchField.getAttribute("type")) +
                String.format("\nValue attribute: %s", searchField.getAttribute("value")) +
                String.format("\nPosition: (%d; %d)", searchField.getLocation().x, searchField.getLocation().y) +
                String.format("\nSize: %dx%d", searchField.getSize().width, searchField.getSize().height));

        String inputValue = "I need info";
        searchField.sendKeys(inputValue);

        Assert.assertEquals(searchField.getText(), inputValue);
        
        searchField.sendKeys(Keys.ENTER);
        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), studentPageUrl);
    }

    @Test
    public void testSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("next"));
        WebElement nextButtonByCss = chromeDriver.findElement(By.cssSelector("a.next"));
        Assert.assertEquals(nextButton, nextButtonByCss);

        WebElement previousButton = chromeDriver.findElement(By.className("prev"));

        for(int i = 0; i < 20; i++) {
            if(nextButton.getAttribute("class").contains("disabled")) {
                previousButton.click();
                Assert.assertTrue(previousButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            } else {
                nextButton.click();
                Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
                Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            }
        }
    }

    @Test
    public void testCustomScenarioSaucedemo() {
        chromeDriver.get("https://www.saucedemo.com/");

        WebElement usernameField = chromeDriver.findElement(By.id("user-name"));
        usernameField.sendKeys("standard_user");

        Assert.assertEquals(usernameField.getAttribute("value"), "standard_user");

        WebElement passwordField = chromeDriver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce");

        WebElement loginButton = chromeDriver.findElement(By.xpath("//input[@type='submit' and contains(@class, 'submit-button')]"));
        
        loginButton.click();

        WebElement productsTitle = chromeDriver.findElement(By.cssSelector("span.title"));
        Assert.assertTrue(productsTitle.isDisplayed());
        Assert.assertEquals(productsTitle.getText(), "Products");
    }
}