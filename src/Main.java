import java.awt.image.DataBufferInt;
import java.io.DataInputStream;
import java.io.IOException;

public class Main {
    private static final LamportClock clock = new LamportClock();
    public static void main(String[] args) {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            DataInputStream ipConsola;
            //String ipDestino= "172.24.1.15";
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);
            //Desde aca Comentar cuando se ejecuta en modo SERVER
            System.out.println("A continuación se utilizara el clock en la máquina cliente y se enviará");
            String timestampEnviar= Integer.toString(clock.getTimestamp());
            Coms.SendTCP(timestampEnviar); //Descomentar esta línea para ejecutarse del lado del cliente
            // Hasta aca comentar cuando se ejecuta en modo servidor
            //PD: Ejecuten directamente el Main, lo otro no
            String respuestaServerTCP=Coms.ReceiveTCP();//De aca para abajo descomentar para ejecutarlo en modo servidor
            // Actualizar reloj lógico
            clock.increment();
            clock.waitUntil(Integer.parseInt(respuestaServerTCP));
            Coms.SendTCP(timestampEnviar);
            System.out.println(respuestaServerTCP);
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }
    }

}