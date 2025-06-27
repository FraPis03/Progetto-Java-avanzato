package wordageddon;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
//fare controlli sui file per numero di parole escluse le stopword e numero di parole diverse per evitare problemi
public class Analisi extends Service<Map<String,Map<String, Integer>>> {

    private List<File> files;
    private List<String> stopWords;

    //definisco la lista di file da analizzare e la lista di stopwords da escludere
    public Analisi(List<File> files, List<String> stopWords) {
        this.files = files;
        this.stopWords = stopWords.stream()
                                  .map(parola -> parola.toLowerCase())
                                  .collect(Collectors.toList());
    }

    //metodo per restituire una mappa con nome del file e le frequenze delle parole al suo interno
    @Override
    protected Task<Map<String,Map<String, Integer>>> createTask() {
        return new Task<Map<String,Map<String, Integer>>>() {
            @Override
            protected Map<String,Map<String, Integer>> call() throws Exception {
                Map<String,Map<String, Integer>> risultati=new LinkedHashMap<>();
                
                List<Map<String, Integer>> risultatiLista = new ArrayList<>();

                for (File file : files) {
                    try (BufferedReader testo = new BufferedReader(new FileReader(file))) {
                        Map<String, Integer> mappa = testo.lines()
                                .flatMap(line -> Stream.of(line.split("[^\\p{L}\\p{Nd}]+")))//faccio la divisione sulla base di tutto ciò 
                                //che non sono caratteri o numeri
                                .filter(parola -> !parola.isEmpty())//controllo che la parola non sia vuota
                                .map(parola -> parola.toLowerCase())
                                .filter(parola -> !stopWords.contains(parola))//filtro per eliminare le stopwords
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        w -> 1,
                                        Integer::sum
                                ));//mappo le parole sulla base di quante volte compaiono
                        // Ordino lo mappa per valore decrescente
                        Map<String, Integer> ordinata = mappa.entrySet().stream()
                                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (a, b) -> a,
                                        LinkedHashMap::new
                                ));
                        String nomeFile = file.getName();
                        int ultimoPunto = nomeFile.lastIndexOf('.');
                        if (ultimoPunto > 0) {
                        nomeFile = nomeFile.substring(0, ultimoPunto);
                        }
                        risultati.putIfAbsent(nomeFile,ordinata);
                    }
                }

                return risultati;
            }
        };
    }
}
