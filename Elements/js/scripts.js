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
			var message = event.data.replace(/</g,"&lt").replace(/>/g,"&gt");
			var elements = message.split("§");		
			//document.getElementById('debug').innerHTML += "<br>" + message;
			
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
				var container = document.getElementById('output');

				container.innerHTML = '';

				var codeTab = document.createElement('div');
				codeTab.setAttribute('class','tabs');

				container.appendChild(codeTab);
				
				for (var i = 1; i < elements.length ; i+=2) {
					var button = document.createElement('button');
					button.setAttribute('class','tablinks');
					button.setAttribute('onClick','openOutputTag(event, \"file#' + i + '\")');
					button.innerHTML = elements[i];
					codeTab.appendChild(button);
				}

				for (var i = 1; i < elements.length ; i+=2) {
					var codeContent = document.createElement('div');
					codeContent.setAttribute('id','file#' + i);
					codeContent.setAttribute('class','tabcontent');
									
					var title = document.createElement('h3');
					title.innerHTML = elements[i]; 
					
					var content = document.createElement('pre');
					content.innerHTML = elements[i+1].replace(/^\s*/, "");
					content.setAttribute('class', 'prettyprint');

					container.appendChild(codeContent);
					codeContent.appendChild(title);
					codeContent.appendChild(content);
				}
				openOutputTag(event, "file#1");
				prettyPrint();
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
		var texte = "@§" + "en§" + optionIn.value + "§" + optionOut.value + "§" + content.value;
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

function openOutputTag(evt, tagName) {
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(tagName).style.display = "block";
    evt.currentTarget.className += " active";
} 