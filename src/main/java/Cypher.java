import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;

import java.io.*;

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
                        inputString.append(Search.getNewCharByIndexFromAlphabet(index + key));
                    }
                }

                inputFile.write(inputString + "\n");
                inputFile.flush();
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "brute force", description = "Decrypt from file to file using brute force") // |3|
    void bruteForce(
            @Parameters(paramLabel = "<source file>", description = "source file with encrypted text") File src,
            @Option(names = {"-r", "--representative"}, description = "file with unencrypted representative text") File representativeFile,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") File dest) {
        // TODO
    }

    @Command(name = "statistical decryption", description = "Decrypt from file to file using statistical analysis") // |3|
    void statisticalDecrypt(
            @Parameters(paramLabel = "<source file>", description = "source file with encrypted text") File src,
            @Option(names = {"-r", "--representative"}, description = "file with unencrypted representative text") File representativeFile,
            @Parameters(paramLabel = "<dest file>", description = "dest file which should have decrypted text") File dest) {
        // TODO
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
                        inputString.append(Search.getNewCharByIndexFromAlphabet(index + key));
                    } else {
                        throw new Exception("Совершена попытка взлома! Операция будет остановлена!");
                    }
                }

                inputFile.write(inputString + "\n");
                inputFile.flush();
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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