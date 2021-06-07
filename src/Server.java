import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The Server.
 */
public class Server{

    /**
     * Main.
     *
     * @param args the args
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        int count = 0;
        try{
            serverSocket = new ServerSocket(8000);
            System.out.println("Server is listening...");
            ExecutorService pool = Executors.newFixedThreadPool(5);
            while(count < 5){
                Socket socket = serverSocket.accept();
                System.out.println("Server connected to port " + socket.getLocalAddress() + "----" +socket.getPort());
                count++;
                pool.execute(new ClientHandler(socket,count));
            }


            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }

    private static class ClientHandler implements Runnable
    {
        private Socket socket;
        private int clientNumber;

        /**
         * Instantiates a new Client handler.
         *
         * @param socket       the socket
         * @param clientNumber the client number
         */
        public ClientHandler(Socket socket,int clientNumber)
        {
            this.socket = socket;
            this.clientNumber = clientNumber;
        }

        @Override
        public void run()
        {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                String original;
                String text = "";

                //sending data which came from terminal
                while (true) {

                    original = dataInputStream.readUTF();

                    //stop the connection when we get over from terminal
                    if (original.equals("over")) {
                        dataOutputStream.writeUTF("over");
                        break;
                    }

                    text = text.concat(original);
                    System.out.println("wrote " + text + " on client " + clientNumber);
                    dataOutputStream.writeUTF(text);
                }

                System.out.println("Connection ended! ");
                dataOutputStream.close();
                dataInputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}