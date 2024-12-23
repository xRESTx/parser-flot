package org.example.ui;

import org.example.ParseAzurit;
import org.example.ParseCaesarTravel;
import org.example.ParseVolgaPles;
import org.example.ParserWhiteSwan;

import javax.swing.*;
import java.awt.*;

public class CruiseParserGUI {

    public static void main(String[] args) {
        // Создаем главное окно
        JFrame frame = new JFrame("Cruise Routes Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Панель для ввода данных
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Кнопки для запуска парсинга различных сайтов
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        Dimension buttonSize = new Dimension(150, 50);

        JButton volgaButton = new JButton("Parse VolgaPles");
        volgaButton.setPreferredSize(buttonSize);
        JButton whiteSwanButton = new JButton("Parse White Swan");
        whiteSwanButton.setPreferredSize(buttonSize);
        JButton caesarTravelButton = new JButton("Parse Caesar Travel");
        caesarTravelButton.setPreferredSize(buttonSize);
        JButton azuritButton = new JButton("Parse Azurit");
        azuritButton.setPreferredSize(buttonSize);

        buttonPanel.add(volgaButton);
        buttonPanel.add(whiteSwanButton);
        buttonPanel.add(caesarTravelButton);
        buttonPanel.add(azuritButton);

        // Область для отображения логов
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(580, 150));

        // Добавление обработчиков для кнопок
        volgaButton.addActionListener(e -> {
            logArea.append("Starting parsing VolgaPles...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                volgaPlace(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing VolgaPles completed.\n");
                    scrollToBottom(logArea);
                });
            }).start();
        });

        whiteSwanButton.addActionListener(e -> {
            logArea.append("Starting parsing White Swan...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                WhiteSwan(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing White Swan completed.\n");
                    scrollToBottom(logArea);
                });
            }).start();
        });

        caesarTravelButton.addActionListener(e -> {
            logArea.append("Starting parsing Caesar Travel...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                CeasarTravel(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Caesar Travel completed.\n");
                    scrollToBottom(logArea);
                });
            }).start();
        });

        azuritButton.addActionListener(e -> {
            logArea.append("Starting parsing Azurit Travel...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                Azurit(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Azurit Travel completed.\n");
                    scrollToBottom(logArea);
                });
            }).start();
        });

        // Размещение компонентов в окне
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Отображаем окно
        frame.setVisible(true);
    }

    static public void volgaPlace(JTextArea logArea) {
        ParseVolgaPles parveVolga = new ParseVolgaPles();
        String[] urls = {
                "https://volgaples.ru/boats/boat-1.html",
                "https://volgaples.ru/boats/boat-2.html",
                "https://volgaples.ru/boats/boat-3.html",
                "https://volgaples.ru/boats/boat-4.html"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parveVolga.Course(url, "VolgaPles.txt");
        }
    }

    static public void CeasarTravel(JTextArea logArea) {
        ParseCaesarTravel parseCaesarTravel = new ParseCaesarTravel();
        String[] urls = {
                "https://www.cezar-travel.ru/president-ship",
                "https://www.cezar-travel.ru/muromets-ship"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parseCaesarTravel.Course(url, "CeasarTravel.txt");
        }
    }

    static public void WhiteSwan(JTextArea logArea) {
        ParserWhiteSwan parserWhiteSwan = new ParserWhiteSwan();
        String[] urls = {
                "https://www.bely-lebed.ru/ship.asp?t=147",
                "https://www.bely-lebed.ru/ship.asp?t=132"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parserWhiteSwan.Course(url, "WhiteSwan.txt");
        }
    }
    static public void Azurit(JTextArea logArea) {
        ParseAzurit parseAzurit = new ParseAzurit();
        String[] urls = {
                "https://azurit-tour.ru/kruizy/kruizy-na-t-h-ivan-bunin/"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parseAzurit.Course(url, "Azurit.txt");
        }
    }
    private static void scrollToBottom(JTextArea textArea) {
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}
