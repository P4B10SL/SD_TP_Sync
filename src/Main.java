import java.awt.image.DataBufferInt;
import java.io.DataInputStream;
import java.io.IOException;

public class Main {
    private static final LamportClock clock = new LamportClock();
    public static void main(String[] args) {
        SistemaComunicaciones Coms= new SistemaComunicaciones();
        try {
            /*

            LocalDateTime now = LocalDateTime.now();

            // Formatea el timestamp en formato de fecha y hora
            String timestampFormateado = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

            System.out.println(timestampFormateado);
            Coms.SendTCP(timestampFormateado);


            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);
            String timestampEnviar= Integer.toString(clock.getTimestamp());
            System.out.println("ANTES DE ENVIAR: "+timestampEnviar);
            /*Desde aca Comentar cuando se ejecuta en modo SERVER
            System.out.println("A continuación se utilizara el clock en la máquina cliente y se enviará");
            String timestampEnviar= Integer.toString(clock.getTimestamp());
            Coms.SendTCP(timestampEnviar); //Descomentar esta línea para ejecutarse del lado del cliente
             Hasta aca comentar cuando se ejecuta en modo servidor
            String respuestaServerTCP=Coms.ReceiveTCP();//De aca para abajo descomentar para ejecutarlo en modo servidor
            // Actualizar reloj lógico
            clock.increment();
            System.out.println("DESPUES DE ENVIAR: "+timestampEnviar);
            System.out.println("RECIBIDO DEL SERVIDOR: "+respuestaServerTCP);
            clock.waitUntil(Integer.parseInt(respuestaServerTCP));
            Coms.SendTCP(timestampEnviar);
            System.out.println(respuestaServerTCP);
            */

            //En cliente ingresa el ip del servidor y viceversa
            DataInputStream ipConsola;
            System.out.println("Ingrese la dirección IP a la que se desea conectar:");
            ipConsola=new DataInputStream(System.in);
            String ipDestino=ipConsola.readLine();
            Coms.init(ipDestino);

            /*
            //Cliente
            String contador= "0";
            Coms.SendTCP(contador);
            while (true) {
                String recibidoTCPCliente = Coms.ReceiveTCP();
                Integer contadorRecibidoCliente = Integer.parseInt(recibidoTCPCliente);
                String contadorActualizadoCliente = Integer.toString(contadorRecibidoCliente + 1);
                Coms.SendTCP(contadorActualizadoCliente);
                System.out.println(contadorActualizadoCliente);
            }
            */
            //Servidor
            while (true) {
                System.out.println("HASTA ACA LLEGA");
                String recibidoTCPServidor = Coms.ReceiveTCP();
                System.out.println("Esto es lo que recibió: "+ recibidoTCPServidor);
                Integer contadorRecibido = Integer.parseInt(recibidoTCPServidor);
                String contadorActualizado = Integer.toString(contadorRecibido + 1);
                Coms.SendTCP(contadorActualizado);
                System.out.println(contadorActualizado);
            }
        }
        catch (IOException e)
        {
            System.out.print("Error al ingresar el mensaje");
            System.out.print(e);
        }
    }

}