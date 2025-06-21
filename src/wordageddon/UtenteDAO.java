package wordageddon;


import java.util.List;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public interface UtenteDAO {
    
    public boolean inserisciUtente(Utente u);
    public void eliminaUtente(Utente u);
    public void aggiornaPunteggio(Utente u,int punteggio);
    public boolean checkCredenziali(String nome,String password);
    public boolean checkNomeUtente(String nome);
    public List<Integer> punteggiUtente(String nomeUtente);
    public Map<String,Integer> punteggiGlobali();
}
