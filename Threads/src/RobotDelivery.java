import java.util.*;

public class RobotDelivery {
    // Статическая мапа для хранения частот
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int rCount = countR(route);
                System.out.println(Thread.currentThread().getName() +
                        ": Маршрут содержит " + rCount + " команд 'R'");

                synchronized (sizeToFreq) {
                    sizeToFreq.put(rCount, sizeToFreq.getOrDefault(rCount, 0) + 1);
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static int countR(String route) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == 'R') {
                count++;
            }
        }
        return count;
    }

    private static void printResults() {
        int maxFreq = 0;
        int mostCommonSize = 0;

        synchronized (sizeToFreq) {
            for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                if (entry.getValue() > maxFreq) {
                    maxFreq = entry.getValue();
                    mostCommonSize = entry.getKey();
                }
            }

            System.out.println("\n=== РЕЗУЛЬТАТЫ АНАЛИЗА ===");
            System.out.println("Самое частое количество повторений " +
                    mostCommonSize + " (встретилось " + maxFreq + " раз)");

            System.out.println("\nДругие размеры:");

            List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(sizeToFreq.entrySet());
            sortedEntries.sort((a, b) -> b.getValue() - a.getValue());

            for (Map.Entry<Integer, Integer> entry : sortedEntries) {
                if (entry.getKey() != mostCommonSize) {
                    System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
                }
            }

            System.out.println("\n=== ДОПОЛНИТЕЛЬНАЯ СТАТИСТИКА ===");
            System.out.println("Всего уникальных значений: " + sizeToFreq.size());

            int totalRoutes = 0;
            for (int count : sizeToFreq.values()) {
                totalRoutes += count;
            }
            System.out.println("Всего обработано маршрутов: " + totalRoutes);
        }
    }
}