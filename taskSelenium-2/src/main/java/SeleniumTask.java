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



    public static void main(String[] args) throws IOException, UnsupportedFlavorException, AWTException, URISyntaxException, InterruptedException {

        Thread t1 = new Thread(new FirstThreadClass());
        t1.start();  // запуск нового потока

        Thread t2 = new Thread(new SecondThreadClass());
        t2.start();  // запуск нового потока
    }
}
