package angga7togk.badword;

import angga7togk.badword.utils.Color;
import angga7togk.badword.utils.ListBadWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BadWord extends ListBadWord {
    public static void main(String[] args)
    {
        System.out.println(Color.YELLOW + """
                
                 ______  .___________.  ______     _______  __  ___    .______        ___       _______  ____    __    ____  ______   .______       _______ \s
                |____  | |           | /  __  \\   /  _____||  |/  /    |   _  \\      /   \\     |       \\ \\   \\  /  \\  /   / /  __  \\  |   _  \\     |       \\\s
                    / /  `---|  |----`|  |  |  | |  |  __  |  '  /     |  |_)  |    /  ^  \\    |  .--.  | \\   \\/    \\/   / |  |  |  | |  |_)  |    |  .--.  |
                   / /       |  |     |  |  |  | |  | |_ | |    <      |   _  <    /  /_\\  \\   |  |  |  |  \\            /  |  |  |  | |      /     |  |  |  |
                  / /        |  |     |  `--'  | |  |__| | |  .  \\     |  |_)  |  /  _____  \\  |  '--'  |   \\    /\\    /   |  `--'  | |  |\\  \\----.|  '--'  |
                 /_/         |__|      \\______/   \\______| |__|\\__\\    |______/  /__/     \\__\\ |_______/     \\__/  \\__/     \\______/  | _| `._____||_______/\s
                                                                                                                                                            \s
                """ + Color.RESET);
        Scanner in = new Scanner(System.in);

        while (true){
            System.out.print(Color.GREEN + "Input Word " + Color.YELLOW + "(input '/exit' to exit the terminal): " + Color.RESET);
            String input = in.nextLine();

            if (input.equalsIgnoreCase("/exit")){
                break;
            }

            System.out.println(Color.BLUE + masking(input, "*", List.of("yatim")));
        }
        in.close();
    }

    /**
     * Menggunakan jarak Levenshtein untuk menghitung kemiripan kata
     *
     * @param wordCollect List<String>
     * @param word        String
     * @return boolean
     */
    private static boolean levenshtein(List<String> wordCollect, String word) {
        for (String bad : wordCollect) {
            if (calculateLevenshteinDistance(word, bad) <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Menghitung jarak Levenshtein antara dua string.
     *
     * @param s1 String pertama
     * @param s2 String kedua
     * @return Jarak Levenshtein antara s1 dan s2
     */
    private static int calculateLevenshteinDistance(String s1, String s2) {
        int[][] distance = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            distance[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + cost);
            }
        }

        return distance[s1.length()][s2.length()];
    }

    public static boolean check(String kata){
        kata = kata.toLowerCase();
        for (String word : kata()){
            for (Map.Entry<String, String> entry : numToChar().entrySet()){
                kata = kata.replace(entry.getKey(), entry.getValue());
            }
            if (kata.contains(word.toLowerCase())){
                return true;
            }
            if (levenshtein(kata(), word.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * Mengganti huruf vokal dalam kata atau kalimat dengan karakter masking
     *
     * @param kata        String
     * @param masking     String
     * @param customWords List<String>
     * @return String
     */
    public static String masking(String kata, String masking, List<String> customWords) {
        String[] words = kata.split(" ");
        List<String> badWords = kata();
        List<String> newWords = new ArrayList<>();

        for (String word : words) {
            String tmpWord = word;
            word = word.replaceAll("(.)\\1+", "$1");
            word = caseFolding(word);

            for (Map.Entry<String, String> entry : numToChar().entrySet()) {
                word = word.replace(entry.getKey(), entry.getValue());
            }

            if (badWords.contains(word.toLowerCase()) || levenshtein(badWords, word)) {
                String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                if (!replaceString.contains(masking)) {
                    newWords.add(word.substring(0, word.length() - 1) + masking);
                } else {
                    newWords.add(replaceString);
                }
            } else {
                if (customWords.size() > 0 && (customWords.contains(word.toLowerCase()) || levenshtein(customWords, word))) {
                    String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                    if (!replaceString.contains(masking)) {
                        newWords.add(word.substring(0, word.length() - 1) + masking);
                    } else {
                        newWords.add(replaceString);
                    }
                } else {
                    newWords.add(tmpWord);
                }
            }
        }
        return String.join(" ", newWords);
    }

    /**
     * Mengganti huruf vokal dalam kata atau kalimat dengan karakter masking
     *
     * @param kata        String
     * @param masking     String
     * @return String
     */
    public static String masking(String kata, String masking) {
        List<String> customWords = new ArrayList<>();
        String[] words = kata.split(" ");
        List<String> badWords = kata();
        List<String> newWords = new ArrayList<>();

        for (String word : words) {
            String tmpWord = word;
            word = word.replaceAll("(.)\\1+", "$1");
            word = caseFolding(word);

            for (Map.Entry<String, String> entry : numToChar().entrySet()) {
                word = word.replace(entry.getKey(), entry.getValue());
            }

            if (badWords.contains(word.toLowerCase()) || levenshtein(badWords, word)) {
                String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                if (!replaceString.contains(masking)) {
                    newWords.add(word.substring(0, word.length() - 1) + masking);
                } else {
                    newWords.add(replaceString);
                }
            } else {
                if (customWords.size() > 0 && (customWords.contains(word.toLowerCase()) || levenshtein(customWords, word))) {
                    String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                    if (!replaceString.contains(masking)) {
                        newWords.add(word.substring(0, word.length() - 1) + masking);
                    } else {
                        newWords.add(replaceString);
                    }
                } else {
                    newWords.add(tmpWord);
                }
            }
        }
        return String.join(" ", newWords);
    }

    /**
     * Mengganti huruf vokal dalam kata atau kalimat dengan karakter masking
     *
     * @param kata        String
     * @return String
     */
    public static String masking(String kata) {
        String masking = "*";
        List<String> customWords = new ArrayList<>();
        String[] words = kata.split(" ");
        List<String> badWords = kata();
        List<String> newWords = new ArrayList<>();

        for (String word : words) {
            String tmpWord = word;
            word = word.replaceAll("(.)\\1+", "$1");
            word = caseFolding(word);

            for (Map.Entry<String, String> entry : numToChar().entrySet()) {
                word = word.replace(entry.getKey(), entry.getValue());
            }

            if (badWords.contains(word.toLowerCase()) || levenshtein(badWords, word)) {
                String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                if (!replaceString.contains(masking)) {
                    newWords.add(word.substring(0, word.length() - 1) + masking);
                } else {
                    newWords.add(replaceString);
                }
            } else {
                if (customWords.size() > 0 && (customWords.contains(word.toLowerCase()) || levenshtein(customWords, word))) {
                    String replaceString = tmpWord.replaceAll("[aiueoAIUEO]", masking);

                    if (!replaceString.contains(masking)) {
                        newWords.add(word.substring(0, word.length() - 1) + masking);
                    } else {
                        newWords.add(replaceString);
                    }
                } else {
                    newWords.add(tmpWord);
                }
            }
        }
        return String.join(" ", newWords);
    }

    private static String caseFolding(String word) {
        return word.toLowerCase();
    }
}