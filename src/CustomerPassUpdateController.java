
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
 * @author Dulal
 */
public class CustomerPassUpdateController implements Initializable {

    @FXML
    private Label checkLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField idTf;

    @FXML
    private PasswordField newPassPf;

    @FXML
    private Button updatePassBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        messageLabel.setVisible(false);
        checkLabel.setVisible(false);
        newPassPf.setVisible(false);
        updatePassBtn.setVisible(false);

    }

    String idtext;

    public void backbtnpushed(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("customer_login.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        //This line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

//    String id;
//
//    public void fun(String id) {
//        this.id = id;
//    }

//     public void changeSceneToDetailedPersonView(ActionEvent event) throws IOException
//    {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("CustomerForgetPass.fxml"));
//        Parent tableViewParent = loader.load();
//        
//        Scene tableViewScene = new Scene(tableViewParent);
//        
//        //access the controller and call a method
//        //CustomerForgetPassController controller = loader.getController();
//        //controller.fun(tableView.getSelectionModel().getSelectedItem());
//        
//        //This line gets the Stage information
//        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//        
//        window.setScene(tableViewScene);
//        window.show();
//    }
    public void checkbtnpushed(ActionEvent event) throws IOException {
        idtext = idTf.getText();

        File file = new File("customer_info.txt");
        Scanner fsc = new Scanner(file);
        int flag1 = 0;
        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (idtext.equals(arr[0])) {
                    flag1 = 1;
                    break;
                }
            }

        }

        if (flag1 == 0) {
            checkLabel.setVisible(true);
            checkLabel.setText("Your id is not found!! Please enter correct Id");

        } else {
            messageLabel.setVisible(true);
            newPassPf.setVisible(true);
            updatePassBtn.setVisible(true);
        }

    }

    public void updatebtnpushed(ActionEvent event) throws IOException {
        
        idtext = idTf.getText();

       String passupdate=newPassPf.getText();
       
       ArrayList<String> temArray= new ArrayList<>();
       
       File file = new File("customer_info.txt");
        Scanner fsc = new Scanner(file);
        
        while (fsc.hasNext()) {
            String line = fsc.nextLine();
            if (!line.equals("")) {
                String[] arr = line.split("#");
                if (idtext.equals(arr[0])) {
                    
                    temArray.add(arr[0]+"#"+arr[1]+"#"+passupdate);
                    //System.out.println(arr[0]+"#"+arr[1]+"#"+passupdate);
    
                }
                else{
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
        
        for(int i=0;i<temArray.size();i++){
            bw.write(temArray.get(i)+"\n");
        }
       
        bw.close();
        
        messageLabel.setVisible(true);
        messageLabel.setText("Your password Update Successfully");
//        idtext = nameTf.getText();
//
//        File file = new File("customer_info.txt");
//        File file1 = new File("temp.txt");
//        Scanner fsc = new Scanner(file);
//        int flag1 = 0;
//        while (fsc.hasNext()) {
//            String line = fsc.nextLine();
//            if (!line.equals("")) {
//                String[] arr = line.split("#");
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//                FileWriter fw = new FileWriter(file, true); // the true indicates append mode
//                BufferedWriter bw = new BufferedWriter(fw);
//                String toWrite = x + "#" + "\n";
//                bw.write(toWrite);
//                bw.close();
//
////                if (idtext.equals(arr[1])) {
////                    flag1 = 1;
////                    break;
////                }
//            }
//
//        }
    }

}
