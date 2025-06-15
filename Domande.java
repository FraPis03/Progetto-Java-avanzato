
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
public class Domande {
    
    private List<Map<String,Integer>> documenti;

    public Domande(List<Map<String, Integer>> documenti) {
        this.documenti = documenti;
    }

    public List<String> generateFrequenza(){
        
        List<String> domande=new ArrayList<>();
       
       String domanda1=documenti.get(0).entrySet().stream().findFirst().toString();
       String domanda2=documenti.get(0).entrySet().stream().skip(1).findAny().toString();
       String domanda3=documenti.get(0).entrySet().stream().skip(3).findAny().toString();
       String domanda4=documenti.get(0).entrySet().stream().skip(4).findAny().toString();
       
       domande.add(domanda1);
       domande.add(domanda2);
       domande.add(domanda3);
       domande.add(domanda4);
       
       System.out.println("le domande riguardano le parole:"+domanda1+","+domanda2+","+domanda3+","+domanda4);
       
       return domande;
    }
    
    public List<String> generateConfronto(){
        return null;
    }
    
    public List<String> generateSpecifico(){
        return null;
    }
    
    public List<String> generateEsclusione(){
        return null;
    }
}
