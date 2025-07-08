package wordageddon;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
 * Implementazione dell'interfaccia UtenteDAO tramite JDBC.
 * Si occupa della gestione del database per operazioni relative agli utenti e ai punteggi.
 * Utilizza PostgreSQL come sistema di gestione del database.
 */
public class UtenteJDBC implements UtenteDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/wordageddon";
    private final String USER = "postgres";
    private final String PASS = "2003";

 /**
 * Inserisce un nuovo utente nel database.
 *
 * @param u Oggetto Utente da inserire.
 * @return true se l'inserimento è avvenuto con successo, false altrimenti.
 * @throws RuntimeException se si verifica un errore SQL.
 */
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

    
 /**
 * Elimina un utente dal database sulla base del nome utente.
 *
 * @param u Oggetto Utente da eliminare.
 * @throws RuntimeException se l'utente non viene trovato o in caso di errore SQL.
 */
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

 /**
 * Inserisce un nuovo punteggio per l'utente nel database.
 *
 * @param u Oggetto Utente per cui aggiornare il punteggio.
 * @param punteggio Il punteggio ottenuto.
 * @param difficolta La difficoltà della partita.
 * @param lingua La lingua utilizzata nella partita.
 * @throws RuntimeException se si verifica un errore SQL.
 */
    @Override
    public void aggiornaPunteggio(Utente u, int punteggio,String difficolta,String lingua) {
    String query = "INSERT INTO punteggi (nomeUtente, punteggio, dataOra, difficolta, lingua) VALUES (?, ?, ?, ?, ?)";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stmt = con.prepareStatement(query)
    ) {
        stmt.setString(1, u.getNomeUtente());
        stmt.setInt(2, punteggio);
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        stmt.setTimestamp(3, timestamp);
        stmt.setString(4, difficolta);
        stmt.setString(5, lingua);

        int rows = stmt.executeUpdate();
        if (rows == 0) {
            System.out.println("Nessun punteggio inserito");
        }

    } catch (SQLException ex) {
        throw new RuntimeException("Errore inserimento punteggio", ex);
    }
 }


 /**
 * Verifica se le credenziali (nome e password) dell'utente sono corrette.
 *
 * @param nome Il nome utente.
 * @param password La password da verificare.
 * @return true se le credenziali corrispondono a un utente nel database, false altrimenti.
 * @throws RuntimeException se si verifica un errore SQL.
 */
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

 /**
 * Verifica se il nome utente è già presente nel database.
 *
 * @param nome Il nome utente da verificare.
 * @return true se il nome utente esiste già, false altrimenti.
 * @throws RuntimeException in caso di errore SQL.
 */
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

 /**
 * Restituisce la lista dei punteggi ottenuti da un utente, filtrati per difficoltà e lingua.
 *
 * @param nomeUtente Il nome dell'utente.
 * @param difficolta La difficoltà delle partite.
 * @param lingua La lingua delle partite.
 * @return Lista di oggetti Punteggi ordinati per data decrescente.
 * @throws RuntimeException in caso di errore SQL.
 */
    @Override
    public List<Punteggi> punteggiUtente(String nomeUtente,String difficolta, String lingua) {
        List<Punteggi> punteggi=new ArrayList<>();
        
        String query = "SELECT punteggio,dataOra FROM punteggi WHERE nomeUtente=? AND difficolta=? AND lingua=? ORDER BY dataOra DESC";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nomeUtente);
            stm.setString(2, difficolta);
            stm.setString(3, lingua);
            try(ResultSet rs=stm.executeQuery()){
            while(rs.next()){
                punteggi.add(new Punteggi(rs.getInt("punteggio"),rs.getTimestamp("dataOra")));  
            }

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL recupera stopword", ex);
        }
        
        return punteggi;
    }

 /**
 * Restituisce una mappa contenente la somma dei punteggi di tutti gli utenti
 * per una determinata difficoltà e lingua, ordinata in ordine decrescente di punteggio.
 *
 * @param difficolta La difficoltà selezionata.
 * @param lingua La lingua selezionata.
 * @return Mappa con il nome utente come chiave e il punteggio totale come valore.
 * @throws RuntimeException in caso di errore SQL.
 */
    @Override
public Map<String, Punteggi> punteggiGlobali(String difficolta,String lingua) {
    Map<String, Punteggi> punteggi = new HashMap<>();

    String query = "SELECT nomeUtente, MAX(dataOra) AS dataOra, SUM(punteggio) AS sum_punteggio"
            + " FROM punteggi WHERE difficolta=? AND lingua=? GROUP BY nomeUtente";

    try (
        Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement stm = con.prepareStatement(query);
        
    ) {
        
        stm.setString(1, difficolta);
        stm.setString(2, lingua);
        try (ResultSet rs = stm.executeQuery()) {
        while (rs.next()) {
            String nome = rs.getString("nomeUtente");
            int somma = rs.getInt("sum_punteggio");
            Timestamp tempo = rs.getTimestamp("dataOra");

            punteggi.put(nome, new Punteggi(somma, tempo));
        }
    }
    } catch (SQLException ex) {
        Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
        throw new RuntimeException("Errore SQL nel recupero dei punteggi", ex);
    }

    // Ordina la mappa per punteggio in ordine decrescente
    return punteggi.entrySet().stream()
        .sorted(Comparator.comparingInt((Map.Entry<String, Punteggi> e) -> e.getValue().getPunteggio()).reversed())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
}

    
 /**
 * Restituisce il ruolo di un utente a partire dal nome.
 *
 * @param nome Il nome dell'utente.
 * @return Il ruolo associato (es. "Utente" o "Amministratore").
 * @throws RuntimeException in caso di errore SQL.
 */
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

 /**
 * Restituisce una lista contenente il punteggio massimo e il punteggio medio dell’utente,
 * filtrati per difficoltà e lingua.
 *
 * @param nomeUtente Il nome dell’utente.
 * @param difficolta La difficoltà delle partite.
 * @param lingua La lingua delle partite.
 * @return Lista di due interi: il primo è il punteggio massimo, il secondo è il punteggio medio.
 * @throws RuntimeException in caso di errore SQL.
 */
    @Override
    public List<Integer> statistichePunteggio(String nomeUtente, String difficolta, String lingua) {
        List<Integer> punteggi=new ArrayList<>();
        
        String query = "SELECT MAX(punteggio) as maxPunteggio,AVG(punteggio) as mediaPunteggio FROM punteggi WHERE nomeUtente=? AND difficolta=? AND lingua=?";

        try (
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement stm = con.prepareStatement(query);
                
        ) {
            stm.setString(1, nomeUtente);
            stm.setString(2, difficolta);
            stm.setString(3, lingua);
            try(ResultSet rs=stm.executeQuery()){
            while(rs.next()){
                punteggi.add(rs.getInt("maxPunteggio")); 
                punteggi.add(rs.getInt("mediaPunteggio")); 
            }

            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AmministratoreJDBC.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Errore SQL statistiche punteggi", ex);
        }
        
        return punteggi;
    }

}

