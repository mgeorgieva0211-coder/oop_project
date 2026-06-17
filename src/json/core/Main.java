package json.core;

import java.util.Scanner;
/**
 * Главен стартов клас, съдържащ входната точка на конзолното Java приложение.
 */
public class Main {
    /**
     * Конструктор по подразбиране за главния стартиращ клас.
     */
    public Main() {
        super();
    }
    /**
     * Главен изпълнителен метод (входна точка), стартиращ безкрайния цикъл за четене на команди.
     * @param args масив от входни аргументи от командния ред (не се ползват)
     */
    public static void main(String[] args) {
        JsonEngine engine = new JsonEngine();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- JSON Parser Console ---");
        engine.help();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            if (command.equals("exit")) {
                System.out.println("Exiting program...");
                break;
            }

            switch (command) {
                case "open":
                    if (parts.length > 1) engine.open(parts[1].trim());
                    else System.out.println("Error: Missing file path.");
                    break;
                case "close":
                    engine.close();
                    break;
                case "save":
                    engine.save();
                    break;
                case "validate":
                    engine.validate();
                    break;
                case "print":
                    engine.print();
                    break;
                case "help":
                    engine.help();
                    break;
                case "search":
                    if (parts.length > 1) engine.search(parts[1].trim().replace("\"", ""));
                    else System.out.println("Error: Missing search key.");
                    break;
                case "set":
                    if (parts.length > 1) {
                        String[] cmdArgs = parts[1].trim().split(" ", 2);
                        if (cmdArgs.length == 2) {
                            engine.set(cmdArgs[0].trim().replace("\"", ""), cmdArgs[1].trim());
                        } else System.out.println("Error: Usage: set <path> <string>");
                    } else System.out.println("Error: Usage: set <path> <string>");
                    break;
                case "create":
                    if (parts.length > 1) {
                        String[] cmdArgs = parts[1].trim().split(" ", 2);
                        if (cmdArgs.length == 2) {
                            engine.create(cmdArgs[0].trim().replace("\"", ""), cmdArgs[1].trim());
                        } else System.out.println("Error: Usage: create <path> <string>");
                    } else System.out.println("Error: Usage: create <path> <string>");
                    break;
                case "delete":
                    if (parts.length > 1) engine.delete(parts[1].trim().replace("\"", ""));
                    else System.out.println("Error: Usage: delete <path>");
                    break;
                case "move":
                    if (parts.length > 1) {
                        String[] cmdArgs = parts[1].trim().split(" ", 2);
                        if (cmdArgs.length == 2) {
                            engine.move(cmdArgs[0].trim().replace("\"", ""), cmdArgs[1].trim().replace("\"", ""));
                        } else System.out.println("Error: Usage: move <from> <to>");
                    } else System.out.println("Error: Usage: move <from> <to>");
                    break;
                default:
                    if (input.toLowerCase().startsWith("save as ")) {
                        engine.saveAs(input.substring(8).trim());
                    } else {
                        System.out.println("Неизвестна команда. Въведете 'help' за помощ.");
                    }
                    break;
            }
        }
        scanner.close();
    }
}