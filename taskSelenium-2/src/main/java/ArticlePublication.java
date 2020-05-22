import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;;
import java.net.URL;


public class ArticlePublication {


    private WebDriver driver;
    static WebDriverWait wait;  // by default in our program we will wait 5 sec
    static WebDriverWait wait3sec;

    private static int capthaNumber = 1;
    boolean captchaWindowHandle = false;
    boolean startCaptchaHandling = false;

    public ArticlePublication(WebDriver driver) throws IOException {
        this.driver = driver;
        wait3sec = new WebDriverWait(driver, 3);
        wait = new WebDriverWait(driver, 5);
    }


    public void goToDraftsTab() {
        // ждём, пока появится кнопка "Черновики":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[contains(text(), 'Черновики')]"))));

        // нажать на кнопку "Черновики":
        driver.findElement(By.xpath("//span[contains(text(), 'Черновики')]")).click();
    }

    public void workWithInformationWindowInDrafts() {
        //после некоторого количества публикаций появляется окно с различного рода информацией от яндекс-дзен
        try {
            // если появится окно:
            if (wait3sec.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@class='notification-popup__content']")))).isDisplayed()) {
                // закрываем появившееся окно:
                driver.findElement(By.xpath("button[@class='ui-lib-modal__close']")).click();
            }
        } catch (Exception ignored) {
        }
    }

    public void workWithInformationWindowInArticleEditor() {
        try {
            // если появится окно с подсказками:
            if (wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//span[contains(text(), 'Статья')]")))).isDisplayed()) {
                // закрываем появившееся окно с подсказками:
                driver.findElement(By.xpath("//div[@class='close-cross close-cross_black close-cross_size_s help-popup__close-cross']")).click();
            }
        } catch (Exception ignored) {
        }
    }

    public void removeAllAutoSuggestedTags() {
        // удалить все автоматически предложенные тэги:
        try {
            while (true) {
                if (wait3sec.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//button[@class='ui-lib-tag-input-child__cross']")))).isDisplayed()) {
                    driver.findElement(By.xpath("//button[@class='ui-lib-tag-input-child__cross']")).click();
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void chooseFirstImageAsTheCoverOfArticle() {
        // выберем в качестве обложки статьи первую картинку:
        driver.findElement(By.xpath("//div[@class='cover-picker']//div[@class='cover-picker__cover-container']")).click();
    }

    public void addingOurOwnTags() {
        // добавим собственные тэги:
        driver.findElement(By.xpath("//input[contains(@class, 'ui-lib-tag-input')]")).sendKeys("локомотивы, поезда, вагоны, тепловозы", Keys.ENTER);
    }

    public void goToSettingsTab() {
        // перейдём на вкладку "Настройки":
        driver.findElement(By.xpath("//button/span[contains(text(), 'Настройки')]")).click();
        // ждём, когда загрузится вкладка "Настройки":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[contains(@class, 'publication-settings-additional')]"))));
    }

    public void disableCommentsIfTheyAreEnabled() {
        // если комментарии включены, отключим их:
        try {
            if (wait3sec.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@class='publication-settings-content']/div[1]/label[contains(@class, 'ui-lib-checkbox _size_l _with-label')]")))).isDisplayed()) {
                driver.findElement(By.xpath("//span[contains(text(), 'Отключить комментарии')]")).click();
            }
        } catch (Exception ignored) {
        }
    }

    public void publishedArticleDraft(int articleNumberFromTheLeftEdge) throws InterruptedException {
        // выбрать третью слева статью.
        // проверим, существует ли в данный момент данная статья.
        // Если нет, ждём, когда появится:
        boolean elementIsPresent = false;
        do {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(("//div[contains(@class, 'publication-card-item')][" + articleNumberFromTheLeftEdge + "]" +
                        "//a[contains(@class, card-cover)]/div[contains(@class, card-cover)]")))));
                elementIsPresent = true;
            } catch (Exception ignored) {
                Thread.sleep(4000);
            }
        } while (!elementIsPresent);


        WebElement webElement = driver.findElement(By.xpath("//div[contains(@class, 'publication-card-item')][" + articleNumberFromTheLeftEdge + "]//a[contains(@class, card-cover)]/div[contains(@class, card-cover)]"));

        // убедимся, что для этой публикации присутствует фоновая картинка:
        // если картинка есть, опубликуем статью:
        if (webElement.getAttribute("style").contains("background-color")) {
            //System.out.println("With picture");
            driver.findElement(By.xpath("//div[contains(@class, 'publication-card-item')][" + articleNumberFromTheLeftEdge + "]//button[contains(@class, 'ui-lib-button')]")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ui-lib-context-menu']//span[contains(text(), 'Отредактировать')]")));
            driver.findElement(By.xpath("//div[@class='ui-lib-context-menu']//span[contains(text(), 'Отредактировать')]")).click();


            workWithInformationWindowInArticleEditor();


            wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//button/span[contains(text(), 'Опубликовать')]"))));
            driver.findElement(By.xpath("//button/span[contains(text(), 'Опубликовать')]")).click();

            wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//form[@class='publication-settings__form']"))));

            chooseFirstImageAsTheCoverOfArticle();

            removeAllAutoSuggestedTags();

            addingOurOwnTags();

            goToSettingsTab();

            disableCommentsIfTheyAreEnabled();


            //Нажмём кнопку "Опубликовать":
            driver.findElement(By.xpath("//div[@class='ui-lib-modal__scrollbar-fix']//button/span[contains(text(), 'Опубликовать')]")).click();

            // если появится капча, обработаем её:
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@class='ReactModal__Content ReactModal__Content--after-open captcha-popup']"))));

                startCaptchaHandling = true;
                workWithCaptcha();
                startCaptchaHandling = false;

            } catch (Exception ignored) {
            }

            waitPublicationsPageAppearance();

        }

        // иначе удалим эту статью:
        else {
            //System.out.println("No picture");
            driver.findElement(By.xpath("//div[contains(@class, 'publication-card-item')][3]//button[contains(@class, 'ui-lib-button')]")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='ui-lib-context-menu']//span[contains(text(), 'Удалить')]")));
            driver.findElement(By.xpath("//div[@class='ui-lib-context-menu']//span[contains(text(), 'Удалить')]")).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='popup__content popup__content_normal']/p[contains(text(), Удалить)]")));
            driver.findElement(By.xpath("//div[@class='popup__content popup__content_normal']//button[@class='control button2 button2_view_classic button2_size_m button2_theme_zen-clear-black']")).click();
        }
    }


    public synchronized void waitPublicationsPageAppearance() {
        while (startCaptchaHandling) {
            try {
                wait(); // ждём, пока будет производиться работа с капчей (после этого сможем перейти на страницу "Публикации")
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // дождёмся скрытия материала статьи со страницы и появления страницы "Публикации":
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//div[@class='publications-groups-view__status-filter']"))));
    }

    public synchronized void waitPublicButtonAppearanceAfterCaptchaInput() {
        while (captchaWindowHandle) {
            try {
                wait(); // ждём, пока будет производиться работа с окном капчи (после этого сможем нажать кнопку "Опубликовать")
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //ждём, пока не будет обработана капча и не станет доступна кнопка "Опубликовать":
        wait.until((ExpectedConditions.elementToBeClickable((driver.findElement(By.xpath("//footer[@class='captcha-popup__footer']/button/span[contains(text(), 'Опубликовать')]"))))));
    }

    public synchronized void waitInputCaptchaWindowWork() {
        notify();
    }


    public synchronized void workWithCaptcha() throws IOException, InterruptedException {
        do {
            captchaWindowHandle = true;
            String captcha = driver.findElement(By.xpath("//img[@class='captcha__image']")).getAttribute("src");
            //System.out.println(captcha);

            // производим скачивание по url картинки:
            String pathSaveImage = "src/main/images/captcha/" + (capthaNumber++) + ".png";
            BufferedImage bufferedImage = ImageIO.read(new URL(captcha));
            File file = new File(pathSaveImage);
            if (!file.exists()) {
                file.createNewFile();
            }
            if (bufferedImage != null) {
                ImageIO.write(bufferedImage, "png", file);
            }

            //--------------------------------------------------------------------------------------------------

            // создать фрейм в потоке диспетчеризации событий:
            SwingUtilities.invokeLater(new Runnable() {                                                                      // java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CaptchaWindow captchaWindow = new CaptchaWindow(pathSaveImage);
                    JTextField humanEnteredCaptcha = captchaWindow.getTextField();

                    humanEnteredCaptcha.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {


                            try {
                                // проверим, что введённое значение является числом:
                                Integer.parseInt(humanEnteredCaptcha.getText());
                                if (humanEnteredCaptcha.getText().length() == 6) {
                                    // в поле ввода капчи в Яндекс Дзене поместим введённые человеком символы:
                                    driver.findElement(By.xpath("//div[@class='captcha']//input[@class='ui-lib-input__control']")).sendKeys(humanEnteredCaptcha.getText());
                                    System.out.println(humanEnteredCaptcha);
                                    captchaWindowHandle = false;
                                    waitInputCaptchaWindowWork();
                                    captchaWindow.getFrame().dispose();  // убираем окно с экрана и освобождаем все принадлежащие ему ресурсы
                                } else {
                                    JOptionPane.showMessageDialog(null, "Введите корректные данные!", "Warning", JOptionPane.WARNING_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Введите корректные данные!", "Warning", JOptionPane.WARNING_MESSAGE);
                            }

                        }
                    });
                }
            });

            //--------------------------------------------------------------------------------------------------
            waitPublicButtonAppearanceAfterCaptchaInput(); // работа с окном капчи закончена, возвращаем управление для выполнения дальнейших действий
            // нажмём кнопку "Опубликовать":
            driver.findElement(By.xpath("//footer[@class='captcha-popup__footer']/button/span[contains(text(), 'Опубликовать')]")).click();


            try {
                // ждём, появится ли вновь пустое поле для ввода капчи (возможно при случае, если ввели неверную капчу):
                wait3sec.until(ExpectedConditions.attributeToBe(driver.findElement(By.xpath("//input[@class='ui-lib-input__control']")), "value", ""));
            } catch (Exception ignored) {
            }

            // повторяем, пока отображается поле ввода капчи, т.е. пока не ввели верную капчу:
        } while (driver.findElement((By.xpath("//div[@class='ReactModal__Content ReactModal__Content--after-open captcha-popup']"))).isDisplayed());
        notify(); // капча обработана, возвращаем управление для выполнения дальнейших действий
    }
}
