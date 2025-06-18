import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('../views/HomeView.vue'),
      props: {
        type: 'normal',
      },
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/LoginView.vue'),
    },
    {
      path: '/free',
      name: 'Free',
      component: () => import('../views/HomeView.vue'),
      props: {
        type: 'free',
      },
    },
    {
      path: '/vip',
      name: 'Vip',
      component: () => import('../views/HomeView.vue'),
      props: {
        type: 'vip',
      },
    },
    {
      path: '/forgotPwd',
      name: 'ForgotPwd',
      component: () => import('@/views/ForgotPwdView.vue'),
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/RegisterView.vue'),
    },
    {
      path: '/payfor',
      name: 'Pay',
      component: () => import('@/views/PayView.vue'),
    },
    {
      path: '/rank',
      name: 'Rank',
      component: () => import('@/views/MovieRankView.vue'),
    },
    {
      path: '/classify',
      name: 'Classify',
      component: () => import('@/views/ClassifyView.vue'),
    },
    {
      path: '/moviePlay',
      name: 'MoviePlay',
      component: () => import('@/views/PlayView.vue'),
    },
    {
      path: '/hot',
      name: 'Hot',
      component: () => import('@/views/HotRankingView.vue'),
    },
    {
      path: '/movieAboutPlayer',
      name: 'MovieAboutPlayer',
      component: () => import('@/views/MovieAboutPlayerView.vue'),
    },
  ],
})

export default router
