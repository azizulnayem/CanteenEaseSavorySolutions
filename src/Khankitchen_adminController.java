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
public class Khankitchen_adminController implements Initializable {

    @FXML private Button see_order;
    @FXML private Button clear_order;
      @FXML private Button logout;
      @FXML private Label order_clear;
        @FXML private Label warning;
      
      @FXML private TextField pizza_new_price;
      @FXML private TextField sandwich_new_price;
      @FXML private TextField tea_new_price;
    
    
      
    
    
 public void initialize(URL url, ResourceBundle rb) {
      warning.setVisible(false);
               
    } 
    
    
    
    
      
  public void see_orderbtnpushed(ActionEvent event) throws IOException
    {
       try  
      {  
     //constructor of file class having file as argument  
     File file = new File("C:\\Users\\DCL\\OneDrive\\Documents\\NetBeansProjects\\CanteenManage\\khan_kitchen_order.txt");   
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
       try{ File file3=new File("C:\\Users\\DCL\\OneDrive\\Documents\\NetBeansProjects\\CanteenManage\\khan_kitchen_order.txt");
              
               if(!file3.exists()){
                   file3.createNewFile();
                }
               FileWriter fw=new FileWriter(file3,false);
               
               order_clear.setText("Order cleared !!!");
       }
       catch(Exception e)
       {
           System.out.println("Exception found");
       }
               
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
      
   public void updatepricebtnpushed(ActionEvent event) throws IOException
    {
      try{
        int p1,p2,p3;
        
        if( pizza_new_price.getText().equals("")    )
            p1=0;
        else
             p1=Integer.parseInt(pizza_new_price.getText());
        
        
          if( sandwich_new_price.getText().equals("")    )
            p2=0;
        else
             p2=Integer.parseInt(sandwich_new_price.getText());
          if( tea_new_price.getText().equals("")    )
            p3=0;
        else
            p3=Integer.parseInt(tea_new_price.getText());
          
          
          
          
          
          
               File file1=new File("pizza_price.txt");
               
               if(!file1.exists()){
                   file1.createNewFile();
                }
               FileWriter fw=new FileWriter(file1,false); // the true indicates append mode
               BufferedWriter bw=new BufferedWriter(fw);
               if(! pizza_new_price.getText().equals("")    )
               bw.write(pizza_new_price.getText());
               bw.close();
               fw.close();
               
               
               File file2=new File("sandwich_price.txt");
               
               if(!file2.exists()){
                   file2.createNewFile();
                }
                fw=new FileWriter(file2,false); // the true indicates append mode
                bw=new BufferedWriter(fw);
               if(! sandwich_new_price.getText().equals("")    )
               bw.write(sandwich_new_price.getText());
               bw.close();
               
                File file3=new File("tea_price.txt");
               
               if(!file3.exists()){
                   file3.createNewFile();
                }
                fw=new FileWriter(file3,false); // the true indicates append mode
                bw=new BufferedWriter(fw);
               if(! tea_new_price.getText().equals("")    )
               bw.write(tea_new_price.getText());
               bw.close();
               
                warning.setVisible(true);
               warning.setText("Updated");
               
               
             
         
          
        
       
      
       
      }
      catch(Exception e){
          warning.setVisible(true);
          warning.setText("price should be a number !!!");
      }
        
    }
    
}
