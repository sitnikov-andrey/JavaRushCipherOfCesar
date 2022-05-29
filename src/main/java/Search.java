public class Search {
    private static final char[] alphabet = Data.ALPHABET;

    public static int getIndexCharFromAlphabet(char item) {
        int firstIndex = Data.firstAlphabetIndex;
        int lastIndex = Data.lastAlphabetIndex;
        int position;
        
        position = (firstIndex + lastIndex) / 2;

        while ((alphabet[position] != item) && (firstIndex <= lastIndex)) {
            if (alphabet[position] > item) {
                lastIndex = position - 1;
            } else {
                firstIndex = position + 1;
            }
            position = (firstIndex + lastIndex) / 2;
        }
        
        if (firstIndex <= lastIndex) {
            return position;
        } else {
            return -1;
        }
    }

    public static char getNewCharByIndexFromAlphabet(int index) {
        return alphabet[index % alphabet.length];
    }
}
