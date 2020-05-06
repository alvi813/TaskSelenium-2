import org.openqa.selenium.By;
import org.openqa.selenium.json.JsonOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class CaptchaWindow {

    private String imagePath;
    private JTextField textField;
    private JFrame frame;

    public CaptchaWindow(String imagePath) {
        this.imagePath = imagePath;
        initComponents();
    }


    private void initComponents() {


        ImageIcon imageIcon = new ImageIcon(imagePath);

        JLabel img = new JLabel(imageIcon);
        img.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 5, true));

        textField = new JTextField(6);

        JPanel panel = new JPanel();
        panel.add(img, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.SOUTH);

        frame = new JFrame("Captcha");
        frame.setPreferredSize(new Dimension(250, 180)); // размер окна
        frame.setResizable(false); // неизменяемый размер окна
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // разместим по центру экрана
        frame.setVisible(true);
    }

    public JTextField getTextField() {
        return textField;
    }

    public JFrame getFrame() {
        return frame;
    }
}



/*
textField.addActionListener(new ActionListener() {

public void actionPerformed(ActionEvent e) {


        System.out.println(textField.getText());
        //driver.findElement(By.xpath("//input[contains(placeholder, 'Введите символы с картинки')]")).sendKeys(humanEnteredCaptcha);
        }
        });*/
