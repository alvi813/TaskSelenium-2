import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;


public class SeleniumTask {

    private static final List<String> DRIVER_NAMES = new ArrayList<String>() {{
        add("webdriver.gecko.driver");
        add("webdriver.chrome.driver");
    }};

    static WebDriverWait wait;
    static int numberOfLinkInTextFile = 1; // номер ссылки из текстового файла, с которой будем работать

    public static void setDriver(String driverName, String driverFilePath) {
        System.setProperty(driverName, driverFilePath);
    }

    public void openWebPage(WebDriver driver, String urlLine) {
        driver.get(urlLine);
    }

    public FirefoxProfile specifyFirefoxProfile(String myFirefoxProfile) {
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile firefoxProfile = profile.getProfile(myFirefoxProfile);
        return firefoxProfile;
    }

    public FirefoxBinary setFirefoxHeadlessMode() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("-headless");
        return firefoxBinary;
    }

    public void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public void closeDriver(WebDriver driver) {
        driver.quit();
    }


    public static void main(String[] args) throws IOException, UnsupportedFlavorException, AWTException, URISyntaxException, InterruptedException {

        //файл, который хранит свойства нашего проекта
        File file = new File("src/main/resources/data.properties");

        //создаем объект Properties и загружаем в него данные из файла.
        Properties properties = new Properties();
        properties.load(new FileReader(file));

        //получаем значения свойств из объекта Properties:
        String urlTextFilePathName = properties.getProperty("urlTextFilePathName");
        String yandexZenChannelUrl = properties.getProperty("yandexZenChannelUrl");
        String myFirefoxProfile = properties.getProperty("myFirefoxProfile");
        String setFirefoxHeadlessMode = properties.getProperty("setFirefoxHeadlessMode");


        SeleniumTask seleniumTask = new SeleniumTask();
        setDriver(DRIVER_NAMES.get(0), "drivers/geckodriver.exe");

        FirefoxBinary firefoxBinary = seleniumTask.setFirefoxHeadlessMode();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if(setFirefoxHeadlessMode.equals("yes")) {
            firefoxOptions.setBinary(firefoxBinary);
        }

        for (int counterOnHowManyPagesWillCollectLinks = 1;
             numberOfLinkInTextFile <= counterOnHowManyPagesWillCollectLinks;
             numberOfLinkInTextFile++) {

            WebDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
            //seleniumTask.maximizeWindow(firefoxDriver);

            LinksCollection linksCollection = new LinksCollection(firefoxDriver, urlTextFilePathName);
            String urlLine = linksCollection.getUrlLineFromFile(numberOfLinkInTextFile);
            seleniumTask.openWebPage(firefoxDriver, urlLine);
            List<WebElement> webLinksList = linksCollection.collectWebElementLinks(urlLine);
            Set<String> urlLinksSet = linksCollection.collectNonrepeatingStringLinks(webLinksList);
            linksCollection.writeLinksToFile(urlLinksSet);
            linksCollection.printLinkCollectionToConsole(urlLinksSet);

            ParagraphsCollection paragraphsCollection = new ParagraphsCollection(firefoxDriver);
            List<WebElement> webTextList = paragraphsCollection.collectWebElementParagraphs();
            List<String> textList = paragraphsCollection.collectStringParagraphs(webTextList);
            String articleTitle = paragraphsCollection.collectArticleTitle(firefoxDriver);
            List<String> paragraphsTagList = paragraphsCollection.collectParagraphsTags(webTextList);

            // Соберём url картинок:
            ImgUrlCollection imgUrlCollection = new ImgUrlCollection(firefoxDriver);
            List<String> imgUrlLinks = imgUrlCollection.collectWebElementImgUrl();

            paragraphsCollection.printArticleTitleToConsole(articleTitle);
            paragraphsCollection.printTextCollectionToConsole(textList);

            seleniumTask.closeDriver(firefoxDriver);


            //---------------------------------------------------------------------------------------------------


            /*WebDriver firefoxDriver2 = new FirefoxDriver(firefoxOptions);
            seleniumTask.maximizeWindow(firefoxDriver2);

            TextTranslation textTranslation = new TextTranslation(firefoxDriver2);
            String googleTranslateUrl = textTranslation.getGoogleTranslateUrl();
            seleniumTask.openWebPage(firefoxDriver2, googleTranslateUrl);
            List<String> translatedTextList = textTranslation.translateParagraphs(textList);
            String translatedArticleTitle = textTranslation.translateArticleTitle(articleTitle);
            //textTranslation.printTranslatedTextWithTitleToConsole(translatedArticleTitle, translatedTextList);

            seleniumTask.closeDriver(firefoxDriver2);*/


            //---------------------------------------------------------------------------------------------------

            //------------
            FirefoxProfile firefoxProfile = seleniumTask.specifyFirefoxProfile(myFirefoxProfile);
            firefoxOptions.setProfile(firefoxProfile);
            //------------

            WebDriver firefoxDriver3 = new FirefoxDriver(firefoxOptions);

            YandexZenArticle yandexZenArticle = new YandexZenArticle(firefoxDriver3);
            seleniumTask.openWebPage(firefoxDriver3, yandexZenChannelUrl);
            //seleniumTask.maximizeWindow(firefoxDriver3);
            Dimension r = new Dimension(1920, 1080);
            firefoxDriver3.manage().window().setSize(r);

            ////////////////////////////////////////////yandexZenArticle.addArticleToYandexZen(translatedArticleTitle, translatedTextList, paragraphsTagList, imgUrlLinks);
            yandexZenArticle.addArticleToYandexZen(articleTitle, textList, paragraphsTagList, imgUrlLinks);

            seleniumTask.closeDriver(firefoxDriver3);


            //---------------------------------------------------------------------------------------------------


            /*WebDriver firefoxDriver4 = new FirefoxDriver(firefoxOptions);

            ArticlePublication articlePublication = new ArticlePublication(firefoxDriver4);
            seleniumTask.openWebPage(firefoxDriver4, seleniumTask.yandexZenChannelUrl);
            articlePublication.publicArticle();

            //seleniumTask.closeDriver(firefoxDriver4);*/
        }
    }
}
