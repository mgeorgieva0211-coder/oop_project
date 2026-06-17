package json.model;
import java.util.*;
/**
 * Представлява подреден списък (масив) от JSON стойности, обвити в квадратни скоби.
 */
public class JsonArray extends JsonValue {
    /**
     * Конструктор по подразбиране за създаване на празен масив.
     */
    public JsonArray() {
        super();
    }
    private List<JsonValue> elements = new ArrayList<>();
    /**
     * Добавя нов JSON елемент към списъка с елементи на масива.
     * @param value стойността, която се добавя в масива
     */
    public void add(JsonValue value) { elements.add(value); }
    /**
     * Извлича списъка от всички вътрешни елементи, съдържащи се в масива.
     * @return списък (List) от JsonValue компоненти
     */
    public List<JsonValue> getElements() { return elements; }

    @Override
    public String toString(int indent) {
        if (elements.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[\n");
        String spacing = "  ".repeat(indent + 1);
        for (int i = 0; i < elements.size(); i++) {
            sb.append(spacing).append(elements.get(i).toString(indent + 1));
            if (i < elements.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ".repeat(indent)).append("]");
        return sb.toString();
    }

    @Override
    public JsonValue getByPath(List<String> tokens, int index) {
        if (index >= tokens.size()) return this;
        String token = tokens.get(index);
        if (token.startsWith("[") && token.endsWith("]")) {
            int idx = Integer.parseInt(token.substring(1, token.length() - 1));
            if (idx >= 0 && idx < elements.size()) {
                return elements.get(idx).getByPath(tokens, index + 1);
            }
        }
        return null;
    }

    @Override
    public boolean setByPath(List<String> tokens, int index, JsonValue newValue) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (token.startsWith("[") && token.endsWith("]")) {
            int idx = Integer.parseInt(token.substring(1, token.length() - 1));
            if (idx >= 0 && idx < elements.size()) {
                if (index == tokens.size() - 1) {
                    elements.set(idx, newValue);
                    return true;
                } else {
                    return elements.get(idx).setByPath(tokens, index + 1, newValue);
                }
            }
        }
        return false;
    }

    @Override
    public boolean createByPath(List<String> tokens, int index, JsonValue newValue) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (token.startsWith("[") && token.endsWith("]")) {
            int idx = Integer.parseInt(token.substring(1, token.length() - 1));
            if (idx == elements.size() && index == tokens.size() - 1) {
                elements.add(newValue);
                return true;
            }
            if (idx >= 0 && idx < elements.size()) {
                return elements.get(idx).createByPath(tokens, index + 1, newValue);
            }
        }
        return false;
    }

    @Override
    public boolean deleteByPath(List<String> tokens, int index) {
        if (index >= tokens.size()) return false;
        String token = tokens.get(index);
        if (token.startsWith("[") && token.endsWith("]")) {
            int idx = Integer.parseInt(token.substring(1, token.length() - 1));
            if (idx >= 0 && idx < elements.size()) {
                if (index == tokens.size() - 1) {
                    elements.remove(idx);
                    return true;
                } else {
                    return elements.get(idx).deleteByPath(tokens, index + 1);
                }
            }
        }
        return false;
    }

    @Override
    public void searchKey(String key, String currentPath, List<String> results) {
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).searchKey(key, currentPath + "[" + i + "]", results);
        }
    }
}