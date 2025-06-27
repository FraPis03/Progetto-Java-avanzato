/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antoniobellofatto
 */
public class AdminController implements Initializable {
    
    UtenteJDBC u;
    
    AmministratoreJDBC adminDB;
    
    Amministratore admin;

    @FXML
    private Label titoloLabel;
    @FXML
    private Separator separatoreCentrale;
    @FXML
    private Button bottoneIndietro;
    @FXML
    private StackPane areaTrascinamento;
    @FXML
    private Text testoTrascina;
    @FXML
    private Label etichettaUpload;
    @FXML
    private Label etichettaOppure;
    @FXML
    private Button bottoneSfoglia;
    @FXML
    private Label etichettaStopwords;
    @FXML
    private TextArea areaStopWords;
    @FXML
    private Text testoIstruzioni;
    @FXML
    private Button bottoneAggiungiStopword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       u=new UtenteJDBC(); 
       adminDB=new AmministratoreJDBC(); 
       
       bottoneSfoglia.setOnAction(e -> {
        apriFileChooser();
    });
       
       bottoneIndietro.setOnAction(e -> {
           try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) bottoneIndietro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Wordageddon");
        stage.show();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
       });
       
       //segnalare all amministratore che l aggiunta di stopword potrebbe comportare errori nei file già caricati
       bottoneAggiungiStopword.setOnAction(e -> {
        List<String> stopWordsList=new ArrayList<>(); 
        String testo = areaStopWords.getText();
        if (testo != null && !testo.isEmpty()) {
            // Divido le parole per virgola e le pulisco da spazi bianchi
            stopWordsList.addAll(
           Arrays.stream(testo.split(","))
          .map(String::trim)
          .map(parola->parola.toLowerCase())
          .filter(p -> !p.isEmpty() && !stopWordsList.contains(p))
          .collect(Collectors.toList()));
            if(this.checkFilesOnUpdate(stopWordsList)){
                this.inserisciStopWords(stopWordsList);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("File non più validi");
                alert.setHeaderText(null);
                alert.setContentText("Le StopWords comportano errori nei file già inseriti\n"
                        + "Desideri inserirle lo stesso?");
                alert.showAndWait();
                if(alert.getResult().getText().contentEquals("OK")) this.inserisciStopWords(stopWordsList);
            }
        }
            
    });
    }

    private void apriFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleziona un file di testo");

    // Limita ai soli file .txt
    FileChooser.ExtensionFilter filtroTxt = new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt");
    fileChooser.getExtensionFilters().add(filtroTxt);

    // Ottieni lo stage attuale
    Stage stage = (Stage) bottoneSfoglia.getScene().getWindow();

    // Mostra la finestra di dialogo per la selezione del file
    File fileSelezionato = fileChooser.showOpenDialog(stage);
    String difficolta=null;

    if (fileSelezionato != null) {
        admin.setStopWords(adminDB.recuperaStopWords());
        if(admin.checkFile(fileSelezionato)){
            int lunghezzaFile=admin.getLunghezza(fileSelezionato);
            if(lunghezzaFile>150 || lunghezzaFile<450){
                if(lunghezzaFile>=150 && lunghezzaFile<250) difficolta="Facile";
                if(lunghezzaFile>=250 && lunghezzaFile<350) difficolta="Medio";
                if(lunghezzaFile>=350 && lunghezzaFile<450) difficolta="Difficile";
                if(adminDB.memorizzaFile(admin, fileSelezionato,difficolta)){
                    admin.addFiles(fileSelezionato);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("File caricato");
                    alert.setHeaderText(null);
                    alert.setContentText("il file è stato caricato con successo nel database");
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("File errore");
                    alert.setHeaderText(null);
                    alert.setContentText("il File non è stato caricato nel database \n"
                            + "perchè già presente al suo interno oppure per un errore di caricamento");
                     alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File errore");
                alert.setHeaderText(null);
                alert.setContentText("il File deve avere un numero di parole compreso tra 150 e 450");
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore File");
        alert.setHeaderText(null);
        alert.setContentText("Il file deve avere più di 50 e meno di 300 parole diverse \nle parole devono avere almeno 4 freuqenze diverse "
                + "con frequenza maggiore di uno");
        alert.showAndWait();
        }
    }
}
    
    public void setAmministratore(Utente u){
        admin=new Amministratore(u.getNomeUtente(),u.getPassword(),u.getEmail());
    }

    private boolean checkFilesOnUpdate(List<String> stopWordsAgg) {
        List<File> filesErrati=new ArrayList<>();
        Set<String> stopWords=new HashSet<>();
        stopWords.addAll(adminDB.recuperaStopWords());
        stopWords.addAll(stopWordsAgg);
        List<String> stop=new ArrayList<>(stopWords);
        admin.setStopWords(stop);
        boolean check=true;
        for(File f:adminDB.recuperaAllFile()){
            if(!admin.checkFile(f)){
                check=false;
                filesErrati.add(f);
                System.out.println("il file non risulta più valido: "+f.getName());
            }
        }
        
        return check;
    }
    
    public void inserisciStopWords(List<String> stopWordsList){
        if(adminDB.updateStopWords(admin,stopWordsList)){
                    areaStopWords.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                     alert.setTitle("StopWords caricate");
                     alert.setHeaderText(null);
                     alert.setContentText("Le StopWords sono state caricate con successo nel database");
                    alert.showAndWait();
                }
                else{
                    areaStopWords.clear();
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("StopWords errore");
                    alert.setHeaderText(null);
                    alert.setContentText("Le StopWords non sono state caricate nel database \n"
                            + "perchè già presenti al suo interno oopure per un errore di caricamento");
                    alert.showAndWait();
            }
    }
}
