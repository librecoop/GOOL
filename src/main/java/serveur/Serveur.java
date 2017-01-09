package serveur;

import gool.GOOLCompiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;




/**
 * La classe Serveur.java contient les listeners utilis&eacute;s par glassfish pour reagir aux stimulus du r&eacute;seau.<br>
 * Elle consiste en methode et classes comment&eacute;es que glassfish utilise pour orienter ses r&eacute;actions.<br>
 * Par exemple le ServerEndPoint correspond a la classe qui sera utilis&eacute;e par toutes les demandes de connection passant par WWWglassfish/domaine/appli.<br>
 * Un autre exemple est le tag OnMessage qui &eacute;x&eacute;cute le corps de la methode associ&eacute;e a chaque fois qu'un message est d&eacute;t&eacute;ct&eacute; entrant. 
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
@ServerEndpoint("/appli")
public class Serveur {
	
	/**
	 * Methode appel&eacute;e en cas de connection &eacute;tablie avec un client.
	 * @param session -La session unique cr&eacute;&eacute;e pour le client
	 */
	@OnOpen
    public void onOpen(Session session){
        System.out.println("INFOS: " + session.getId() + " has log in."); 
        
    }
 
    /**
     * Methode appel&eacute;e en cas de r&eacute;ception d'un message client.
     * @param message -Le message envoy&eacute; par le client
     * @param session -La session unique cr&eacute;&eacute;e pour le client
     */
    @OnMessage
    public void onMessage(String message, Session session){
    	
    	ByteArrayOutputStream consoleToString = new ByteArrayOutputStream();
    	PrintStream flux = new PrintStream(consoleToString);
    	System.setErr(flux);
    	
    	//definition d'un delimiteur pour le parse
    	String delimiteur = "[§]";
    	
    	//tableau généré par le parser
    	String [] elements = message.split(delimiteur);    	 
    	
    	/* Un message entrant a, par exemple, pour structure: @§fr§Java§C++§contenu
    	 * Le parser va creer un tableau qui contiendra donc:
    	 * elements [0] == @
	 * elements [1] == fr
    	 * elements [2] == Java
    	 * elements [3] == C++
    	 * elements [4] == le contenu que l'on veut traiter
    	 * 
    	 * elements [0] est le type de message, permettant d'identifier les messages specifiques au traitements (@: pour les traitements, 1: pour les erreurs)
	 * elements [1] est la langue maternelle de l'utilisateur
    	 * elements [2] est la langue d'entrée désirée par l'utilisateur
    	 * elements [3] est la langue de sortie désirée par l'utilisateur
    	 * elements [4] est le contenu recupéré du champ d'entrée de l'utilisateur qui sera traité par le traducteur
    	 */
    	
    	if (elements[0].equals("@")) {
    		
    		String messageOut = new String("@");
    		
    		String messageErr = new String("");
    		
    		GOOLCompiler translator = new GOOLCompiler();
    		
    		Map<String , String> tabResults = new HashMap<String, String>();
    		
    		/*
    		 * Une message de retour a, par exemple, pour structure: @§contenu
    		 * Le parser pourra creer coté client un tableau qui contiendra donc:
    		 * elements [0] == @
    		 * elements [1] == le contenu traité que l'on renvoit au client
    		 * 
    		 * elements [0] est le type de message (@: pour les messages destinés a la sortie coté client, 1: pour les messages destinés aux erreurs coté client)
    		 * elements [1] est le contenu renvoyé au client apres traitement par l'application
    		 */
    		
    		//si le message vient d'un client français
    		if (elements [1].equals("fr")) {

    			try {
    				//appel du traducteur
    				tabResults = translator.launchHTMLTranslation(elements[2], elements[3], elements[4]);
    				//construction du message de retour
    				for (Map.Entry<String, String> entry : tabResults.entrySet()) {
    					
    					messageOut += "§" + entry.getKey() + "§" + entry.getValue();
    					
    				}
    				
    	    			session.getBasicRemote().sendText(messageOut); 
    	    		
    				messageErr = "Traitement correctement exécuté par le serveur";
    				
    				
    			} catch(Exception e) {
    				
    				System.err.println(e);
    				
    				messageErr = consoleToString.toString();
    				
    			}
				
    			try {
					session.getBasicRemote().sendText("1§" + messageErr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		}

    		//si le message vient d'un client anglais
    		if (elements [1].equals("en")) {
    			
			try {
    				//appel du traducteur
    				tabResults = translator.launchHTMLTranslation(elements[2], elements[3], elements[4]);
    				//construction du message de retour
    				for (Map.Entry<String, String> entry : tabResults.entrySet()) {
    					
    					messageOut += "§" + entry.getKey() + "§" + entry.getValue();
    					
    				}
    				
    	    			session.getBasicRemote().sendText(messageOut); 
    	    		
    				messageErr = "Server treatment properly executed";
    				
    				
    			} catch(Exception e) {
    				
    				System.err.println(e);
    				
    				messageErr = consoleToString.toString();
    				
    			}
				
    			try {
					session.getBasicRemote().sendText("1§" + messageErr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    			
    		}

	//on enleve les references vers les flux pour s'assurer de leur fermeture par le garbage collector
	consoleToString = null;
	flux = null;

	}

    }

    /**
     * Methode appel&eacute;e en cas de fermeture du socket par le client.
     * @param session -La session unique cr&eacute;&eacute;e pour le client
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("INFOS: " + session.getId() + " has log out.");
    }
}



