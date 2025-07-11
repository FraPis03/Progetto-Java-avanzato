/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller della vista "LectureView", che consente all'utente di leggere
 * i documenti selezionati in base alla difficoltà e lingua prima di iniziare il quiz.
 * 
 * Carica i file, gestisce la navigazione tra di essi, mostra il contenuto e avvia
 * un timer che reindirizza alla schermata del quiz una volta scaduto il tempo.
 * 
 * Collegato al file FXML {@code LectureView.fxml}.
 * 
 * @author antoniobellofatto
 */
public class LectureViewController implements Initializable {
    
    AmministratoreJDBC adminDB;
       
    Domande d;
    
    int numFileDifficolta;
    
    IntegerProperty numFileCorrente;
    
    List<File> files1;
    
    String difficolta;
    
    String lingua;

    @FXML
    private VBox contenitoreVBox;
    @FXML
    private Label labelTempo;
    @FXML
    private ScrollPane scrollPaneTesto;
    @FXML
    private Text testoContenuto;
    @FXML
    private Label labelNomeDocumento;
    @FXML
    private Button bottonePrecedente;
    @FXML
    private Label labelIndiceDocumento;
    @FXML
    private Button bottoneSuccessivo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        numFileCorrente=new SimpleIntegerProperty(0);
        bottoneSuccessivo.setOnAction(e->{
            numFileCorrente.set(numFileCorrente.get()+1);
            this.setVisualizazzioneDocumenti();
            this.aggiornaIndiceDocumento();
        });
        
        bottonePrecedente.setOnAction(e->{
            numFileCorrente.set(numFileCorrente.get()-1);
            this.setVisualizazzioneDocumenti();
            this.aggiornaIndiceDocumento();
        }); 
    }    

    /**
     * Avvia un timer countdown (5 secondi) alla fine del quale viene caricata la schermata del quiz.
     */
    private void startCountdownTimer() {
    int[] timeLeft = {5};
    labelTempo.setText("Tempo rimanente: " + timeLeft[0] + "s");

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        timeLeft[0]--;
        labelTempo.setText("Tempo rimanente: " + timeLeft[0] + "s");

        if (timeLeft[0] <= 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("QuizView.fxml"));
                Parent root = loader.load();

                QuizViewController controller = loader.getController();
                controller.domande(d,this.difficolta);

                Scene scene = new Scene(root);
                Stage stage = (Stage) contenitoreVBox.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }));
    timeline.setCycleCount(5);  // numero di secondi
    timeline.play();
}

    /**
     * Metodo chiamato dall'esterno per inizializzare il controller con
     * la difficoltà e lingua selezionate, recuperare i documenti e avviare l’analisi.
     * Lancia un'eccezione se non ci sono abbastanza documenti per la difficoltà scelta.
     */
   public void difficolta(){
        adminDB=new AmministratoreJDBC();
        this.difficolta=MainViewController.getDifficolta();
        this.lingua=MainViewController.getLingua();
        files1=new ArrayList();
        numFileDifficolta=0;
        numFileCorrente.set(0);
        
        switch(difficolta){
            case "Facile":{
                numFileDifficolta=3;
                break;
            }
            case "Medio":{
                numFileDifficolta=4;
                break;
            }
            case "Difficile":{
                numFileDifficolta=5;
                break;
            }
        }
        
                
        bottonePrecedente.disableProperty().bind(numFileCorrente.isEqualTo(0));
        bottoneSuccessivo.disableProperty().bind(numFileCorrente.greaterThanOrEqualTo(numFileDifficolta - 1));
       
        
        List<File> files=adminDB.recuperaFile(this.difficolta,this.lingua);
        System.out.println(files);
        List<String> stopWords=adminDB.recuperaStopWords(this.lingua);
        
        int numFile=files.size();
        Random r=new Random();
        while(files1.size()<numFileDifficolta){
            
            if(numFile<numFileDifficolta){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Numero File Errore");
                    alert.setHeaderText(null);
                    alert.setContentText("Non sono presenti abbastanza documenti per giocare a questa difficoltà \n"
                            + "Attendi aggiornamenti dall'Amministratore");
                    alert.showAndWait();
                throw new RuntimeException("non sono presenti abbastanza file");
            }
            
            File f=files.get(r.nextInt(numFile));

            if(!files1.contains(f)) files1.add(f);
        }
        
        System.out.println("le stop word sono: "+stopWords.toString());
                
        Analisi analisi=new Analisi(files1,stopWords);
        
        analisi.createTask();
        
        analisi.start();
        
        analisi.setOnSucceeded((WorkerStateEvent e)->{
            Map<String,Map<String, Integer>> result = (Map<String,Map<String, Integer>>) analisi.getValue();
            d=new Domande(result);
            result.values().stream().forEach(m->System.out.println(m));
            startCountdownTimer();
        });
        
        analisi.setOnFailed(event -> {
            Throwable ex = analisi.getException();
            System.err.println("Errore: " + ex.getMessage());
            ex.printStackTrace();
        });
        
        this.setVisualizazzioneDocumenti();
   }

    /**
     * Imposta la visualizzazione del documento corrente all'interno della schermata.
     * Carica il contenuto del file e lo mostra nella TextArea.
     */
   public void setVisualizazzioneDocumenti(){
       StringBuffer sb=new StringBuffer();
        
        labelNomeDocumento.setText(this.getNomeFile());
        
        try (BufferedReader br = new BufferedReader(new FileReader(files1.get(numFileCorrente.get())))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ex) {
              ex.printStackTrace();
        }
        testoContenuto.setText(sb.toString());
        this.aggiornaIndiceDocumento();
   }

   /**
   * Aggiorna l'etichetta dell’indice documento (es. 1/3).
   */
   public void aggiornaIndiceDocumento(){
       labelIndiceDocumento.setText((numFileCorrente.get()+1)+"/"+numFileDifficolta);
   }

    /**
     * Restituisce il nome del file attualmente selezionato, senza estensione e in maiuscolo.
     *
     * @return Nome del file corrente (senza estensione).
     */
   public String getNomeFile(){
       String nomeFile = files1.get(numFileCorrente.get()).getName();
       int ultimoPunto = nomeFile.lastIndexOf('.');
        if (ultimoPunto > 0) {
            nomeFile = nomeFile.substring(0, ultimoPunto);
        }
     return nomeFile.toUpperCase();
   }
}
