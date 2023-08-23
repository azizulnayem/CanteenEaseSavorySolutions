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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 88017
 */
public class Admin_loginController implements Initializable {
 @FXML private TextField id;
     @FXML private PasswordField password;
     
     @FXML private Button proceed;
     @FXML private Button signbtn;
     @FXML private Button backbtn;
     @FXML private Button forgetbtn;
      
    
       
       
     @FXML private Label forgetlabel;
     //@FXML private Label welcomelabel;
     @FXML private Label customerlogin;
     
            

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //welcomelabel.setVisible(false);
          proceed.setVisible(false);
          forgetlabel.setVisible(false);
        // TODO
    }    
    
    
    public void backbtnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
     public void signupbtnnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("admin_signup.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    
    
    
    public void signinbtnpushed(ActionEvent event) throws Exception
    {   
       String idtext=id.getText();
       String passwordtext=password.getText();
       String name=""; 
        
       File file=new File("admin_info.txt");
       Scanner fsc =new Scanner(file);
       
        int flag=0;
        while(fsc.hasNext()){
            String line=fsc.nextLine();
            if(!line.equals(""))
            {
                 String[] arr=line.split("#");
                if( idtext.equals(arr[0])   &&  passwordtext.equals(arr[2])  )
                {
                    name=arr[1];
                    flag=1;
                    break;
                }
                
            }
            
         }
        if(flag==1)
        {  
             Parent tableViewParent = FXMLLoader.load(getClass().getResource("cafelist.fxml"));
             if(idtext.equals("111"))   
             tableViewParent = FXMLLoader.load(getClass().getResource("olympia_admin.fxml"));
             else if(idtext.equals("222"))
                 tableViewParent = FXMLLoader.load(getClass().getResource("uiucafe_admin.fxml"));
              else if(idtext.equals("333"))
                 tableViewParent = FXMLLoader.load(getClass().getResource("khankitchen_admin.fxml"));  
                 
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show(); 
                
            
        }
        else
        {   
            forgetlabel.setVisible(true);
            forgetlabel.setText("Wrong login info ! Please try again");
        }
            
            
            
    }
    
    
    
    public void forgetbtnpushed(ActionEvent event) throws IOException
    {
         Parent tableViewParent = FXMLLoader.load(getClass().getResource("AdminForgetPass.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
       
    
}
