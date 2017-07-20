import { NAV_ACTIVATE } from "../utils/constants";
const initialState = {
  active: false,
  itemsAdmin: [
    { path: "/dashboard", label: "Dashboard" },
    { path: "/user", label: "User" },
    { path: "/test", label: "Test" }
  ],
  itemsUser: [
    { path: "/dashboard", label: "Dashboard" },
    { path: "/test", label: "Test" }
  ]
};

const handlers = {
  [NAV_ACTIVATE]: (_, action) => ({ active: action.payload.active })
};

export default function nav(state = initialState, action) {
  let handler = handlers[action.type];
  if (!handler) return state;
  return { ...state, ...handler(state, action) };
}
