/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
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
 * Controller della schermata del quiz. Gestisce la visualizzazione delle domande, 
 * la raccolta delle risposte dell'utente e il passaggio alla schermata dei risultati.
 * 
 * Le domande vengono generate in base alla difficoltà scelta (Facile, Medio, Difficile)
 * utilizzando i metodi forniti dalla classe {@code Domande}. 
 * Ogni domanda ha 4 risposte possibili e viene mostrata una alla volta.
 * 
 * Le risposte corrette e quelle date dall'utente vengono raccolte e passate alla 
 * schermata successiva per il calcolo del punteggio.
 * 
 * @author antoniobellofatto
 */
public class QuizViewController implements Initializable {
    
    Domande domande;
    
    private int numeroDomanda;
    
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
       
        btnRisposta1.setOnAction(e->{
            
            risposteUtente.add(btnRisposta1.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            else{
            
            textDomanda.setText(domandeFatte.get(domandaCorrente).get(4));
    
            btnRisposta1.setText(domandeFatte.get(domandaCorrente).get(0));
            btnRisposta2.setText(domandeFatte.get(domandaCorrente).get(1));
            btnRisposta3.setText(domandeFatte.get(domandaCorrente).get(2));
            btnRisposta4.setText(domandeFatte.get(domandaCorrente).get(3));

            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
            }
        });
        
        btnRisposta2.setOnAction(e->{
            
            risposteUtente.add(btnRisposta2.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            else{
            textDomanda.setText(domandeFatte.get(domandaCorrente).get(4));
    
            btnRisposta1.setText(domandeFatte.get(domandaCorrente).get(0));
            btnRisposta2.setText(domandeFatte.get(domandaCorrente).get(1));
            btnRisposta3.setText(domandeFatte.get(domandaCorrente).get(2));
            btnRisposta4.setText(domandeFatte.get(domandaCorrente).get(3));

            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
            }
        });
        
        btnRisposta3.setOnAction(e->{
            
            risposteUtente.add(btnRisposta3.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            
            else{
            textDomanda.setText(domandeFatte.get(domandaCorrente).get(4));
    
            btnRisposta1.setText(domandeFatte.get(domandaCorrente).get(0));
            btnRisposta2.setText(domandeFatte.get(domandaCorrente).get(1));
            btnRisposta3.setText(domandeFatte.get(domandaCorrente).get(2));
            btnRisposta4.setText(domandeFatte.get(domandaCorrente).get(3));

            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
            }
        });
        
        btnRisposta4.setOnAction(e->{
            risposteUtente.add(btnRisposta4.getText());
            
            if(domandaCorrente==totaleDomande) this.vaiAiRisultati();
            
            else{
            textDomanda.setText(domandeFatte.get(domandaCorrente).get(4));
    
            btnRisposta1.setText(domandeFatte.get(domandaCorrente).get(0));
            btnRisposta2.setText(domandeFatte.get(domandaCorrente).get(1));
            btnRisposta3.setText(domandeFatte.get(domandaCorrente).get(2));
            btnRisposta4.setText(domandeFatte.get(domandaCorrente).get(3));

            domandaCorrente++;
            this.aggiornaProgresso(domandaCorrente, totaleDomande);
            }
        });   
    }    
    
    /**
     * Avvia il quiz in base alla difficoltà scelta.
     * @param d oggetto Domande per generare le domande.
     * @param difficolta livello di difficoltà: "Facile", "Medio" o "Difficile".
     */
    public void domande(Domande d,String difficolta){    
        
    this.domande=d;
    
    switch (difficolta){
        case "Facile":{
            totaleDomande=10;
            this.primaDomanda();
            this.generaDomandeFacile();
            break;
        }
        case "Medio":{
            totaleDomande=15;
            this.primaDomanda();
            this.generaDomandeMedio();
            break;
        }
        case "Difficile":{
            totaleDomande=18;
            this.primaDomanda();
            this.generaDomandeDifficile();
            break;
        }
    }
     
}
    /**
     * Aggiorna l'etichetta che mostra il progresso dell'utente nel quiz.
     * @param domandaCorrente numero della domanda corrente.
     * @param totaleDomande numero totale di domande.
     */
    public void aggiornaProgresso(int domandaCorrente, int totaleDomande) {
        labelProgresso.setText(domandaCorrente + "/" + totaleDomande);
    }

    /**
     * Passa alla schermata dei risultati al termine del quiz.
     */
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

    /**
     * Genera le domande di livello Facile.
     */
    public void generaDomandeFacile(){
            List<String> domanda=domande.generateFrequenzaAssoluto();
            risposteCorrette.add(domanda.get(0));
            String dom="Quante volte compare la parola \""+domanda.get(4) +"\" tra tutti i documenti ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateFrequenzaSingolo(0);
            risposteCorrette.add(domanda.get(0));
            dom="Quante volte compare la parola \""+domanda.get(4) +"\" nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateMassimoSingolo(1);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateConfrontoAssoluto();
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte tra tutti i documenti ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateFrequenzaSingolo(1);
            risposteCorrette.add(domanda.get(0));
            dom="Quante volte compare la parola \""+domanda.get(4) +"\" nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateConfrontoDocumentoSingolo(1);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom); 
            
            domanda=domande.generateMassimoSingolo(2);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateSpecifico();
            risposteCorrette.add(domanda.get(5));
            dom="In quale documento compare la parola \""+domanda.get(4) +"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateFrequenzaSingolo(2);
            risposteCorrette.add(domanda.get(0));
            dom="Quante volte compare la parola \""+domanda.get(4) +"\" nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
    }

    /**
     * Mostra la prima domanda all'utente.
     */
    public void primaDomanda(){
        this.aggiornaProgresso(domandaCorrente, totaleDomande);
        List<String> domanda=domande.generateConfrontoDocumentoSingolo(0);
        System.out.println("la prima risposta vale"+domanda.get(4));
            risposteCorrette.add(domanda.get(4));
            System.out.println("le risposte corrette dopo la prima sono: "+risposteCorrette);
            String dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            List<String> risposte = domanda.subList(0, 4); 
            Collections.shuffle(risposte);
            risposte.add(dom);
            domandeFatte.add(risposte);
        textDomanda.setText(dom);
        btnRisposta1.setText(risposte.get(0));
        btnRisposta2.setText(risposte.get(1));
        btnRisposta3.setText(risposte.get(2));
        btnRisposta4.setText(risposte.get(3)); 
    }

    /**
     * Genera le domande di livello Medio (include quelle Facili).
     */
    public void generaDomandeMedio(){
        this.generaDomandeFacile();
        
            List<String> domanda=domande.generateSpecifico();
            risposteCorrette.add(domanda.get(5));
            String dom="In quale documento compare la parola \""+domanda.get(4) +"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateFrequenzaSingolo(3);
            risposteCorrette.add(domanda.get(0));
            dom="Quante volte compare la parola \""+domanda.get(4) +"\" nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateConfrontoDocumentoSingolo(3);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom); 
            
            domanda=domande.generateFrequenzaAssoluto();
            risposteCorrette.add(domanda.get(0));
            dom="Quante volte compare la parola \""+domanda.get(4) +"\" tra tutti i documenti ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateConfrontoAssoluto();
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte tra tutti i documenti ?";
            this.mischia(domanda, dom);
            
    }
    
    /**
     * Genera le domande di livello Difficile (include quelle Facili e Medie).
     */
    //aggiornare correttamente questo metodo
    public void generaDomandeDifficile(){
        this.generaDomandeFacile();
        this.generaDomandeMedio();
        
        List<String> domanda=domande.generateSpecifico();
            risposteCorrette.add(domanda.get(5));
            String dom="In quale documento compare la parola \""+domanda.get(4) +"\" ?";
            this.mischia(domanda, dom);
            
            domanda=domande.generateMassimoSingolo(4);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom); 
            
            domanda=domande.generateConfrontoDocumentoSingolo(4);
            risposteCorrette.add(domanda.get(4));
            dom="Qual è la parola che compare più volte nel documento \""+domanda.get(5)+"\" ?";
            this.mischia(domanda, dom);
  
           

        
    }

    /**
     * Aggiunge una nuova domanda mischiando le risposte e salvandole nella lista domandeFatte.
     * @param domanda lista di risposte generate da {@code Domande}.
     * @param dom testo della domanda.
     */
    public void mischia(List<String> domanda,String dom){
        List<String> risposte = new ArrayList<>(domanda.subList(0, 4)); 
            Collections.shuffle(risposte);
            risposte.add(dom);
            domandeFatte.add(risposte);
    }
}
