/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller della vista Classifica del gioco Wordageddon.
 * Gestisce la visualizzazione delle classifiche punteggi dei giocatori, sia globali che personali,
 * filtrate per lingua e difficoltà.
 * 
 * Permette all'utente di visualizzare:
 * - la classifica personale ("Singolo") o quella globale ("Globale")
 * - i punteggi in base alla difficoltà selezionata (Facile, Medio, Difficile)
 * - i punteggi in base alla lingua della partita (IT, EN, ESP, FR)
 * 
 * Inoltre mostra le statistiche personali dell'utente: miglior punteggio e punteggio medio.
 * 
 * @author antoniobellofatto
 */
public class ClassificaViewController implements Initializable {
    
    UtenteJDBC userDB;
    
    Utente user;

    @FXML
    private TableView<ObservableList<String>> classificaTable;
    @FXML
    private TableColumn<ObservableList<String>, String> colUtente;
    @FXML
    private TableColumn<ObservableList<String>, String> colPunteggio;
    @FXML
    private TableColumn<ObservableList<String>, String> colDataOra;
    @FXML
    private Label titoloLabel;
    @FXML
    private Button indietroButton;
    @FXML
    private ChoiceBox<String> tipoClassificaChoiceBox;
    @FXML
    private ChoiceBox<String> difficoltaChoiceBox;
    @FXML
    private Label labelMigliorPunteggio;
    @FXML
    private Label labelPunteggioMedio;
    @FXML
    private ChoiceBox<String> linguaChoiceBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userDB=new UtenteJDBC();
    tipoClassificaChoiceBox.getItems().addAll("Singolo", "Globale");
    difficoltaChoiceBox.getItems().addAll("Facile", "Medio", "Difficile");
    linguaChoiceBox.getItems().addAll("IT","EN","ESP","FR");

    tipoClassificaChoiceBox.setOnAction(e -> aggiornaClassifica());
    difficoltaChoiceBox.setOnAction(e -> aggiornaClassifica());
    linguaChoiceBox.setOnAction(e -> aggiornaClassifica());

    tipoClassificaChoiceBox.setValue("Singolo");
    difficoltaChoiceBox.setValue("Facile");
    linguaChoiceBox.setValue("IT");
    aggiornaClassifica();
        colUtente.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(0))
    );
    colPunteggio.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(1))
    );
    colDataOra.setCellValueFactory(cellData -> 
        new ReadOnlyStringWrapper(cellData.getValue().get(2))
    );
    
       indietroButton.setOnAction(e->{
           try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root;
            root = loader.load();
             
            Stage stage = (Stage) indietroButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Wordageddon");
            stage.show();
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
       });
    }   

    /**
     * Carica e visualizza la classifica per il singolo utente.
     * 
     * @param difficolta difficoltà della partita (Facile, Medio, Difficile)
     * @param lingua lingua della partita (IT, EN, ESP, FR)
     */
    public void utenteSingoloClassifica(String difficolta, String lingua){
        this.user=MainViewController.getUtente();
    
        List<Punteggi> punteggiUtente=userDB.punteggiUtente(user.getNomeUtente(),difficolta,lingua);

        ObservableList<ObservableList<String>> punteggi=FXCollections.observableArrayList();

    for (int i = 0; i < punteggiUtente.size(); i++) {
        ObservableList<String> riga = FXCollections.observableArrayList();
        riga.add(user.getNomeUtente());            
        riga.add(String.valueOf(punteggiUtente.get(i).getPunteggio()));            
        riga.add(String.valueOf(punteggiUtente.get(i).getTempo()));
        riga.add(difficolta);
        punteggi.add(riga);
    }
        
        classificaTable.setItems(punteggi);
        this.setStatistiche(difficolta,lingua);

    }

    /**
     * Carica e visualizza la classifica globale per tutti gli utenti.
     * 
     * @param difficolta difficoltà della partita (Facile, Medio, Difficile)
     * @param lingua lingua della partita (IT, EN, ESP, FR)
     */
    public void utenteGlobaleClassifica(String difficolta, String lingua){
        this.user=MainViewController.getUtente();
        
        Map<String,Punteggi> punteggiGlobali=userDB.punteggiGlobali(difficolta,lingua);

        ObservableList<ObservableList<String>> punteggi=FXCollections.observableArrayList();

    for (Map.Entry<String,Punteggi> m:punteggiGlobali.entrySet()) {
        ObservableList<String> riga = FXCollections.observableArrayList();
        riga.add(m.getKey());            
        riga.add(String.valueOf(m.getValue().getPunteggio()));            
        riga.add(String.valueOf(m.getValue().getTempo()));    
        punteggi.add(riga);
    }
        
        classificaTable.setItems(punteggi);
        this.setStatistiche(difficolta,lingua);
    }

    /**
     * Aggiorna la classifica in base ai valori selezionati nelle choicebox.
     */
    private void aggiornaClassifica() {
    String tipo = tipoClassificaChoiceBox.getValue();
    String difficolta = difficoltaChoiceBox.getValue();
    String lingua=linguaChoiceBox.getValue();

    if (tipo == null || difficolta == null || lingua==null) return;

    if (tipo.equals("Singolo")) {
        utenteSingoloClassifica(difficolta,lingua);
    } else if (tipo.equals("Globale")) {
        utenteGlobaleClassifica(difficolta,lingua);
    }
}

    /**
     * Imposta le etichette delle statistiche personali dell'utente.
     * 
     * @param difficolta difficoltà della partita
     * @param lingua lingua della partita
     */
    public void setStatistiche(String difficolta,String lingua){
        List<Integer> statistiche=userDB.statistichePunteggio(user.getNomeUtente(), difficolta,lingua);
        labelMigliorPunteggio.setText("Miglior punteggio: "+String.valueOf(statistiche.get(0)));
        labelPunteggioMedio.setText("Media punteggi: "+String.valueOf(statistiche.get(1)));
    }
}
