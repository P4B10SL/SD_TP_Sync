package Berkeley;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    public static void main(String[] args) {
String ipServidor; // IP del servidor
    int puerto = 732; // Puerto del servidor
    try {
        // Solicitar la IP del servidor
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese la dirección IP del servidor:");
        ipServidor = reader.readLine();

        // Crear un socket para conectarse al servidor
        Socket socket = new Socket(ipServidor, puerto);

        // Crear streams de entrada y salida
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Enviar el timestamp al servidor
        LocalDateTime now = LocalDateTime.now();
        String timestampFormateado = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        out.println(timestampFormateado);

        // Recibir la diferencia de tiempo promedio del servidor
        String averageDifference = in.readLine();
        System.out.println("Diferencia de tiempo promedio recibida del servidor: " + averageDifference);

        // Cerrar el socket
        socket.close();
    } catch (IOException e) {
        System.out.println("Error al conectar al servidor");
        System.out.print(e);
    }
    }
    
}
