/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author antoniobellofatto
 */
public class QuizViewController implements Initializable {
    
    Domande domande;
    
    List<List<String>> domandeFatte;
    
    List<String> risposteUtente;
    
    List<String> risposteCorrette;
    
    private int domandaCorrente;
    
    private int totaleDomande;

    @FXML
    private VBox vboxDomanda;
    @FXML
    private ScrollPane scrollPaneDomanda;
    @FXML
    private Text textDomanda;
    @FXML
    private Button btnRisposta1;
    @FXML
    private Button btnRisposta2;
    @FXML
    private Button btnRisposta3;
    @FXML
    private Button btnRisposta4;
    @FXML
    private Label labelProgresso;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        domandeFatte=new ArrayList<>();
        
        risposteUtente=new ArrayList<>();
        
        risposteCorrette=new ArrayList<>();
        
        domandaCorrente=1;
        
        totaleDomande=5;
       
        btnRisposta1.setOnAction(e->{
            
            risposteUtente.add(btnRisposta1.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            
            List<String> domanda=domande.generateMassimoSingolo(2);
            domandeFatte.add(domanda);
            risposteCorrette.add(domanda.get(4));
            textDomanda.setText("Qual è la parola che compare nel 2 documento?");
            List<String> risposte = new ArrayList<>(domanda.subList(0, 4)); 
            Collections.shuffle(risposte); 
    
            btnRisposta1.setText(risposte.get(0));
            btnRisposta2.setText(risposte.get(1));
            btnRisposta3.setText(risposte.get(2));
            btnRisposta4.setText(risposte.get(3));
            
            
            
            
            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
        });
        
        btnRisposta2.setOnAction(e->{
            
            risposteUtente.add(btnRisposta2.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            List<String> domanda=domande.generateConfrontoAssoluto();
            domandeFatte.add(domanda);
            risposteCorrette.add(domanda.get(4));
            textDomanda.setText("Qual è la parola che compare più volte tra tutti i documenti ?");
            List<String> risposte = new ArrayList<>(domanda.subList(0, 4)); 
            Collections.shuffle(risposte); 
    
            btnRisposta1.setText(risposte.get(0));
            btnRisposta2.setText(risposte.get(1));
            btnRisposta3.setText(risposte.get(2));
            btnRisposta4.setText(risposte.get(3));
            
            
            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
        });
        
        btnRisposta3.setOnAction(e->{
            
            risposteUtente.add(btnRisposta3.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            List<String> domanda=domande.generateFrequenzaSingolo();
            domandeFatte.add(domanda);
            risposteCorrette.add(domanda.get(4));
            textDomanda.setText("Quante volte compare la parola "+domanda.get(4) +" nel documento "+domanda.get(5));
            List<String> risposte = new ArrayList<>(domanda.subList(0, 4)); 
            Collections.shuffle(risposte); 
    
            btnRisposta1.setText(risposte.get(0));
            btnRisposta2.setText(risposte.get(1));
            btnRisposta3.setText(risposte.get(2));
            btnRisposta4.setText(risposte.get(3));
            
            
            
            
            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
        });
        
        btnRisposta4.setOnAction(e->{
            risposteUtente.add(btnRisposta4.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            List<String> domanda=domande.generateFrequenzaAssoluto();
            domandeFatte.add(domanda);
            risposteCorrette.add(domanda.get(4));
            textDomanda.setText("Quante volte compare la parola "+domanda.get(4) +" tra tutti i documenti ?");
            List<String> risposte = new ArrayList<>(domanda.subList(0, 4)); 
            Collections.shuffle(risposte); 
    
            btnRisposta1.setText(risposte.get(0));
            btnRisposta2.setText(risposte.get(1));
            btnRisposta3.setText(risposte.get(2));
            btnRisposta4.setText(risposte.get(3));
            
            

            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
        });
    }    
    
    
    public void domande(Domande d){
        
        this.domande=d;
        
        List<String> domanda=domande.generateConfrontoDocumentoSingolo();
        risposteCorrette.add(domanda.get(4));
        domandeFatte.add(domanda);
        textDomanda.setText("Qual è la parola che compare più volte nel documento "+domanda.get(5));
        btnRisposta1.setText(domanda.get(0));
        btnRisposta2.setText(domanda.get(1));
        btnRisposta3.setText(domanda.get(2));
        btnRisposta4.setText(domanda.get(3));
       
    }
    
    public void aggiornaProgresso(int domandaCorrente, int totaleDomande) {
        labelProgresso.setText(domandaCorrente + "/" + totaleDomande);
    }
    
    public void vaiAiRisultati(){
        try {
            //Cambio la schermata quando sono finite le domande
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RisultatoView.fxml"));
            Parent root = loader.load();
            
            RisultatiController controller = loader.getController();
            controller.setRisultati(risposteUtente, risposteCorrette);

            // Ottieni lo stage corrente
            Stage stage = (Stage) labelProgresso.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Risultati");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
