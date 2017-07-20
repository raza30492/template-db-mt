import { USER_CONSTANTS as c } from "../utils/constants";

const initialState = {
  lastSync: undefined, // last sync done. time value in long
  authProgress: false,
  authenticated: false,
  busy: false,
  fetching: false,
  adding: false,
  editing: false,
  syncUser: false, // If it is time to sync user
  users: [],
  user: {},
  filter: {},
  sort: "name:asc",
  toggleStatus: true,
  message: "",
  error: {}
};

const handlers = {
  [c.USER_BUSY]: (_, action) => ({ busy: true }),
  [c.INITIALIZE_USER]: (_, action) => ({
    users: action.payload.resp.users,
    lastSync: action.payload.resp.lastSync,
    toggleStatus: !_.toggleStatus
  }),
  [c.USER_SYNC_SUCCESS]: (_, action) => {
    let users = _.users;
    const resp = action.payload.resp;
    _.lastSync = resp.lastSync;
    let idx;
    resp.users.forEach(user => {
      idx = users.findIndex(i => i.id == user.id);
      if (idx == -1) users.push(user);
      else users[idx] = user;
    });
    return {
      users,
      toggleStatus: !_.toggleStatus,
      busy: false,
      syncUser: false
    };
  },
  [c.USER_SYNC_FAIL]: (_, action) => ({ busy: false, syncUser: false }),
  [c.USER_AUTH_PROGRESS]: (_, action) => ({
    authProgress: true,
    authenticated: false
  }),
  [c.USER_AUTH_SUCCESS]: (_, action) => {
    sessionStorage.email = action.payload.username;
    window.sessionStorage.access_token = action.payload.data.access_token;
    window.sessionStorage.refresh_token = action.payload.data.refresh_token;
    return { authProgress: false, authenticated: true };
  },
  [c.USER_AUTH_FAIL]: (_, action) => ({ authProgress: false }),
  [c.USER_SEARCH_SUCCESS]: (_, action) => {
    let user = action.payload.user;
    sessionStorage.userId = user.id;
    sessionStorage.username = user.name;
    sessionStorage.role = user.role;
    sessionStorage.session = true;
    return { busy: false };
  },
  [c.USER_ADD_FORM_TOGGLE]: (_, action) => ({
    adding: action.payload.adding,
    error: {}
  }),
  [c.USER_ADD_SUCCESS]: (_, action) => {
    return { adding: false, syncUser: true };
  },
  [c.USER_ADD_FAIL]: (_, action) => ({ adding: false, busy: false }),
  [c.USER_EDIT_FORM_TOGGLE]: (_, action) => ({
    editing: action.payload.editing,
    user: action.payload.user,
    error: {}
  }),
  [c.USER_EDIT_SUCCESS]: (_, action) => {
    let users = _.users;
    let i = users.findIndex(u => u.id == action.payload.user.id);
    users[i] = action.payload.user;
    return {
      editing: false,
      toggleStatus: !_.toggleStatus,
      users: users,
      busy: false
    };
  },
  [c.USER_EDIT_FAIL]: (_, action) => ({ editing: false, busy: false }),
  [c.USER_REMOVE_SUCCESS]: (_, action) => {
    let users = _.users.filter(u => u.id != action.payload.user.id);
    return { toggleStatus: !_.toggleStatus, users: users };
  },
  [c.USER_SORT]: (_, action) => ({
    sort: action.payload.sort,
    toggleStatus: !_.toggleStatus
  }),
  [c.USER_FILTER]: (_, action) => ({
    filter: action.payload.filter,
    toggleStatus: !_.toggleStatus
  }),
  [c.USER_CHANGE_PASSWD]: (_, action) => ({ message: action.payload.message }),
  [c.USER_BAD_REQUEST]: (_, action) => {
    let error = {};
    action.payload.errors.forEach(err => {
      error[err.field] = err.message;
    });
    return { error, busy: false };
  }
};

export default function user(state = initialState, action) {
  let handler = handlers[action.type];
  if (!handler) return state;
  return { ...state, ...handler(state, action) };
}
