import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SincronizadorCliente {

    private static final LamportClock clock = new LamportClock();

    public static void main(String[] args) throws IOException {
        // Crear socket TCP
        Socket cliente = new Socket("localhost", 8080);

        // Enviar timestamp
        OutputStream salida = cliente.getOutputStream();
        salida.write(Integer.toString(clock.getTimestamp()).getBytes());

        // Recibir timestamp actualizado
        InputStream entrada = cliente.getInputStream();
        int timestampActualizado = Integer.parseInt(new String(entrada.readAllBytes()));

        // Actualizar reloj lógico
        clock.increment();
        clock.waitUntil(timestampActualizado);

        // Cerrar conexión
        cliente.close();
    }
}
