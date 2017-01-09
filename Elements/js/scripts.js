var webSocket;
var optionIn;
var optionOut;
var contenu;
var erreurs;
var champsEntree;

//fonction qui intialise les variables relatives aux differents champs du client ainsi que le contenu de certains
function initFields() {

	optionIn = document.getElementById("langagesEntree");
	optionOut = document.getElementById("langagesSortie");
	contenu = document.getElementById("entreeTXT");	
	erreurs = document.getElementById("erreursTXT");

	champsEntree = document.getElementById("entreeTXT");
	champsEntree.value = "";
}

//la seule fonction a appeler au niveau HTML
function ouvrirSocket() {
	
	//on verifie la compatibilité du navigateur avec les websockets
	if ("WebSocket" in window) {

		//appel de la methode d'initialisation
		initFields();

		//creation d'un socket vers le serveur
		webSocket = new WebSocket("ws://localhost:2009/www/appli");
	
		/*
		*Les Methodes socket.onX sont des listeners implémentés
		*On associe des methodes JS en implementant les listeners
		*
		*Exemple: WebSocket.onopen
		* = function (evenement) créé une fonction anonyme qui sera exécutée quant le socket detectera un message du serveur
		* (evenement) correspond a l'objet transmis par le serveur en reponse, on peut acceder au contenu via evenement.data
		*/

		//ce qui se passe quand le client detecte une ouverture valide de socket
		webSocket.onopen = function (evenement) {
		
			erreurs.value = "Connexion ouverte, vous pouvez traduire.";
		
		};
	
		//ce qui se passe quand le client detecte un message entrant de la part du serveur
		webSocket.onmessage = function (evenement) {
				
			var message = evenement.data;

			var elements = message.split("§");		

			/*
			*Exemple de message serveur:
			*@§contenu
			*Le parser va creer un tableau qui contiendra
			*elements [0] == identifiant de message (@ = message sortie ; 1 = message erreurs)
			*elements [1] == contenu traité ou message d'erreur
			*
			*Selon l'identifiant message ou envoie le contenu soit vers la sortie soit vers les erreurs
			*/

			if (elements [0] === "@") {

				var decalage = 2;
				var container = document.getElementById('sortie');

				container.innerHTML = '';

				var article = document.createElement('div');
				article.setAttribute('class','tabs');

				container.appendChild(article);

				for (var i = 1; i < elements.length ; i+=2) {

					var section = document.createElement('section');
					section.setAttribute('id','tab' + i);
				
					var titre = document.createElement('span');
					titre.style.left = decalage + 'px';
					titre.innerHTML = elements[i]; 
					titre.setAttribute('class','spanTitre');

					var lienTitre = document.createElement('a');
					lienTitre.setAttribute('href','#tab' + i);

					/*lienTitre.setAttribute('onclick','return false');*/

					var txtAreaNouveau = document.createElement('pre');

					txtAreaNouveau.innerHTML = elements[i+1];
					txtAreaNouveau.setAttribute('class', 'prettyprint');

					article.appendChild(section);
					section.appendChild(lienTitre);
					lienTitre.appendChild(titre);
					section.appendChild(txtAreaNouveau);

					decalage += 82;
				}

				prettyPrint();
				window.location.hash='#tab1';

			}

			if (elements [0] === "1") {
	
				erreurs.value = elements [1];
			}
		};

		//ce qui se passe quand le client detecte une fermeture du socket
		webSocket.onclose = function (evenement) {
			erreurs.value = "Fermeture de la connnexion avec le serveur, vous ne pouvez pas traduire.";
		
		};
	
		//ce qui se passe quant une erreur a lieu au niveau du socket
		webSocket.onerror = function (evenement) {
			alert("Erreur de connexion avec le serveur: " + evenement.data);
		
		};

	} else {

		//si le navigateur ne supporte pas les websockets on informe l'utilisateur
		var zoneAffichage = document.getElementById("blocCentreur");
		var erreurTxt = document.createElement('span');
		var bold = document.createElement('b');
		bold.innerHTML = 'Votre navigateur ne prend pas en charge certaines fonctionnalités nécessaires à la bonne marche du programme !';
		erreurTxt.appendChild(bold);
		
		zoneAffichage.innerHTML = '';
		zoneAffichage.style.padding = '20px';
		zoneAffichage.appendChild(erreurTxt);

	}

}

//fonction appelée par le bouton "traduire"
//elle va recuperer le contenu du champ d'entrée et creer une chaine de characteres avec delimiteurs
//la chaine créé est envoyée via le socket au serveur pour traitement.
function envoyer() {
	
	if (contenu.value !== '') {

		//on recupere le contenu du champ d'entrée et on y ajoute les delimiteurs avec les parametres
		var texte = "@§" + "fr§" + optionIn.value + "§" + optionOut.value + "§" + contenu.value;
	
		//envoi de la chaine de characteres créée.
		webSocket.send(texte);

	} else {

		erreurs.value = "Veuillez renseigner le champ d'entrée";

	}
}

//fonction qui gere l'intervertion des langages d'entree/sortie
function swap() {

	var oIn = optionIn.value;
	
	optionIn.value = optionOut.value;
	optionOut.value = oIn;

}
