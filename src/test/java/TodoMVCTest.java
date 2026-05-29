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

        WebElement todoItem = driver.findElement(By.xpath("//label[text()='Buy milk']"));
        Assertions.assertEquals("Buy milk", todoItem.getText());
    }

    @Test
    public void canDeleteATodoItem() {
        TodoMVCPage page = new TodoMVCPage(driver);
        int countBefore = page.getTodoItemCount();

        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Feed the cats");
        input.sendKeys(Keys.ENTER);

        WebElement todoItem = driver.findElement(By.cssSelector(".todo-list li"));
        new Actions(driver).moveToElement(todoItem).perform();
        WebElement deleteButton = driver.findElement(By.cssSelector("[data-testid='todo-item-button']"));
        deleteButton.click();

        Assertions.assertEquals(countBefore, page.getTodoItemCount());
    }

    @Test
    public void countDisplaysCorrectNumberOfItemsRemaining() {
        TodoMVCPage page = new TodoMVCPage(driver);

        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Buy antihistamines");
        input.sendKeys(Keys.ENTER);
        input.sendKeys("Walk the dog");
        input.sendKeys(Keys.ENTER);

        int totalItems = page.getTodoItemCount();

        WebElement count = driver.findElement(By.cssSelector(".todo-count"));
        Assertions.assertEquals(String.format("%d items left!", totalItems), count.getText());
    }

    @Test
    public void cannotAddAnEmptyTodoItem() {
        TodoMVCPage page = new TodoMVCPage(driver);
        int countBefore = page.getTodoItemCount();

        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys(Keys.ENTER);

        Assertions.assertEquals(countBefore, page.getTodoItemCount());
    }

    @Test
    public void canEditATodoItem() {
        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Buy chocolate");
        input.sendKeys(Keys.ENTER);


        WebElement todoItem = driver.findElement(By.xpath("//label[text()='Buy chocolate']"));
        new Actions(driver).doubleClick(todoItem).perform();


        WebElement editField = driver.findElement(By.cssSelector("[data-testid='text-input']"));
        editField.clear();
        editField.sendKeys("Buy Galaxy chocolate");
        editField.sendKeys(Keys.ENTER);


        WebElement updatedItem = driver.findElement(By.xpath("//label[text()='Buy Galaxy chocolate']"));
        Assertions.assertEquals("Buy Galaxy chocolate", updatedItem.getText());
    }

    @Test
    public void canMarkATodoItemAsComplete() {
        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Clean the litter boxes");
        input.sendKeys(Keys.ENTER);

        WebElement todoItem = driver.findElement(By.xpath("//label[text()='Clean the litter boxes']/.."));
        WebElement checkbox = todoItem.findElement(By.cssSelector("[data-testid='todo-item-toggle']"));
        checkbox.click();

        Assertions.assertTrue(checkbox.isSelected());
    }

    @Test
    public void canMarkATodoItemAsIncomplete() {
        WebElement input = driver.findElement(By.id("todo-input"));
        input.sendKeys("Do the laundry");
        input.sendKeys(Keys.ENTER);

        WebElement todoItem = driver.findElement(By.xpath("//label[text()='Do the laundry']/.."));
        WebElement checkbox = todoItem.findElement(By.cssSelector("[data-testid='todo-item-toggle']"));
        checkbox.click();
        checkbox.click();

        Assertions.assertFalse(checkbox.isSelected());
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}