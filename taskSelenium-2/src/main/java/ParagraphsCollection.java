import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ParagraphsCollection {

    private WebDriver driver;

    public ParagraphsCollection(WebDriver driver) throws IOException {
        this.driver = driver;
    }

    public List<WebElement> collectWebElementParagraphs() {
        //Соберём в коллекцию поабзацно содержимое статьи:
        List<WebElement> webTextList = driver.findElements(By.xpath("//article//p | //article//h3 | //article//li | //article//blockquote | //article//figure"));
        //List<WebElement> webTextList = driver.findElements(By.xpath("//article//h3 | //article//figure"));
        return webTextList;
    }


    public List<String> collectStringParagraphs(List<WebElement> webTextList) {
        List<String> textList = new ArrayList<String>();

        //Добавим в коллекцию поабзацно содержимое статьи:
        for (WebElement webText : webTextList) {
            if (webText.getText().length() != 0) {
                textList.add(webText.getText());
            }
        }
        return textList;
    }


    public List<String> collectParagraphsTags(List<WebElement> webTextList) {
        List<String> paragraphsTagList = new ArrayList<String>();

        //Добавим в коллекцию теги каждого абзаца:
        for (WebElement webText : webTextList) {
            if (webText.getText().length() != 0) {
                paragraphsTagList.add(webText.getTagName());
            }
        }
        return paragraphsTagList;
    }


    public String collectArticleTitle(WebDriver driver) {
        // сохраним заголовок статьи:
        String articleTitle = driver.findElement(By.xpath("//h1[@class='article__title']")).getText();
        return articleTitle;
    }


    public void printArticleTitleToConsole(String articleTitle) {
        // Выведем заголовок статьи в консоль:
        System.out.println(articleTitle);
    }


    public void printTextCollectionToConsole(List<String> textList) {
        // Выведем собранный текст статьи в консоль:
        for (String text : textList) {
            System.out.println(text);
        }
    }

}
