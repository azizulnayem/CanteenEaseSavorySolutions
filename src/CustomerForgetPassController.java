
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

/**
 *
 * @author jwright
 */
public class CustomerForgetPassController implements Initializable {

    //These items are for the CheckBox example
    //@FXML private Label pizzaOrderLabel;
    @FXML
    private TextField IdTf;

    @FXML
    private Label checkLabel;

    @FXML
    private Label verifyLabel;

    @FXML
    private TextField contenttf;

    @FXML
    private TextField sutf;

    @FXML
    private TextField totf;

    @FXML
    private Button recoverBtn;

    @FXML
    private Button verifyBtn;

    @FXML
    private PasswordField newPassPf;
    
    @FXML
    private Label messageLabel;
    
     @FXML
    private Button checkBtn;


    String idtext;
    int x;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        verifyLabel.setVisible(false);
        checkLabel.setVisible(false);
        totf.setVisible(false);
        recoverBtn.setVisible(false);
        verifyBtn.setVisible(false);
        newPassPf.setVisible(false);
        messageLabel.setVisible(false);
        //checkBtn.setStyle("-fx-background-fill: #FF0000;"); // -fx-text-fill: #FF0000;

    }

    public void checkbtnpushed(ActionEvent event) throws IOException {
        
        idtext = IdTf.getText();
        String from, to, host, sub, content;

        File file = new File("varsity_database.txt");
        Scanner fsc = new Scanner(file);
        int flag1 = 0;
        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (idtext.equals(arr[0])) {
                    from = "uiucanteen@gmail.com";
                    to = arr[2];
                    host = "localhost";
                    //sub = sutf.getText();
                    sub = "Recover Id";
                    //content = contenttf.getText();
                    Random ran = new Random();
                    int min = 100000;
                    int max = 999999;
                    //int x = ran.nextInt(10000);
                    double y = Math.random() * (max - min + 1) + min;
                    x = (int) y;
                    content = "Your password recovery OTP \n" + x;

                    File file1 = new File("recover id.txt");

                    if (!file1.exists()) {
                        file1.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file1, true); // the true indicates append mode
                    BufferedWriter bw = new BufferedWriter(fw);
                    String toWrite = x + "#" + "\n";
                    bw.write(toWrite);
                    bw.close();
                    Properties p = new Properties();
                    p.put("mail.smtp.auth", "true");
                    p.put("mail.smtp.starttls.enable", "true");
                    p.put("mail.smtp.host", "smtp.gmail.com");
                    p.put("mail.smtp.port", "587");
                    Session s = Session.getDefaultInstance(p, new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("uiucanteen@gmail.com", "Admin.1234");
                        }
                    });

                    try {
                        MimeMessage m = new MimeMessage(s);
                        m.setFrom(from);
                        m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                        m.setSubject(sub);
                        m.setText(content);
                        Transport.send(m);
                        checkLabel.setVisible(true);
                        checkLabel.setText("Mail Send Successfully");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag1 = 1;
                    break;
                }
            }

        }

        if (flag1 == 0) {
            checkLabel.setVisible(true);
            checkLabel.setText("Your Id is not found!! Please enter correct Id");
            checkLabel.setStyle("-fx-text-fill: #FF0000;");

        } else {
            totf.setVisible(true);
            verifyBtn.setVisible(true);

        }
        
        //totf.setVisible(true);

    }

    public void verifyButtonPushed() throws IOException {
        
        String otp = totf.getText();
        //String passwordtext=password.getText();
        //String name=""; 
        File file = new File("recover id.txt");
        Scanner fsc = new Scanner(file);

        int flag = 0;
        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (otp.equals(arr[0])) {
                    //System.out.println(arr[0]);
                    flag = 1;
                    break;
                }

            }

        }
        
        //fsc.close();

        if (flag == 0) {
            
            verifyLabel.setVisible(true);
            //verifyLabel.setStyle("-fx-text-inner-color: #FF0000;");
            verifyLabel.setText("Wrong verify code!! Please try again");
            verifyLabel.setStyle("-fx-text-fill: #FF0000;"); // red color
            //verifyLabel.setVisible(false);
        } 
        else {

            //System.out.println("hi");
            verifyLabel.setVisible(true);
            //verifyLabel.setStyle("-fx-text-inner-color:  #00FF00;");
            verifyLabel.setText("Successfully verify your otp");
            verifyLabel.setStyle("-fx-text-fill: #00FF00;"); // green color
            //verifyLabel.setVisible(false);
            newPassPf.setVisible(true);
            recoverBtn.setVisible(true);
            
        }
    }

    public void backtocustomerloginbtnpushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("customer_login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

//    public void recoverpasswordbtnpushed(ActionEvent event) throws IOException {
//        Parent tableViewParent = FXMLLoader.load(getClass().getResource("CustomerRecoverPassword.fxml"));
//        Scene tableViewScene = new Scene(tableViewParent);
//
//        //This line gets the Stage information
//        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//        window.setScene(tableViewScene);
//        window.show();
//    }
    public void updatebtnpushed(ActionEvent event) throws IOException {

        String id = idtext;

        String passupdate = newPassPf.getText();

        ArrayList<String> temArray = new ArrayList<>();

        File file = new File("customer_info.txt");
        Scanner fsc = new Scanner(file);

        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (id.equals(arr[0])) {

                    temArray.add(arr[0] + "#" + arr[1] + "#" + passupdate);
                    //System.out.println(arr[0]+"#"+arr[1]+"#"+passupdate);

                } else {
                    //System.out.println(arr[0]+"#"+arr[1]+"#"+passupdate);

                    temArray.add(line);

                }
            }

        }

        File file1 = new File("customer_info.txt");

        if (!file1.exists()) {
            file1.createNewFile();
        }

        FileWriter fw = new FileWriter(file1);
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < temArray.size(); i++) {
            bw.write(temArray.get(i) + "\n");
        }

        bw.close();

        messageLabel.setVisible(true);
        messageLabel.setText("Your password Update Successfully");
    }

//    public void recoverpasswordbtnpushed(ActionEvent event) throws IOException
//    {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("CustomerRecoverPassword.fxml"));
//        Parent tableViewParent = loader.load();
//        
//        Scene tableViewScene = new Scene(tableViewParent);
//        
//        //access the controller and call a method
//        CustomerRecoverPasswordController controller = loader.getController();
//        //controller.fun(tableView.getSelectionModel().getSelectedItem());
//        controller.fun(idtext);
//        //This line gets the Stage information
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        
//        window.setScene(tableViewScene);
//        window.show();
//    }
}
