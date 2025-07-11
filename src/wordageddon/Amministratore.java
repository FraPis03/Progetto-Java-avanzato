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

/*
 * La classe {@code Amministratore} estende {@link Utente} e rappresenta un utente
 * con privilegi amministrativi in grado di gestire file di testo e stopwords.
 * 
 * Le sue funzionalità principali includono:
 * 
 *   Verifica dei file in base a criteri di lunghezza e frequenza delle parole
 *   Gestione di stopwords
 *   Memorizzazione dei file caricati
 * 
 * @author antoniobellofatto
 */
public class Amministratore extends Utente{
    
    private List<String> stopWords;
    private List<File> files;

    /**
     * Costruttore della classe {@code Amministratore}.
     *
     * @param nomeUtente nome utente dell'amministratore
     * @param password password dell'amministratore
     * @param email email dell'amministratore
     */
    public Amministratore(String nomeUtente, String password, String email) {
        super(nomeUtente, password, email);
        this.stopWords = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    /**
     * Restituisce la lista di stopwords associate all'amministratore.
     *
     * @return lista di stopwords
     */
    public List<String> getStopWords() {
        return stopWords;
    }

    /**
     * Restituisce la lista di file caricati dall'amministratore.
     *
     * @return lista di file
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * Aggiunge una nuova stopword alla lista, se non è già presente.
     *
     * @param parola parola da aggiungere come stopword
     */
    public void addStopWords(String parola){
        if(!this.stopWords.contains(parola)) this.stopWords.add(parola);
    }

    /**
     * Aggiunge un file alla lista dei file caricati, se non è già presente.
     *
     * @param f file da aggiungere
     */
    public void addFiles(File f){
        if(!this.files.contains(f)) this.files.add(f);
    }
    
    
    /**
     * Verifica se un file rispetta i vincoli:
     * 
     *   Numero di parole diverse compreso tra 51 e 299
     *   Almeno 4 frequenze diverse 
     *
     * @param f file da verificare
     * @return true se il file è valido, false altrimenti
     */
    public boolean checkFile(File f){
        
        boolean check=false;
        
        Map<String,Integer> m=new LinkedHashMap();

    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
  
       List<String> parole = br.lines()
            .flatMap(linea -> Arrays.stream(linea.toLowerCase().split("[^\\p{L}\\p{Nd}]+")))
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

    /**
     * Imposta la lista di stopwords.
     *
     * @param stopWords lista di parole da considerare come stopwords
     */
    public void setStopWords(List<String> stopWords) {
        this.stopWords = stopWords;
    }

    /**
     * Calcola la lunghezza di un file in termini di numero totale di parole.
     *
     * @param f file da analizzare
     * @return numero di parole nel file
     */
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
