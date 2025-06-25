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
    public void aggiornaPunteggio(Utente u,int punteggio,String difficolta);
    public boolean checkCredenziali(String nome,String password);
    public boolean checkNomeUtente(String nome);
    public List<Punteggi> punteggiUtente(String nomeUtente,String difficolta);
    public Map<String,Punteggi> punteggiGlobali(String difficolta);
    public List<Integer> statistichePunteggio(String nomeUtente,String difficolta);
}
