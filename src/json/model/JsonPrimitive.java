package json.model;
/**
 * Представлява крайните (скаларни) примитивни данни в JSON – низове, числа, булеви стойности и null.
 */
public class JsonPrimitive extends JsonValue {
    private Object value;
    /**
     * Конструктор, създаващ примитивна стойност от подаден Java обект.
     * @param value стойността (String, Double, Boolean или null)
     */
    public JsonPrimitive(Object value) {
        this.value = value;
    }
    /**
     * Връща капсулираната вътрешна Java стойност на примитива.
     * @return обект от тип Object, съдържащ реалната стойност
     */
    public Object getValue() {
        return value;
    }

    @Override
    public String toString(int indent) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + value + "\"";
        return String.valueOf(value);
    }
}