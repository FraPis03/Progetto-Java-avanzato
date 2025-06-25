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
 * FXML Controller class
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
    private ChoiceBox<?> tipoClassificaChoiceBox;
    @FXML
    private ChoiceBox<?> difficoltaChoiceBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        userDB=new UtenteJDBC();
        
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
    
    public void utenteClassifica(){
        this.user=MainViewController.getUtente();
        
        System.out.println(user.getNomeUtente());
        
        List<Punteggi> punteggiUtente=userDB.punteggiUtente(user.getNomeUtente());
        
        System.out.println(punteggiUtente);

        ObservableList<ObservableList<String>> punteggi=FXCollections.observableArrayList();

    for (int i = 0; i < punteggiUtente.size(); i++) {
        ObservableList<String> riga = FXCollections.observableArrayList();
        riga.add(user.getNomeUtente());            
        riga.add(String.valueOf(punteggiUtente.get(i).getPunteggio()));            
        riga.add(String.valueOf(punteggiUtente.get(i).getTempo()));          
        punteggi.add(riga);
    }
        
        classificaTable.setItems(punteggi);

    }
    
}
