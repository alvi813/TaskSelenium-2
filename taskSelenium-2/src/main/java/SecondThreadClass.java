import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SecondThreadClass extends YandexZen implements Runnable {
private int articleNumberFromTheLeftEdge = 3;
    // точка входа нового потока
    @Override
    public void run() {

        setDriver(DRIVER_NAMES.get(0), "drivers/geckodriver.exe");
        FirefoxOptions firefoxOptions = setFirefoxMode();

        //------------
        specifyFirefoxProfile(firefoxOptions, myFirefoxProfile);
        //------------

        int counterHowManyPagesWillPublished = 10;

        WebDriver firefoxDriver4 = new FirefoxDriver(firefoxOptions);


        for (int i = 0; i < counterHowManyPagesWillPublished; i++) {
            ArticlePublication articlePublication = null;
            try {
                articlePublication = new ArticlePublication(firefoxDriver4);
            } catch (IOException e) {
                e.printStackTrace();
            }

            openWebPage(firefoxDriver4, yandexZenChannelUrl);

            assert articlePublication != null;
            articlePublication.goToDraftsTab();
            articlePublication.workWithInformationWindowInDrafts();
            try {
                articlePublication.publishedArticleDraft(articleNumberFromTheLeftEdge);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        closeDriver(firefoxDriver4);

    }
}
