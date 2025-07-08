package wordageddon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe che rappresenta un utente del sistema Wordageddon.
 * Contiene le informazioni di autenticazione e metodi di validazione.
 * 
 * @author antoniobellofatto
 */
public class Utente {
    
    private String nomeUtente;
    private String password;
    private String email;

    /**
     * Costruttore con tutti i campi (nome utente, password, email).
     * 
     * @param nomeUtente Il nome utente
     * @param password La password dell'utente
     * @param email L'email dell'utente
     */
    public Utente(String nomeUtente, String password, String email) {
        this.nomeUtente = nomeUtente;
        this.password = password;
        this.email = email;
    }

    /**
     * Costruttore con solo nome utente e password (senza email).
     * 
     * @param nomeUtente Il nome utente
     * @param password La password
     */
    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    /**
     * Restituisce il nome utente.
     * 
     * @return Il nome utente
     */
    public String getNomeUtente() {
        return nomeUtente;
    }

    /**
     * Imposta un nuovo nome utente.
     * 
     * @param nomeUtente Il nuovo nome utente
     */
    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    /**
     * Restituisce la password dell'utente.
     * 
     * @return La password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta una nuova password per l'utente.
     * 
     * @param password La nuova password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce l'email dell'utente.
     * 
     * @return L'indirizzo email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta una nuova email per l'utente.
     * 
     * @param email Il nuovo indirizzo email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    //controlli sulla validità dell'utente
    public boolean checkUtente(){
        if(this.nomeUtente.length()<3 || this.nomeUtente.length()>20) return false;
        if(this.password.length()<5 || this.password.length()>20) return false;
        if (!email.contains("@") || !email.contains(".")) return false;
        return true;    
    }
    
    
}
