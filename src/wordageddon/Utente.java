package wordageddon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public class Utente {
    
    private String nomeUtente;
    private String password;
    private String email;

    public Utente(String nomeUtente, String password, String email) {
        this.nomeUtente = nomeUtente;
        this.password = password;
        this.email = email;
    }

    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

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
