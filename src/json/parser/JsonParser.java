package json.parser;

import json.model.*;
import java.util.regex.*;

public class JsonParser {
    public static JsonValue parse(String input) throws Exception {
        input = input.trim();
        if (input.startsWith("{")) return parseObject(input);
        if (input.startsWith("[")) return parseArray(input);
        return parsePrimitive(input);
    }

    private static JsonObject parseObject(String input) {
        JsonObject obj = new JsonObject();
        Matcher m = Pattern.compile("\"(\\w+)\"\\s*:\\s*([^,}]+)").matcher(input);
        while (m.find()) {
            obj.put(m.group(1), parsePrimitive(m.group(2)));
        }
        return obj;
    }

    private static JsonArray parseArray(String input) {
        return new JsonArray();
    }

    private static JsonValue parsePrimitive(String v) {
        v = v.trim().replace("\"", "");
        if (v.equals("true") || v.equals("false")) return new JsonPrimitive(Boolean.parseBoolean(v));
        try { return new JsonPrimitive(Double.parseDouble(v)); }
        catch (Exception e) { return new JsonPrimitive(v); }
    }
}