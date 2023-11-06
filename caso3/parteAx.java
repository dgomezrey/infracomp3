import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;

public class parteAx {
    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese la cadena de entrada (C):");
        String cadenaEntrada = br.readLine();

        System.out.println("Elija cuál algoritmo va a utilizar (1 para SHA-256, 2 para SHA-512):");
        String eleccion = br.readLine();
        String algoritmo = "";

        if (eleccion.equals("1")) {
            algoritmo = "SHA-256";
        } else if (eleccion.equals("2")) {
            algoritmo = "SHA-512";
        } else {
            System.out.println("Opción no válida");
            System.exit(0);
        }

        System.out.println("Ingrese el número de bits en cero requeridos (20, 24, 28, 32 o 36):");
        int bitsEnCero = Integer.parseInt(br.readLine());

        if (bitsEnCero != 20 && bitsEnCero != 24 && bitsEnCero != 28 && bitsEnCero != 32 && bitsEnCero != 36) {
            System.out.println("Número de bits en cero no válido");
            System.exit(0);
        }

        System.out.println("Ingrese el número de threads (1 o 2):");
        int numeroThreads = Integer.parseInt(br.readLine());

        if (numeroThreads != 1 && numeroThreads != 2) {
            System.out.println("Número de threads no válido");
            System.exit(0);
        }

        long startTime = System.nanoTime();
        PruebaDeco prueba1 = new PruebaDeco(cadenaEntrada, algoritmo, bitsEnCero, 0, 13);
        PruebaDeco prueba2 = new PruebaDeco(cadenaEntrada, algoritmo, bitsEnCero, 13, 26);

        prueba1.start();
        if (numeroThreads == 2) {
            prueba2.start();
        }

        prueba1.join();
        if (numeroThreads == 2) {
            prueba2.join();
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        if (PruebaDeco.encontrado) {
            System.out.println("Cadena de entrada (C): " + cadenaEntrada);
            System.out.println("Valor encontrado (v): " + PruebaDeco.valorEncontrado);
            System.out.println("Tiempo de búsqueda: " + elapsedTime + " nanosegundos");
        } else {
            System.out.println("No se encontró una respuesta en todo el espacio de búsqueda.");
            System.out.println("Tiempo de revisión de todo el espacio de búsqueda: " + elapsedTime + " nanosegundos");
        }
    }
}
