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

        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.err.println("Logs from your program will appear here!");

        if (matchPattern(inputLine, pattern)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    public static boolean matchPattern(String inputLine, String pattern) {
        if (pattern.length() == 1) {
            return inputLine.contains(pattern);
        } else if (pattern.equals("\\d")) {
            return matchDigit(inputLine);
        } else if (pattern.equals("\\w")) {
            return matchWordCharacters(inputLine);
        } else if (pattern.startsWith("[^") && pattern.endsWith("]")) {
            return negativeCharacterGroups(inputLine, pattern);
        } else if (pattern.startsWith("[") && pattern.endsWith("]")) {
            return positiveCharacterGroups(inputLine, pattern);
        } else {
            throw new RuntimeException("Unhandled pattern: " + pattern);
        }
    }

    private static Boolean matchDigit(String input) {
        Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        boolean result = false;

        for (int number : numbers) {
            if (input.contains(String.valueOf(number))) {
                result = true;
                break;
            }
        }

        return result;
    }

    private static Boolean matchWordCharacters(String input) {
        boolean result = false;
        for (int i = 0; i < input.length(); i++) {
            char temp = input.charAt(i);
            result = Character.isLetter(temp) || Character.isDigit(temp) || temp == '_';
            if (result) {
                break;
            }
        }

        return result;
    }

    private static Boolean positiveCharacterGroups(String input, String pattern) {
        String updatedPattern = pattern.substring(1, pattern.length() - 1);
        char[] patterns = updatedPattern.toCharArray();
        boolean result = false;
        for (char c : patterns) {
            if (input.contains(String.valueOf(c))) {
                result = true;
                break;
            }
        }

        return result;
    }

    private static Boolean negativeCharacterGroups(String input, String pattern) {
        String updatedPattern = pattern.substring(2, pattern.length() - 1);
        char[] patterns = updatedPattern.toCharArray();
        boolean result = false;
        for (char c : patterns) {
            if (!input.contains(String.valueOf(c))) {
                result = true;
                break;
            }
        }

        return result;
    }
}
