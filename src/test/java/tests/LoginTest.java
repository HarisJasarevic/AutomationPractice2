package tests;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Locale;

public class LoginTest {

    WebDriver driver;
    public static final String URL = "https://the-internet.herokuapp.com/login";
    Faker faker;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "waitTime"})
    public void setUp(String browser, String waitTime) throws InterruptedException {
        int wait = Integer.parseInt(waitTime);
        if (browser.equalsIgnoreCase("chrome")){
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }else if (browser.equalsIgnoreCase("edge")){
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        faker = new Faker(new Locale("US"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
        Thread.sleep(3000);
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        Thread.sleep(3000);
        driver.quit();
    }

    @Test
    @Parameters({"username", "password"})
    public void loginTest(String username, String password){
        WebElement usernameField = driver.findElement(By.cssSelector("input[name='username']"));
        usernameField.sendKeys(username);

        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.className("radius")).click();

        WebElement loggedInText = driver.findElement(By.id("flash"));

        String actualBacgroundColor = loggedInText.getCssValue("background-color");

        String expectedText = "You logged into a secure area!";
        String expectedColor = "rgba(93, 164, 35, 1)";

        String actual [] = loggedInText.getText().split("(?<=!)");

        String actualText = actual [0];

        String actualText2 = loggedInText.getText().substring(0, loggedInText.getText().length() - 2);

        boolean result = expectedText.equals(actualText);
        Assert.assertTrue(result);

        Assert.assertEquals(actualText, expectedText);
        Assert.assertEquals(actualBacgroundColor, expectedColor);

    }


}
