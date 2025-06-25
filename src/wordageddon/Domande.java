package wordageddon;


import java.util.ArrayList;
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
    public List<String> generateConfrontoDocumentoSingolo() {

    List<String> domandeParole = new ArrayList<>();
    Map<String, Integer> domande = new LinkedHashMap<>();

    Random r = new Random();

    
    int numeroDocumento = r.nextInt(this.documenti.size());
    
    List<Map.Entry<String,Map<String, Integer>>> documento = new ArrayList<>(this.documenti.entrySet());
    Map.Entry<String,Map<String, Integer>> documentoSelezionato=documento.get(numeroDocumento);
    
    
    List<Map.Entry<String, Integer>> listaParole = new ArrayList<>(documentoSelezionato.getValue().entrySet());
    String docSel=documentoSelezionato.getKey();

    if (listaParole.size() < 15) {
        throw new RuntimeException("Il documento ha meno di 10 parole.");
    }

    
    Map.Entry<String, Integer> prima = listaParole.get(0);//seleziono la prima parola che so ha la frequenza maggiore
    domande.put(prima.getKey(), prima.getValue());
    domandeParole.add(prima.getKey());

    int tentativi = 0;

    //aggiungo altre 3 parole casuali
    while (domandeParole.size() < 4 && tentativi < 150) {
        Map.Entry<String, Integer> scelta = listaParole.get(r.nextInt(25));
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

   
    //seleziono 4 parole casuali scelte tra tutti i documenti che abbiano frequenze diverse
    public List<String> generateConfrontoAssoluto(){
        
        List<String> domandeParole=new ArrayList<>();
        Map<String,Integer> domande=new LinkedHashMap<>();
        int numeroDocumenti=this.documenti.size();
        Random r=new Random();
        int count=0;
        int numeroMassimeIterazioni=0;       
       
        while(domande.size()<4 && numeroMassimeIterazioni<150){
            List<Map.Entry<String,Map<String,Integer>>> doc=new ArrayList<>(this.documenti.entrySet());
            Map.Entry<String,Map<String, Integer>> documentoSelezionato=doc.get(r.nextInt(this.documenti.size()));
            Map<String,Integer> documento=documentoSelezionato.getValue();
   
            List<String> parole = new ArrayList<>(documento.keySet());
            count=0;
            String parola=null;
            
           if(documento.size()<10) throw new RuntimeException(" il documento ha troppe poche parole");
           if(numeroMassimeIterazioni>149) throw new RuntimeException(" problemi con il documento");
           
           if(domandeParole.size()<2) parola=parole.get(r.nextInt(10));
           else parola=parole.get(r.nextInt(parole.size()));
           System.out.println("la parola è "+parola);
           if(!domande.containsKey(parola)){       
               for(Map<String,Integer> m:this.documenti.values()){
                   if(m.containsKey(parola)) count+=m.get(parola);
               }
               
               if(!domande.containsValue(count)) domande.put(parola, count);
               
           }
           
           numeroMassimeIterazioni++;
        }
        
        domandeParole.addAll(domande.keySet());
    
       domandeParole.add(this.risultato(domande.entrySet()));
        
       System.out.println("le domande riguardano le parole:"+domande);
       
       return domandeParole;
    }
    

    //Ampliare nel caso in cui ci siano 2 o più parole con la stessa frequenza massima
    // mischiare i risultati poichè la più frequente è sempre al primo posto
    //scelgo una parola in un documento e restituisco la sua frequenza in quel documento e altre 3 frequenze casuali
    public List<String> generateFrequenzaSingolo(){
        
        List<String> domanda=new ArrayList<>();
        
        Random r = new Random();

        List<Map.Entry<String, Map<String, Integer>>> docs = new ArrayList<>(documenti.entrySet());
        Map.Entry<String, Map<String, Integer>> documentoSelezionato = docs.get(r.nextInt(docs.size()));
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

    
    int tentativi = 0;
    while (domanda.size() < 4 && tentativi < 150) {
        int i = r.nextInt(25);
        String parolaCasuale = doc.get(i).getKey();
        int freqCasuale = doc.get(i).getValue();

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
