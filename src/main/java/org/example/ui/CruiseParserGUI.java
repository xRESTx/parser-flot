package org.example.ui;

import org.example.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.example.Main.vodohod;

public class CruiseParserGUI {

    public static void main(String[] args) {

        File directory = new File("./tempFile");
        if(!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Папка создана");
            }
        }
        // Создаем главное окно
        JFrame frame = new JFrame("Cruise Routes Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
//
//        // Панель для ввода данных
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        // Кнопки для запуска парсинга различных сайтов
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        Dimension buttonSize = new Dimension(150, 50);

        JButton volgaButton = new JButton("Parse VolgaPles");
        volgaButton.setPreferredSize(buttonSize);
        JButton whiteSwanButton = new JButton("Parse White Swan");
        whiteSwanButton.setPreferredSize(buttonSize);
        JButton caesarTravelButton = new JButton("Parse Caesar Travel");
        caesarTravelButton.setPreferredSize(buttonSize);
        JButton azuritButton = new JButton("Parse Azurit");
        azuritButton.setPreferredSize(buttonSize);
        JButton sputnikButton = new JButton("Parse Sputnik-Hermes");
        sputnikButton.setPreferredSize(buttonSize);
        JButton gamaButton = new JButton("Parse Gama");
        gamaButton.setPreferredSize(buttonSize);
        JButton vodohodButton = new JButton("Parse Vodohod");
        vodohodButton.setPreferredSize(buttonSize);

        buttonPanel.add(volgaButton);
        buttonPanel.add(whiteSwanButton);
        buttonPanel.add(caesarTravelButton);
        buttonPanel.add(azuritButton);
        buttonPanel.add(sputnikButton);
        buttonPanel.add(gamaButton);
        buttonPanel.add(vodohodButton);

        // Область для отображения логов
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(580, 150));

        // Добавление обработчиков для кнопок
        volgaButton.addActionListener(e -> {
            volgaButton.setEnabled(false);
            logArea.append("Starting parsing VolgaPles...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File file = new File("tempFile/VolgaPles.txt");
                File file1 = new File("VolgaPles.txt");
                if(file1.exists()){
                    file1.delete();
                }
                if(file.exists()){
                    file.delete();
                }
                volgaPlace(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing VolgaPles completed.\n");
                    scrollToBottom(logArea);
                });
                volgaButton.setEnabled(true);
            }).start();
        });

        gamaButton.addActionListener(e -> {
            gamaButton.setEnabled(false);
            logArea.append("Starting parsing Gama...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File file1 = new File("./Gama");
                if(file1.exists()){
                    file1.delete();
                }
                gamaInfo(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Gama completed.\n");
                    scrollToBottom(logArea);
                });
                gamaButton.setEnabled(true);
            }).start();
        });
        sputnikButton.addActionListener(e -> {
            sputnikButton.setEnabled(false);
            logArea.append("Starting parsing Sputnik-Hermes...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File file = new File("SputnikGermes.txt");
                if(file.exists()){
                    file.delete();
                }
                SputnikGermes(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Sputnik-hermes completed.\n");
                    scrollToBottom(logArea);
                });
                sputnikButton.setEnabled(true);
            }).start();
        });

        whiteSwanButton.addActionListener(e -> {
            whiteSwanButton.setEnabled(false);
            logArea.append("Starting parsing White Swan...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File file = new File("WhiteSwan.txt");
                if(file.exists()){
                    file.delete();
                }
                WhiteSwan(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing White Swan completed.\n");
                    scrollToBottom(logArea);
                });
                whiteSwanButton.setEnabled(true);
            }).start();
        });

        caesarTravelButton.addActionListener(e -> {
            caesarTravelButton.setEnabled(false);
            logArea.append("Starting parsing Caesar Travel...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File fileTemp = new File("tempFile/CeasarTravel.txt");
                File file = new File("CeasarTravel.txt");
                if(file.exists()){
                    file.delete();
                }
                if(fileTemp.exists()){
                    fileTemp.delete();
                }
                CeasarTravel(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Caesar Travel completed.\n");
                    scrollToBottom(logArea);
                });
                caesarTravelButton.setEnabled(true);
            }).start();
        });

        azuritButton.addActionListener(e -> {
            azuritButton.setEnabled(false);
            logArea.append("Starting parsing Azurit Travel...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                File fileTemp = new File("tempFile/Azurit.txt");
                File file = new File("Azurit.txt");
                if(file.exists()){
                    file.delete();
                }
                if(fileTemp.exists()){
                    fileTemp.delete();
                }
                Azurit(logArea);
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Azurit Travel completed.\n");
                    scrollToBottom(logArea);
                });
                azuritButton.setEnabled(true);
            }).start();
        });

        vodohodButton.addActionListener(e -> {
            vodohodButton.setEnabled(false);
            logArea.append("Starting parsing Vodohod...\n");
            scrollToBottom(logArea);
            new Thread(() -> {
                vodohod();
                SwingUtilities.invokeLater(() -> {
                    logArea.append("Parsing Azurit Travel completed.\n");
                    scrollToBottom(logArea);
                });
                vodohodButton.setEnabled(true);
            }).start();
        });

        // Размещение компонентов в окне
        frame.setLayout(new BorderLayout(10, 10));
//        frame.add(inputPanel, BorderLayout.NORTH);
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
            parveVolga.Course(url, "tempFile/VolgaPles.txt");
        }
        replaceMonthNamesInFile("tempFile/VolgaPles.txt","VolgaPles.txt" );
    }

    static public void SputnikGermes(JTextArea logArea) {
        Parser_SputnikGermes parserSputnikGermes = new Parser_SputnikGermes();
        String[] urls = {
                "https://river.sputnik-germes.ru/index.php?option=com_content&view=article&id=9&aid=1000&t=49&price1=100&price2=200000&day1=01-04-25&day2=01-11-25&dlitfrom=01&dlitto=30&city=0&mc=&acts=&preset=11111",
                "https://river.sputnik-germes.ru/index.php?option=com_content&view=article&id=9&aid=1000&t=2&price1=100&price2=200000&day1=01-04-25&day2=01-11-25&dlitfrom=01&dlitto=30&city=0&mc=&acts=&preset=11111",
                "https://river.sputnik-germes.ru/index.php?option=com_content&view=article&id=9&aid=1000&t=128&price1=100&price2=200000&day1=01-04-25&day2=01-11-25&dlitfrom=01&dlitto=30&city=0&mc=&acts=&preset=11111",
                "https://river.sputnik-germes.ru/index.php?option=com_content&view=article&id=9&aid=1000&t=53&price1=100&price2=200000&day1=01-04-25&day2=01-11-25&dlitfrom=01&dlitto=30&city=0&mc=&acts=&preset=11111"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parserSputnikGermes.Course(url, "SputnikGermes.txt");
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
            parseCaesarTravel.Course(url, "tempFile/CeasarTravel.txt");
        }
        replaceMonthNamesInFile("tempFile/CeasarTravel.txt","CeasarTravel.txt" );
    }

    static public void WhiteSwan(JTextArea logArea) {
        ParserWhiteSwan parserWhiteSwan = new ParserWhiteSwan();
        String[] urls = {
                "https://www.bely-lebed.ru/ship.asp?t=147",
                "https://www.bely-lebed.ru/ship.asp?t=132",
                "https://www.bely-lebed.ru/ship.asp?t=131"
        };
        for (String url :  urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parserWhiteSwan.Course(url, "WhiteSwan.txt");
        }
    }
    static public void gamaInfo(JTextArea logArea){
        Parser_gama parserGama = new Parser_gama();
        String[] urlsGama = {
                "https://gama-nn.ru/booking/tours/pts4pdd0pcc51159588/",
                "https://gama-nn.ru/booking/tours/pts7pdd0pcc48852463/",
                "https://gama-nn.ru/booking/tours/pts64pdd0pcc66281954/",
                "https://gama-nn.ru/booking/tours/pts119pdd0pcc59832314/",
                "https://gama-nn.ru/booking/tours/pts12pdd0pcc58976894/",
                "https://gama-nn.ru/booking/tours/pts52pdd0pcc24862626/",
                "https://gama-nn.ru/booking/tours/pts11pdd0pcc32656545/",
                "https://gama-nn.ru/booking/tours/pts39pdd0pcc72646484/",
                "https://gama-nn.ru/booking/tours/pts50pdd0pcc79437688/",
                "https://gama-nn.ru/booking/tours/pts141pdd0pcc22489493/",
                "https://gama-nn.ru/booking/tours/pts60pdd0pcc57675214/",
                "https://gama-nn.ru/booking/tours/pts74pdd0pcc68545846/",
                "https://gama-nn.ru/booking/tours/pts2pdd0pcc33532292/",
                "https://gama-nn.ru/booking/tours/pts42pdd0pcc27888798/",
                "https://gama-nn.ru/booking/tours/pts1pdd0pcc26483433/",
                "https://gama-nn.ru/booking/tours/pts84pdd0pcc52483775/",
                "https://gama-nn.ru/booking/tours/pts95pdd0pcc48472153/",
                "https://gama-nn.ru/booking/tours/pts5pdd0pcc81992714/",
                "https://gama-nn.ru/booking/tours/pts8pdd0pcc65896382/",
                "https://gama-nn.ru/booking/tours/pts102pdd0pcc57925189/",
                "https://gama-nn.ru/booking/tours/pts13pdd0pcc82665779/",
                "https://gama-nn.ru/booking/tours/pts14pdd0pcc41542399/",
                "https://gama-nn.ru/booking/tours/pts148pdd0pcc49266578/",
                "https://gama-nn.ru/booking/tours/pts149pdd0pcc28936716/"
        };
        for (String url : urlsGama) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parserGama.CourseInfo(url,"Gama.txt");
        }
    }
    static public void Azurit(JTextArea logArea) {
        ParseAzurit parseAzurit = new ParseAzurit();
        String[] urls = {
                "https://azurit-tour.ru/kruizy/kruizy-na-t-h-ivan-bunin/",
                "https://azurit-tour.ru/kruizy/kruiz-na-t-h-pavel-mironov/kruizy/"
        };
        for (String url : urls) {
            logArea.append("Parsing URL: " + url + "\n");
            scrollToBottom(logArea);
            parseAzurit.Course(url, "tempFile/Azurit.txt");
        }
        replaceMonthNamesInFile("tempFile/Azurit.txt","Azurit.txt" );
    }

    private static void scrollToBottom(JTextArea textArea) {
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    public static void replaceMonthNamesInFile(String inputFilePath, String outputFilePath) {
        Map<String, String> months = new HashMap<>();
        months.put(" января ", ".01.2025 ");
        months.put(" февраля ", ".02.2025 ");
        months.put(" марта ", ".03.2025 ");
        months.put(" апреля ", ".04.2025 ");
        months.put(" мая ", ".05.2025 ");
        months.put(" июня ", ".06.2025 ");
        months.put(" июля ", ".07.2025 ");
        months.put(" августа ", ".08.2025 ");
        months.put(" сентября ", ".09.2025 ");
        months.put(" октября ", ".10.2025 ");
        months.put(" ноября ", ".11.2025 ");
        months.put(" декабря ", ".12.2025 ");
        months.put(" Января ", ".01.");
        months.put(" Февраля ", ".02.");
        months.put(" Марта ", ".03.");
        months.put(" Апреля ", ".04.");
        months.put(" Мая ", ".05.");
        months.put(" Июня ", ".06.");
        months.put(" Июля ", ".07.");
        months.put(" Августа ", ".08. ");
        months.put(" Сентября ", ".09.");
        months.put(" Октября ", ".10.");
        months.put(" Ноября ", ".11.");
        months.put(" Декабря ", ".12.");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                for (Map.Entry<String, String> entry : months.entrySet()) {
                    line = line.replaceAll(entry.getKey(), entry.getValue());
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
