var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var user = "1234567890";
    var socket = new SockJS('http://localhost:8001/websocket?user=' + user);
    stompClient = Stomp.over(socket);
    
    stompClient.connect({login:user}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        },{'X-Authorization': 'token-auth'});

        stompClient.subscribe('/topic/time', function (greeting) {
            showTime(JSON.parse(greeting.body).content);
        },{'X-Authorization': 'token-auth'});
    },function(error){
      alert("connet error");
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/ws/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    console.log(message);
    $("#greetings").html("<tr><td>" + message + "</td></tr>");
}

function showTime(time) {
    console.log(time);
    $("#time").text(time);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });

    //connect();
});

