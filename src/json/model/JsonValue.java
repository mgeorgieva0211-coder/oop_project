package json.model;

import java.util.List;
/**
 * Абстрактен базов клас, представляващ всяка възможна стойност в JSON структурата.
 * Служи за суперклас на обекти, масиви и примитиви.
 */
public abstract class JsonValue {
    /**
     * Конструктор по подразбиране за базовия клас на JSON стойностите.
     */
    public JsonValue() {
        super();
    }
    /**
     * Преобразува JSON компонента в текстово представяне с форматиране.
     * @param indent текущото ниво на отстъп (брой интервали)
     * @return добре форматиран низов поток (Pretty Print)
     */
    public abstract String toString(int indent);
    /**
     * Конструктор по подразбиране за класа JsonValue.
     */
    // Празни методи по подразбиране, които се пренаписват в наследниците
    /**
     * Търси и извлича вложен JSON елемент по зададен йерархичен път от токени.
     * @param tokens списък от разбити компоненти (ключове/индекси) на пътя
     * @param index текущ обработван индекс в списъка с токени
     * @return намереният JsonValue обект или null, ако пътят не съществува
     */
    public JsonValue getByPath(List<String> tokens, int index) { return null; }
    /**
     * Променя стойността на съществуващ елемент по зададен йерархичен път.
     * @param tokens списък от компоненти на пътя
     * @param index текущ обработван индекс в токените
     * @param newValue новата JSON стойност за запис
     * @return true при успешна модификация, false в противен случай
     */
    public boolean setByPath(List<String> tokens, int index, JsonValue newValue) { return false; }
    /**
     * Създава изцяло нов елемент на указания йерархичен път.
     * @param tokens списък от компоненти на пътя
     * @param index текущ обработван индекс в токените
     * @param newValue стойността, която да бъде присвоена на новия елемент
     * @return true при успешно създаване, false ако елементът вече съществува
     */
    public boolean createByPath(List<String> tokens, int index, JsonValue newValue) { return false; }
    /**
     * Изтрива елемент от JSON структурата по зададен йерархичен път.
     * @param tokens списък от компоненти на пътя
     * @param index текущ обработван индекс в токените
     * @return true при успешно изтриване, false при некоректен път
     */
    public boolean deleteByPath(List<String> tokens, int index) { return false; }
    /**
     * Рекурсивно обхожда дървото и търси всички съвпадения по зададен ключ.
     * @param key търсеният низ за ключ (property name)
     * @param currentPath изминатият до момента структурен път от корена
     * @param results списък, в който се натрупват текстовите съвпадения и техните пътища
     */
    public void searchKey(String key, String currentPath, List<String> results) {}
}