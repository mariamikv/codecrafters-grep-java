import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-E")) {
            System.out.println("Usage: ./your_program.sh -E <pattern>");
            System.exit(1);
        }

        String pattern = args[1];
        Scanner scanner = new Scanner(System.in);
        String inputLine = scanner.nextLine();

        System.err.println("Logs from your program will appear here!");

        if (matchPattern(inputLine, pattern)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    public static boolean matchPattern(String inputLine, String pattern) {
        for (int i = 0; i <= inputLine.length(); i++) {
            if (matchFromPosition(inputLine, i, pattern, 0)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchFromPosition(String input, int inputPos, String pattern, int patternPos) {
        if (patternPos >= pattern.length()) {
            return true;
        }

        if (inputPos >= input.length()) {
            return false;
        }

        char currentChar = input.charAt(inputPos);

        if (pattern.charAt(patternPos) == '\\' && patternPos + 1 < pattern.length()) {
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

        if (pattern.charAt(patternPos) == '[') {
            int closingBracket = pattern.indexOf(']', patternPos);
            if (closingBracket != -1) {
                String charClass = pattern.substring(patternPos, closingBracket + 1);
                if (matchCharacterClass(currentChar, charClass)) {
                    return matchFromPosition(input, inputPos + 1, pattern, closingBracket + 1);
                }
                return false;
            }
        }

        if (pattern.charAt(patternPos) == currentChar) {
            return matchFromPosition(input, inputPos + 1, pattern, patternPos + 1);
        }

        return false;
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