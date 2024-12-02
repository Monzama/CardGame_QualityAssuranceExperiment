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
            } else if (message.body.startsWith("player-status:")) {
                const playerData = message.body.replace("player-status:", "").trim();
                const [playerName, status] = playerData.split(":");
                updatePlayerStatus(playerName.trim(), status.trim());
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

function updatePlayerStatus(playerName, status) {
    let playerDiv = document.getElementById(`status-${playerName}`);
    if (!playerDiv) {
        playerDiv = document.createElement("div");
        playerDiv.id = `status-${playerName}`;
        document.getElementById("status").appendChild(playerDiv);
    }
    playerDiv.textContent = `${playerName}: ${status}`;
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
function sendTest(command) {
    if (stompClient) {
        stompClient.send("/app/test-setup", {}, command);
    }
}
inputBar.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        const outputLine = document.createElement("div");
        outputLine.className = "console-line";
        outputLine.textContent = inputBar.value;
        consoleDiv.appendChild(outputLine);
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
            }else if (command === "setup_scenario1"){
                inputBar.value = "";
                sendTest("setup_scenario1");
            }else if (command === "setup_scenario2"){
                inputBar.value = "";
                sendTest("setup_scenario2");
            }else if (command === "setup_scenario3"){
                inputBar.value = "";
                sendTest("setup_scenario3");
            }else if (command === "setup_scenario4"){
                inputBar.value = "";
                sendTest("setup_scenario4");
            }else if (command === "display hands"){
                inputBar.value = ""; // Clear the input field
                sendCommand(command);
            }else {
                inputBar.value = ""; // Clear the input field
                sendResponse(command); // Send the response to the server
            }
    }
});
