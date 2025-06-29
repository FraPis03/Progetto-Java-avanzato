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
    
    public boolean updateStopWords(Amministratore admin,List<String> parole,String lingua);
    public boolean memorizzaFile(Amministratore admin,File f,String difficolta,String lingua);
    public List<File> recuperaFile(String difficolta,String lingua);
    public List<File> recuperaAllFile(String lingua);
    public List<String> recuperaStopWords(String lingua);
    public boolean checkNomeFile(String nomeFile);
}
