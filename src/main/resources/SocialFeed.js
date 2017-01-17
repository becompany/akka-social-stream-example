"use strict";

$(function() {
  var socket = new WebSocket("ws://" + location.host + "/feed");
  var $container = $("#feed");

  socket.onmessage = function(event) {
    var p = $("<p>").text(event.data);
    $container.prepend(p);
  }

});

