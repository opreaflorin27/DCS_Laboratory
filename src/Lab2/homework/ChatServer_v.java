package Lab2.homework;

import java.net.InetAddress;
import java.util.Objects;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class ChatServer_v {

    private DatagramSocket socket;
    private Set<ClientAddress> clients;

    public ChatServer_v(int port) throws SocketException {
        socket = new DatagramSocket(port);
        clients = new HashSet<>();
    }

    public void start() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + message);


                ClientAddress senderAddress = new ClientAddress(packet.getAddress(), packet.getPort());
                clients.add(senderAddress);


                if (clients.size() == 1) {
                    String feedbackMsg = "Welcome to the chat!";
                    DatagramPacket feedbackPacket = new DatagramPacket(
                            feedbackMsg.getBytes(),
                            feedbackMsg.getBytes().length,
                            senderAddress.getAddress(),
                            senderAddress.getPort());
                    socket.send(feedbackPacket);
                } else {
                    // Broadcast message to all known clients except the sender
                    for (ClientAddress client : clients) {
                        if (!client.equals(senderAddress)) {
                            DatagramPacket sendPacket = new DatagramPacket(
                                    message.getBytes(),
                                    message.getBytes().length,
                                    client.getAddress(),
                                    client.getPort());
                            socket.send(sendPacket);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer_v server = new ChatServer_v(4445);
            server.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static class ClientAddress {
        private final InetAddress address;
        private final int port;

        public ClientAddress(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClientAddress that = (ClientAddress) o;
            return port == that.port && address.equals(that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address, port);
        }
    }
}
