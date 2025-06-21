package wordageddon;


import java.io.File;
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
public interface AmministratoreDAO {
    
    public void updateStopWords(Amministratore admin,List<String> parole);
    public void memorizzaFile(Amministratore admin,File f);
    public List<File> recuperaFile();
    public List<String> recuperaStopWords();
    public boolean checkNomeFile(String nomeFile);
}
