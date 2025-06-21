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

    public Analisi(List<File> files, List<String> stopWords) {
        this.files = files;
        this.stopWords = stopWords.stream()
                                  .map(parola -> parola.toLowerCase())
                                  .collect(Collectors.toList());
    }

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
                                .flatMap(line -> Stream.of(line.split("\\W+")))
                                .filter(parola -> !parola.isEmpty())
                                .map(parola -> parola.toLowerCase())
                                .filter(parola -> !stopWords.contains(parola))
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        w -> 1,
                                        Integer::sum
                                ));
                        // Ordina la mappa per valore decrescente
                        Map<String, Integer> ordinata = mappa.entrySet().stream()
                                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (a, b) -> a,
                                        LinkedHashMap::new
                                ));

                        risultati.putIfAbsent(file.getName(),ordinata);
                    }
                }

                return risultati;
            }
        };
    }
}
