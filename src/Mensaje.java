import java.nio.charset.StandardCharsets;

public class Mensaje {
    static String mensajeString;
    static byte[] mensajeArrayB;
    public static byte[] ToArrayBytes(String mensajeString){
        byte[] arrayBytes = mensajeString.getBytes();
        return arrayBytes;
    }
    public static String ToMessage(byte[] arrayBytes, int truncador){
        String mensajeStr= new String(arrayBytes, 0, truncador,StandardCharsets.UTF_8);
        return mensajeStr;
    }
}
