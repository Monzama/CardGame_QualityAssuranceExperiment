var stompClient = null;
const consoleDiv = document.getElementById("console");
const inputBar = document.getElementById("input-bar");

function connect() {
    var socket = new SockJS('http://127.0.0.1:8080/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Subscribe to /topic/public
        stompClient.subscribe('/topic/public', function (message) {
            showMessage(JSON.parse(message.body));
        });

        // Subscribe to /topic/console
        stompClient.subscribe('/topic/console', function (message) {
            console.log("Message from server: " + message.body);

            if (message.body === "clear-console") {
                // Clear the console UI
                while (consoleDiv.firstChild) {
                    consoleDiv.removeChild(consoleDiv.firstChild);
                }
            } else {
                // Append the message to the console UI
                const outputLine = document.createElement("div");
                outputLine.className = "console-line";
                outputLine.textContent = message.body;
                consoleDiv.appendChild(outputLine);
            }
        });
    });
}

function sendMessage() {
    var messageContent = document.getElementById("message").value;
    if (messageContent && stompClient) {
        var chatMessage = {
            sender: document.getElementById("username").value,
            content: messageContent,
            type: 'CHAT'
        };
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        document.getElementById("message").value = '';
    }
}

function sendCommand(command) {
    if (stompClient) {
        stompClient.send("/app/send-command", {}, command);
    }
}

function showMessage(message) {
    var messageElement = document.createElement('li');
    messageElement.appendChild(document.createTextNode(message.sender + ": " + message.content));
    document.getElementById('messages').appendChild(messageElement);
}

window.onload = function () {
    connect();

    // Add event listener for the input bar
    inputBar.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            const command = inputBar.value.trim();
            if (command) {
                sendCommand(command); // Send the command to the server
                inputBar.value = ""; // Clear the input field
            }else{
                sendCommand("ENTER"); // Process newline for enter only operations
            }
        }
    });
};
