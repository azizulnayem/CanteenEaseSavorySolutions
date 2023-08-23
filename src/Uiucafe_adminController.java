/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.awt.Desktop;
import java.awt.Image;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 88017
 */
public class Uiucafe_adminController implements Initializable {

    @FXML private Button see_order;
    @FXML private Button clear_order;
      @FXML private Button logout;
          @FXML private Label order_clear;
    
      
    
    
 public void initialize(URL url, ResourceBundle rb) {
               
    } 
    
    
    
    
      
  public void see_orderbtnpushed(ActionEvent event) throws IOException
    {
       try  
      {  
     //constructor of file class having file as argument  
     File file = new File("C:\\Users\\DCL\\OneDrive\\Documents\\NetBeansProjects\\CanteenManage\\uiucafe_order.txt");   
    if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
     {  
      System.out.println("not supported");  
      return;  
      }  
     Desktop desktop = Desktop.getDesktop();  
     if(file.exists())         //checks file exists or not  
     desktop.open(file); 
     else
              System.out.println("file not exist");//opens the specified file  
     }  
     catch(Exception e)  
     {  
       e.printStackTrace();  
     } 
    }
        
        
        
        
        
        
        
 public void clearbtnpushed(ActionEvent event) throws IOException
    {
        File file3=new File("C:\\Users\\DCL\\OneDrive\\Documents\\NetBeansProjects\\CanteenManage\\uiucafe_order.txt");
              System.out.println("clearde"); 
               if(!file3.exists()){
                   file3.createNewFile();
                }
               FileWriter fw=new FileWriter(file3,false);
                order_clear.setText("Order cleared !!!");
               
               //order_clear.setText("Order cleared");
    }
     
     
      public void logoutbtnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("Homepage.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
}
