/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author antoniobellofatto
 */
public class main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        File f = new File("testo.txt");
        List<String> stopWords = new ArrayList();
        stopWords.add("ciao");

        Analisi analisi = new Analisi(f, stopWords);

        analisi.setOnSucceeded(event -> {
            Map<String, Integer> result = (Map<String, Integer>) analisi.getValue();
            List<Map<String, Integer>> m=new ArrayList();
            m.add(result);
            Domande d=new Domande(m);
            System.out.println(d.generateFrequenza());
            System.out.println("Risultato: " + result);
        });

        analisi.setOnFailed(event -> {
            Throwable ex = analisi.getException();
            System.err.println("Errore: " + ex.getMessage());
            ex.printStackTrace();
        });

        analisi.start();
                
        
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
