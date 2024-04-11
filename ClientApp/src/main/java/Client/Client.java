package Client;

import javafx.scene.layout.VBox;

import java.io.*;

import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Client(Socket socket) {
        try {

            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e){
            System.out.println("client creation error");
            close(socket,bufferedWriter,bufferedReader);
            throw new RuntimeException(e);
        }
    }


    public void sendMessageToServer(String messageToServer){
        try {
            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (IOException e){
            System.out.println("error sending to server");
            close(socket,bufferedWriter,bufferedReader);
            throw new RuntimeException(e);
        }
    }

    public void receiveMessageFromServer(VBox vbox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    try {
                        String messageFromServer = bufferedReader.readLine();
                        Controller.receive(messageFromServer,vbox);
                    }
                    catch (IOException e){
                        System.out.println("error receiving from server");
                        close(socket,bufferedWriter,bufferedReader);
                        e.printStackTrace();
                        break;

                    }
                }

            }
        }).start();

    }













    public void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader){

        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }

        catch (IOException e){
            System.out.println("error closing");
            throw new RuntimeException(e);

        }
    }



}
