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
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author antoniobellofatto
 */
public class LectureViewController implements Initializable {
    
    AmministratoreJDBC adminDB;
       
    Domande d;
    
    String difficolta;

    @FXML
    private VBox contenitoreVBox;
    @FXML
    private Label labelTempo;
    @FXML
    private ScrollPane scrollPaneTesto;
    @FXML
    private Text testoContenuto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
    }    
    
    private void startCountdownTimer() {
    final int[] timeLeft = {5};
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

   public void difficolta(){
        adminDB=new AmministratoreJDBC();
        this.difficolta=MainViewController.getDifficolta();
        List<File> files1=new ArrayList();
        int numFileDifficolta=0;
        
        switch(difficolta){
            case "Facile":{
                numFileDifficolta=3;
                break;
            }
            case "Medio":{
                numFileDifficolta=5;
                break;
            }
            case "Difficile":{
                numFileDifficolta=6;
                break;
            }
        }
        
        List<File> files=adminDB.recuperaFile(MainViewController.getDifficolta());
        List<String> stopWords=adminDB.recuperaStopWords();
        
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
        
        StringBuffer sb=new StringBuffer();
        
        for(File f:files1){
            sb.append(f.getName()+"\n");
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException ex) {
              ex.printStackTrace();
        }
 
        sb.append('\n');
        
        }
        
        testoContenuto.setText(sb.toString());
        
   }

}
