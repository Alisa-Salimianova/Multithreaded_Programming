package ru.netology.phonebook;

import java.util.*;

public class PhoneBook {
    private final NavigableMap<String, String> nameToNumber = new TreeMap<>();
    private final Map<String, String> numberToName = new HashMap<>();

    public int add(String name, String number) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(number, "number must not be null");
        if (nameToNumber.containsKey(name)) {
            return nameToNumber.size();
        }
        nameToNumber.put(name, number);
        numberToName.put(number, name);
        return nameToNumber.size();
    }

        public String findByNumber(String number) {
                Objects.requireNonNull(number, "number must not be null");
                return numberToName.get(number);
        }

        public String findByName(String name) {
            Objects.requireNonNull(name, "name must not be null");
            return nameToNumber.get(name);
        }
}


