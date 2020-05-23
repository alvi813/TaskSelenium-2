import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class YandexZenArticleCreate {

    private WebDriver driver;
    static WebDriverWait wait;
    static WebDriverWait waitImgLoad;

    public YandexZenArticleCreate(WebDriver driver) {
        this.driver = driver;
    }

    private int tagListIndex = 0;
    private int imgUrlListIndex = 0;

    public void downloadFileImage(List<String> imgUrlLinks, String pathSaveImage) throws IOException {
        URL connection = new URL(imgUrlLinks.get(imgUrlListIndex++));
        HttpURLConnection urlconn;
        urlconn = (HttpURLConnection) connection.openConnection();
        urlconn.setRequestMethod("GET");
        urlconn.connect();
        InputStream in = null;
        in = urlconn.getInputStream();
        OutputStream writer = new FileOutputStream(pathSaveImage);
        byte[] buffer = new byte[1];
        int c = in.read(buffer);
        while (c > 0) {
            writer.write(buffer, 0, c);
            c = in.read(buffer);
        }
        writer.flush();
        writer.close();
        in.close();
    }

    public void addArticleToYandexZen(String translatedArticleTitle, List<String> translatedTextList, List<String> tagList, List<String> imgUrlLinks) throws IOException {
        wait = new WebDriverWait(driver, 10);
        waitImgLoad = new WebDriverWait(driver, 60);  // для ожидания загрузки картинок

        // ждём, пока появится кнопка "+":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[@class='control link link_theme_zen']"))));

        // нажать на кнопку "+":
        driver.findElement(By.xpath("//span[@class='control link link_theme_zen']")).click();

        // ждём, пока появится кнопка "статья":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@class='new-publication-dropdown__button']"))));

        // нажать на кнопку "статья":
        driver.findElement(By.xpath("//div[@class='new-publication-dropdown__button']")).click();


        try {
            // если появится окно с подсказками:
            if (wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[contains(text(), 'Статья')]")))).isDisplayed()) {
                // закрываем появившееся окно с подсказками:
                driver.findElement(By.xpath("//div[@class='close-cross close-cross_black close-cross_size_s help-popup__close-cross']")).click();
            }
        }
        catch (Exception ignored){
        }


        try {
            // если появится окно с подсказкой:
            if (wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//p[contains(text(), 'Теперь в статью можно вставить виджет Яндекс Форм')]")))).isDisplayed()) {
                // закрываем появившееся окно с подсказкой:
                driver.findElement(By.xpath("//div[@class='ui-lib-popup-element__close']")).click();
            }
        }
        catch (Exception ignored){
        }

        // ждём, пока появится поле ввода текста:
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='editor__content']")));

        // копируем заголовок в поле ввода заголовка:
        driver.findElement(By.xpath("//div[@class='editable-input editor__title-input']//div[@class='notranslate public-DraftEditor-content']")).sendKeys(translatedArticleTitle);

        // копируем текст поабзацно в поле ввода статьи:
        WebElement webTextInputField = driver.findElement(By.xpath("//div[@class='zen-editor']//div[@class='notranslate public-DraftEditor-content']"));

        for (String paragraph : translatedTextList) {

            if (tagList.get(tagListIndex++).equals("figure")) {

                // производим скачивание по url картинки:
                String pathSaveImage = "src/main/images/articleImage.jpeg";

                downloadFileImage(imgUrlLinks, pathSaveImage);

                File file = new File(pathSaveImage);


                // нажмём на кнопку, чтобы вставить картинку:
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='side-button side-button_logo_image ']")));
                driver.findElement(By.xpath("//button[@class='side-button side-button_logo_image ']")).click();

                // ждём, пока появится окно для вставки ссылки на картинку:
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='popup__content popup__content_normal']")));

                // находим на странице элемент, который является файловым полем ввода (input с типом file), и печатаем в него абсолютный путь к файлу с картинкой:
                driver.findElement(By.xpath("//input[@class='image-popup__file-input']")).sendKeys(file.getAbsolutePath());

                //ждём, пока картинка появится на странице:
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//figure[" + imgUrlListIndex + "]//img[@class='zen-editor-block-image__image']")));

                // ждём, пока загрузятся картинки:
                waitImgLoad.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[contains(text(), 'загружается')]")));

                // удаляем временный файл, в котором сохраняли картинки для статьи, при завершении работы программы:
                file.deleteOnExit();

                // перейдём в элемент под картинкой (для вставки подписи под картинкой):
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//figcaption[@class='zen-editor-block-image__caption zen-editor-block-image__caption_empty']")));
                driver.findElement(By.xpath("//figcaption[@class='zen-editor-block-image__caption zen-editor-block-image__caption_empty']")).click();
            }

            webTextInputField.sendKeys(paragraph);
            webTextInputField.sendKeys(Keys.ENTER);
        }

        // ждём, пока появится надпись "Сохранено":
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'Сохранено')]")));
    }
}



       /*
        //for debugging:
        System.out.println(driver.getPageSource());
        //
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotPath = "D:\\Temp\\Screenshots\\" + screenshot.getName();
        FileUtils.copyFile(screenshot, new File(screenshotPath));
      */


             /*
                URL connection = new URL(imgUrlLinks.get(0));
                HttpURLConnection urlconn;
                urlconn = (HttpURLConnection) connection.openConnection();
                urlconn.setRequestMethod("GET");
                urlconn.connect();
                InputStream in = null;
                in = urlconn.getInputStream();
                OutputStream writer = new FileOutputStream("src/main/images/articleImage.jpeg");
                byte buffer[] = new byte[1];
                int c = in.read(buffer);
                while (c > 0) {
                    writer.write(buffer, 0, c);
                    c = in.read(buffer);
                }
                writer.flush();
                writer.close();
                in.close();


                // производим скачивание по url картинки:
                String pathSaveImage = "src/main/images/articleImage.jpeg";
                BufferedImage bufferedImage = ImageIO.read(new URL(imgUrlLinks.get(imgUrlListIndex)));
                File file = new File(pathSaveImage);
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (bufferedImage != null) {
                    ImageIO.write(bufferedImage, "jpeg", file);
                }




                // нажмём на кнопку, чтобы вставить картинку:
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='side-button side-button_logo_image ']")));

                driver.findElement(By.xpath("//button[@class='side-button side-button_logo_image ']")).click();

                // помещаем url картинки из коллекции в буфер обмена:
                StringSelection stringSelection = new StringSelection(imgUrlLinks.get(imgUrlListIndex));
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

                // ждём, пока появится окно для вставки ссылки на картинку:
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='image-popup__url-input']")));
                // Set focus on target element by clicking on it:
                driver.findElement(By.xpath("//div[@class='image-popup__url-input']//input")).click();
                //now paste our content from clipboard:
                driver.findElement(By.xpath("//div[@class='image-popup__url-input']//input")).sendKeys(Keys.LEFT_CONTROL + "v");
                imgUrlListIndex++;

                // ждём, пока загрузятся картинки:
                waitImgLoad.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//span[contains(text(), 'загружается')]")));
                // вставим подписи под картинками:
                int figureNumber = 1;
                String classAttribute = "zen-editor-block-image__caption zen-editor-block-image__caption_empty";
                WebElement figure = driver.findElement(By.xpath("//div[@class='zen-editor']//div[@class='notranslate public-DraftEditor-content']//figure[" + figureNumber + "]//figcaption"));
                while (!(figure.getAttribute("class").equals(classAttribute))) {
                    figureNumber++;
                    figure = driver.findElement(By.xpath("//div[@class='zen-editor']//div[@class='notranslate public-DraftEditor-content']//figure[" + figureNumber + "]//figcaption"));
                }
                figure.click();
             */

                // webTextInputField.sendKeys(file.getAbsolutePath());



             /*
                Robot rb = new Robot();
                rb.keyPress(KeyEvent.VK_CONTROL);
                rb.keyPress(KeyEvent.VK_V);
                rb.keyRelease(KeyEvent.VK_V);
                rb.keyRelease(KeyEvent.VK_CONTROL);
            */

             /*
             //копируем картинку в буфер обмена:
                Image image = ImageIO.read(new URL(imgUrlLinks.get(imgUrlListIndex++)));
                ImageTransferable imageTransferable = new ImageTransferable(image);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageTransferable, null);
              */


               //waitHintWindow.ignoring(org.openqa.selenium.TimeoutException.class);


