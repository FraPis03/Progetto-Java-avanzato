/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Francesco
 */
public class WordageddonController implements Initializable {
    
    UtenteJDBC u;

    @FXML 
    private Label messaggioUsername;
    @FXML
    private HBox contenitoreBottoniIniziali;
    @FXML
    private Button bottoneAccedi;
    @FXML
    private Button bottoneRegistrati;
    @FXML
    private VBox boxAccesso;
    @FXML
    private TextField campoUsernameAccesso;
    @FXML
    private PasswordField campoPasswordAccesso;
    @FXML
    private Button bottoneAnnullaAccesso;
    @FXML
    private Button bottoneConfermaAccesso;
    @FXML
    private VBox boxRegistrazione;
    @FXML
    private TextField campoUsernameRegistrazione;
    @FXML
    private TextField campoEmailRegistrazione;
    @FXML
    private PasswordField campoPasswordRegistrazione;
    @FXML
    private PasswordField campoConfermaPasswordRegistrazione;
    @FXML
    private Button bottoneAnnullaRegistrazione;
    @FXML
    private Button bottoneConfermaRegistrazione;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        u=new UtenteJDBC();
        
        //Faccio un binding per notficare all'utente se il nome è già presente nel db
        campoUsernameRegistrazione.textProperty().addListener((observable, oldValue, newValue) -> {
                
        if (u.checkNomeUtente(newValue)) {
            messaggioUsername.setText("Nome utente già esistente");
            messaggioUsername.setStyle("-fx-text-fill: red;");
        } else {
            messaggioUsername.setText("Nome utente disponibile");
            messaggioUsername.setStyle("-fx-text-fill: green;");
        }
    });
    
        
        //vai alla schermata di accesso
        bottoneAccedi.setOnAction(e -> {
            contenitoreBottoniIniziali.setVisible(false);
            boxRegistrazione.setVisible(false);
            boxAccesso.setVisible(true);
        });

        //schermata registrazione
        bottoneRegistrati.setOnAction(e -> {
            contenitoreBottoniIniziali.setVisible(false);
            boxAccesso.setVisible(false);
            boxRegistrazione.setVisible(true);
        });

        //torna alla schermata iniziale
        bottoneAnnullaAccesso.setOnAction(e -> {
            boxAccesso.setVisible(false);
            contenitoreBottoniIniziali.setVisible(true);
        });
        
        //torna alla schermata iniziale
        bottoneAnnullaRegistrazione.setOnAction(e->{
            contenitoreBottoniIniziali.setVisible(true);
            boxRegistrazione.setVisible(false);
        });
        
        bottoneConfermaAccesso.setOnAction(e->{
           
            String nomeUtente=campoUsernameAccesso.getText();
            String password=campoPasswordAccesso.getText();
            
            if (u.checkCredenziali(nomeUtente, password)) {
        try {
            if(u.getRuolo(nomeUtente).contentEquals("Utente")){//controllo se l utente è un admin oppure no
            //Cambio la schermata se le credenziali sono corrette
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            
            MainViewController controller = loader.getController();
            controller.setUtenteLoggato(new Utente(nomeUtente,password,""));

            // Ottieni lo stage corrente
            Stage stage = (Stage) bottoneConfermaAccesso.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Wordageddon");
            stage.show();
            }
            else{
                //Cambio la schermata se le credenziali sono corrette
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminView.fxml"));
            Parent root = loader.load();
            
            AdminController controller = loader.getController();
            controller.setAmministratore(new Utente(nomeUtente,password,""));

            // Ottieni lo stage corrente
            Stage stage = (Stage) bottoneConfermaAccesso.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Wordageddon");
            stage.show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } else {
        //mostro un alert di errore
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore di accesso");
        alert.setHeaderText(null);
        alert.setContentText("Username o password non validi.");
        alert.showAndWait();
    }
        });
        
        bottoneConfermaRegistrazione.setOnAction(e->{
            if(campoPasswordRegistrazione.getText().contentEquals(campoConfermaPasswordRegistrazione.getText())){
            Utente user=new Utente(campoUsernameRegistrazione.getText(),campoPasswordRegistrazione.getText(),campoEmailRegistrazione.getText());
            if(user.checkUtente()){       
            if (u.inserisciUtente(user)) {
            try {
            //Cambio la schermata se l'inserimento va a buon fine
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            // Ottieni lo stage corrente
            Stage stage = (Stage) bottoneConfermaAccesso.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Wordageddon");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } else {
        //mostro un alert di errore
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore nell inserimento");
        alert.setHeaderText(null);
        alert.setContentText("Username o password non validi.");
        alert.showAndWait();
    }
    }
    else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore nell inserimento");
        alert.setHeaderText(null);
        alert.setContentText("Username deve avere i caratteri compresi tra 3 e 20\nPassword deve avere i caratteri compresi tra 5 e 20\nEmail deve avere @ e .\n");
        alert.showAndWait();
    }
            }
            else{
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore nell inserimento");
        alert.setHeaderText(null);
        alert.setContentText("Le password non combaciano");
        alert.showAndWait();
    }
        });
    }    
}    
    

