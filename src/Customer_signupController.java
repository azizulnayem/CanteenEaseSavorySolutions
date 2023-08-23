/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 88017
 */
public class Customer_signupController implements Initializable {
    
     @FXML private Button backbtn;
     @FXML private Button signupbtn;
     
      @FXML private TextField id;
      @FXML private TextField name;
       
        @FXML private TextField mailpassword;
        @FXML private PasswordField canteenpassword;
        @FXML private PasswordField confirmPassword;
     
      @FXML private Label message;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        message.setVisible(false);
        // TODO
    }  
    
    
     public void backbtnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("customer_login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
    public void signupbtnpushed(ActionEvent event) throws IOException
    {
        String idtext=id.getText();
         String nametext=name.getText();
         String varsitypasstext=mailpassword.getText();
         String canteenpasstext=canteenpassword.getText();
         String confirmPasswordText = confirmPassword.getText();
         
          if (!canteenpasstext.equals(confirmPasswordText)) {
            message.setVisible(true);
            message.setText("Passwords do not match.");
            message.setStyle("-fx-text-fill: #FF0000;");
            return;
        }
          
        File file=new File("varsity_database.txt");
        Scanner fsc =new Scanner(file);
        int flag1=0,flag2=0;
        while(fsc.hasNext()){
            String line=fsc.nextLine();
            if(!line.equals(""))
            {
                 String[] arr=line.split("#");
                 if( idtext.equals(arr[0])   &&  nametext.equals(arr[1])  &&  varsitypasstext.equals(arr[2]) )
                 {
                     flag1=1;
                     break;
                 }
            }
           
         
            
        }
        
        if(flag1==0)
        {
            message.setVisible(true);
            message.setText("Wrong info !!! Please provide correct info");
            message.setStyle("-fx-text-fill: #FF0000;");
            
        }
        else
            
        {
             
           File file2=new File("customer_info.txt");
           Scanner fsc2 =new Scanner(file2);
        
           while(fsc2.hasNext()){
            String line=fsc2.nextLine();
            if(!line.equals(""))
            {
                 String[] arr=line.split("#");
                 if( idtext.equals(arr[0])   &&  nametext.equals(arr[1])  )
                 {
                     flag2=1;
                     break;
                 }
            }
           
         
            
          }
        
          if(flag2==1)
          { 
            message.setVisible(true);
            message.setText("You are already Registered !!!");
            message.setStyle("-fx-text-fill: #FFFFFF;");
          }
          else
          {
             
        
               File file3=new File("customer_info.txt");
               
               if(!file3.exists()){
                   file3.createNewFile();
                }
               FileWriter fw=new FileWriter(file3,true); // the true indicates append mode
               BufferedWriter bw=new BufferedWriter(fw);
               String toWrite="\n"+idtext+"#"+nametext+"#"+canteenpasstext+"\n";
               bw.write(toWrite);
              bw.close();
              message.setVisible(true);
              message.setText("Congratualtions !!! Registration complete");
              message.setStyle("-fx-text-fill: #00FF00;");
          }
        
        
        
        
        }
         
        
        
        
        
      }
         
         
    
    
}
