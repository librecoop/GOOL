package applicationsServeur;

import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JFrame;

import org.glassfish.embeddable.*;


import vue.IHMServeur;

/**
 * La classe ServeurIntegre.java est le point d'entr&eacute;e du programme.<br>
 * Cette classe int&eacute;gre un serveur blackfish, le parametre et le lance. C'est elle aussi qui se charge d'instancier le traducteur.
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class ServeurIntegre {

	private int port;
	private AtomicBoolean initialise = new AtomicBoolean();
	private GlassFish glassfish;

	/**
	 * Constructeur de la classe avec d&eacute;finition du port
	 * @param port
	 */
	public ServeurIntegre(int port) {
		this.port = port;
	}

	/**
	 * Methode qui parametre un serveur int&eacute;gr&eacute; glassfish
	 * @return Un serveur int&eacute;gr&eacute; configur&eacute;
	 * @throws Exception
	 */
	public ServeurIntegre init() throws Exception{
		if ( initialise.get() ) {
			throw new RuntimeException("Server already started.");
		}
		BootstrapProperties bootstrapProperties = new BootstrapProperties();
		GlassFishRuntime glassfishRuntime = GlassFishRuntime.bootstrap(bootstrapProperties);

		GlassFishProperties glassfishProperties = new GlassFishProperties();
		glassfishProperties.setPort("http-listener", port);
		glassfish = glassfishRuntime.newGlassFish(glassfishProperties);
		initialise.set(true);
		return this;
	}

	/**
	 * Methode qui verifie la bonne initialisation du serveur
	 */
	private void check() {
		if ( !initialise.get() ) {
			throw new RuntimeException("Server is not started.");
		}
	}

	
	/**
	 * Methode qui demarre un serveur param&eacute;tr&eacute; en v&eacute;rifiant qu'il n'y ai pas une instance du serveur d&eacute;j&agrave; d&eacute;mar&eacute;e dans le contexte.
	 * @return le serveur param&eacute;tr&eacute; et d&eacute;marr&eacute;
	 * @throws Exception
	 */
	public ServeurIntegre start() throws Exception{
		check();
		glassfish.start();
		glassfish.getDeployer().deploy(new File("build"));
		return this;
	}

	/**
	 * Methode qui arrete l'instance du serveur en cours de fonctionnement
	 * @return le serveur arr&eacute;t&eacute;
	 * @throws Exception
	 */
	public ServeurIntegre stop() throws Exception{
		check();
		glassfish.stop();
		return this;
	}

	/**
	 * Entr&eacute;e du programme
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		
		
		
		JFrame serveur = new IHMServeur();
		
		//lancement du serveur
		final ServeurIntegre runner = new ServeurIntegre(2009).init().start();
		
		/*
		 * Pour des raison de securité la croix de fermeture de la fenetre se voit associer un nouveau listenner
		 * on s'assure de lancer la commande de fermeture du serveur intégré avant de lancer la fermeture du programme
		 */
		serveur.addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent we) {
				  try {
					//fermeture serveur
					runner.stop();
					
					//fermeture programme
					System.exit(0);
				} catch (Exception e) {
					System.out.println("ERROR: **Server can't safely stop.**");
				}
			}
		});
	}	
}
