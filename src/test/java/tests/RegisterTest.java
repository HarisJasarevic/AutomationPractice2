package tests;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.awt.*;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class RegisterTest {

    //DOM - Document object model

    WebDriver driver;
    public static final String URL = "https://automationpractice.com/index.php";
    Faker faker;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        Thread.sleep(3000);
        faker = new Faker(new Locale("US"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15)); //čeka da li će se element pojaviti na stranici
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        //driver.quit();
    }

    @Test
    @Parameters({"titleLocator"})
    public void registerUser(String titleLocator){

        driver.findElement(By.cssSelector("a[title*='Log in']")).click();
        driver.findElement(By.cssSelector("input[name='email_create']")).sendKeys(faker.internet().emailAddress());
        driver.findElement(By.id("SubmitCreate")).click();
        driver.findElement(By.id("uniform-id_gender1")).click();
        driver.findElement(By.cssSelector("input[name='customer_firstname']")).sendKeys(faker.name().firstName());
        driver.findElement(By.cssSelector("input[name='customer_lastname']")).sendKeys(faker.name().lastName());
        //driver.findElement(By.cssSelector("input[name='email']")).sendKeys(faker.internet().emailAddress());
        driver.findElement(By.cssSelector("input[name='passwd']"))
                .sendKeys(faker.internet()
                        .password(6, 10, true, true, true));

        WebElement days = driver.findElement(By.id("days"));
        days.click();
        Select select = new Select(days);
        select.selectByValue("5");

        WebElement month = driver.findElement(By.id("months"));
        month.click();
        Select selectMonth = new Select(month);
        selectMonth.selectByValue("6");

        WebElement year = driver.findElement(By.id("years"));
        year.click();
        Select selectYears = new Select(year);
        selectYears.selectByValue("1990");

        driver.findElement(By.xpath("//label[contains(text(), 'Sign')]")).click();
        driver.findElement(By.xpath("//input[@name='address1']")).sendKeys(faker.address().fullAddress());
        driver.findElement(By.xpath("//input[@name='city']")).sendKeys(faker.address().city());

        driver.findElement(By.id("id_state")).click();
        getAndClickOnRandomElement(By.cssSelector("#id_state > option"));
        driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys(faker.number().digits(5));
        driver.findElement(By.id("phone_mobile")).sendKeys(faker.phoneNumber().cellPhone());

        driver.findElement(By.id("submitAccount")).click();
        String expectedText = "Welcome to your account. Here you can manage all of your personal information and orders.";
        String actualText = driver.findElement(By.cssSelector(".info-account")).getText();

        Assert.assertEquals(actualText, expectedText);


    }

    @Test
    public void test(){
        System.out.println(randomize(5));
        System.out.println(RandomStringUtils.randomAlphabetic(10));
    }


    public String randomize(int lenght){
        String [] chars = {"a", "b", "d", "7", "y", "r", "o", "z", "x" };
        String result = "";
        for (int i = 0; i < lenght; i++) {
            Random random = new Random();
            int index = random.nextInt(chars.length);
            result += chars[index];
        }

        return result;
    }

    public void getAndClickOnRandomElement(By locator){
        Random random = new Random();
        List<WebElement> list = driver.findElements(locator);
        list.remove(0);
        int randomElement = random.nextInt(list.size());
        list.get(randomElement).click();
    }







}//end class
