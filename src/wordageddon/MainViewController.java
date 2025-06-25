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
    
    static Utente user;

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
        
        
        bottoneStorico.setOnAction(e->{
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassificaView.fxml"));
            Parent root;
            root = loader.load();
            
            ClassificaViewController controller = loader.getController();
            controller.utenteClassifica();
            
             // Ottieni lo stage corrente
             
            Stage stage = (Stage) bottoneGioca.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Classifica");
            stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        bottoneGioca.setOnAction(e->{
            String difficolta=sceltaDifficolta.getValue();
            int difficolta1=0;
            switch (difficolta){
                case "Facile": {
                    difficolta1=3;
                    break;
                }
                case "Medio": {
                    difficolta1=5;
                    break;
                }             
                case "Difficile":{
                    difficolta1=6;
                    break;
                }
            }
            
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LectureView.fxml"));
            Parent root;
            root = loader.load();
            
            LectureViewController controller = loader.getController();
            System.out.println("la difficoltà vale: "+difficolta1);
            controller.difficolta(difficolta1);
            
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
    
    public static Utente getUtente(){
        return user;
    }
}
