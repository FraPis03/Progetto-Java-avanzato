
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniobellofatto
 */
public class UtenteJDBC implements UtenteDAO{
    private final String URL="jdbc:postgresql://localhost:5432/wordageddon";
    private final String USER="postgres";
    private final String PASS="2003";

    @Override
    public void inserisciUtente(Utente u) {
        try(
             Connection con=DriverManager.getConnection(URL, USER, PASS);
             Statement stm=con.createStatement();
                ){
            String query=String.format("INSERT INTO Utente VALUES(%s,%s,%s,%s)",u.getNomeUtente(),u.getPassword(),u.getEmail(),"Utente");
            
           if(!stm.execute(query)){
               System.out.println("il nome utente o le credenziali sono incorette");
           }
           
            
        } catch (SQLException ex) {
            Logger.getLogger(UtenteJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminaUtente(Utente u) {
        try(
             Connection con=DriverManager.getConnection(URL, USER, PASS);
             Statement stm=con.createStatement();
                ){
            String query=String.format("DELETE FROM Utente WHERE nome=%s",u.getNomeUtente());
            
           if(!stm.execute(query)){
               System.out.println("Eliminazione non andata a buon fine");
           }
           
            
        } catch (SQLException ex) {
            Logger.getLogger(UtenteJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
