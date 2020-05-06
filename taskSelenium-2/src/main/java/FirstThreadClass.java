import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class FirstThreadClass extends YandexZen implements Runnable {

    private static int numberOfLinkInTextFile = 1; // номер ссылки из текстового файла, с которой будем работать

    public void closeDriver(WebDriver driver) {
        driver.quit();
    }

    //----------------------------------------------------------------------------------------

    // точка входа нового потока
    @Override
    public void run() {

        setDriver(DRIVER_NAMES.get(0), "drivers/geckodriver.exe");

        FirefoxOptions firefoxOptions = setFirefoxMode();





        for (int counterOnHowManyPagesWillCollectLinks = 10;
             numberOfLinkInTextFile <= counterOnHowManyPagesWillCollectLinks;
             numberOfLinkInTextFile++) {


            WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
            //seleniumTask.maximizeWindow(firefoxDriver);


            LinksCollection linksCollection = null;
            try {
                linksCollection = new LinksCollection(firefoxDriver, urlTextFilePathName);
            } catch (IOException e) {
                e.printStackTrace();
            }


            String urlLine = null;
            try {
                assert linksCollection != null;
                urlLine = linksCollection.getUrlLineFromFile(numberOfLinkInTextFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            openWebPage(firefoxDriver, urlLine);


            assert urlLine != null;
            List<WebElement> webLinksList = linksCollection.collectWebElementLinks(urlLine);


            Set<String> urlLinksSet = null;
            try {
                urlLinksSet = linksCollection.collectNonrepeatingStringLinks(webLinksList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                assert urlLinksSet != null;
                linksCollection.writeLinksToFile(urlLinksSet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            linksCollection.printLinkCollectionToConsole(urlLinksSet);

            ParagraphsCollection paragraphsCollection = null;
            try {
                paragraphsCollection = new ParagraphsCollection(firefoxDriver);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert paragraphsCollection != null;
            List<WebElement> webTextList = paragraphsCollection.collectWebElementParagraphs();
            List<String> textList = paragraphsCollection.collectStringParagraphs(webTextList);
            String articleTitle = paragraphsCollection.collectArticleTitle(firefoxDriver);
            List<String> paragraphsTagList = paragraphsCollection.collectParagraphsTags(webTextList);

            // Соберём url картинок:
            ImgUrlCollection imgUrlCollection = null;
            try {
                imgUrlCollection = new ImgUrlCollection(firefoxDriver);
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert imgUrlCollection != null;
            List<String> imgUrlLinks = imgUrlCollection.collectWebElementImgUrl();

            paragraphsCollection.printArticleTitleToConsole(articleTitle);
            paragraphsCollection.printTextCollectionToConsole(textList);

            closeDriver(firefoxDriver);


            //---------------------------------------------------------------------------------------------------


/*            WebDriver firefoxDriver2 = new FirefoxDriver(firefoxOptions);
            maximizeWindow(firefoxDriver2);

            TextTranslation textTranslation = null;
            try {
                textTranslation = new TextTranslation(firefoxDriver2);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String googleTranslateUrl = textTranslation.getGoogleTranslateUrl();
            openWebPage(firefoxDriver2, googleTranslateUrl);
            List<String> translatedTextList = textTranslation.translateParagraphs(textList);
            String translatedArticleTitle = textTranslation.translateArticleTitle(articleTitle);
            //textTranslation.printTranslatedTextWithTitleToConsole(translatedArticleTitle, translatedTextList);

            closeDriver(firefoxDriver2);
*/

            //---------------------------------------------------------------------------------------------------

            //------------
            specifyFirefoxProfile(firefoxOptions, myFirefoxProfile);
            //------------

            WebDriver firefoxDriver3 = new FirefoxDriver(firefoxOptions);

            YandexZenArticleCreate yandexZenArticle = null;
            try {
                yandexZenArticle = new YandexZenArticleCreate(firefoxDriver3);
            } catch (IOException e) {
                e.printStackTrace();
            }

            openWebPage(firefoxDriver3, yandexZenChannelUrl);
            //seleniumTask.maximizeWindow(firefoxDriver3);
            setWindowSize(firefoxDriver3);

            /*try {
                assert yandexZenArticle != null;
                yandexZenArticle.addArticleToYandexZen(translatedArticleTitle, translatedTextList, paragraphsTagList, imgUrlLinks);
            } catch (IOException | UnsupportedFlavorException | AWTException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }*/

            try {
                assert yandexZenArticle != null;
                yandexZenArticle.addArticleToYandexZen(articleTitle, textList, paragraphsTagList, imgUrlLinks);
            } catch (IOException | UnsupportedFlavorException | AWTException | URISyntaxException | InterruptedException e) {
                e.printStackTrace();
            }

            closeDriver(firefoxDriver3);


            //---------------------------------------------------------------------------------------------------

        }
    }
}