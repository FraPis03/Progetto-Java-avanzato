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
    
    public boolean updateStopWords(Amministratore admin,List<String> parole);
    public boolean memorizzaFile(Amministratore admin,File f,String difficolta);
    public List<File> recuperaFile(String difficolta);
    public List<File> recuperaAllFile();
    public List<String> recuperaStopWords();
    public boolean checkNomeFile(String nomeFile);
}
