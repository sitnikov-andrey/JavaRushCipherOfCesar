import java.io.File;

public class Main {

    public static void main(String[] args) {
        //Тут надо инициализировать объекты класса Cypher
        Cypher cypher = new Cypher();

        cypher.encrypt(
                new File("src/main/resources/encrypt/inputFile.txt"),
                new File("src/main/resources/encrypt/outputFile.txt"),
                1
        );

        /*cypher.decrypt(
                new File("src/main/resources/decrypt/inputFile.txt"),
                new File("src/main/resources/decrypt/outputFile.txt"),
                1
        );

        cypher.bruteForce(
                new File("src/main/resources/bruteForce/inputFile.txt"),
                new File("src/main/resources/bruteForce/representativeFile.txt"),
                new File("src/main/resources/bruteForce/outputFile.txt")
        );*/


        cypher.statisticalDecrypt(
                new File("src/main/resources/statisticalDecrypt/inputFile.txt"),
                new File("src/main/resources/statisticalDecrypt/outputFile.txt")
        );

    }
}
