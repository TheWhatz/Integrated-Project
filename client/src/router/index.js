import { useUsers } from '../stores/users'
import { createRouter, createWebHistory } from 'vue-router'
import { useUser } from '../stores/user'

const history = createWebHistory(import.meta.env.VITE_BASE_PATH)
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/VLogin.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/VRegister.vue'),
  },
  {
    path: '/',
    name: 'Schedules',
    alias: '/schedules',
    meta: { requireAuth: true },
    component: () => import('../views/VSchedules.vue'),
  },
  {
    path: '/categories',
    name: 'Categories',
    meta: { requireAuth: true },
    component: () => import('../views/VCategories.vue'),
  },
  {
    path: '/categories/:categoryId',
    name: 'EditCategory',
    meta: { requireAuth: true },
    component: () => import('../views/VEditCategory.vue'),
  },
  {
    path: '/users',
    name: 'Users',
    meta: { requireAuth: true },
    component: () => import('../views/VUsers.vue'),
  },
  {
    path: '/users/:userId/edit',
    name: 'EditUser',
    props: true,
    meta: { requireAuth: true },
    component: () => import('../views/VEditUser.vue'),
    beforeEnter: async (to, from, next) => {
      const userId = parseInt(to.params.userId)

      if (isNaN(userId)) {
        return next('/not-found')
      }

      const userStore = useUsers()

      try {
        const user = await userStore.fetchUserByUserId(to.params.userId)
        to.params.user = user
        next()
      } catch (err) {
        return next('/not-found')
      }
    },
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    alias: '/not-found',
    component: () => import('../views/VNotFound.vue'),
  },
]

const router = createRouter({ history, routes })
router.beforeEach(async (to, from, next) => {
  const userStore = useUser()
  if (to.meta.requireAuth && !userStore.getToken()) {
    userStore.logout()
    return next('/login')
  }

  if (['Login', 'Register'].includes(to.name) && userStore.getToken()) {
    return next('/')
  }

  if (to.meta.requireAuth && !userStore.isAuthenticated) {
    try {
      await userStore.loadUser()
    } catch (err) {
      if (err.response.status === 401) {
        if (!userStore.getRefreshToken()) {
          return next('/login')
        }

        const res = await userStore.refreshToken()
        if (res.status !== 200) {
          userStore.logout()
          return next('/login')
        }
        await userStore.loadUser()
      }
    }
  }

  next()
})
export default router
