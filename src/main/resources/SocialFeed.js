"use strict";

$(function() {

  var url = "ws://" + location.host + "/feed";
  var $loading = $("#loading");
  var $container = $("#feed");

  function setupFeed() {
    var socket = new WebSocket(url);

    socket.onopen = function(event) {
      $loading.remove();
    }

    socket.onmessage = function(event) {
      var p = $("<p>").html(event.data);
      $container.prepend(p);
    }

    socket.onclose = function() {
      setTimeout(setupFeed, 1000);
    };

  }

  setupFeed();

});

