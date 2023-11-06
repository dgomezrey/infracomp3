import java.security.MessageDigest;

public class parteB extends Thread {
    private String cadenaEntrada;
    private String algoritmo;
    private int bitsEnCero;
    private Integer indiceInicial;
    private Integer indiceFinal;
    public static boolean encontrado = false;
    public static String valorEncontrado = "";

    public void PruebaDeco(String cadenaEntrada, String algoritmo, int bitsEnCero, Integer indiceInicial, Integer indiceFinal) {
        this.cadenaEntrada = cadenaEntrada;
        this.algoritmo = algoritmo;
        this.bitsEnCero = bitsEnCero;
        this.indiceInicial = indiceInicial;
        this.indiceFinal = indiceFinal;
    }

    @Override
    public void run() {
        descifrar();
    }

    public void descifrar() {
        for (int i = indiceInicial; i < indiceFinal; i++) {
            if (encontrado) {
                return;
            }
            generarCombinaciones(Character.toString((char) ('a' + i)), 7, "");
        }
    }

    public void generarCombinaciones(String prefijo, int longitud, String sufijo) {
        if (encontrado) {
            return;
        }
        if (longitud == 0) {
            match(cadenaEntrada, prefijo, sufijo);
            return;
        }
        for (int i = 0; i < 26; i++) {
            String nuevaCombinacion = prefijo + (char) ('a' + i);
            generarCombinaciones(nuevaCombinacion, longitud - 1, sufijo);
        }
    }

    public void match(String cadenaEntrada, String prefijo, String sufijo) {
        String guess = prefijo + sufijo;
        String cadenaConcatenada = cadenaEntrada + guess;
        byte[] bytesCadena = cadenaConcatenada.getBytes();
        byte[] arreglo = null;

        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            md.update(bytesCadena);
            arreglo = md.digest();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        int cerosRequeridos = bitsEnCero / 4;
        boolean encontradoLocal = true;

        for (int i = 0; i < cerosRequeridos; i++) {
            if ((arreglo[i] & 0xFF) != 0) {
                encontradoLocal = false;
                break;
            }
        }

        if (encontradoLocal) {
            encontrado = true;
            valorEncontrado = guess;
        }
    }
}
