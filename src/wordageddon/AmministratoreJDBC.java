package wordageddon;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Implementazione dell'interfaccia {@link AmministratoreDAO} che fornisce metodi per interagire
 * con il database PostgreSQL, relativi alla gestione delle stopwords e dei file caricati
 * dagli amministratori.
 * 
 * Il database deve contenere le tabelle:
 * - AmministratoreStopWords(nome TEXT, stopword TEXT, lingua TEXT)
 * - AmministratoreFile(nome TEXT, file TEXT, difficolta TEXT, lingua TEXT)
 * 
 * Connessione effettuata tramite JDBC.
 * 
 * Autore: antoniobellofatto
 */
public class AmministratoreJDBC implements AmministratoreDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/wordageddon";
    private final String USER = "postgres";
    private final String PASS = "2003";

    
    /**
     * Aggiunge al database una lista di stopword specificando l'amministratore e la lingua.
     * 
     * @param admin oggetto {@link Amministratore} che effettua l'inserimento.
     * @param parole lista di parole da aggiungere come stopword.
     * @param lingua lingua associata alle stopword.
     * @return true se tutte le parole sono state inserite correttamente, false altrimenti.
     */
    @Override
    public boolean updateStopWords(Amministratore admin, List<String> parole, String lingua) {
        String query = "INSERT INTO AmministratoreStopWords (nome, stopword,lingua) VALUES (?, ?,?)";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query)
        ) {
           for(String parola:parole){
            stm.setString(1, admin.getNomeUtente());
            stm.setString(2, parola);
            stm.setString(3, lingua);

            int righe = stm.executeUpdate();
            if (righe == 0) {
                return false;
            }
         }

        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Salva nel database il percorso relativo di un file caricato dall'amministratore.
     * 
     * @param admin oggetto {@link Amministratore} che carica il file.
     * @param f file da memorizzare.
     * @param difficolta livello di difficoltà del file.
     * @param lingua lingua associata al contenuto del file.
     * @return true se il file è stato salvato correttamente, false altrimenti.
     */
    @Override
public boolean memorizzaFile(Amministratore admin, File f, String difficolta, String lingua) {
    String query = "INSERT INTO AmministratoreFile (nome, file, difficolta, lingua) VALUES (?, ?, ?, ?)";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query)
    ) {
        
        stm.setString(1, admin.getNomeUtente());
        stm.setString(2, f.getPath()); 
        stm.setString(3, difficolta);
        stm.setString(4, lingua);

        int righe = stm.executeUpdate();
        return righe > 0;

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
}



    /**
     * Recupera una lista di file salvati nel database in base alla difficoltà e lingua.
     * 
     * @param difficolta livello di difficoltà dei file richiesti.
     * @param lingua lingua dei file richiesti.
     * @return lista di oggetti {@link File} corrispondenti ai risultati della query.
     */
    @Override
public List<File> recuperaFile(String difficolta, String lingua) {

    List<File> files = new ArrayList<>();
    File baseDir = new File("testi");  // base relativa al progetto

    String query = "SELECT file FROM AmministratoreFile WHERE difficolta=? AND lingua=?";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
    ) {
        stm.setString(1, difficolta);
        stm.setString(2, lingua);

        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                files.add(new File(rs.getString("file")));
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL recupera file", ex);
    }

    return files;
}


    /**
     * Recupera tutte le stopword presenti nel database per una determinata lingua.
     * 
     * @param lingua lingua per cui recuperare le stopword.
     * @return lista di stringhe contenente tutte le stopword della lingua indicata.
     */
    @Override
    public List<String> recuperaStopWords(String lingua) {
    List<String> stopWords = new ArrayList<>();
    
    String query = "SELECT stopword FROM AmministratoreStopWords WHERE lingua = ?";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
    ) {
        stm.setString(1, lingua);
        
        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                stopWords.add(rs.getString("stopword"));
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL recupera stopword", ex);
    }

    return stopWords;
}


    /**
     * Verifica che non esista già nel database un file con lo stesso nome.
     * 
     * @param nomeFile nome del file da controllare.
     * @return true se il nome non è presente nel database (quindi può essere inserito), false altrimenti.
     */
    @Override
    public boolean checkNomeFile(String nomeFile) {
        boolean check=false;
        
        String query = "SELECT file FROM AmministratoreFile WHERE file=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nomeFile);
            try(ResultSet rs=stm.executeQuery()){
                if(rs.next()) check=false;
                else check=true;
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        return check;
    }
     
     /**
     * Recupera tutti i file salvati nel database per una determinata lingua.
     * 
     * @param lingua lingua per cui recuperare i file.
     * @return lista di oggetti {@link File}.
     */
    @Override
    public List<File> recuperaAllFile(String lingua) {
    List<File> files = new ArrayList<>();
    
    String query = "SELECT file FROM AmministratoreFile WHERE lingua = ?";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
    ) {
        stm.setString(1, lingua); // <-- Imposta parametro lingua
        
        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                files.add(new File(rs.getString("file")));
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL recupera file", ex);
    }

    return files;
}


    
}
