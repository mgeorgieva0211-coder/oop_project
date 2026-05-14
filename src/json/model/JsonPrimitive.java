package json.model;

public class JsonPrimitive extends JsonValue {
    private Object value;
    public JsonPrimitive(Object value) { this.value = value; }

    @Override
    public String toString(int indent) {
        if (value instanceof String) return "\"" + value + "\"";
        return String.valueOf(value);
    }
}