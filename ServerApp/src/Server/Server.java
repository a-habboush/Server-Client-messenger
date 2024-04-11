package Server;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e){
            System.out.println("server creation error");
            close(socket,bufferedWriter,bufferedReader);
            throw new RuntimeException(e);
        }
    }


    public void sendMessageToClient(String messageToClient){
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }
        catch (IOException e){
        System.out.println("error sending to client");
        close(socket,bufferedWriter,bufferedReader);
        throw new RuntimeException(e);

        }
    }


    public void receiveMessageFromClient(VBox vbox){
    new Thread(new Runnable() {
        @Override
        public void run() {
        while(socket.isConnected()){
            try {
                String messageFromClient = bufferedReader.readLine();
                Controller.receive(messageFromClient,vbox);
            }
            catch (IOException e){
                System.out.println("error receiving from client");
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
