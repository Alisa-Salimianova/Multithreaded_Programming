import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TextAnalyzer {
    // Генератор случайного текста из условия
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Блокирующие очереди для каждого анализирующего потока
    private static final BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    // Переменные для хранения результатов
    private static volatile String maxAText = "";
    private static volatile int maxACount = -1;

    private static volatile String maxBText = "";
    private static volatile int maxBCount = -1;

    private static volatile String maxCText = "";
    private static volatile int maxCCount = -1;

    public static void main(String[] args) throws InterruptedException {
        // Флаг для остановки генерации
        final int TEXTS_COUNT = 10000;
        final int TEXT_LENGTH = 100000;

        // Поток-генератор текстов
        Thread generatorThread = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < TEXTS_COUNT; i++) {
                String text = generateText("abc", TEXT_LENGTH);
                try {
                    // Помещаем текст во все три очереди
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            // Сигналы о завершении генерации
            try {
                queueA.put("END");
                queueB.put("END");
                queueC.put("END");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Поток для анализа символа 'a'
        Thread analyzerAThread = new Thread(() -> {
            try {
                while (true) {
                    String text = queueA.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countChar(text, 'a');
                    if (count > maxACount) {
                        maxACount = count;
                        maxAText = text;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Поток для анализа символа 'b'
        Thread analyzerBThread = new Thread(() -> {
            try {
                while (true) {
                    String text = queueB.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countChar(text, 'b');
                    if (count > maxBCount) {
                        maxBCount = count;
                        maxBText = text;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Поток для анализа символа 'c'
        Thread analyzerCThread = new Thread(() -> {
            try {
                while (true) {
                    String text = queueC.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countChar(text, 'c');
                    if (count > maxCCount) {
                        maxCCount = count;
                        maxCText = text;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Запуск всех потоков
        long startTime = System.currentTimeMillis();

        generatorThread.start();
        analyzerAThread.start();
        analyzerBThread.start();
        analyzerCThread.start();

        // Ожидание завершения всех потоков
        generatorThread.join();
        analyzerAThread.join();
        analyzerBThread.join();
        analyzerCThread.join();

        long endTime = System.currentTimeMillis();

        // Вывод результатов
        System.out.println("=== Результаты анализа ===");
        System.out.println("Максимальное количество символов 'a': " + maxACount);
        System.out.println("Текст с максимальным количеством 'a' (первые 50 символов): " +
                (maxAText.length() > 50 ? maxAText.substring(0, 50) + "..." : maxAText));

        System.out.println("\nМаксимальное количество символов 'b': " + maxBCount);
        System.out.println("Текст с максимальным количеством 'b' (первые 50 символов): " +
                (maxBText.length() > 50 ? maxBText.substring(0, 50) + "..." : maxBText));

        System.out.println("\nМаксимальное количество символов 'c': " + maxCCount);
        System.out.println("Текст с максимальным количеством 'c' (первые 50 символов): " +
                (maxCText.length() > 50 ? maxCText.substring(0, 50) + "..." : maxCText));

        System.out.println("\nВремя выполнения: " + (endTime - startTime) + " мс");
    }

    // Метод для подсчёта количества символов в строке
    private static int countChar(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
}