import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImgUrlCollection {
    private WebDriver driver;

    public ImgUrlCollection(WebDriver driver) throws IOException {
        this.driver = driver;
    }


    public List<String> collectWebElementImgUrl() {
        //Соберём в коллекцию Url картинок:
        List<WebElement> webImgUrlList = driver.findElements(By.xpath("//div[@class='article-render']//img"));

        List<String> imgUrlLinks = new ArrayList<String>();
        for (WebElement webImgLink : webImgUrlList) {
            imgUrlLinks.add(webImgLink.getAttribute("src"));
        }

        return imgUrlLinks;
    }

}
