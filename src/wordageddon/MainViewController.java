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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
 * Controller della vista principale dell'applicazione Wordageddon.
 * 
 * Gestisce:
 * - La selezione della difficoltà e della lingua.
 * - L'accesso alla modalità Gioco.
 * - L'accesso alla Classifica.
 * - L'accesso alla sezione Admin (se l'utente è amministratore).
 * 
 * @author antoniobellofatto
 */
public class MainViewController implements Initializable {
    
    static Utente user;
    
    UtenteJDBC userDB;
    
    private static String difficoltaSelezionata;
    
    private static String linguaSelezionata;
    
    @FXML
    private Button bottoneGioca;
    @FXML
    private Label etichettaDifficolta;
    @FXML
    private ChoiceBox<String> sceltaDifficolta;
    @FXML
    private Button bottoneAdmin;
    @FXML
    private Button bottoneClassifica;
    @FXML
    private ChoiceBox<String> sceltaLingua;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sceltaDifficolta.getItems().addAll("Facile", "Medio", "Difficile");
        sceltaLingua.getItems().addAll("IT", "EN", "ESP","FR");
        userDB=new UtenteJDBC();
        
        sceltaDifficolta.setOnAction(e->{
            difficoltaSelezionata=sceltaDifficolta.getValue();
        });
        
        sceltaLingua.setOnAction(e->{
            linguaSelezionata=sceltaLingua.getValue();
        });
        
        if(user!=null) this.setAdminButton();
       
        sceltaDifficolta.setValue("Facile");
        sceltaLingua.setValue("IT");
        
        bottoneClassifica.setOnAction(e->{
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassificaView.fxml"));
            Parent root;
            root = loader.load();
            
            Stage stage = (Stage) bottoneGioca.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Classifica");
            stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        bottoneAdmin.setOnAction(e->{
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
            Parent root;
            try {
                root = loader.load();
                AdminController controller = loader.getController();
            controller.setAmministratore(user);

            // Ottieni lo stage corrente
            Stage stage = (Stage) bottoneAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Wordageddon");
            stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        bottoneGioca.setOnAction(e->{   
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LectureView.fxml"));
            Parent root;
            root = loader.load();
            
            LectureViewController controller = loader.getController();
            controller.difficolta();
            
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

    /**
    * Imposta l'utente attualmente loggato e abilita/disabilita il bottone admin.
    * 
    * @param u Oggetto Utente da impostare come loggato.
    */
    public void setUtenteLoggato(Utente u){
        user=u;
        this.setAdminButton();
    }    

    /**
     * Restituisce l'utente attualmente loggato.
     * 
     * @return Oggetto Utente corrente.
     */
    public static Utente getUtente(){
        return user;
    }

    /**
    * Restituisce la difficoltà selezionata dall'utente.
    * 
    * @return Stringa contenente la difficoltà ("Facile", "Medio", "Difficile").
    */
    public static String getDifficolta(){
        return difficoltaSelezionata;
    }

    /**
    * Restituisce la lingua selezionata dall'utente.
    * 
    * @return Codice lingua selezionata ("IT", "EN", "ESP", "FR").
    */
    public static String getLingua(){
        return linguaSelezionata;
    }

    /**
    * Controlla se l'utente ha ruolo di amministratore.
    * 
    * Se non è admin, disabilita il bottone per accedere alla vista Admin.
    */
    public void setAdminButton(){
        if(!userDB.getRuolo(user.getNomeUtente()).contentEquals("admin")) bottoneAdmin.setDisable(true);
    }
}
