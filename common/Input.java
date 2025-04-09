package common;
import java.util.Scanner;

public class Input {
    private static final Scanner scanner = new Scanner(System.in);

    public static int getInt(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextInt();
    }

    public static float getFloat(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextFloat();
    }

    public static String getString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.next();
    }

    public static String getLine(String prompt) {
        System.out.print(prompt + ": ");
        scanner.nextLine(); // Consume newline
        return scanner.nextLine();
    }
}