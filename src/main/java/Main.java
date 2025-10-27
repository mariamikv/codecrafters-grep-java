import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        if (args.length != 2 || !args[0].equals("-E")) {
//            System.out.println("Usage: ./your_program.sh -E <pattern>");
//            System.exit(1);
//        }

        String pattern = "\\d+";
        Scanner scanner = new Scanner(System.in);
        String inputLine = "123";

        System.err.println("Logs from your program will appear here!");

        if (matchPattern(inputLine, pattern)) {
            System.out.println(0);
        } else {
            System.out.println(1);
        }
    }

    public static boolean matchPattern(String inputLine, String pattern) {
        if (pattern.contains("+")) {
            if (pattern.contains("d+")) {
                for (int i = 0; i <= inputLine.length(); i++) {
                    if (matchFromPosition(inputLine, i, pattern.replace("+", ""), 0)) {
                        return true;
                    }
                }
            }

            int patternIndex = pattern.indexOf("+");
            char character = pattern.charAt(patternIndex - 1);
            int count = 0;
            if (inputLine.contains(Character.toString(character))) {
                for (int i = 0; i < inputLine.length(); i++) {
                    if (inputLine.charAt(i) == character) {
                        count++;
                    }
                }
            }

            return count >= 1;
        }

        if (pattern.startsWith("^")) {
            return matchFromPosition(inputLine, 0, pattern, 1);
        }

        if (pattern.endsWith("$")) {
            String newPattern = pattern.substring(0, pattern.length() - 1);
            for (int i = 0; i <= inputLine.length(); i++) {
                if (matchFromPosition(inputLine, i, newPattern, 0)
                        && i + newPatternLength(newPattern) == inputLine.length()) {
                    return true;
                }
            }
            return false;
        }

        for (int i = 0; i <= inputLine.length(); i++) {
            if (matchFromPosition(inputLine, i, pattern, 0)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchFromPosition(String input, int inputPos, String pattern, int patternPos) {
        System.out.println(input);
        System.out.println(pattern);
        if (patternPos >= pattern.length()) {
            return true;
        }

        if (inputPos > input.length()) {
            return false;
        }

        if (pattern.charAt(patternPos) == '$') {
            return inputPos == input.length();
        }

        if (inputPos >= input.length()) {
            return false;
        }

        char currentChar = input.charAt(inputPos);
        char currentPattern = pattern.charAt(patternPos);

        if (currentPattern == '\\' && patternPos + 1 < pattern.length()) {
            char escapeChar = pattern.charAt(patternPos + 1);

            if (escapeChar == 'd') {
                if (Character.isDigit(currentChar)) {
                    return matchFromPosition(input, inputPos + 1, pattern, patternPos + 2);
                }
                return false;
            } else if (escapeChar == 'w') {
                if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                    return matchFromPosition(input, inputPos + 1, pattern, patternPos + 2);
                }
                return false;
            }
        }

        if (currentPattern == '[') {
            int closingBracket = pattern.indexOf(']', patternPos);
            if (closingBracket != -1) {
                String charClass = pattern.substring(patternPos, closingBracket + 1);
                if (matchCharacterClass(currentChar, charClass)) {
                    return matchFromPosition(input, inputPos + 1, pattern, closingBracket + 1);
                }
                return false;
            }
        }

        if (currentPattern == currentChar) {
            return matchFromPosition(input, inputPos + 1, pattern, patternPos + 1);
        }

        return false;
    }

    private static int newPatternLength(String pattern) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '\\' && i + 1 < pattern.length()) {
                i++;
            }
            count++;
        }
        return count;
    }

    private static boolean matchCharacterClass(char c, String charClass) {
        if (charClass.startsWith("[^") && charClass.endsWith("]")) {
            String chars = charClass.substring(2, charClass.length() - 1);
            for (int i = 0; i < chars.length(); i++) {
                if (chars.charAt(i) == c) {
                    return false;
                }
            }
            return true;
        }

        if (charClass.startsWith("[") && charClass.endsWith("]")) {
            String chars = charClass.substring(1, charClass.length() - 1);
            for (int i = 0; i < chars.length(); i++) {
                if (chars.charAt(i) == c) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }
}