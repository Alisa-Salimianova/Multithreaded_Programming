import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGenerator {
    // Генератор случайного текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Счётчики для красивых слов разной длины
    private static final AtomicInteger beautifulWords3 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWords4 = new AtomicInteger(0);
    private static final AtomicInteger beautifulWords5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // Генерация 100 000 текстов
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Создание потоков для проверки каждого критерия
        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread sameLetterThread = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetter(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        Thread ascendingOrderThread = new Thread(() -> {
            for (String text : texts) {
                if (isAscendingOrder(text)) {
                    incrementCounter(text.length());
                }
            }
        });

        // Запуск потоков
        palindromeThread.start();
        sameLetterThread.start();
        ascendingOrderThread.start();

        // Ожидание завершения всех потоков
        palindromeThread.join();
        sameLetterThread.join();
        ascendingOrderThread.join();

        // Вывод результатов
        System.out.println("Красивых слов с длиной 3: " + beautifulWords3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + beautifulWords4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + beautifulWords5.get() + " шт");
    }

    // Метод для проверки, является ли строка палиндромом
    private static boolean isPalindrome(String text) {
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // Метод для проверки, состоит ли строка из одной и той же буквы
    private static boolean isSameLetter(String text) {
        if (text.isEmpty()) {
            return false;
        }
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    // Метод для проверки, идут ли буквы в строке по возрастанию
    private static boolean isAscendingOrder(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    // Метод для увеличения счётчика в зависимости от длины слова
    private static void incrementCounter(int length) {
        switch (length) {
            case 3:
                beautifulWords3.incrementAndGet();
                break;
            case 4:
                beautifulWords4.incrementAndGet();
                break;
            case 5:
                beautifulWords5.incrementAndGet();
                break;
            default:
                // Другие длины не обрабатываем
                break;
        }
    }
}