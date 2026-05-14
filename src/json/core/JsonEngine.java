package json.core;
import json.model.*;
import json.parser.JsonParser;
import java.nio.file.*;

public class JsonEngine {
    private JsonValue root;

    public void open(String path) {
        try {
            String content = Files.readString(Path.of(path));
            this.root = JsonParser.parse(content);
            System.out.println("File opened successfully.");
        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void print() {
        if (root != null) System.out.println(root.toString(0));
        else System.out.println("No file loaded.");
    }
}