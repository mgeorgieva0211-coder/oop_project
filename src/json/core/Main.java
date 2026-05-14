package json.core;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JsonEngine engine = new JsonEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- JSON Parser Console ---");
        System.out.println("Команди: open <file>, print, exit");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            if (command.equals("exit")) break;

            if (command.equals("open") && parts.length > 1) {
                engine.open(parts[1]);
            } else if (command.equals("print")) {
                engine.print();
            } else {
                System.out.println("Неизвестна команда или липсващ път.");
            }
        }
    }
}