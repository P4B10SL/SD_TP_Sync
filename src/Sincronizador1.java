import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Sincronizador1 {

    private static final LamportClock clock = new LamportClock();

    public static void main(String[] args) throws IOException, InterruptedException {
        // Crear servidor TCP
        ServerSocket servidor = new ServerSocket(8080);

        // Aceptar conexión de la máquina 2
        Socket cliente2 = servidor.accept();

        // Recibir timestamp de la máquina 2
        InputStream entrada2 = cliente2.getInputStream();
        int timestamp2 = Integer.parseInt(new String(entrada2.readAllBytes()));

        // Actualizar reloj lógico
        clock.increment();
        clock.waitUntil(timestamp2);

        // Enviar timestamp actualizado a la máquina 2
        OutputStream salida2 = cliente2.getOutputStream();
        salida2.write(Integer.toString(clock.getTimestamp()).getBytes());

        // Aceptar conexión de la máquina 3
        Socket cliente3 = servidor.accept();

        // Recibir timestamp de la máquina 3
        InputStream entrada3 = cliente3.getInputStream();
        int timestamp3 = Integer.parseInt(new String(entrada3.readAllBytes()));

        // Actualizar reloj lógico
        clock.increment();
        clock.waitUntil(timestamp3);

        // Enviar timestamp actualizado a la máquina 3
        OutputStream salida3 = cliente3.getOutputStream();
        salida3.write(Integer.toString(clock.getTimestamp()).getBytes());

        // Aceptar conexión de la máquina 4
        Socket cliente4 = servidor.accept();

        // Recibir timestamp de la máquina 4
        InputStream entrada4 = cliente4.getInputStream();
        int timestamp4 = Integer.parseInt(new String(entrada4.readAllBytes()));

        // Actualizar reloj lógico
        clock.increment();
        clock.waitUntil(timestamp4);

        // Enviar timestamp actualizado a la máquina 4
        OutputStream salida4 = cliente4.getOutputStream();
        salida4.write(Integer.toString(clock.getTimestamp()).getBytes());

        // Iniciar proceso P1
        Thread thread1 = new Thread(() -> {
            // Ejecutar proceso P1
            System.out.println("Proceso P1 iniciado");
        });
        thread1.start();

        // Iniciar proceso P2
        Thread thread2 = new Thread(() -> {
            // Esperar a que P1 termine
            clock.waitUntil(clock.getTimestamp() + 1);

            // Ejecutar proceso P2
            System.out.println("Proceso P2 iniciado");
        });
        thread2.start();

        // Iniciar proceso P3
        Thread thread3 = new Thread(() -> {
            // Esperar a que P2 termine
            clock.waitUntil(clock.getTimestamp() + 2);

            // Ejecutar proceso P3
            System.out.println("Proceso P3 iniciado");
        });
        thread3.start();

        // Iniciar proceso P4
        Thread thread4 = new Thread(() -> {
            // Esperar a que P3 termine
            clock.waitUntil(clock.getTimestamp() + 3);

            // Ejecutar proceso P4
            System.out.println("Proceso P4 iniciado");
        });
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }
}
