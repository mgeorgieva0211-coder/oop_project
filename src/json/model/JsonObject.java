package json.model;
import java.util.*;
/**
 * Представлява JSON обект, състоящ се от двойки от тип "ключ-стойност", обвити в фигурни скоби.
 */
public class JsonObject extends JsonValue {
    /**
     * Конструктор по подразбиране за създаване на нов обект.
     */
    public JsonObject() {
        super();
    }
    private Map<String, JsonValue> properties = new LinkedHashMap<>();
    /**
     * Добавя или презаписва свойство (двойка ключ-стойност) в обекта.
     * @param key уникалното име на ключа
     * @param value асоциираната с него JSON стойност
     */
    public void put(String key, JsonValue value) { properties.put(key, value); }
    /**
     * Извлича JSON стойността, асоциирана с конкретен ключ.
     * @param key името на търсеното свойство
     * @return намереният JsonValue обект или null, ако ключът липсва
     */
    public JsonValue get(String key) { return properties.get(key); }

    @Override
    public String toString(int indent) {
        if (properties.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{\n");
        String spacing = "  ".repeat(indent + 1);
        int count = 0;
        for (var entry : properties.entrySet()) {
            sb.append(spacing).append("\"").append(entry.getKey()).append("\": ")
                    .append(entry.getValue().toString(indent + 1));
            if (count < properties.size() - 1) sb.append(",");
            sb.append("\n");
            count++;
        }
        sb.append("  ".repeat(indent)).append("}");
        return sb.toString();
    }

    @Override
    public JsonValue getByPath(List<String> tokens, int index) {
        if (index >= tokens.size()) return this;
        String token = tokens.get(index);
        if (properties.containsKey(token)) {
            return properties.get(token).getByPath(tokens, index + 1);
        }
        return null;
    }

    @Override
    public boolean setByPath(List<String> tokens, int index, JsonValue newValue) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (properties.containsKey(token)) {
            if (index == tokens.size() - 1) {
                properties.put(token, newValue);
                return true;
            } else {
                return properties.get(token).setByPath(tokens, index + 1, newValue);
            }
        }
        return false;
    }

    @Override
    public boolean createByPath(List<String> tokens, int index, JsonValue newValue) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (index == tokens.size() - 1) {
            if (properties.containsKey(token)) return false;
            properties.put(token, newValue);
            return true;
        } else {
            if (!properties.containsKey(token)) {
                String nextToken = tokens.get(index + 1);
                if (nextToken.startsWith("[") && nextToken.endsWith("]")) {
                    properties.put(token, new JsonArray());
                } else {
                    properties.put(token, new JsonObject());
                }
            }
            return properties.get(token).createByPath(tokens, index + 1, newValue);
        }
    }

    @Override
    public boolean deleteByPath(List<String> tokens, int index) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (properties.containsKey(token)) {
            if (index == tokens.size() - 1) {
                properties.remove(token);
                return true;
            } else {
                return properties.get(token).deleteByPath(tokens, index + 1);
            }
        }
        return false;
    }

    @Override
    public void searchKey(String key, String currentPath, List<String> results) {
        for (var entry : properties.entrySet()) {
            String pathPart = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
            if (entry.getKey().equals(key)) {
                results.add(entry.getValue().toString(0) + " (at root." + pathPart + ")");
            }
            entry.getValue().searchKey(key, pathPart, results);
        }
    }
}