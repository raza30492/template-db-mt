import axios from "axios";

import {MISC_CONSTANTS as m, USER_CONSTANTS as u, NAV_ACTIVATE} from  '../utils/constants';
import {getHeaders} from  '../utils/restUtil';

axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.status == 401) {
    delete sessionStorage.session;
  }
  return Promise.reject(error);
});

export function initialize () {
  console.log("initialize()");

  return function (dispatch) {

    axios.all([
      axios.get(window.serviceHost + '/users', {headers: getHeaders()})
    ])
    .then(axios.spread(function (users) {
      dispatch({type: u.INITIALIZE_USER, payload: { resp: users.data.content}});
      dispatch({type: m.STORE_INITIALIZED});

    }))
    .catch( (err) => {
      console.log(err); 
    });

  };
}

export function navActivate (active) {
  return { type: NAV_ACTIVATE, payload: {active}};
}
