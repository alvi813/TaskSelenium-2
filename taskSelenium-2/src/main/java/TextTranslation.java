import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextTranslation {

    private WebDriver driver;
    private String googleTranslateUrl;
    static WebDriverWait wait;

    public TextTranslation(WebDriver driver) throws IOException {
        this.driver = driver;
        googleTranslateUrl = "https://translate.google.ru";
    }


    public String getGoogleTranslateUrl() {
        return googleTranslateUrl;
    }


    public List<String> translateParagraphs(List<String> textList) {
        List<WebElement> webTranslatedTextList = new ArrayList<WebElement>();
        List<String> translatedTextList = new ArrayList<String>();

        // выставим языки вводимого и переведённого текстов:
        driver.findElement(By.xpath("//div[@class='sl-wrap']//div[@id='sugg-item-ru']")).click();
        driver.findElement(By.xpath("//div[@class='tl-wrap']//div[@id='sugg-item-en']")).click();
        for (String paragraph : textList) {
            driver.findElement(By.xpath("//textarea[@id='source']")).sendKeys(paragraph);

            /*
            Зададим ожидание того, пока в поле для перевода исчезнет пустое поле с подсказкой и появится переведённый текст,
            чтобы затем работать с переведённым текстом, а не с пустым полем, т.к. перевод происходит с некоторой задержкой
             */
            wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='text-wrap tlid-copy-target']")));


            // получаем переведённый текст из поля перевода и помещаем его в коллекцию:
            WebElement webTranslatedText = driver.findElement(By.xpath("//div[@class='text-wrap tlid-copy-target']"));
            webTranslatedTextList.add(webTranslatedText);

            translatedTextList.add(webTranslatedText.getText());

            // нажимаем кнопку "х" очистки поля ввода текста для перевода:
            driver.findElement(By.xpath("//div[@class='clear-wrap']")).click();
        }

        return translatedTextList;
    }


    public String translateArticleTitle(String articleTitle) {

        // переведём заголовок статьи:
        driver.findElement(By.xpath("//textarea[@id='source']")).sendKeys(articleTitle);
        /*
        Зададим ожидание того, пока в поле для перевода исчезнет пустое поле с подсказкой и появится переведённый текст,
        чтобы затем работать с переведённым текстом, а не с пустым полем, т.к. перевод происходит с некоторой задержкой
        */
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//span[@class='empty-placeholder placeholder']"))));
        // получаем переведённый заголовок из поля перевода
        String translatedArticleTitle = (driver.findElement(By.xpath("//div[@class='text-wrap tlid-copy-target']"))).getText();
        // нажимаем кнопку "х" очистки поля ввода текста для перевода:
        driver.findElement(By.xpath("//div[@class='clear-wrap']")).click();
        return translatedArticleTitle;
    }


    public void printTranslatedTextWithTitleToConsole(String translatedArticleTitle, List<String> translatedTextList) {
        // выведем переведённый текст статьи вместе с заголовком в консоль:
        System.out.println(translatedArticleTitle);
        for (String str : translatedTextList) {
            System.out.println(str);
        }
    }
}
