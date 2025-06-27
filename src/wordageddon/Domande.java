package wordageddon;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
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
// inserire classi per le eccezzioni e vedere dove gestirle
//classe per la produzione delle domande sui documenti che vengono passati
public class Domande {
    
    private Map<String,Map<String,Integer>> documenti;//una map con il nome del documento e la map delle parole e
    //frequenze all'interno del documento

    public Domande(Map<String,Map<String, Integer>> documenti) {
        this.documenti = documenti;
    }

    
    /*
    vengono selezionate 4 parole casuali con 4 frequenze diverse da un singolo
    documento casuale tra quelli passati
    */
   public List<String> generateConfrontoDocumentoSingolo(int numdoc) {

    List<String> domandeParole = new ArrayList<>();
    Map<String, Integer> domande = new LinkedHashMap<>();

    Random r = new Random();

    List<Map.Entry<String, Map<String, Integer>>> documento = new ArrayList<>(this.documenti.entrySet());
    Map.Entry<String, Map<String, Integer>> documentoSelezionato = documento.get(numdoc);

    List<Map.Entry<String, Integer>> listaParole = new ArrayList<>(documentoSelezionato.getValue().entrySet());
    String docSel = documentoSelezionato.getKey();

    if (listaParole.size() < 15) {
        throw new RuntimeException("Il documento ha meno di 15 parole.");
    }
    Collections.shuffle(listaParole);

    int tentativi = 0;

    while (domandeParole.size() < 4 && tentativi < 350) {
        Map.Entry<String, Integer> scelta = listaParole.get(tentativi);
        String parola = scelta.getKey();
        int frequenza = scelta.getValue();

        
        if (!domandeParole.contains(parola) && !domande.containsValue(frequenza)) {
            domande.put(parola, frequenza);
            domandeParole.add(parola);
            System.out.println("Domanda generata con parole: " + domande);
        }

        tentativi++;
    }

    if (domandeParole.size() < 4) {
        throw new RuntimeException("Impossibile trovare 4 parole con frequenze diverse.");
    }

    domandeParole.add(this.risultato(domande.entrySet())); 
    domandeParole.add(docSel);

    System.out.println("Domande generate con parole: " + domande);

    return domandeParole;
}


   //scelgo 4 parole casuali tra tutti i documenti
    public List<String> generateConfrontoAssoluto() {
    List<String> domandeParole = new ArrayList<>();
    Map<String, Integer> domande = new LinkedHashMap<>();
    Random r = new Random();
    int maxTentativi = 150;
    int tentativi = 0;

    List<Map.Entry<String, Map<String, Integer>>> documentiList = new ArrayList<>(this.documenti.entrySet());

    while (domande.size() < 4 && tentativi < maxTentativi) {
        // Scelgo un documento casuale
        Map.Entry<String, Map<String, Integer>> documentoSelezionato = documentiList.get(r.nextInt(documentiList.size()));
        Map<String, Integer> documento = documentoSelezionato.getValue();

        List<String> parole = new ArrayList<>(documento.keySet());
        String parola;
        if (domandeParole.size() >= 3) {
            parola = parole.get(r.nextInt(parole.size()));
        } else {
            parola = parole.get(r.nextInt(8));
        }

        // Calcolo la frequenza in tutti i documenti
        if (!domande.containsKey(parola)) {
            int count = 0;
            for (Map<String, Integer> m : this.documenti.values()) {
                count += m.getOrDefault(parola, 0);
            }

            if (!domande.containsValue(count)) {
                domande.put(parola, count);
            }
        }

        tentativi++;
    }

    if (domande.size() < 4) {
        throw new RuntimeException("Impossibile trovare 4 parole con frequenze assolute diverse.");
    }

    domandeParole.addAll(domande.keySet());
    domandeParole.add(this.risultato(domande.entrySet())); // metodo per trovare la parola con freq max
    System.out.println("Domande generate: " + domande);

    return domandeParole;
}


    //scelgo una parola in un documento e restituisco la sua frequenza in quel documento e altre 3 frequenze casuali
    public List<String> generateFrequenzaSingolo(int numdoc){
        
        List<String> domanda=new ArrayList<>();
        
        Random r = new Random();

        List<Map.Entry<String, Map<String, Integer>>> docs = new ArrayList<>(documenti.entrySet());
        Map.Entry<String, Map<String, Integer>> documentoSelezionato = docs.get(numdoc);
        Map<String, Integer> documento= documentoSelezionato.getValue();
        String docSel=documentoSelezionato.getKey();
        List<Map.Entry<String,Integer>> doc=new ArrayList<>(documento.entrySet());
        
        int num=r.nextInt(7);
        
        String parola=doc.get(num).getKey();
        int frequenza=doc.get(num).getValue();
        
        domanda.add(String.valueOf(frequenza));
        
        domanda.addAll(this.generaFrequenzeCasuali(frequenza, 3));
        
        domanda.add(parola);
        
        domanda.add(docSel);
      
        return domanda;
    }
    
    //scelgo una parola tra tutti i documenti e restituisco la frequenza in tutti i documenti e 3 frequenze casuali
    public List<String> generateFrequenzaAssoluto(){
        List<String> domanda=new ArrayList<>();
        
        Random r = new Random();

        List<Map.Entry<String, Map<String, Integer>>> docs = new ArrayList<>(documenti.entrySet());
        Map.Entry<String, Map<String, Integer>> documentoSelezionato = docs.get(r.nextInt(docs.size()));
        Map<String, Integer> documento= documentoSelezionato.getValue();
        
        List<Map.Entry<String,Integer>> doc=new ArrayList<>(documento.entrySet());
        
        int num=r.nextInt(7);
        
        String parola=doc.get(num).getKey();
        int frequenza=0;
        
        for(Map<String,Integer> m:this.documenti.values()){
            if(m.containsKey(parola)) frequenza+=m.get(parola);
        }
        
        domanda.add(String.valueOf(frequenza));
        
        domanda.addAll(this.generaFrequenzeCasuali(frequenza, 3));
        
        domanda.add(parola);
        
        
        
        return domanda;
    }
    
    //restituisco la parola che compare più volte nel documento scelto e altre 3 parole casuali
    public List<String> generateMassimoSingolo(int documento) {
    List<String> domanda = new ArrayList<>();
    Map<String, Integer> domande = new LinkedHashMap<>();
    Random r = new Random();

   
    List<Map.Entry<String, Map<String, Integer>>> docs = new ArrayList<>(documenti.entrySet());
    Map.Entry<String, Map<String, Integer>> documentoSelezionato = docs.get(documento);
    Map<String, Integer> doc1 = documentoSelezionato.getValue();
    String nomeDoc = documentoSelezionato.getKey();

    
    List<Map.Entry<String, Integer>> doc = new ArrayList<>(doc1.entrySet());

    if (doc.size() < 4) {
        throw new RuntimeException("Il documento ha meno di 4 parole.");
    }

    
    String parola = doc.get(0).getKey();
    int freq = doc.get(0).getValue();
    domanda.add(parola);
    domande.put(parola, freq);
    
    Collections.shuffle(doc);

    
    int tentativi = 0;
    while (domanda.size() < 4 && tentativi < 350) {
        Map.Entry<String, Integer> scelta = doc.get(tentativi);
        String parolaCasuale = doc.get(tentativi).getKey();
        int freqCasuale = doc.get(tentativi).getValue();

        if (!domande.containsKey(parolaCasuale) && !domande.containsValue(freqCasuale)) {
            domande.put(parolaCasuale, freqCasuale);
            domanda.add(parolaCasuale);
        }

        tentativi++;
    }

    if (domanda.size() < 4) {
        throw new RuntimeException("Non è stato possibile selezionare abbastanza parole uniche.");
    }

    domanda.add(parola);
    domanda.add(nomeDoc);
    
    return domanda;
}


    
    //seleziono una parola tra i documenti e restituisco il nome del documento in cui compare e altri 3 documenti
    public List<String> generateSpecifico(){
        List<String> domanda=new ArrayList<>();
        
        Random r=new Random();
   
       List<Map.Entry<String, Map<String, Integer>>> docs = new ArrayList<>(documenti.entrySet());
       Map.Entry<String, Map<String, Integer>> documentoSelezionato = docs.get(r.nextInt(this.documenti.size()));
       Map<String, Integer> doc1 = documentoSelezionato.getValue();
       String nomeDoc = documentoSelezionato.getKey();
    
       List<Map.Entry<String, Integer>> doc = new ArrayList<>(doc1.entrySet());
       String parolaScelta=doc.get(r.nextInt(20)).getKey();
       boolean contain;

    do {
       parolaScelta = doc.get(r.nextInt(Math.min(20, doc.size()))).getKey();
       contain = false;

        for (Entry<String, Map<String, Integer>> m : docs) {
            if (!m.getKey().equals(nomeDoc) && m.getValue().containsKey(parolaScelta)) {
               contain = true; // parola trovata in un altro documento
               break; // esco dal ciclo, scelgo un'altra parola
            }
        }
    } while (contain);
       
       
       domanda.add(nomeDoc);
       for(Entry<String,Map<String,Integer>> m:docs){
                if(domanda.size()==4) break;
               if(!m.getKey().contentEquals(nomeDoc)) domanda.add(m.getKey());
        }
       
       int i=0;
       while(domanda.size()<4){
           String doc2="Documento"+i;
           domanda.add(doc2);
            i++;
       }
       
       
       domanda.add(parolaScelta);
       domanda.add(nomeDoc);
       

       return domanda;
    }
    
    public List<String> generateEsclusione(int documento){
        return null;
    }
    
    //ottengo la parola con la frequenza massima
    public String risultato(Set<Map.Entry<String,Integer>> m){
        int valoreMassimo=0;
        String risultato="";
        for (Map.Entry<String, Integer> entry : m) {
        if (entry.getValue() > valoreMassimo) {
            valoreMassimo= entry.getValue();
            risultato = entry.getKey();
        }
    }
        return risultato;
    }

    //genero 3 frequenza casuali in un range di +- 10 da quella di riferimento
    public List<String> generaFrequenzeCasuali(int frequenzaRiferimento, int numeroFrequenze) {
    Random r = new Random();
    Set<String> frequenze = new HashSet<>();

    while (frequenze.size() < numeroFrequenze) {
        int delta = r.nextInt(21) - 10; 
        int freqCasuale = frequenzaRiferimento + delta;

        if (freqCasuale >= 0 && freqCasuale != frequenzaRiferimento) {
            frequenze.add(String.valueOf(freqCasuale));
        }
    }

    return new ArrayList<>(frequenze);
   }
}
