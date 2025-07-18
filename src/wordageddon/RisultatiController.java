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

/**
 * Controller della schermata dei risultati di gioco.
 * Mostra il punteggio finale, la tabella con gli errori e permette di tornare al menu principale.
 * 
 * Implementa l'interfaccia Initializable per inizializzare i componenti al caricamento.
 */
public class RisultatiController implements Initializable {
    
    Utente user;

    UtenteJDBC userDB;
    
    @FXML
    private Label labelPunteggio;

    @FXML
    private TableView<ObservableList<String>> tableErrori;

    @FXML
    private TableColumn<ObservableList<String>, String> colDomanda;

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
        colDomanda.setCellValueFactory(cellData -> 
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

    /**
     * Imposta i risultati nella schermata e aggiorna il punteggio nel database.
     * 
     * @param risposteUtente Lista delle risposte fornite dall'utente
     * @param risposteCorrette Lista delle risposte corrette
     */
    public void setRisultati(List<String> risposteUtente,List<String> risposteCorrette){
    this.user=MainViewController.getUtente();
    ObservableList<ObservableList<String>> dati = FXCollections.observableArrayList();
    
    for (int i = 0; i < risposteUtente.size(); i++) {
        ObservableList<String> riga = FXCollections.observableArrayList();
        riga.add(String.valueOf(i+1));            
        riga.add(risposteUtente.get(i));            
        riga.add(risposteCorrette.get(i));          
        dati.add(riga);
    }

    tableErrori.setItems(dati);
    int punteggio = this.calcolaPunteggio(risposteUtente, risposteCorrette);
    labelPunteggio.setText("Punteggio: " + punteggio);
    userDB.aggiornaPunteggio(user, punteggio,MainViewController.getDifficolta(),MainViewController.getLingua());
    }

    /**
     * Calcola il punteggio finale confrontando le risposte dell'utente con quelle corrette.
     * Ogni risposta corretta vale 10 punti.
     * 
     * @param risposteUtente Lista delle risposte fornite dall'utente
     * @param risposteCorrette Lista delle risposte corrette
     * @return Punteggio totale calcolato
     */
    public int calcolaPunteggio(List<String> risposteUtente,List<String> risposteCorrette){
        int punteggio=0;
        for(int i=0;i<risposteUtente.size();i++){
            if(risposteUtente.get(i).contentEquals(risposteCorrette.get(i))) punteggio+=10;
        }
        
        return punteggio;
    }
}
