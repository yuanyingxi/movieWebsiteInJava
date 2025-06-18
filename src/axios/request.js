// 定义axios实例并导出使用
import axios from 'axios'
import router from '@/router'
import { setToken, setRefreshToken, getToken } from './storageToken'
import { isRefreshToken, refreshToken } from './refreshToken'
const instance = axios.create({
  baseURL: 'http://localhost:3000/api', // server api 请求的基础路径
  timeout: 10000,
})

// 请求拦截器 //用于将token自动附加到请求头
instance.interceptors.request.use((config) => {
  // 排除不需要token的请求
  if (config.url.includes('/login') || config.url.includes('/register')) {
    // 不需要token
    return config
  }
  const token = getToken() // 获取短token
  if (token) {
    console.log('短token存在,自动附加到请求头。')
    // 添加token到请求头
    config.headers.authorization = `Bearer ${token}`
  } else {
    console.log('短token不存在,跳转到登录页面。')
    // 尝试刷新token
    return Promise.resolve(config)
  }
  return config
})

// 响应拦截器 //用于将token自动刷新
instance.interceptors.response.use(
  async (res) => {
    if (res.data.data.access_token) {
      const token = res.data.data.access_token.replace('Bearer ', '') // remove 'Bearer '
      if (!setToken(token)) {
        console.log('token设置失败')
      }
    }
    if (res.data.data.refresh_token) {
      const refreshToken = res.data.data.refresh_token.replace('Bearer ', '') // remove 'Bearer '
      if (!setRefreshToken(refreshToken)) {
        console.log('refreshToken设置失败')
      }
    }
    console.log('vneoggv')
    if (res.data.code === 401 && !isRefreshToken(res.config)) {
      // token失效，刷新token
      // 刷新短token
      console.log('短token失效,刷新短token。')

      const isSuccess = await refreshToken()
      if (isSuccess) {
        // 重新请求原请求
        res.config.headers.authorization = `Bearer ${getToken()}`
        const newRes = await instance.request(res.config)
        return newRes
      } else {
        // 长token过期，需要重新登录
        console.log('长token过期,需要重新登录。')
        router.push('/login') // 跳转到登录页面
      }
    }
    return res.data
  },
  (error) => {
    // debugger
    console.log(error)
    // debugger
    // 跳转到登录页面
    return Promise.reject(error)
  },
)

export default instance
