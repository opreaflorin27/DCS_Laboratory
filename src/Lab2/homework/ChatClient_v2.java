package Lab2.homework;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ChatClient_v2 {

    private DatagramSocket socket;
    private JTextArea textArea;
    private JTextField inputField;

    public ChatClient_v2(String host, int port) throws IOException {
        socket = new DatagramSocket();

        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        inputField = new JTextField(25);
        JButton sendButton = new JButton("Send");
        panel.add(inputField);
        panel.add(sendButton);

        frame.add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String message = inputField.getText();
                    try {
                        send(message, host, port);
                        SwingUtilities.invokeLater(() -> {
                            textArea.append("Sent: " + message + "\n");
                            inputField.setText("");
                        });
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }).start();
            }
        });

        frame.setVisible(true);

        new Thread(() -> {
            try {
                receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void send(String message, String host, int port) throws IOException {
        byte[] buffer = message.getBytes();
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }

    private void receive() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength());
            SwingUtilities.invokeLater(() -> {
                textArea.append("Received: " + message + "\n");
            });
        }
    }

    public static void main(String[] args) {
        try {
            new ChatClient_v2("localhost", 4445);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
