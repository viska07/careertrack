import { createRouter, createWebHistory } from 'vue-router'

import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Dashboard from '../views/Dashboard.vue'
import Applications from '../views/Applications.vue'
import ApplicationDetail from '../views/ApplicationDetail.vue'
import NotFound from '../views/NotFound.vue'

const router = createRouter({
  history: createWebHistory(),

  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },

    {
      path: '/login',
      name: 'Login',
      component: Login
    },

    {
      path: '/register',
      name: 'Register',
      component: Register
    },

    {
      path: '/dashboard',
      name: 'Dashboard',
      component: Dashboard,
      meta: {
        requiresAuth: true
      }
    },

    {
        path: '/applications',
        name: 'Applications',
        component: Applications,
        meta: {
            requiresAuth: true
        }
    },

    {
        path: '/applications/:id',
        name: 'ApplicationDetail',
        component: ApplicationDetail,
        meta: {
            requiresAuth: true
        }
    },

    {
        path: '/applications/:id',
        name: 'ApplicationDetail',
        component: ApplicationDetail,
        meta: {
            requiresAuth: true
        }
    },

    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound
    }

  ]
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  const isAuthenticated = Boolean(token)

  if (to.meta.requiresAuth && !isAuthenticated) {
    return '/login'
  }

  if (
    (to.path === '/login' ||
      to.path === '/register') &&
    isAuthenticated
  ) {
    return '/dashboard'
  }

  return true
})

export default router