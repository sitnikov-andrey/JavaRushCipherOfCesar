import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean flag = true;
        while (flag){
            chooseOption();
            flag = continueOrBreak();

        }

    }

    private static boolean continueOrBreak(){
        System.out.println("Хотите продолжить работу в программе? Y/N:");
        Scanner console = new Scanner(System.in);
        String answer = console.nextLine();
        if ("Y".equals(answer)) {
            return true;
        } else if ("N".equals(answer)) {
            System.out.println("Пока!");
            return false;
        } else {
            System.out.println("Вы ввели не корректный символ! Попробуйте еще раз!");
            return continueOrBreak();
        }
    }

    private static void chooseOption(){
        System.out.println("Выберите один из вариантов:\n" +
                "1: Зашифровать файл\n" +
                "2: Расшифровать файл\n" +
                "3: Brutal Force\n" +
                "4: Статистический анализ\n" +
                "Введите число: ");

        Scanner console = new Scanner(System.in);
        while (true) {
            int option = console.nextInt();
            if (option > 0 && option < 5) {
                switch (option) {
                    case (1):
                        encrypt();
                        break;
                    case (2):
                        decrypt();
                        break;
                    case (3):
                        bruteForce();
                        break;
                    case (4):
                        statisticalDecrypt();
                        break;
                }
                break;
            } else {
                System.out.println("Вы вели не верные данные! Введи номер предложенного варианта!");
            }
        }
    }

    private static void encrypt() {
        Cypher cypher = new Cypher();
        Scanner console = new Scanner(System.in);

        System.out.println("Укажите путь до файлка .txt который Вы хотите закодировать:");
        String inputFilePath = console.nextLine();
        checkTxtFormat(inputFilePath);

        System.out.println("Укажите путь до файлка .txt в который будет записан зашифрованный текст:");
        String outputFilePath = console.nextLine();
        checkTxtFormat(outputFilePath);

        System.out.println("Укажите ключ, это целое число:");
        int key = console.nextInt();

        cypher.encrypt(new File(inputFilePath), new File(outputFilePath), key);

        System.out.println("Текст из файла был удачно закодирован!");
    }

    private static void decrypt() {
        Cypher cypher = new Cypher();
        Scanner console = new Scanner(System.in);

        System.out.println("Укажите путь до файлка .txt который Вы хотите расшифровать:");
        String inputFilePath = console.nextLine();
        checkTxtFormat(inputFilePath);

        System.out.println("Укажите путь до файлка .txt в который будет записан расшифрованный текст:");
        String outputFilePath = console.nextLine();
        checkTxtFormat(outputFilePath);

        System.out.println("Укажите ключ которым был зашифрован текст, это целое число:");
        int key = console.nextInt();

        cypher.decrypt(new File(inputFilePath), new File(outputFilePath), key);

        System.out.println("Текст из файла был удачно расшифрован!");
    }

    private static void bruteForce() {
        Cypher cypher = new Cypher();
        Scanner console = new Scanner(System.in);

        System.out.println("Укажите путь до файлка .txt который Вы хотите расшифровать:");
        String inputFilePath = console.nextLine();
        checkTxtFormat(inputFilePath);

        System.out.println("Укажите путь до файлка .txt в котором лежат ключевые слова для расшифровки текста.\n" +
                "Они должны быть записаны через запятую, без пробелов и Вы должны быть уверены, что эти слова есть в тексте:");
        String representativeFilePath = console.nextLine();
        checkTxtFormat(representativeFilePath);

        System.out.println("Укажите путь до файлка .txt в который будет записан расшифрованный текст:");
        String outputFilePath = console.nextLine();
        checkTxtFormat(outputFilePath);

        cypher.bruteForce(new File(inputFilePath), new File(representativeFilePath), new File(outputFilePath));

        System.out.println("Текст из файла был удачно расшифрован!");
    }

    private static void statisticalDecrypt() {
        Cypher cypher = new Cypher();
        Scanner console = new Scanner(System.in);

        System.out.println("Укажите путь до файлка .txt который Вы хотите расшифровать.\n" +
                "Он будет расшифрован по самому частому символу который встречается в тексте, предпологаем - это пробел:");
        String inputFilePath = console.nextLine();
        checkTxtFormat(inputFilePath);

        System.out.println("Укажите путь до файлка .txt в который будет записан расшифрованный текст:");
        String outputFilePath = console.nextLine();
        checkTxtFormat(outputFilePath);

        cypher.statisticalDecrypt(new File(inputFilePath), new File(outputFilePath));

        System.out.println("Текст из файла был удачно расшифрован!");
    }

    private static void checkTxtFormat(String file) {
        if (!file.endsWith(".txt")) {
            try {
                throw new Error("Файл должен быть с расширением .txt");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
