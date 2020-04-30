import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

public class ArticlePublication {


    private WebDriver driver;
    static WebDriverWait wait;

    public ArticlePublication(WebDriver driver) throws IOException {
        this.driver = driver;
    }


    public void publicArticle() throws IOException {
        wait = new WebDriverWait(driver, 5);

        // ждём, пока появится кнопка "Черновики":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[contains(text(), 'Черновики')]"))));

        // нажать на кнопку "Черновики":
        driver.findElement(By.xpath("//span[contains(text(), 'Черновики')]")).click();

        // выбрать статью:
        driver.findElement(By.xpath("//div[@class='card-cover-publication']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[contains(text(), 'Опубликовать')]"))));
        driver.findElement(By.xpath("//span[contains(text(), 'Опубликовать')]")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//form[@class='publication-settings__form']"))));
        driver.findElement(By.xpath("//span[contains(text(), 'Опубликовать')]")).click();
    }
}
