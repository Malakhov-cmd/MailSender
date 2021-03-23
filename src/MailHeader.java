import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MailHeader extends Application implements Initializable {
    public Label creator;
    public Button enterProps;
    Stage mainStage;
    File addedFile;
    Session session;

    public TextArea address;
    public TextArea theme;
    public TextArea messageText;
    public Button clearText;
    public Button addFiles;
    public Button sendButton;

    String from;
    private boolean check;
    private ArrayList<String> listEMAIL = new ArrayList<>();
    private ArrayList<File> listFile = new ArrayList<>();
    private ArrayList<String> listProp = new ArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Elements.fxml"));
        stage.setScene(new Scene(root));
       // stage.getIcons().add(new Image("Lab2/Icon.png"));
        stage.setTitle("Mail Sender");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //preSet();
        EnterParametrToConnection enterDataClass = new EnterParametrToConnection();

        enterProps.setOnAction(event -> {
            //enterDataClass = new EnterParametrToConnection();
            myLaunch(enterDataClass);
            check = true;
        });
        sendButton.setOnAction(event -> {
            listProp = enterDataClass.getEnteredParameters();
            if(check) {
                preSet(listProp.get(0), listProp.get(1), listProp.get(2), listProp.get(3));
                if (!Objects.equals(address.getText(), "")) {
                    Scanner scanner = new Scanner(address.getText());
                    while (scanner.hasNextLine()) {
                        String checkAddress = scanner.nextLine();
                        if (checkAddress.matches("\\S+\\@\\w+\\.\\w+")) {
                            listEMAIL.add(checkAddress);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "You entered an invalid email address. Try again.");
                            alert.showAndWait();
                        }
                    }
                    for (String addressMail : listEMAIL) {
                        send(addressMail, listFile);
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Enter email address");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enter property");
                alert.showAndWait();
            }
        });
        addFiles.setOnAction(event -> {
            FileChooser dialog = new FileChooser();
            dialog.setInitialDirectory(new File("E:\\Загрузки"));
            addedFile = dialog.showOpenDialog(mainStage);
            if (addedFile != null) {
                listFile.add(addedFile);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "File added to you email");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "File was not chosen");
                alert.showAndWait();
            }
        });
        clearText.setOnAction(event -> {
            address.setText("");
            theme.setText("");
            messageText.setText("");
        });
    }

    public void preSet(String host, String port, String user, String passWord ) {

        /*from = "malahovgeorg@gmail.com";
        final String username = "enter yours";
        final String password = "enter yours";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");*/
        from = user;
        final String username = user;
        final String password = passWord;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public void send(String emailTo, ArrayList<File> listFile) {
        try {
            Message message = new MimeMessage(session);

            System.out.println(emailTo);
            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));

            message.setSubject(theme.getText());

            if (listFile != null) {
                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText(messageText.getText());

                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(messageBodyPart);

                for (File file : listFile) {
                    messageBodyPart = new MimeBodyPart();
                    String filename = file.getAbsolutePath();
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(file.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
                message.setContent(multipart);

                Transport.send(message);
                System.out.println("Message was sent successfully");
            } else {
                message.setText(messageText.getText());
                Transport.send(message);
                System.out.println("Message was sent successfully");
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void myLaunch(Application applicationClass) {
        Platform.runLater(() -> {
            try {
                Application application = applicationClass;
                Stage primaryStage = new Stage();
                application.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
