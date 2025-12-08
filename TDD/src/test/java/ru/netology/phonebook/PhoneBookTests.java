package ru.netology.phonebook;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTests {

    @Test
    void add_returnsCountAndStoresContact() {
        PhoneBook pb = new PhoneBook();
        int c1 = pb.add("Alice", "111");
        assertEquals(1, c1);
        int c2 = pb.add("Bob", "222");
        assertEquals(2, c2);

    }

    @Test
    void add_duplicateNameDoesNotIncreaseSize() {
        PhoneBook pb = new PhoneBook();
        pb.add("Alice", "111");
        int before = pb.add("Alice", "111"); // добавление с тем же именем — не изменит размер
        assertEquals(1, before);
    }

    @Test
    void findByNumber_returnsNameWithoutFullScan() {
        PhoneBook pb = new PhoneBook();
        pb.add("Alice", "111");
        pb.add("Bob", "222");

        assertEquals("Alice", pb.findByNumber("111"));
        assertEquals("Bob", pb.findByNumber("222"));
        assertNull(pb.findByNumber("333"));
    }


    @Test
    void findByName_returnsNumber() {
        PhoneBook pb = new PhoneBook();
        pb.add("Charlie", "333");
        assertEquals("333", pb.findByName("Charlie"));
        assertNull(pb.findByName("Unknown"));
    }

}
