package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private BorderPane BorderPane;
    @FXML
    private ScrollPane ScrollPane;
    @FXML
    private VBox VboxMessage;
    @FXML
    private Button buttonSend;
    @FXML
    private HBox hboxSend;
    @FXML
    private HBox hboxTittle;
    @FXML
    private Label labelTittle;
    @FXML
    private TextField textField;
    @FXML
    private TextFlow textFlow;
    @FXML
    private ImageView icon;
    @FXML
    void buttonSend(ActionEvent event) {
    }
    @FXML
    void textField(ActionEvent event) {
    }

    private Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost",1234));
            System.out.println("Connected to server.");
        }catch (IOException e){
            e.printStackTrace();
        }

        VboxMessage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ScrollPane.setVvalue((Double) newValue);
            }
        });
        client.receiveMessageFromServer(VboxMessage);
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend=textField.getText();
                if(!messageToSend.isEmpty()){
                    HBox hBox= new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5,5,5,10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow= new TextFlow(text);
                    textFlow.setStyle("-fx-color:rgb(239,242,255);"+
                            "-fx-background-color:rgb(15,125,242);"+
                            "-fx-background-radius:20px;");

                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hBox.getChildren().add(textFlow);
                    VboxMessage.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend);
                    textField.clear();
                }
            }
        });
    }
    public static void addLabel(String msgFromServer,VBox vBox){

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color:rgb(233,233,235);"+
                "-fx-background-radius:20px;");
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);

            }
        });
    }
}
