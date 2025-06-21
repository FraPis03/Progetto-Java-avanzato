package wordageddon;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public class UtenteJDBC implements UtenteDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/wordageddon";
    private final String USER = "postgres";
    private final String PASS = "2003";

    @Override
    public boolean inserisciUtente(Utente u) {
        String query = "INSERT INTO Utente (nome, password, email, ruolo) VALUES (?, ?, ?, ?)";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = con.prepareStatement(query)
        ) {
            stmt.setString(1, u.getNomeUtente());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail()); 
            stmt.setString(4, "Utente");

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                return false;
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Errore nell'inserimento utente", ex);
        }
        return true;
    }

    @Override
    public void eliminaUtente(Utente u) {
        String query = "DELETE FROM Utente WHERE nome = ?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = con.prepareStatement(query)
        ) {
            stmt.setString(1, u.getNomeUtente());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Utente non trovato per eliminazione");
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante eliminazione utente", ex);
        }
    }

    @Override
    public void aggiornaPunteggio(Utente u, int punteggio) {
        String query = "INSERT INTO punteggi VALUES(?,?)";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stmt = con.prepareStatement(query)
        ) {
            stmt.setInt(1, punteggio);
            stmt.setString(2, u.getNomeUtente());

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                System.out.println("Nessun punteggio inserito");
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Errore inserimento punteggio", ex);
        }
    }

    @Override
    public boolean checkCredenziali(String nome, String password) {
        boolean check=false;
        
        String query = "SELECT nome,password FROM Utente WHERE nome=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nome);
            try(ResultSet rs=stm.executeQuery()){
            if(rs.next()){
                if(rs.getString("password").contentEquals(password)) check=true;
            }

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        return check;
    }

    @Override
    public boolean checkNomeUtente(String nome) {
        boolean check=false;
        
        String query = "SELECT nome FROM Utente WHERE nome=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nome);
            try(ResultSet rs=stm.executeQuery()){
                if(rs.next()) check=true;
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        return check;
    }

    @Override
    public List<Integer> punteggiUtente(String nomeUtente) {
        List<Integer> punteggi=new ArrayList<>();
        
        String query = "SELECT punteggio FROM punteggi WHERE nome=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nomeUtente);
            try(ResultSet rs=stm.executeQuery()){
            while(rs.next()){
                punteggi.add(rs.getInt("punteggio"));
            }

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        
        punteggi.sort(Integer::compareTo);
        return punteggi;
    }

    @Override
    public Map<String, Integer> punteggiGlobali() {
    Map<String, Integer> punteggi = new HashMap<>();
    
    String query = "SELECT nome, MAX(punteggio) AS max_punteggio FROM punteggi GROUP BY nome";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
        ResultSet rs = stm.executeQuery()
    ) {
        while (rs.next()) {
            String nome = rs.getString("nome");
            int maxPunteggio = rs.getInt("max_punteggio");
            punteggi.put(nome, maxPunteggio);
        }
    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL nel recupero dei punteggi", ex);
    }

    
    return punteggi.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
}
    
    public String getRuolo(String nome){
        String ruolo="Utente";
        
        String query = "SELECT ruolo FROM Utente WHERE nome=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nome);
            try(ResultSet rs=stm.executeQuery()){
                if(rs.next()) ruolo=rs.getString("ruolo");
            }
            

        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        return ruolo;
    }

}

