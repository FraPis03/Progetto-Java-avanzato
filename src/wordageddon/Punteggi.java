/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * La classe {@code Punteggi} rappresenta un punteggio ottenuto in una sessione di gioco
 * nell'applicazione Wordageddon, insieme al timestamp in cui è stato registrato.
 * 
 * Contiene due attributi:
 * 
 *   {@code punteggio} - il valore numerico del punteggio ottenuto
 *   {@code tempo} - il momento esatto in cui è stato ottenuto il punteggio
 * 
 * @author antoniobellofatto
 */
public class Punteggi {
    
    private int punteggio;
    private Timestamp tempo;

    /**
     * Costruttore della classe {@code Punteggi}.
     * 
     * @param punteggio il punteggio ottenuto
     * @param tempo il timestamp del momento in cui il punteggio è stato registrato
     */
    public Punteggi(int punteggio, Timestamp tempo) {
        this.punteggio = punteggio;
        this.tempo = tempo;
    }

    /**
     * Restituisce il punteggio.
     * 
     * @return il punteggio ottenuto
     */
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Imposta un nuovo valore per il punteggio.
     * 
     * @param punteggio il nuovo punteggio da assegnare
     */
    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    /**
     * Restituisce il tempo associato al punteggio.
     * 
     * @return il timestamp in cui è stato registrato il punteggio
     */
    public Timestamp getTempo() {
        return tempo;
    }

    /**
     * Imposta un nuovo valore per il timestamp del punteggio.
     * 
     * @param tempo il nuovo timestamp da assegnare
     */
    public void setTempo(Timestamp tempo) {
        this.tempo = tempo;
    }

    
    
    
}
