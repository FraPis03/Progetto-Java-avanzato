/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author antoniobellofatto
 */
public class QuizViewController implements Initializable {
    
    Domande domande;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    
    public void domande(Domande d){
        
        this.domande=d;
        
        List<String> domanda=domande.generateConfrontoDocumentoSingolo();
        textDomanda.setText("Qual è la parola che compare più volte nel documento "+domanda.get(5));
        btnRisposta1.setText(domanda.get(0));
        btnRisposta2.setText(domanda.get(1));
        btnRisposta3.setText(domanda.get(2));
        btnRisposta4.setText(domanda.get(3));
       
    }
}
