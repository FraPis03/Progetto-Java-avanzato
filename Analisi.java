
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
public class Analisi extends Service{
    
    private File file;
    private List<String> stopWords;

    public Analisi(File file, List<String> stopWords) {
        this.file = file;
        this.stopWords = stopWords.stream().map(parola->parola.toLowerCase()).collect(Collectors.toList());
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Map<String,Integer> call() throws Exception {
                try(
                    BufferedReader testo = new BufferedReader(new FileReader(file))    
                        ){
                    return testo.lines().flatMap(line->Stream.of(line.split("\\W+"))).map(parola->parola.toLowerCase()).filter(parola-> !stopWords.contains(parola)).collect(Collectors.toMap(
                                    Function.identity(),
                                    w -> 1,
                                    Integer::sum
                            )).entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new
    ));
                }
            }
        };
        
    }
    
}
