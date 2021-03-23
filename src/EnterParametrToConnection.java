import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EnterParametrToConnection extends Application {
    private String ipData;
    private String portData;
    private String userData;
    private String passwordData;

    public ArrayList<String> getEnteredParameters() {
        ArrayList<String> listParametrs = new ArrayList<>();
        listParametrs.add(this.ipData);
        listParametrs.add(this.portData);
        listParametrs.add(this.userData);
        listParametrs.add(this.passwordData);
        return listParametrs;
    }

    public void start(Stage stage) {
        final double rem = new Text("").getLayoutBounds().getHeight();

        GridPane pane = new GridPane();
        pane.setHgap(0.8 * rem);
        pane.setVgap(0.8 * rem);
        pane.setPadding(new Insets(0.8 * rem));
        Label host = new Label("Host:");
        Label port = new Label("Port:");
        Label usernameLabel = new Label("User name:");
        Label passwordLabel = new Label("Password:");

        TextField hostName = new TextField();
        hostName.setPromptText("Choose a host");
        TextField portName = new TextField();
        portName.setPromptText("Choose a port");
        TextField userName = new TextField();
        userName.setPromptText("Choose a user name");
        PasswordField password = new PasswordField();
        password.setPromptText("Choose a password");
        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(10);
        textArea.setPrefColumnCount(20);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        Button okButton = new Button("Ok");
        okButton.setMinSize(75, 35);
        okButton.setOnAction(event ->
        {
            ipData = hostName.getText();
            portData = portName.getText();
            userData = userName.getText();
            passwordData = password.getText();
            //FTPServerWork.getListParametrs(getEnteredParameters());
            stage.close();
        });

        pane.add(host, 0, 0);
        pane.add(hostName, 1, 0);
        pane.add(port, 0, 1);
        pane.add(portName, 1, 1);
        pane.add(usernameLabel, 0, 2);
        pane.add(userName, 1, 2);
        pane.add(passwordLabel, 0, 3);
        pane.add(password, 1, 3);
        pane.add(okButton, 0, 4, 2, 1);

        GridPane.setHalignment(usernameLabel, HPos.RIGHT);
        GridPane.setHalignment(passwordLabel, HPos.RIGHT);
        GridPane.setHalignment(okButton, HPos.CENTER);

        stage.setScene(new Scene(pane));
        stage.setTitle("InputData");
        stage.show();
    }
}
