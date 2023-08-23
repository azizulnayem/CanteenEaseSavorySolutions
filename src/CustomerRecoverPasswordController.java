/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dulal
 */
public class CustomerRecoverPasswordController implements Initializable {

    @FXML
    private TextField recoverIdTf;

    @FXML
    private Label verifyLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        verifyLabel.setVisible(false);
    }
    /**
     * Initializes the controller class.
     */
    public void verifybtnpushed(ActionEvent event) throws IOException {
        String idtext = recoverIdTf.getText();
        //String passwordtext=password.getText();
        //String name=""; 
        File file = new File("recover id.txt");
        Scanner fsc = new Scanner(file);

        int flag = 0;
        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (idtext.equals(arr[0])) {

                    flag = 1;
                    break;
                }

            }

        }

        if (flag == 1) {
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("customerPassUpdate.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);

            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(tableViewScene);
            window.show();
        }
        
        else
        {
            verifyLabel.setVisible(true);
            verifyLabel.setText("Wrong verify code!! Please try again");
        }
    }
    
    public void backtbtnpushed(ActionEvent event) throws IOException
    {
         Parent tableViewParent = FXMLLoader.load(getClass().getResource("customer_login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }

    

}
