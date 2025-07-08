package wordageddon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Classe principale per l'avvio dell'applicazione Wordageddon.
 * 
 * Questa classe estende {@link javafx.application.Application} e carica la vista iniziale
 * dell'applicazione (LoginView.fxml), mostrando la schermata di login all'avvio.
 *
 * L'applicazione è una piattaforma per giochi linguistici, in cui gli utenti possono
 * registrarsi, accedere e interagire con l'interfaccia grafica creata in JavaFX.
 * 
 * @author antoniobellofatto
 */
public class main extends Application {

 /**
 * Metodo chiamato automaticamente da JavaFX all'avvio dell'applicazione.
 * 
 * Carica l'interfaccia grafica dal file FXML `LoginView.fxml` e imposta la scena principale.
 *
 * @param primaryStage lo stage principale fornito dal sistema all'avvio dell'app.
 * @throws IOException se il file FXML non viene trovato o non può essere caricato.
 */
    @Override
    public void start(Stage primaryStage) throws IOException {
        
       
    FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
    Parent root = loader.load();

    
    Scene scene = new Scene(root);
    primaryStage.setTitle("Login");
    primaryStage.setScene(scene);
    primaryStage.show();
    }

 /**
 * Metodo main standard che avvia l'applicazione JavaFX.
 *
 * @param args argomenti da riga di comando (non utilizzati in questa applicazione).
 */
    public static void main(String[] args) {
        launch(args);
    }
    
}
