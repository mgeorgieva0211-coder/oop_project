package json.core;

import json.model.*;
import json.parser.JsonParser;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
/**
 * Основно ядро (изпълнителен софтуерен модул), което съдържа състоянието на сесията
 * и управлява логиката зад всички конзолни команди.
 */
public class JsonEngine {
    /**
     * Конструктор по подразбиране за инициализиране на изпълнителното ядро.
     */
    public JsonEngine() {
        super();
    }
    private JsonValue root;
    private String currentFilePath;
    /**
     * Отваря текстов файл от диска и зарежда съдържанието му в паметта.
     * @param path абсолютен или относителен път до файла
     */
    public void open(String path) {
        try {
            String content = Files.readString(Path.of(path));
            this.root = JsonParser.parse(content);
            this.currentFilePath = path;
            System.out.println("Successfully opened " + path);
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    /**
     * Затваря текущо отворения файл и напълно нулира оперативната памет.
     */
    public void close() {
        if (currentFilePath == null) {
            System.out.println("No file is currently open.");
            return;
        }
        root = null;
        System.out.println("Successfully closed " + currentFilePath + " (Changes NOT saved).");
        currentFilePath = null;
    }
    /**
     * Записва текущите промени в JSON структурата обратно в заредилия я файл.
     */
    public void save() {
        if (currentFilePath == null || root == null) {
            System.out.println("No file is currently open to save.");
            return;
        }
        try {
            Files.writeString(Path.of(currentFilePath), root.toString(0));
            System.out.println("Successfully saved changes to " + currentFilePath);
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    /**
     * Експортира текущата JSON структура от паметта в нов файл на диска.
     * @param path път и име на новия дестинационен файл
     */
    public void saveAs(String path) {
        if (root == null) {
            System.out.println("No data loaded to save.");
            return;
        }
        try {
            Files.writeString(Path.of(path), root.toString(0));
            System.out.println("Successfully saved as " + path);
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    /**
     * Извършва валидация на синтаксиса на заредения JSON документ съгласно RFC 8259.
     */
    public void validate() {
        if (currentFilePath == null) {
            System.out.println("No file loaded to validate.");
            return;
        }
        try {
            String content = Files.readString(Path.of(currentFilePath));
            JsonParser.parse(content);
            System.out.println("The file is a valid JSON.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + ". The file is NOT a valid JSON.");
        }
    }
    /**
     * Отпечатва цялата JSON структура в конзолата в четим и подреден вид.
     */
    public void print() {
        if (root != null) System.out.println(root.toString(0));
        else System.out.println("No file loaded.");
    }
    /**
     * Изпълнява дълбоко търсене в документа за откриване на стойности по даден ключ.
     * @param key ключът, чиито съвпадения се търсят
     */
    public void search(String key) {
        if (root == null) {
            System.out.println("No file loaded.");
            return;
        }
        List<String> results = new ArrayList<>();
        root.searchKey(key, "", results);
        if (results.isEmpty()) {
            System.out.println("No matches found for key '" + key + "'.");
        } else {
            System.out.println("Found " + results.size() + " matches:");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }
    /**
     * Модифицира стойността на съществуващ елемент по зададен вложен път.
     * @param path низов йерархичен път до целевия елемент
     * @param jsonString новата стойност, подадена като JSON подниз
     */
    public void set(String path, String jsonString) {
        if (root == null) {
            System.out.println("No file loaded.");
            return;
        }
        try {
            JsonValue newValue = JsonParser.parse(jsonString);
            List<String> tokens = parsePath(path);
            if (tokens.isEmpty()) {
                root = newValue;
                System.out.println("Successfully updated root.");
                return;
            }
            boolean success = root.setByPath(tokens, 0, newValue);
            if (success) System.out.println("Value updated in memory.");
            else System.out.println("Error: Element '" + path + "' does not exist.");
        } catch (Exception e) {
            System.out.println("Error parsing value: " + e.getMessage());
        }
    }
    /**
     * Създава нов елемент на указания път и му вгражда генерирания обект.
     * @param path низов йерархичен път за създаване
     * @param jsonString стойността на новия елемент като JSON подниз
     */
    public void create(String path, String jsonString) {
        if (root == null) {
            System.out.println("No file loaded.");
            return;
        }
        try {
            JsonValue newValue = JsonParser.parse(jsonString);
            List<String> tokens = parsePath(path);
            if (tokens.isEmpty()) return;
            if (root.getByPath(tokens, 0) != null) {
                System.out.println("Error: Element '" + path + "' already exists. Use 'set' to modify it.");
                return;
            }
            boolean success = root.createByPath(tokens, 0, newValue);
            if (success) System.out.println("Successfully created new path '" + path + "' with value.");
            else System.out.println("Error creating path.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    /**
     * Изтрива вложен компонент от JSON дървото по зададен йерархичен път.
     * @param path път до елемента, който трябва да се премахне
     */
    public void delete(String path) {
        if (root == null) {
            System.out.println("No file loaded.");
            return;
        }
        List<String> tokens = parsePath(path);
        if (tokens.isEmpty()) {
            root = null;
            System.out.println("Successfully deleted root.");
            return;
        }
        boolean success = root.deleteByPath(tokens, 0);
        if (success) System.out.println("Successfully deleted element at '" + path + "'.");
        else System.out.println("Error: Incorrect path '" + path + "'.");
    }
    /**
     * Премества транзакционно цяла подструктура от една локация на друга.
     * @param fromPath изходен структурен път на обекта
     * @param toPath нов целеви структурен път за вграждане
     */
    public void move(String fromPath, String toPath) {
        if (root == null) {
            System.out.println("No file loaded.");
            return;
        }
        List<String> fromTokens = parsePath(fromPath);
        List<String> toTokens = parsePath(toPath);

        JsonValue elementToMove = root.getByPath(fromTokens, 0);
        if (elementToMove == null) {
            System.out.println("Error: Source path '" + fromPath + "' not found.");
            return;
        }
        if (root.getByPath(toTokens, 0) != null) {
            System.out.println("Error: Destination path '" + toPath + "' already exists.");
            return;
        }

        boolean createSuccess = root.createByPath(toTokens, 0, elementToMove);
        if (!createSuccess) {
            System.out.println("Error: Failed to move.");
            return;
        }
        root.deleteByPath(fromTokens, 0);
        System.out.println("Successfully moved elements from '" + fromPath + "' to '" + toPath + "'.");
        System.out.println("Original path '" + fromPath + "' was deleted.");
    }
    /**
     * Показва на екрана структуриран помощен списък със синтаксиса на командите.
     */
    public void help() {
        System.out.println("Available commands:");
        System.out.println("  open <file>            - Отваря JSON файл");
        System.out.println("  close                  - Затваря текущия файл без запис");
        System.out.println("  save                   - Записва промените обратно във файла");
        System.out.println("  save as <file>         - Записва съдържанието в нов файл");
        System.out.println("  validate               - Валидира JSON синтаксиса на файла");
        System.out.println("  print                  - Показва форматирания JSON");
        System.out.println("  search <key>           - Търси стойности по даден ключ");
        System.out.println("  set <path> <string>    - Променя елемент по път");
        System.out.println("  create <path> <string> - Създава нов елемент по път");
        System.out.println("  delete <path>          - Изтрива елемент по път");
        System.out.println("  move <from> <to>       - Премества елемент от един път на друг");
        System.out.println("  help                   - Информация за команди");
        System.out.println("  exit                   - Изход от конзолата");
    }

    private List<String> parsePath(String path) {
        List<String> tokens = new ArrayList<>();
        String[] parts = path.split("\\.");
        for (String part : parts) {
            if (part.contains("[")) {
                int openBracket = part.indexOf('[');
                String key = part.substring(0, openBracket);
                if (!key.isEmpty()) tokens.add(key);
                String sub = part.substring(openBracket);
                Matcher m = Pattern.compile("\\[(\\d+)\\]").matcher(sub);
                while (m.find()) {
                    tokens.add("[" + m.group(1) + "]");
                }
            } else {
                tokens.add(part);
            }
        }
        return tokens;
    }
}