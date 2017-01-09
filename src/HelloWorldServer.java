import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by lotrik on 07.01.2017.
 */
public class HelloWorldServer {

    public static void main(String[] args) throws Exception{

        DatagramSocket serverSocket = new DatagramSocket(10700);

        byte[] receiveData = new byte[512];
        byte[] sendData;
        InetAddress IPAddress;
        int port;

        while(true)
        {
            DatagramPacket receivedPacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivedPacket);

            String receivedLogin = new String(receivedPacket.getData());
            receivedLogin = "Hello, " + receivedLogin;

            IPAddress = receivedPacket.getAddress();
            port = receivedPacket.getPort();

            sendData = receivedLogin.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}
