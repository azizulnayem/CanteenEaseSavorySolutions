/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OlympiaController implements Initializable {

    @FXML private Button place_order;
    @FXML private Button confirm_order;
    @FXML private Label total_amount;
    @FXML private Label confirm_status;
     @FXML private ImageView bkash;
     @FXML private TextField bkash_number;
      @FXML private TextField bkash_password;
      
       @FXML private TextField roll_quantity;
        @FXML private TextField burger_quantity;
         @FXML private TextField tea_quantity;
      
      @FXML private Label roll_price;
      @FXML private Label burger_price;
      @FXML private Label tea_price;
    
    
    public void initialize(URL url, ResourceBundle rb) {
        confirm_order.setVisible(false);
        total_amount.setVisible(false);
        confirm_status.setVisible(false);
        bkash.setVisible(false);
        bkash_number.setVisible(false); 
        bkash_password.setVisible(false); 
        
       
       //Scanner fsc;
        try {
         File file=new File("roll_price.txt");   
         Scanner   fsc = new Scanner(file);
         String roll=fsc.nextLine();
         roll_price.setText("price: "+roll+" per piece");
         fsc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OlympiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         //Scanner fsc;
        try {
         File file=new File("burger_price.txt");   
         Scanner   fsc = new Scanner(file);
         String burger=fsc.nextLine();
         burger_price.setText("price: "+burger+" per piece");
         fsc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OlympiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
         //Scanner fsc;
        try {
         File file=new File("tea_price.txt");   
         Scanner   fsc = new Scanner(file);
         String tea=fsc.nextLine();
         tea_price.setText("price: "+tea+" per cup");
         fsc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OlympiaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    } 
    
    
      public void place_orderpushed(ActionEvent event) throws IOException
    {  try{
        int p1,p2,p3;
        
        if( roll_quantity.getText().equals("")    )
            p1=0;
        else
             p1=Integer.parseInt(roll_quantity.getText());
        
        
          if( burger_quantity.getText().equals("")    )
            p2=0;
        else
             p2=Integer.parseInt(burger_quantity.getText());
          if( tea_quantity.getText().equals("")    )
            p3=0;
        else
            p3=Integer.parseInt(tea_quantity.getText());
          
        int amount=p1*20+p2*50+p3*10;
        if(amount==0)
        {
            total_amount.setVisible(true);
          total_amount.setText("Please insert quantity");
        }
        else
            
        {
             confirm_order.setVisible(true);
        total_amount.setVisible(true);
        bkash.setVisible(true);
        bkash_number.setVisible(true); 
        bkash_password.setVisible(true);
      
        total_amount.setText("Total amount: "+amount);
            
        }
       
      }
      catch(Exception e){
          total_amount.setVisible(true);
          total_amount.setText("Quantity should be a number");
      }
        
      
    }
      
      
      
    public void confirm_btnpushed(ActionEvent event) throws Exception
    {   
       String mobile_number=bkash_number.getText();
       String bkash_pin=bkash_password.getText();
        
        
        File file=new File("bkash.txt");
       Scanner fsc =new Scanner(file);
      
       
        int flag=0;
        while(fsc.hasNext()){
            String line=fsc.nextLine();
            if(!line.equals(""))
            {
                 String[] arr=line.split("#");
                 
                if( mobile_number.equals(arr[0])   &&  bkash_pin.equals(arr[1])  )
                {
                    
                    flag=1;
                    break;
                }
                
            }
            
         }
        if(flag==1)
        {  
            confirm_status.setVisible(true);
            int min = 200;  
             int          max = 400; 
            int b = (int)(Math.random()*(max-min+1)+min);  
            confirm_status.setText("Congratulations! Payment is succesfull \n your order collection code: "+b); 
               
            
             int p1,p2,p3;
        
             if( roll_quantity.getText().equals("")    )
             p1=0;
             else
             p1=Integer.parseInt(roll_quantity.getText());
        
        
            if( burger_quantity.getText().equals("")    )
            p2=0;
            else
             p2=Integer.parseInt(burger_quantity.getText());
           if( tea_quantity.getText().equals("")    )
            p3=0;
           else
            p3=Integer.parseInt(tea_quantity.getText());
            
            
            
               File file3=new File("olympia_order.txt");
               
               if(!file3.exists()){
                   file3.createNewFile();
                }
               FileWriter fw=new FileWriter(file3,true); // the true indicates append mode
               BufferedWriter bw=new BufferedWriter(fw);
               String toWrite="Chicken roll "+p1+",Burger "+p2+", green tea "+p3+", order code:: "+b+" mobile: "+mobile_number+"\n";
               bw.write(toWrite);
               bw.close();
            
            
            
            
            
            
        }
        else
        {   
           confirm_status.setVisible(true);
            confirm_status.setText("Wrong Bkash info ! Please try again");
        }
            
            
            
    }  
    
    
    
    
    
    
     public void backbtnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("cafelist.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
     
     
      public void logoutbtnpushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("homepage.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
    
}
