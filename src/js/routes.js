import Main from "./components/Main";
import Dashboard from "./components/Dashboard";
import Login from "./components/Login";
import Profile from "./components/Profile";
import User from "./components/user/User";
import UserAdd from "./components/user/UserAdd";
import UserEdit from "./components/user/UserEdit";

import Test from "./components/Test";

export default {
  path: '/',
  component: Main,
  indexRoute: {component: Login},
  childRoutes: [
    { path: 'dashboard', component: Dashboard},
    { path: 'user', component: User},
    { path: 'user/add', component: UserAdd},
    { path: 'user/edit', component: UserEdit},
    { path: 'profile', component: Profile},
    { path: 'test', component: Test}
  ]
};
