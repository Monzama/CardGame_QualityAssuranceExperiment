var stompClient = null;
const consoleDiv = document.getElementById("console");
const inputBar = document.getElementById("input-bar");
const continueButton = document.getElementById("continue-button"); // Select the Continue button

if (!consoleDiv || !inputBar) {
    console.error("consoleDiv or inputBar not found in the DOM");
}

let isConnected = false;

function connect() {
    if (isConnected) return; // Prevent duplicate connections
    var socket = new SockJS('http://127.0.0.1:8080/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        isConnected = true;

        // Subscribe to console topic
        stompClient.subscribe('/topic/console', function (message) {
            console.log("Message from server: " + message.body);

            if (message.body.trim() === "clear-console") {
                while (consoleDiv.firstChild) {
                    consoleDiv.removeChild(consoleDiv.firstChild);
                }
            } else {
                const outputLine = document.createElement("div");
                outputLine.className = "console-line";
                outputLine.textContent = message.body;
                consoleDiv.appendChild(outputLine);
            }
        });

        // Run the reset command after connection is established
        sendCommand("reset");
    }, function (error) {
        console.error('Connection error:', error);
    });
}
window.onload = function () {
    connect();
};
function sendResponse(response) {
    if (stompClient) {
        stompClient.send("/app/send-response", {}, response);
    }
}

function sendCommand(command) {
    if (stompClient) {
        stompClient.send("/app/send-command", {}, command);
    }
}

inputBar.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        const command = inputBar.value.trim();
            if (command === "start"){
                inputBar.value = ""; // Clear the input field
                sendCommand(command);
            }else if (command === ""){
                sendResponse("ENTER");
            }else if (command === "clear"){
                while (consoleDiv.firstChild) {
                    consoleDiv.removeChild(consoleDiv.firstChild);
                }
                inputBar.value = "";
            }else {
                inputBar.value = ""; // Clear the input field
                sendResponse(command); // Send the response to the server
            }
    }
});
