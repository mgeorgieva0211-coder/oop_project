package json.model;
import java.util.*;

public class JsonArray extends JsonValue {
    private List<JsonValue> elements = new ArrayList<>();
    public void add(JsonValue value) { elements.add(value); }

    @Override
    public String toString(int indent) {
        return "[]"; // Опростено за начало
    }
}