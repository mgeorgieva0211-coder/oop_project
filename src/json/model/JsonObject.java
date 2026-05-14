package json.model;
import java.util.*;

public class JsonObject extends JsonValue {
    private Map<String, JsonValue> properties = new LinkedHashMap<>();

    public void put(String key, JsonValue value) { properties.put(key, value); }
    public JsonValue get(String key) { return properties.get(key); }

    @Override
    public String toString(int indent) {
        StringBuilder sb = new StringBuilder("{\n");
        String spacing = "  ".repeat(indent + 1);
        for (var entry : properties.entrySet()) {
            sb.append(spacing).append("\"").append(entry.getKey()).append("\": ")
                    .append(entry.getValue().toString(indent + 1)).append(",\n");
        }
        sb.append("  ".repeat(indent)).append("}");
        return sb.toString();
    }
}