package json.parser;

import json.model.*;
import java.util.*;
/**
 * Синтактичен анализатор (парсер), отговорен за преобразуването на
 * суров текстов низ в йерархично Java обектно дърво от тип JsonValue.
 */
public class JsonParser {
    private final String input;
    private int pos = 0;

    private JsonParser(String input) {
        this.input = input;
    }
    /**
     * Анализира подадения JSON низ и конструира съответната обектна подструктура.
     * @param input суровият форматиран или неформатиран JSON низ
     * @return коренният обект (JsonValue), изграден след анализа
     * @throws Exception при синтактична некоректност или невалидни символи
     */
    public static JsonValue parse(String input) throws Exception {
        JsonParser parser = new JsonParser(input.trim());
        JsonValue value = parser.parseValue();
        parser.skipWhitespace();
        if (parser.pos < parser.input.length()) {
            throw new Exception("Има непозволени символи в края на JSON структурата на позиция " + parser.pos);
        }
        return value;
    }

    private JsonValue parseValue() throws Exception {
        skipWhitespace();
        if (pos >= input.length()) throw new Exception("Неочакван край на файла");
        char c = input.charAt(pos);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        return parsePrimitiveLiteral();
    }

    private JsonObject parseObject() throws Exception {
        pos++; // пропуска '{'
        JsonObject obj = new JsonObject();
        skipWhitespace();
        if (pos < input.length() && input.charAt(pos) == '}') {
            pos++;
            return obj;
        }
        while (true) {
            skipWhitespace();
            if (pos >= input.length() || input.charAt(pos) != '"') {
                throw new Exception("Очаква се низ за ключ в обекта на позиция " + pos);
            }
            String key = parseStringValue();
            skipWhitespace();
            if (pos >= input.length() || input.charAt(pos) != ':') {
                throw new Exception("Очаква се ':' след ключа на позиция " + pos);
            }
            pos++; // пропуска ':'
            JsonValue val = parseValue();
            obj.put(key, val);
            skipWhitespace();
            if (pos >= input.length()) throw new Exception("Незатворен JSON обект");
            char next = input.charAt(pos);
            if (next == '}') {
                pos++;
                break;
            } else if (next == ',') {
                pos++;
            } else {
                throw new Exception("Очаква се ',' или '}' на позиция " + pos);
            }
        }
        return obj;
    }

    private JsonArray parseArray() throws Exception {
        pos++; // пропуска '['
        JsonArray array = new JsonArray();
        skipWhitespace();
        if (pos < input.length() && input.charAt(pos) == ']') {
            pos++;
            return array;
        }
        while (true) {
            JsonValue val = parseValue();
            array.add(val);
            skipWhitespace();
            if (pos >= input.length()) throw new Exception("Незатворен JSON масив");
            char next = input.charAt(pos);
            if (next == ']') {
                pos++;
                break;
            } else if (next == ',') {
                pos++;
            } else {
                throw new Exception("Очаква се ',' или ']' на позиция " + pos);
            }
        }
        return array;
    }

    private JsonPrimitive parseString() throws Exception {
        return new JsonPrimitive(parseStringValue());
    }

    private String parseStringValue() throws Exception {
        pos++; // пропуска '"'
        StringBuilder sb = new StringBuilder();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '"') {
                pos++;
                return sb.toString();
            }
            sb.append(c);
            pos++;
        }
        throw new Exception("Незатворен стринг литерал");
    }

    private JsonPrimitive parsePrimitiveLiteral() throws Exception {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isWhitespace(c) || c == ',' || c == '}' || c == ']') {
                break;
            }
            sb.append(c);
            pos++;
        }
        String literal = sb.toString().trim();
        if (literal.equals("true")) return new JsonPrimitive(Boolean.TRUE);
        if (literal.equals("false")) return new JsonPrimitive(Boolean.FALSE);
        if (literal.equals("null")) return new JsonPrimitive(null);
        try {
            if (literal.contains(".")) return new JsonPrimitive(Double.parseDouble(literal));
            return new JsonPrimitive(Integer.parseInt(literal));
        } catch (NumberFormatException e) {
            return new JsonPrimitive(literal);
        }
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}