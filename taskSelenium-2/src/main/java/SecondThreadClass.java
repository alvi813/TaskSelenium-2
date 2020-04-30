import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SecondThreadClass extends YandexZen implements Runnable {

    // точка входа нового потока
    @Override
    public void run() {

        setDriver(DRIVER_NAMES.get(0), "drivers/geckodriver.exe");
        FirefoxOptions firefoxOptions = setFirefoxMode();
            WebDriver firefoxDriver4 = new FirefoxDriver(firefoxOptions);

        ArticlePublication articlePublication = null;
        try {
            articlePublication = new ArticlePublication(firefoxDriver4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        openWebPage(firefoxDriver4, yandexZenChannelUrl);
        try {
            assert articlePublication != null;
            articlePublication.publicArticle();
        } catch (IOException e) {
            e.printStackTrace();
        }







        closeDriver(firefoxDriver4);

    }
}
