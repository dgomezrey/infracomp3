import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

public class parteA {
    private static volatile int foundValue = -1;
    private static volatile String foundHash = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el algoritmo (SHA-256 o SHA-512): ");
        String algorithm = scanner.nextLine();

        System.out.print("Ingrese la cadena de entrada (C): ");
        String data = scanner.nextLine();

        System.out.print("Ingrese el número de ceros buscados: ");
        int numZeros = scanner.nextInt();

        System.out.print("Ingrese el número de threads (1 o 2): ");
        int numThreads = scanner.nextInt();

        scanner.close();

        long startTime = System.currentTimeMillis();
        MineBitcoin(algorithm, data, numZeros, numThreads);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Cadena de entrada (C): " + data);
        System.out.println("Valor v encontrado: " + foundValue);
        System.out.println("Hash: " + foundHash);
        System.out.println("Tiempo de búsqueda: " + TimeUnit.MILLISECONDS.toSeconds(elapsedTime) + " segundos");
    }

    public static void MineBitcoin(String algorithm, String data, int numZeros, int numThreads) {
        int range = (int) Math.pow(26, 7);
        int threads = Math.min(numThreads, 2);

        for (int i = 0; i < range && foundValue == -1; i++) {
            final int start = i * range / threads;
            final int end = (i + 1) * range / threads;

            Thread[] minerThreads = new Thread[threads];
            for (int t = 0; t < threads; t++) {
                final int threadNum = t;
                minerThreads[t] = new Thread(() -> {
                    for (int v = start + threadNum; v < end && foundValue == -1; v++) {
                        String combined = data + generateV(v);
                        String hash = calculateHash(algorithm, combined);

                        if (hash.startsWith("0".repeat(numZeros))) {
                            foundValue = v;
                            foundHash = hash;
                        }
                    }
                });
                minerThreads[t].start();
            }

            for (int t = 0; t < threads; t++) {
                try {
                    minerThreads[t].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String calculateHash(String algorithm, String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String generateV(int v) {
        StringBuilder result = new StringBuilder();
        while (v > 0) {
            result.insert(0, (char) ('a' + (v % 26)));
            v /= 26;
        }
        return result.toString();
    }
}