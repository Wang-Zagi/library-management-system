import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    redirect:"book",
    component: Layout,
    children:[
      {
        path:'user',
        name:'user',
        component:() => import("@/views/User")
      },
      {
        path: 'book',
        name: 'book',
        component: () => import("@/views/Book")
      },
      {
        path: 'borrow',
        name: 'borrow',
        component: () => import("@/views/Borrow")
      },
      {
        path: 'return',
        name: 'return',
        component: () => import("@/views/Return")
      },
      {
        path: 'borrowRecord',
        name: 'BorrowRecord',
        component: () => import("@/views/BorrowRecord")
      },
      {
        path:'dashboard',
        name:'Dashboard',
        component:() => import("@/views/Dashboard")
      },
      {
        path: 'borrowingBook',
        name: 'BorrowingBook',
        component: () => import("@/views/BorrowingBook.vue")
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import("@/views/UserInfo")
      },
      {
        path: 'password',
        name: 'Password',
        component: () => import("@/views/Password")
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import("@/views/Register")
  },
  {
    path: '/forget',
    name: 'Forget',
    component: () => import("@/views/Forget")
  },


]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
