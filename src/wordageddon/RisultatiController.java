/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RisultatiController implements Initializable {
    
    Utente user;

    UtenteJDBC userDB;
    
    @FXML
    private Label labelPunteggio;

    @FXML
    private TableView<ObservableList<String>> tableErrori;

    @FXML
    private TableColumn<ObservableList<String>, String> colNumeroDomanda;

    @FXML
    private TableColumn<ObservableList<String>, String> colRispostaUtente;

    @FXML
    private TableColumn<ObservableList<String>, String> colRispostaCorretta;
    @FXML
    private Button btnTornaMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userDB=new UtenteJDBC();
        // TODO
        colNumeroDomanda.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(0))
    );
    colRispostaUtente.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(1))
    );
    colRispostaCorretta.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(2))
    );
    
    btnTornaMenu.setOnAction(e->{
        try {
            //Cambio la schermata se l'inserimento va a buon fine
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();

            // Ottieni lo stage corrente
            Stage stage = (Stage) btnTornaMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Wordageddon");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    });
    }    
    
    public void setRisultati(List<String> risposteUtente,List<String> risposteCorrette){
    this.user=MainViewController.getUtente();
    ObservableList<ObservableList<String>> dati = FXCollections.observableArrayList();
    
    for (int i = 0; i < risposteUtente.size(); i++) {
        ObservableList<String> riga = FXCollections.observableArrayList();
        riga.add(String.valueOf(i + 1));            
        riga.add(risposteUtente.get(i));            
        riga.add(risposteCorrette.get(i));          
        dati.add(riga);
    }

    tableErrori.setItems(dati);
    labelPunteggio.setText("Punteggio: " + risposteUtente.size());
    int punteggio = 5;
    userDB.aggiornaPunteggio(user, punteggio);
    }
}
