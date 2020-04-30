import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class YandexZen {

    public static final List<String> DRIVER_NAMES = new ArrayList<String>() {{
        add("webdriver.gecko.driver");
        add("webdriver.chrome.driver");
    }};

    public void setDriver(String driverName, String driverFilePath) {
        System.setProperty(driverName, driverFilePath);
    }

    public static String getDataFromPropertyFile(String property) {
        //файл, который хранит свойства нашего проекта
        File file = new File("src/main/resources/data.properties");

        //создаем объект Properties и загружаем в него данные из файла.
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //получаем значения свойств из объекта Properties:
        return properties.getProperty(property);
    }

    public FirefoxProfile specifyFirefoxProfile(String myFirefoxProfile) {
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile firefoxProfile = profile.getProfile(myFirefoxProfile);
        return firefoxProfile;
    }


    public FirefoxOptions setFirefoxMode() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("-headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (setFirefoxHeadlessMode.equals("yes")) {
            firefoxOptions.setBinary(firefoxBinary);
        }
        return firefoxOptions;
    }

    public void openWebPage(WebDriver driver, String urlLine) {
        driver.get(urlLine);
    }

    public void closeDriver(WebDriver driver) {
        driver.quit();
    }

    public void maximizeWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    public static void setWindowSize(WebDriver driver) {
        Dimension dimension = new Dimension(1920, 1080);
        driver.manage().window().setSize(dimension);
    }

    //получаем значения свойств из объекта Properties:
    public static String urlTextFilePathName = getDataFromPropertyFile("urlTextFilePathName");
    public static String yandexZenChannelUrl = getDataFromPropertyFile("yandexZenChannelUrl");
    public static String myFirefoxProfile = getDataFromPropertyFile("myFirefoxProfile");
    public static String setFirefoxHeadlessMode = getDataFromPropertyFile("setFirefoxHeadlessMode");

}
