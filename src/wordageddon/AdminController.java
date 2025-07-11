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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller per l'interfaccia grafica dedicata all'amministratore in Wordageddon.
 * Gestisce il caricamento dei file, l'aggiunta di stopwords e la configurazione 
 * della lingua e della difficoltà.
 * 
 * L'interfaccia è costruita con JavaFX.
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
    @FXML
    private ChoiceBox<String> choiceBoxLingua;
    @FXML
    private ChoiceBox<String> choiceBoxLinguaStopword;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       u=new UtenteJDBC(); 
       adminDB=new AmministratoreJDBC();
            
       choiceBoxLingua.getItems().addAll("IT","EN","ESP","FR");
       choiceBoxLingua.setValue("IT");
       choiceBoxLinguaStopword.getItems().addAll("IT","EN","ESP","FR");
       choiceBoxLinguaStopword.setValue("IT");
       
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

    /**
    * Apre un file chooser per selezionare manualmente un file di testo (.txt)
    * e avvia il processo di upload del file selezionato.
    */
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

    uploadFile(fileSelezionato);
}
    /**
    * Verifica se il file selezionato è supportato (solo formato .txt).
    * 
    * @param file File da controllare
    * @return true se il file è supportato, false altrimenti
    */
    private boolean verificaSupportoFile(File file){
	String fileName = file.getName().toLowerCase();
	return fileName.endsWith(".txt");
    }

    /**
    * Mostra un alert all'utente indicando che il file selezionato
    * non è supportato dal sistema.
    */
    private void fileNonSupportatoAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Il file inserito è di un formato non supportato.\n(Inserire solo file in formato .txt)", ButtonType.OK);
        alert.setHeaderText(null); 
        alert.showAndWait(); 
    }

    /**
    * Gestisce l'evento di trascinamento di file sull'area di drop.
    * Accetta solo file.
    * 
    * @param event Evento di tipo DragEvent
    */
    @FXML
    private void trascina(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
           event.acceptTransferModes(TransferMode.COPY);
        }
    }

    /**
    * Gestisce l'evento di rilascio dei file trascinati. Verifica il tipo di file
    * e in caso positivo lo passa alla funzione di upload.
    * 
    * @param event Evento di tipo DragEvent
    */
    @FXML
    private void rilascia(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            List<File> files = db.getFiles();
            File fileSelezionato = files.get(0); 
            if (!verificaSupportoFile(fileSelezionato)) {///< Verifica che il file sia di un formato accettato.
                fileNonSupportatoAlert();
            } else {
                uploadFile(fileSelezionato);
            }
        }
        event.setDropCompleted(success);
        event.consume();
     }

    /**
    * Esegue l'upload del file nel database se rispetta tutti i vincoli:
    * - formato valido
    * - numero parole totali compreso tra 150 e 450
    * - almeno 50 parole diverse con almeno 4 parole con frequenza > 1
    * - non già presente nel database
    * 
    * @param fileSelezionato Il file selezionato da caricare
    */
    private void uploadFile(File fileSelezionato){
        String difficolta=null;
        String linguaStopWords=choiceBoxLinguaStopword.getValue();
        if (fileSelezionato != null) {
        admin.setStopWords(adminDB.recuperaStopWords(linguaStopWords));
        if(admin.checkFile(fileSelezionato)){
            int lunghezzaFile=admin.getLunghezza(fileSelezionato);
            if(lunghezzaFile>150 || lunghezzaFile<450){
                if(lunghezzaFile>=150 && lunghezzaFile<250) difficolta="Facile";
                if(lunghezzaFile>=250 && lunghezzaFile<350) difficolta="Medio";
                if(lunghezzaFile>=350 && lunghezzaFile<450) difficolta="Difficile";
                String lingua=choiceBoxLingua.getValue();
                if(adminDB.memorizzaFile(admin, fileSelezionato,difficolta,lingua)){
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

    /**
    * Imposta i dati dell’amministratore attualmente loggato a partire da un oggetto Utente.
    * 
    * @param u Oggetto Utente da cui costruire l'amministratore
    */
    public void setAmministratore(Utente u){
        admin=new Amministratore(u.getNomeUtente(),u.getPassword(),u.getEmail());
    }

    /**
    * Controlla se l'aggiunta di nuove stopwords invalida i file già caricati.
    * Se anche uno solo dei file non supera più il controllo di validità,
    * il metodo restituisce false.
    * 
    * @param stopWordsAgg Lista di nuove stopwords da aggiungere
    * @return true se tutti i file restano validi, false altrimenti
    */
    private boolean checkFilesOnUpdate(List<String> stopWordsAgg) {
        List<File> filesErrati=new ArrayList<>();
        Set<String> stopWords=new HashSet<>();
        String linguaStopWords=choiceBoxLinguaStopword.getValue();
        stopWords.addAll(adminDB.recuperaStopWords(linguaStopWords));
        stopWords.addAll(stopWordsAgg);
        List<String> stop=new ArrayList<>(stopWords);
        admin.setStopWords(stop);
        boolean check=true;
        for(File f:adminDB.recuperaAllFile(linguaStopWords)){
            if(!admin.checkFile(f)){
                check=false;
                filesErrati.add(f);
                System.out.println("il file non risulta più valido: "+f.getName());
            }
        }
        
        return check;
    }

    /**
    * Inserisce una lista di stopwords nel database per la lingua selezionata.
    * Mostra un alert informativo se l'inserimento è andato a buon fine,
    * altrimenti segnala l'errore.
    * 
    * @param stopWordsList Lista di stopwords da inserire
    */
    public void inserisciStopWords(List<String> stopWordsList){
        String linguaStopWords=choiceBoxLinguaStopword.getValue();
        if(adminDB.updateStopWords(admin,stopWordsList,linguaStopWords)){
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
