import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by lotrik on 07.01.2017.
 */
public class HelloWorldServer {

    static String clientSentence;
    static String capitalizedSentence;

    public static void main(String[] args) throws Exception{

        ServerSocket welcomeSocket = new ServerSocket(10700);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            if (connectionSocket.isConnected()) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connection(connectionSocket);
                    }
                });
                thread.start();
            }
        }
    }

    private static void connection (Socket socket) {
        try {
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase();
            outToClient.writeBytes(capitalizedSentence + "\n");
            outToClient.flush();
            socket.close();
        } catch (Exception ex){

        }
    }
}
