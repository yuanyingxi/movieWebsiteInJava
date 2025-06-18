// 用于书写刷新token的逻辑
import request from './request'
import { getRefreshToken } from './storageToken'
export async function refreshToken() {
  console.log('refreshToken')

  try {
    const resp = await request.post(
      '/user/refresh',
      {
        refresh_token: `${getRefreshToken()}`
      },
      {
        headers: {
          Authorization: `Bearer ${getRefreshToken()}`,
        },
        __isRefreshing: true,
      },
    )
    return resp.code === 200
  } catch (error) {
    console.log(error)
    return false
  }
}

export function isRefreshToken(config) {
  return !!config.__isRefreshing
}
