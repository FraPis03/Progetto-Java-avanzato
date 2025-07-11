package wordageddon;


import java.io.File;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * L'interfaccia {@code AmministratoreDAO} definisce i metodi per la gestione
 * persistente dei dati relativi agli amministratori, in particolare:
 * 
 *   Gestione delle stopwords in base alla lingua
 *   Memorizzazione e recupero dei file testuali
 *   Verifica dell'esistenza di un file tramite nome
 * 
 * Implementazioni tipiche possono prevedere l'interazione con un database o filesystem.
 * 
 * @author antoniobellofatto
 */
public interface AmministratoreDAO {

    /**
     * Aggiorna la lista di stopwords per una determinata lingua.
     *
     * @param admin l'amministratore che esegue l'operazione
     * @param parole la lista di parole da aggiungere/aggiornare come stopwords
     * @param lingua la lingua associata alle stopwords
     * @return {@code true} se l'operazione è andata a buon fine, {@code false} altrimenti
     */
    public boolean updateStopWords(Amministratore admin,List<String> parole,String lingua);

    /**
     * Memorizza un file associandolo a una lingua e a un livello di difficoltà.
     *
     * @param admin l'amministratore che carica il file
     * @param f il file da memorizzare
     * @param difficolta la difficoltà associata al file (es. facile, medio, difficile)
     * @param lingua la lingua del file
     * @return {@code true} se il file è stato memorizzato correttamente, {@code false} altrimenti
     */
    public boolean memorizzaFile(Amministratore admin,File f,String difficolta,String lingua);

    /**
     * Recupera tutti i file associati a una certa difficoltà e lingua.
     *
     * @param difficolta il livello di difficoltà (es. facile, medio, difficile)
     * @param lingua la lingua dei file
     * @return lista dei file corrispondenti, oppure lista vuota se nessuno è trovato
     */
    public List<File> recuperaFile(String difficolta,String lingua);

    /**
     * Recupera tutti i file disponibili per una determinata lingua, indipendentemente dalla difficoltà.
     *
     * @param lingua la lingua dei file
     * @return lista di tutti i file associati alla lingua specificata
     */
    public List<File> recuperaAllFile(String lingua);

    /**
     * Recupera la lista di stopwords relative a una specifica lingua.
     *
     * @param lingua la lingua per cui si vogliono ottenere le stopwords
     * @return lista di stopwords, oppure lista vuota se nessuna è disponibile
     */
    public List<String> recuperaStopWords(String lingua);

    /**
     * Verifica se un file con il nome specificato è già presente nel sistema.
     *
     * @param nomeFile il nome del file da controllare (senza path)
     * @return {@code true} se il file esiste già, {@code false} altrimenti
     */
    public boolean checkNomeFile(String nomeFile);
}
