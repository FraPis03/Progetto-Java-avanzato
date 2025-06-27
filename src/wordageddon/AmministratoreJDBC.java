package wordageddon;


import java.io.File;
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
    public boolean updateStopWords(Amministratore admin, List<String> parole) {
        String query = "INSERT INTO AmministratoreStopWords (nome, stopword) VALUES (?, ?)";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query)
        ) {
           for(String parola:parole){
            stm.setString(1, admin.getNomeUtente());
            stm.setString(2, parola);

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
    public boolean memorizzaFile(Amministratore admin, File f,String difficolta) {
        String query = "INSERT INTO AmministratoreFile (nome, file, difficolta) VALUES (?, ?, ?)";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query)
        ) {
            stm.setString(1, admin.getNomeUtente());
            stm.setString(2, f.getPath());
            stm.setString(3, difficolta);
            
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
public List<File> recuperaFile(String difficolta) {

    List<File> files = new ArrayList<>();

    String query = "SELECT file FROM AmministratoreFile WHERE difficolta=?";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
    ) {
        stm.setString(1, difficolta);

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


    //restituisco tutte le stopwords presenti sul db
    @Override
    public List<String> recuperaStopWords() {
        List<String> stopWords=new ArrayList<>();
        
        String query = "SELECT stopword FROM AmministratoreStopWords";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                ResultSet rs=stm.executeQuery();
        ) {
            
            while(rs.next()) stopWords.add(rs.getString("stopword"));

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
    public List<File> recuperaAllFile() {
        List<File> files=new ArrayList<>();
        
        String query = "SELECT file FROM AmministratoreFile";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                ResultSet rs=stm.executeQuery();
        ) {
            
            while(rs.next()) files.add(new File(rs.getString("file")));

        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera file", ex);
        }
        return files;
    }

    
}
