import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class TodoMVCPage {

    private WebDriver driver;

    public TodoMVCPage(WebDriver driver) {
        this.driver = driver;
    }

    public int getTodoItemCount() {
        List<WebElement> items = driver.findElements(By.cssSelector(".todo-list li"));
        return items.size();
    }
}
