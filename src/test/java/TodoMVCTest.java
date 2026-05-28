import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

public class TodoMVCTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setUpBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://todomvc.com/examples/react/dist/");
    }

    @Test
    public void pageTitleIsCorrect() {
        String title = driver.getTitle();
        Assertions.assertEquals("TodoMVC: React", title);
    }

    @Test
    public void canCreateATodoItem() {

        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Buy milk");
        input.sendKeys(Keys.ENTER);

        WebElement todoItem = driver.findElement(By.cssSelector(".todo-list li"));
        Assertions.assertEquals("Buy milk", todoItem.getText());
    }

    @Test
    public void canDeleteATodoItem() {
        List<WebElement> itemsBefore = driver.findElements(By.cssSelector(".todo-list li"));
        int countBefore = itemsBefore.size();

        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Feed the cats");
        input.sendKeys(Keys.ENTER);

        WebElement todoItem = driver.findElement(By.cssSelector(".todo-list li"));
        new Actions(driver).moveToElement(todoItem).perform();
        WebElement deleteButton = driver.findElement(By.cssSelector("[data-testid='todo-item-button']"));
        deleteButton.click();

        List<WebElement> itemsAfter = driver.findElements(By.cssSelector(".todo-list li"));
        Assertions.assertEquals(countBefore, itemsAfter.size());
    }

    @Test
    public void countDisplaysCorrectNumberOfItemsRemaining() {
        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Buy antihistamines");
        input.sendKeys(Keys.ENTER);
        input.sendKeys("Walk the dog");
        input.sendKeys(Keys.ENTER);

        List<WebElement> items = driver.findElements(By.cssSelector(".todo-list li"));
        int totalItems = items.size();

        WebElement count = driver.findElement(By.cssSelector(".todo-count"));
        Assertions.assertEquals(String.format("%d items left!", totalItems), count.getText());
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}