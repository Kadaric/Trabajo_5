import java.util.Scanner;

public class Chat
{
    private static boolean chatInit = false;
    public static void main(String[] args)
    {
        System.out.println("Bienvenido al chat Socket");

        Servidor chatServidor = new Servidor();
        chatServidor.start();
        if(chatServidor.iniciarChat())
        {
            
            
        }   
      
    }
}