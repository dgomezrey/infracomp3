import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Proceso extends Thread {
    private final AtomicBoolean solucionado;
    private final long iniciaTiempo;
    private final String cadena;
    private final String algoritmo;
    private final int numCeros;
    private final int inicio;
    private final int fin;
    
    public Proceso(AtomicBoolean s, long t, String c, String a, int nc, int i, int f) {
        this.solucionado = s;
        this.iniciaTiempo = t;
        this.cadena = c;
        this.algoritmo = a;
        this.numCeros = nc / 4;
        this.inicio = i;
        this.fin = f;
    }

    private String alfabetizar(int v) {
        StringBuilder sb = new StringBuilder();
        int b = 26;

        while (v > 0) {
            char ch = (char) ('a' + (v % b));
            sb.insert(0, ch);
            v /= b;
        }

        while (sb.length() < 7) {
            sb.insert(0, 'a');
        }

        return sb.toString();
    }

    private static String convertirHexaDecimal(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
    
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
    
        return hexString.toString();
    }
    

    @Override
    public void run() {
        try {
            MessageDigest md = MessageDigest.getInstance(algoritmo);
            String buscar = String.format("%0" + numCeros + "d", 0);

            int v = inicio;
            while (v < fin && !solucionado.get()) {
                String info = cadena + alfabetizar(v);
                byte[] hash = md.digest(info.getBytes());
                String hexaDecimal = convertirHexaDecimal(hash);

                if (hexaDecimal.startsWith(buscar)) {
                    solucionado.set(true);
                    long terminaTiempo = System.currentTimeMillis();
                    System.out.println("Tiempo de búsqueda: " + (terminaTiempo - iniciaTiempo) + " ms");
                    System.out.println("Hash Hexa-Decimal: " + hexaDecimal);
                    System.out.println("Valor (V) Encontrado: " + v);
                }

                if (solucionado.get()) {
                    return;
                }

                v++;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    
}
