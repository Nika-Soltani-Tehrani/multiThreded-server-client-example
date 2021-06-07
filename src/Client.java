import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * The Client.
 */
public class Client{
    /**
     * Main.
     * @param args the args
     */
    public static void main(String[] args){
        try{
            Socket socket = new Socket("localhost",8000);
            System.out.println("Client connected to the server ");
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            String text;

            //sending data which came from terminal
            while(true)
            {

                text = scanner.nextLine();
                dataOutputStream.writeUTF(text);

                //stop the connection when we get over from terminal
                if (text.equalsIgnoreCase("over"))
                    break;
                System.out.println(dataInputStream.readUTF() + " ");
            }
            dataOutputStream.close();
            dataInputStream.close();
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}



