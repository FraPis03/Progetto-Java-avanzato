package wordageddon;


import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Interfaccia DAO per la gestione degli utenti 
 * e dei loro punteggi nel sistema Wordageddon.
 * Definisce i metodi necessari per interagire con la sorgente dati 
 * 
 * @author antoniobellofatto
 */
public interface UtenteDAO {
    
    /**
     * Inserisce un nuovo utente nel sistema.
     * 
     * @param u Oggetto {@link Utente} da inserire
     * @return true se l'inserimento ha avuto successo, false altrimenti
     */
    public boolean inserisciUtente(Utente u);
    
    /**
     * Elimina un utente esistente dal sistema.
     * 
     * @param u Utente da eliminare
     */
    public void eliminaUtente(Utente u);
    
    /**
     * Aggiorna il punteggio dell'utente in base alla difficoltà e alla lingua selezionata.
     * 
     * @param u L'utente da aggiornare
     * @param punteggio Il punteggio ottenuto
     * @param difficolta La difficoltà del quiz (es. Facile, Medio, Difficile)
     * @param lingua La lingua del quiz (es. Inglese, Spagnolo)
     */
    public void aggiornaPunteggio(Utente u,int punteggio,String difficolta,String lingua);
    
    /**
     * Verifica se le credenziali fornite corrispondono a un utente registrato.
     * 
     * @param nome Il nome utente
     * @param password La password
     * @return true se le credenziali sono corrette, false altrimenti
     */
    public boolean checkCredenziali(String nome,String password);
    
    /**
     * Controlla se il nome utente è già presente nel sistema.
     * 
     * @param nome Il nome utente da verificare
     * @return true se il nome è già esistente, false se è disponibile
     */
    public boolean checkNomeUtente(String nome);
    
    /**
     * Restituisce la lista dei punteggi ottenuti da un determinato utente, 
     * filtrati per difficoltà e lingua.
     * 
     * @param nomeUtente Il nome utente
     * @param difficolta La difficoltà selezionata
     * @param lingua La lingua del quiz
     * @return Una lista di oggetti {@link Punteggi} relativi all'utente
     */
    public List<Punteggi> punteggiUtente(String nomeUtente,String difficolta,String lingua);
    
    /**
     * Restituisce la mappa dei migliori punteggi globali per ciascun utente, 
     * in base alla difficoltà e alla lingua.
     * 
     * @param difficolta La difficoltà del quiz
     * @param lingua La lingua selezionata
     * @return Una mappa in cui la chiave è il nome utente e il valore è il relativo {@link Punteggi}
     */
    public Map<String,Punteggi> punteggiGlobali(String difficolta,String lingua);
    
    /**
     * Restituisce le statistiche dei punteggi ottenuti da un utente, 
     * in forma di lista di interi.
     * 
     * @param nomeUtente Il nome utente
     * @param difficolta La difficoltà selezionata
     * @param lingua La lingua del quiz
     * @return Lista di punteggi interi rappresentanti le performance dell'utente
     */
    public List<Integer> statistichePunteggio(String nomeUtente,String difficolta,String lingua);
}
