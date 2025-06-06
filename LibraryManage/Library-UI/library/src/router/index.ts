import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import LoginTest from "@/views/LoginTest.vue";
import NavView from "@/views/NavView.vue";
import HomeView from "@/views/HomeView.vue";
import BookView from "@/views/BookView.vue";
import BorrowView from "@/views/BorrowView.vue";
import ReturnView from "@/views/ReturnView.vue";
import OvertimeView from "@/views/OvertimeView.vue";
import UserView from "@/views/UserView.vue";
import SystemView from "@/views/SystemView.vue";
import LoginViewUser from "@/views/LoginViewUser.vue";
import NavViewUser from "@/views/NavViewUser.vue";
import BookViewUser from "@/views/BookViewUser.vue";
import UserViewUser from "@/views/UserViewUser.vue";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "登录",
    component: LoginTest,
    meta: { show: false },
  },
  {
    path: "/LoginTest",
    name: "LoginTest",
    component: LoginTest,
    meta: { show: false },
  },
  {
    path: "/loginUser",
    name: "用户登录",
    component: LoginViewUser,
    meta: { show: false },
  },
  {
    path: "/nav",
    name: "导航",
    component: NavView,
    redirect: "/home",
    meta: { show: false },
    children: [
      {
        path: "/home",
        name: "主页",
        meta: { show: true },
        components: { home: HomeView },
      },
    ],
  },
  {
    path: "/nav",
    name: "图书",
    component: NavView,
    meta: { show: true },
    children: [
      {
        path: "/book",
        name: "图书管理",
        meta: { show: true },
        components: { book: BookView },
      },
      {
        path: "/borrow",
        name: "借阅图书",
        meta: { show: true },
        components: { borrow: BorrowView },
      },
      {
        path: "/return",
        name: "归还图书",
        meta: { show: true },
        components: { return: ReturnView },
      },
      {
        path: "/overtime",
        name: "超时查询",
        meta: { show: true },
        components: { overtime: OvertimeView },
      },
    ],
  },
  {
    path: "/nav",
    name: "用户",
    component: NavView,
    meta: { show: true },
    children: [
      {
        path: "/user",
        name: "用户管理",
        meta: { show: true },
        components: { user: UserView },
      },
    ],
  },
  {
    path: "/nav",
    name: "其他",
    component: NavView,
    meta: { show: true },
    children: [
      {
        path: "/system",
        name: "系统设置",
        meta: { show: true },
        components: { system: SystemView },
      },
    ],
  },
  {
    path: "/navUser",
    name: "导航User",
    component: NavViewUser,
    redirect: "/bookUser",
    meta: { show: false },
    children: [
      {
        path: "/bookUser",
        name: "图书信息",
        component: BookViewUser,
        meta: { show: true },
      },
      {
        path: "/userUser",
        name: "用户信息",
        component: UserViewUser,
        meta: { show: true },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
});

export default router;
