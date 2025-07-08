package wordageddon;


import java.io.File;
import java.nio.file.Path;
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
 *
 * @author antoniobellofatto
 */
public class AmministratoreJDBC implements AmministratoreDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/wordageddon";
    private final String USER = "postgres";
    private final String PASS = "2003";

    
    //aggiungo le stopwords passate in input al db
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

    //memorizzo sul db il path del file passato dall admin
    @Override
public boolean memorizzaFile(Amministratore admin, File f, String difficolta, String lingua) {
    String query = "INSERT INTO AmministratoreFile (nome, file, difficolta, lingua) VALUES (?, ?, ?, ?)";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query)
    ) {
        File baseDir = new File("testi").getAbsoluteFile();
        Path basePath = baseDir.toPath();
        Path filePath = f.toPath().toAbsolutePath();

        Path relativePath = basePath.relativize(filePath);
        String relativePathStr = relativePath.toString();


        stm.setString(1, admin.getNomeUtente());
        stm.setString(2, relativePathStr);
        stm.setString(3, difficolta);
        stm.setString(4, lingua);

        int righe = stm.executeUpdate();
        if (righe == 0) {
            return false;
        }

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
    return true;
}


    //recupero i path dei vari file presenti sul db e restituisco una lista di file
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
                String relativePath = rs.getString("file");
                File file = new File(baseDir, relativePath);
                files.add(file);
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL recupera file", ex);
    }

    return files;
}


    //restituisco tutte le stopwords presenti sul db
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


    //controllo che non vengano inseriti più volte gli stessi file o file con lo stesso nome
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
