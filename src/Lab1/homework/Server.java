package Lab1.homework;
import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 1950;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // Receive data from the client
                int x = in.readInt();
                int y = in.readInt();

                // Calculate the percentage
                double result = (double) x / y * 100;

                // Send the result back to the client
                out.writeDouble(result);

                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
