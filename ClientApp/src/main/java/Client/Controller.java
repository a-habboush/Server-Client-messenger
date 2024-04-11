package Client;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button sendButton;

    @FXML
    private BorderPane mainScreen;
    @FXML
    private TextField insertBox = new TextField(" ");
    @FXML
    VBox chatVbox = new VBox(2000);
    @FXML
    private ScrollPane ChatBox;

    private Client client;






    public void Enter() {

        mainScreen.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                send();
            }});
    }


    public void send(){

        String aws = insertBox.getText();
        HBox message = new HBox();
        message.setPadding(new Insets(0,20,10,0));
        message.setAlignment(Pos.CENTER_RIGHT);
        message.setMaxSize(400,275);


        if (aws.isEmpty() == false){
            Text text = new Text(aws);
            TextFlow messageText = new TextFlow(text);
            text.setFont(Font.font("Calibre", 14));
            text.setFill(Color.rgb(239,239,239));
            messageText.setBackground(Background.fill(Color.rgb(100,208,255)));
            messageText.setPadding(new Insets(5,10,5,10));
            insertBox.clear();
            messageText.setMaxSize(250,300);
            message.getChildren().addAll(messageText);
            chatVbox.getChildren().addAll(message);
            client.sendMessageToServer(aws);
        }



    }


    public static void receive(String clientMessage,VBox vbox){
        HBox messageR = new HBox();
        messageR.setPadding(new Insets(0,20,10,0));
        messageR.setAlignment(Pos.CENTER_LEFT);

        messageR.setMaxSize(400,275);

        Text textR = new Text(clientMessage);
        TextFlow messageTextR = new TextFlow(textR);
        textR.setFont(Font.font("Calibre", 14));
        messageTextR.setBackground(Background.fill(Color.rgb(196,196,196)));
        messageTextR.setMaxSize(250,300);
        messageTextR.setPadding(new Insets(5,10,5,10));
        messageR.getChildren().add(messageTextR);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                vbox.getChildren().add(messageR);
            }

        });

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            client = new Client(new Socket("localhost",12345));
            System.out.println("Connected to server.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                ChatBox.setVvalue((Double) t1);
            }
        });
        Image img = new Image(getClass().getResourceAsStream("send.jpg"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(25);
        imgView.setPreserveRatio(true);
        sendButton.setGraphic(imgView);

        Enter();
        client.receiveMessageFromServer(chatVbox);

    }
}