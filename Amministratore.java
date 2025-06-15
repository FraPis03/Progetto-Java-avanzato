
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public class Amministratore extends Utente {
    
    private List<String> stopWords;
    private List<File> files;

    public Amministratore(String nomeUtente, String password, String email) {
        super(nomeUtente, password, email);
        this.stopWords = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public List<String> getStopWords() {
        return stopWords;
    }

    public List<File> getFiles() {
        return files;
    }
    
    public void addStopWords(String parola){
        if(!this.stopWords.contains(parola)) this.stopWords.add(parola);
    }
    
    public void addFiles(File f){
        if(!this.files.contains(f)) this.files.add(f);
    }
}
