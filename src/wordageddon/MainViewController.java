/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antoniobellofatto
 */
public class MainViewController implements Initializable {
    
    Utente user;

    @FXML
    private Button bottoneGioca;
    @FXML
    private Label etichettaDifficolta;
    @FXML
    private ChoiceBox<String> sceltaDifficolta;
    @FXML
    private Button bottoneStorico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sceltaDifficolta.getItems().addAll("Facile", "Medio", "Difficile");
        sceltaDifficolta.setValue("Facile");
        
        bottoneGioca.setOnAction(e->{
            String difficolta=sceltaDifficolta.getValue();
            
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LectureView.fxml"));
            Parent root;
            root = loader.load();
             // Ottieni lo stage corrente
            Stage stage = (Stage) bottoneGioca.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lecture");
            stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void setUtenteLoggato(Utente u){
        user=u;
    }    
    
}
