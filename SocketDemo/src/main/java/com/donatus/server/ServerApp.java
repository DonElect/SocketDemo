package com.donatus.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(234);
        Socket socket = serverSocket.accept();
        System.out.println("Client connected!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        //Scanner sc = new Scanner(System.in);

        listener(writer, socket);
        sender(reader, socket);
    }

    public static void listener(BufferedWriter writer, Socket socket) {
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (socket.isConnected()) {
                String messageToSend;
                try {
                    messageToSend = sc.nextLine();
                    if (!messageToSend.isEmpty()) {
                        writer.write("Server: "+messageToSend);
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
