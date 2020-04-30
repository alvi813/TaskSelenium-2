import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LinksCollection {

    private WebDriver driver;
    private String urlTextFilePathName;

    public LinksCollection(WebDriver driver, String urlTextFilePathName) throws IOException {
        this.driver = driver;
        this.urlTextFilePathName = urlTextFilePathName;
    }


    public String getUrlLineFromFile(int linkNumber) throws IOException {
        File file = new File(urlTextFilePathName);
        //создаем объект FileReader для объекта File:
        FileReader fr = new FileReader(file);
        //создаем BufferedReader с существующего FileReader для построчного считывания:
        BufferedReader reader = new BufferedReader(fr);
        // считаем строки из текстового файла, пока не достигнем нужной строки:
        int i = 1;
        String urlLine = null;
        while (i <= linkNumber) {
            urlLine = reader.readLine();
            ++i;
        }
        return urlLine;
    }


    public List<WebElement> collectWebElementLinks(String urlLine) {
        // вычленяем из URL-строки имя_канала, чтобы затем искать записи, относящиеся только к этому каналу:
        String channelName = urlLine.split("/")[4];

        // соберём ссылки, относящиеся к этому каналу, из статьи:
        List<WebElement> webLinksList = driver.findElements(By.xpath("//a[contains(@href,'https://zen.yandex.ru/media/" + channelName + "/')]"));

        return webLinksList;
    }


    public Set<String> collectNonrepeatingStringLinks(List<WebElement> webLinksList) throws IOException {
        Set<String> urlLinksSet = new LinkedHashSet<String>();

        // сначала считываем уже имеющиеся в файле ссылки, чтобы избежать повторений:
        File file = new File(urlTextFilePathName);
        //создаем объект FileReader для объекта File:
        FileReader fr = new FileReader(file);
        //создаем BufferedReader с существующего FileReader для построчного считывания:
        BufferedReader reader = new BufferedReader(fr);

        String urlLine = reader.readLine();
        while (urlLine != null) {
            urlLinksSet.add(urlLine);
            urlLine = reader.readLine();
        }

        // помещаем собранную коллекцию List<WebElement> в LinkedHashSet (в виде строк):
        for (WebElement webLink : webLinksList) {
            urlLinksSet.add(webLink.getAttribute("href"));
        }

        return urlLinksSet;
    }


    public void writeLinksToFile(Set<String> urlLinksSet) throws IOException {
        // запишем собранную коллекцию ссылок в файл:
        FileOutputStream output = new FileOutputStream(urlTextFilePathName);
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        for (String line : urlLinksSet) {
            output.write(line.getBytes());
            output.write(LINE_SEPARATOR.getBytes());
        }
    }


    public void printLinkCollectionToConsole(Set<String> urlLinksSet) {
        // выведем собранную коллекцию ссылок в консоль:
        for (String links : urlLinksSet) {
            System.out.println(links);
        }
    }

}
