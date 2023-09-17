package com.donatus.client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        //Socket socket = null;

        try (Socket socket = new Socket("localhost", 234)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //Scanner sc = new Scanner(System.in);
            listener(writer, socket);
            sender(reader, socket);
        } catch (IOException e) {
            System.out.println("Server is down");
        }
    }

    public static void listener(BufferedWriter writer, Socket socket) {
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (socket.isConnected()) {
                String messageToSend;
                try {
                    messageToSend = sc.nextLine();
                    if (!messageToSend.isEmpty()) {
                        writer.write("Client: "+messageToSend);
                        writer.newLine();
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Sending Error!");
                }
            }
        }).start();
    }

    public static void sender(BufferedReader reader, Socket socket) {
        while (socket.isConnected()) {
            String receivedMessage;
            try {
                receivedMessage = reader.readLine();
                if (receivedMessage != null)
                    System.out.println(receivedMessage);
            } catch (IOException e) {
                System.out.println("Reading error!");
            }
        }
    }
}
