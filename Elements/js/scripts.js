var webSocket;
var optionIn;
var optionOut;
var content;
var errors;
var inputField;

// Initialization of variables relative to the different client fields and their contents
function initFields() {

	optionIn = document.getElementById("inputLang");
	optionOut = document.getElementById("outputLang");
	content = document.getElementById("inputTXT");	
	errors = document.getElementById("errorsTXT");

	inputField = document.getElementById("inputTXT");
	inputField.value = "";
}

// The only function called by the HTML part
function openSocket() {

	// Check navigator compatibility with web sockets
	if ("WebSocket" in window) {

		// initialization
		initFields();

		// socket to server creation
		webSocket = new WebSocket("ws://localhost:2009/www/appli");

		/*
		 * The socket.onX methods are implemented listeners
		 * Functions are associated to the listeners at creation
		 */

		// What happens when the client receives a valid socket opening
		webSocket.onopen = function (event) {
			errors.value = "Server connection established, you can translate.";
		};

		// What happens when the client receives a input message from the server
		webSocket.onmessage = function (event) {
			var message = event.data;
			var elements = message.split("§");		

			/*
			 * Server message example:
			 * @§content
			 * The parser will create a table with:
			 * elements [0] == id of the message (@ = output message; 1 = error message)
			 * elements [1] == processed content or error message
			 *
			 * Depending on the id, the message is sent to the output or to the error
			 */

			if (elements [0] === "@") {

				var shift = 2;
				var container = document.getElementById('output');

				container.innerHTML = '';

				var article = document.createElement('div');
				article.setAttribute('class','tabs');

				container.appendChild(article);

				for (var i = 1; i < elements.length ; i+=2) {

					var section = document.createElement('section');
					section.setAttribute('id','tab' + i);

					var title = document.createElement('span');
					title.style.left = shift + 'px';
					title.innerHTML = elements[i]; 
					title.setAttribute('class','spanTitre');

					var titleLink = document.createElement('a');
					titleLink.setAttribute('href','#tab' + i);

					/*titleLink.setAttribute('onclick','return false');*/

					var txtAreaNew = document.createElement('pre');

					txtAreaNew.innerHTML = elements[i+1];
					txtAreaNew.setAttribute('class', 'prettyprint');

					article.appendChild(section);
					section.appendChild(titleLink);
					titleLink.appendChild(title);
					section.appendChild(txtAreaNew);

					shift += 82;
				}

				prettyPrint();
				window.location.hash='#tab1';

			}

			if (elements [0] === "1") {

				errors.value = elements [1];
			}
		};

		// What happens when the client receives a socket closure
		webSocket.onclose = function (evenement) {
			errors.value = "Server connection closed, you cannot translate.";

		};

		// What happens when the client receives an error
		webSocket.onerror = function (evenement) {
			alert("Server connection error: " + evenement.data);

		};

	} else {

		// If the navigator is unable to deal with web sockets
		var displayArea = document.getElementById("centralBlock");
		var errorTxt = document.createElement('span');
		var bold = document.createElement('b');
		bold.innerHTML = 'Your browser does not support the necessary technology for the program to properly run !';
		errorTxt.appendChild(bold);

		displayArea.innerHTML = '';
		displayArea.style.padding = '20px';
		displayArea.appendChild(errorTxt);

	}

}

// Function called by the translate button
// It gets the content of the text field and creates a character chain with appropriate delimiters.
// The chain is then sent to the server for process.
function send() {

	if (content.value !== '') {
		var texte = "@§" + "fr§" + optionIn.value + "§" + optionOut.value + "§" + content.value;
		webSocket.send(texte);
	} else {
		errors.value = "Please, fill in the input langage field...";
	}
}

// Swap input and output languages
function swap() {

	var oIn = optionIn.value;

	optionIn.value = optionOut.value;
	optionOut.value = oIn;

}