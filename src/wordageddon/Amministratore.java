package wordageddon;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public class Amministratore extends Utente{
    
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
    
    
    //controllo sui file affinchè abbiano una certa lunghezza e una certa varietà di parole con diverse frequenze al suo interno
    public boolean checkFile(File f){
        
        boolean check=false;
        
        Map<String,Integer> m=new LinkedHashMap();

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
  
       List<String> parole = br.lines()
            .flatMap(linea -> Arrays.stream(linea.toLowerCase().split("\\W+")))
            .filter(parola -> !parola.isEmpty())
            .filter(parola -> !this.stopWords.contains(parola)).collect(Collectors.toList());

        long paroleDiverse = parole.stream().distinct().count();
        
         Map<String, Long> mappaFrequenze = parole.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Set<Long> frequenzeUniche = new HashSet<>();

        for (Long freq : mappaFrequenze.values()) {
            //if (freq > 1) { decommentare per necessità sui valori di frequenza
                frequenzeUniche.add(freq);
            //}
        }
        
        System.out.println("Parole filtrate: " + parole);
        System.out.println("Mappa delle frequenze:");
        mappaFrequenze.forEach((parola, frequenza) ->
            System.out.println(parola + ": " + frequenza)
        );
        System.out.println("Parole diverse: " + paroleDiverse);
        System.out.println("Frequenze diverse: " + frequenzeUniche.size());
        
        
        check= paroleDiverse>50 && paroleDiverse<300 && frequenzeUniche.size()>=4;
        
    } catch (IOException e) {
        e.printStackTrace();
        return false; 
    }
    return check;
    }

    public void setStopWords(List<String> stopWords) {
        this.stopWords = stopWords;
    }

    public int getLunghezza(File f) {
    int count = 0;
    try (Scanner scanner = new Scanner(f)) {
        while (scanner.hasNext()) {
            scanner.next();
            count++;
        }
        System.out.println("il numero di parole è: "+count);
    } catch (FileNotFoundException e) {
        System.err.println("File non trovato: " + f.getAbsolutePath());
    }
    return count;
}

    
    
}
