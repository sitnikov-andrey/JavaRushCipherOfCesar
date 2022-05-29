import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

import java.io.*;
import java.util.*;

@Command(name = "cypher", subcommands = {CommandLine.HelpCommand.class },
        description = "Caesar cypher command")
public class Cypher implements Runnable{

    @Spec CommandSpec spec;
    @Command(name = "encrypt", description = "Encrypt from file to file using key")
    void encrypt(
            @Parameters(paramLabel = "<source file>", description = "source file with text to encrypt") File src,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have encrypted text") File dest,
            @Parameters(paramLabel = "<key>", description = "key for encryption") int key) {

        try {
            FileReader outputFile = new FileReader(src);
            FileWriter inputFile = new FileWriter(dest, false);
            BufferedReader reader = new BufferedReader(outputFile);

            String line = reader.readLine();
            while (line != null) {
                StringBuilder inputString = new StringBuilder();

                //Считывем строку по одному char, находим его индекс в ALPHABET
                for (int i = 0; i < line.length(); i++) {
                    int index = Search.getIndexCharFromAlphabet(line.charAt(i));
                    //Если char был найден в ALPHABET смещаем значение char на ключ и записываем в inputString
                    if (index != -1) {
                        index = index+key;
                        inputString.append(Search.getNewCharByIndexFromAlphabet(index));
                    }
                }

                inputFile.write(inputString + "\n");
                inputFile.flush();
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "brute force", description = "Decrypt from file to file using brute force") // |3|
    void bruteForce(
            @Parameters(paramLabel = "<source file>", description = "source file with encrypted text") File src,
            @Option(names = {"-r", "--representative"}, description = "file with unencrypted representative text") File representativeFile,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") File dest) {
        try {
            FileReader representFile = new FileReader(representativeFile);
            FileWriter inputFile = new FileWriter(dest, false);
            //Получаем список контрольных слов
            List<String> controlWords = Arrays.asList(new BufferedReader(representFile).readLine().split(","));

            for (int key = 0; key > (Data.ALPHABET.length * -1); key--) {
                FileReader outputFile = new FileReader(src);
                BufferedReader reader = new BufferedReader(outputFile);
                String line = reader.readLine();
                List<String> allWords = new ArrayList<>();
                StringBuilder inputString = new StringBuilder();

                while (line != null) {
                    //Считывем строку по одному char, находим его индекс в ALPHABET
                    for (int i = 0; i < line.length(); i++) {

                        int index = Search.getIndexCharFromAlphabet(line.charAt(i));
                        //Если char был найден в ALPHABET смещаем значение char на ключ и записываем в inputString
                        if (index != -1) {
                            index = index+key;
                            if (index % Data.ALPHABET.length < 0) {
                                while (index < 0) {
                                    index = Data.ALPHABET.length + index;
                                }
                            }
                            inputString.append(Search.getNewCharByIndexFromAlphabet(index));
                        } else {
                            throw new Exception("Совершена попытка взлома! Операция будет остановлена!");
                        }
                    }
                    inputString.append("\n");

                    //В полученной строке убираем все знаки препинания и преобразуем ее в список строк.
                    //Этот список строк добавляем в список всех слов в котором будем искать контрольные слова
                    allWords.addAll(Arrays.asList(
                                    inputString.toString().replaceAll("[^\\dа-яёА-ЯЁ ]", "")
                                                          .split(" "))
                    );

                    line = reader.readLine();
                }

                //Проверяем, что все контрольные слова есть в списке
                if (controlWords.stream().allMatch(allWords::contains)) {
                    inputFile.write(inputString.toString());
                    inputFile.flush();
                    break;
                } else {
                    inputString.setLength(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Command(name = "statistical decryption", description = "Decrypt from file to file using statistical analysis") // |3|
    void statisticalDecrypt(
            @Parameters(paramLabel = "<source file>", description = "source file with encrypted text") File src,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") File dest) {
        Map<Character, Integer> map = new HashMap<>();
        try {
            FileReader outputFile = new FileReader(src);
            BufferedReader reader = new BufferedReader(outputFile);

            String line = reader.readLine();
            while (line != null) {

                //Считывем строку по одному char, находим его индекс в ALPHABET
                for (int i = 0; i < line.length(); i++) {
                    int index = Search.getIndexCharFromAlphabet(line.charAt(i));
                    if (map.get(Search.getNewCharByIndexFromAlphabet(index)) == null) {
                        map.put(Search.getNewCharByIndexFromAlphabet(index), 1);
                    } else {
                        Integer count = map.get(Search.getNewCharByIndexFromAlphabet(index));
                        map.put(Search.getNewCharByIndexFromAlphabet(index), count + 1);
                    }
                }
                line = reader.readLine();
            }

            //Находим самый часто повторяющейся в тексте символ
            char maxChar = Collections.max(map.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
            int spaceIndex = Search.getIndexCharFromAlphabet(' ');
            int key = -1 * (Search.getIndexCharFromAlphabet(maxChar) - spaceIndex);

            try {
                FileWriter inputFile = new FileWriter(dest, false);
                outputFile = new FileReader(src);
                reader = new BufferedReader(outputFile);
                line = reader.readLine();
                while (line != null) {
                    StringBuilder inputString = new StringBuilder();

                    //Считывем строку по одному char, находим его индекс в ALPHABET
                    for (int i = 0; i < line.length(); i++) {
                        int index = Search.getIndexCharFromAlphabet(line.charAt(i));
                        //Если char был найден в ALPHABET смещаем значение char на ключ и записываем в inputString
                        if (index != -1) {
                            index = index + key;
                            if (index % Data.ALPHABET.length < 0) {
                                while (index < 0) {
                                    index = Data.ALPHABET.length + index;
                                }
                            }
                            inputString.append(Search.getNewCharByIndexFromAlphabet(index));
                        } else {
                            throw new Exception("Совершена попытка взлома! Операция будет остановлена!");
                        }
                    }

                    inputFile.write(inputString + "\n");
                    inputFile.flush();
                    line = reader.readLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Command(name = "decrypt", description = "Decrypt from file to file using statistical analysis") // |3|
    void decrypt(
            @Parameters(paramLabel = "<source file>", description = "source file with encrypted text") File src,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") File dest,
            @Parameters(paramLabel = "<key>", description = "key for encryption") int key) {
        key = -1 * key;
        try {
            FileReader outputFile = new FileReader(src);
            FileWriter inputFile = new FileWriter(dest, false);
            BufferedReader reader = new BufferedReader(outputFile);

            String line = reader.readLine();
            while (line != null) {
                StringBuilder inputString = new StringBuilder();

                //Считывем строку по одному char, находим его индекс в ALPHABET
                for (int i = 0; i < line.length(); i++) {
                    int index = Search.getIndexCharFromAlphabet(line.charAt(i));
                    //Если char был найден в ALPHABET смещаем значение char на ключ и записываем в inputString
                    if (index != -1) {
                        index = index+key;
                        if (index % Data.ALPHABET.length < 0) {
                            while (index < 0) {
                                index = Data.ALPHABET.length + index;
                            }
                        }
                        inputString.append(Search.getNewCharByIndexFromAlphabet(index));
                    } else {
                        throw new Exception("Совершена попытка взлома! Операция будет остановлена!");
                    }
                }

                inputFile.write(inputString + "\n");
                inputFile.flush();
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Cypher()).execute(args);
        System.exit(exitCode);
    }
}