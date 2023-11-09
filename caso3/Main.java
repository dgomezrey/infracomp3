import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String cadena = "";
        while (cadena.isEmpty()) {
            System.out.println("Cadena de entrada: ");
            cadena = scanner.nextLine().trim();
        }

        int numCeros = 0;
        while (numCeros % 4 != 0 || numCeros < 20 || numCeros > 36) {
            System.out.println("Bits en cero (20, 24, 28, 32 o 36): ");
            numCeros = Integer.parseInt(scanner.nextLine().trim());
        }

        int numThreads = 0;
        while (numThreads != 1 && numThreads != 2) {
            System.out.println("Threads (1 o 2): ");
            numThreads = Integer.parseInt(scanner.nextLine().trim());
        }

        String algoritmo = "";
        while (!algoritmo.equals("SHA-256") && !algoritmo.equals("SHA-512")) {
            System.out.println("Algoritmo de hash (SHA-256 o SHA-512): ");
            algoritmo = scanner.nextLine().trim();
        }

        System.out.println();
        scanner.close();

        AtomicBoolean solucionado = new AtomicBoolean(false);
        long iniciaTiempo = System.currentTimeMillis();
        int valoresPosibles = (int) Math.pow(26, 7);
        int rango = valoresPosibles / numThreads;
        Proceso[] threads = new Proceso[numThreads];

        int i = 0;
        while (i < numThreads) {
            int inicio = i * rango;
            int fin = (i + 1) * rango;
            if (i == numThreads - 1) {
                fin = valoresPosibles;
            }
            threads[i] = new Proceso(solucionado, iniciaTiempo, cadena, algoritmo, numCeros, inicio, fin);
            threads[i].start();
            i++;
        }


        for (Proceso thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!solucionado.get()) {
            long terminaTiempo = System.currentTimeMillis();
            System.out.println("Tiempo: " + (terminaTiempo - iniciaTiempo) + " ms");
            System.out.println("No hay solución.");
        }
    }
}
