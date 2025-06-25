/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordageddon;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author antoniobellofatto
 */
public class Punteggi {
    
    private int punteggio;
    private Timestamp tempo;

    public Punteggi(int punteggio, Timestamp tempo) {
        this.punteggio = punteggio;
        this.tempo = tempo;
    }

    public int getPunteggio() {
        return punteggio;
    }

    public void setPunteggio(int punteggio) {
        this.punteggio = punteggio;
    }

    public Timestamp getTempo() {
        return tempo;
    }

    public void setTempo(Timestamp tempo) {
        this.tempo = tempo;
    }

    
    
    
}
