package traitements;

/**
 * La classe Traitement.java est le simulacre de modele qui sera remplac&eacute; par GOOL.<br>
 * Dans le cadre des tests elle se contente de r&eacute;cup&eacute;rer une string, et de lui appliquer un traitement simple, puis de retourner la String trait&eacute;e.
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class Traitement {

	private static Traitement instance = null;
	
	/**
	 * Constructeur de la classe
	 */
	public Traitement () {
		
		instance = this;
		
	}
	
	/**
	 * Accesseur qui s'assure que l'on accede bien a une instance unique de Traitement.
	 * @return L'instance unique de Traitement.
	 */
	public static Traitement getInstance () {
		
		if (instance == null) {
			
			try {
				throw new Exception ("Il est necesaire d'instancier l'application une premiere fois.");
			} catch (Exception e) {
				System.out.println("Erreur: Instance de traitement == " + e.getMessage());
			}
			
		}
		
		return instance;
		
	}
	
	/**
	 * Methode de traitement a propement dite.
	 * @param message le message a traiter.
	 * @return Une String trait&eacute;e par la methode.
	 */
	public String traitements (String message) {
		
		String traitement = "";
		
		traitement = message + " => Traite par l'application";
		
		return traitement;
		
	}
	
}
