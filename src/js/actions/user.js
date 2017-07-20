import axios from "axios";

import { USER_CONSTANTS as c } from "../utils/constants";
import { getHeaders } from "../utils/restUtil";

axios.interceptors.response.use(
  function(response) {
    // Do something with response data
    return response;
  },
  function(error) {
    if (error.response.status == 401) {
      console.log("Session expired");
      delete sessionStorage.session;
    }
    return Promise.reject(error);
  }
);

export function authenticate(username, password) {
  console.log("authenticate");

  return function(dispatch) {
    dispatch({ type: c.USER_AUTH_PROGRESS });
    const config = {
      method: "post",
      url: window.baseUrl + "/oauth/token",
      headers: { Authorization: "Basic " + btoa("client:secret") },
      params: {
        grant_type: "password",
        username: username,
        password: password
      }
    };

    axios(config)
      .then(response => {
        if (response.status == 200) {
          dispatch({
            type: c.USER_AUTH_SUCCESS,
            payload: { username, data: response.data }
          });
        } else {
          console.log(response);
        }
      })
      .catch(err => {
        console.log(err);
        dispatch({ type: c.USER_AUTH_FAIL });
      });
  };
}

export function searchUser(email) {
  console.log("addUser");
  let url = window.serviceHost;
  if (email.includes("@")) {
    url += "/users/search/byEmail?email=" + email;
  } else {
    url += "/users/search/byUsername?username=" + email;
  }

  return function(dispatch) {
    dispatch({ type: c.USER_BUSY });
    axios
      .get(url, {
        headers: getHeaders()
      })
      .then(response => {
        console.log(response);
        if (response.status == 200) {
          dispatch({
            type: c.USER_SEARCH_SUCCESS,
            payload: { user: response.data }
          });
        }
      })
      .catch(err => {
        console.log(err);
        dispatch({ type: c.USER_SEARCH_FAIL });
      });
  };
}

export function addUser(user) {
  console.log("addUser");

  return function(dispatch) {
    dispatch({ type: c.USER_BUSY });
    axios
      .post(window.serviceHost + "/users", JSON.stringify(user), {
        headers: getHeaders()
      })
      .then(response => {
        console.log(response);
        if (response.status == 201) {
          dispatch({
            type: c.USER_ADD_SUCCESS,
            payload: { user: response.data }
          });
        }
      })
      .catch(err => {
        console.log(err);
        if (err.response.status == 400) {
          dispatch({
            type: c.USER_BAD_REQUEST,
            payload: { errors: err.response.data }
          });
        } else if (err.response.status == 409) {
          alert(err.response.data.message);
          dispatch({ type: c.USER_ADD_FAIL });
        } else {
          dispatch({ type: c.USER_ADD_FAIL });
        }
      });
  };
}

export function updateUser(user) {
  console.log("updateUser");
  return function(dispatch) {
    dispatch({ type: c.USER_BUSY });
    axios
      .put(window.serviceHost + "/users/" + user.id, JSON.stringify(user), {
        headers: getHeaders()
      })
      .then(response => {
        console.log(response);
        if (response.status == 200) {
          dispatch({
            type: c.USER_EDIT_SUCCESS,
            payload: { user: response.data }
          });
        }
      })
      .catch(err => {
        console.log(err.response);
        if (err.response.status == 400) {
          dispatch({
            type: c.USER_BAD_REQUEST,
            payload: { errors: err.response.data }
          });
        } else if (err.response.status == 409) {
          alert(err.response.data.message);
          dispatch({ type: c.USER_EDIT_FAIL });
        } else {
          dispatch({ type: c.USER_EDIT_FAIL });
        }
      });
  };
}

export function changePassword(credential) {
  console.log("updateUser");
  return function(dispatch) {
    //dispatch({type: c.USER_AUTH_PROGRESS});
    axios
      .put(
        window.serviceHost +
          "/misc/change_password?email=" +
          credential.email +
          "&oldPassword=" +
          credential.oldPassword +
          "&newPassword=" +
          credential.newPassword,
        null,
        { headers: getHeaders() }
      )
      .then(response => {
        console.log(response);
        if (response.status == 200) {
          if (response.data.status == "SUCCESS") {
            dispatch({
              type: c.USER_CHANGE_PASSWD,
              payload: { message: "Password changed successfully." }
            });
          } else {
            dispatch({
              type: c.USER_CHANGE_PASSWD,
              payload: { message: "Incorrect credential. try again!" }
            });
          }
          //dispatch({type: c.USER_EDIT_SUCCESS, payload: {user: response.data}});
        }
      })
      .catch(err => {
        console.log(err);
        dispatch({
          type: c.USER_CHANGE_PASSWD,
          payload: { message: "Some error occured" }
        });
      });
  };
}

export function removeUser(user) {
  console.log("removeUser");
  return function(dispatch) {
    console.log(user);

    axios
      .delete(window.serviceHost + "/users/" + user.id, {
        headers: getHeaders()
      })
      .then(response => {
        console.log(response);
        dispatch({ type: c.USER_REMOVE_SUCCESS, payload: { user: user } });
      })
      .catch(err => {
        console.log(err);
        if (err.response.status == 409) {
          alert(err.response.data.message);
          dispatch({ type: c.USER_REMOVE_FAIL });
        } else {
          dispatch({ type: c.USER_REMOVE_FAIL });
        }
      });
  };
}

export function syncUser(after) {
  console.log("syncUser");

  return function(dispatch) {
    dispatch({ type: c.USER_BUSY });
    axios
      .get(window.serviceHost + "/users?after=" + after, {
        headers: getHeaders()
      })
      .then(response => {
        console.log(response);
        if (response.status == 200) {
          dispatch({
            type: c.USER_SYNC_SUCCESS,
            payload: { resp: response.data.content }
          });
        }
      })
      .catch(err => {
        console.log(err);
        dispatch({ type: c.USER_SYNC_FAIL });
      });
  };
}
