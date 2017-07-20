import "./index.scss";

import React from "react";
import ReactDOM from "react-dom";
import { Provider } from "react-redux";
import { Router, hashHistory } from "react-router";

import routes from "./routes";
import store from "./store";

(function() {
  // const baseUrl = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
  // window.serviceHost = baseUrl + "/api";
  // window.baseUrl = baseUrl;

  // window.baseUrl = "http://andonsystem.in/andon-system";
  // window.serviceHost = "http://andonsystem.in/andon-system/api";

  window.baseUrl = "http://localhost:8085";
  window.serviceHost = "http://localhost:8085/api";
})();

let element = document.getElementById("content");
ReactDOM.render(
  <div>
    <Provider store={store}>
      <Router routes={routes} history={hashHistory} />
    </Provider>
  </div>,
  element
);

document.body.classList.remove("loading");
