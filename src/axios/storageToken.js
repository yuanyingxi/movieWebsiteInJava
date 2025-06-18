export function setToken(token) {
  if (!isJWT(token)) {
    return false
  }
  localStorage.setItem('token', token)
  return true
}
export function getToken() {
  return localStorage.getItem('token') || ''
}
// 存储长期token
export function setRefreshToken(refreshToken) {
  if (!isJWT(refreshToken)) {
    return false
  }
  localStorage.setItem('refreshToken', refreshToken)
  return true
}
export function getRefreshToken() {
  return localStorage.getItem('refreshToken') || ''
}
function isJWT(token) {
  if (typeof token !== 'string') return false
  // 步骤1：结构检查
  const parts = token.split('.')
  if (parts.length !== 3 || !parts.every((part) => /^[A-Za-z0-9_-]+$/.test(part))) {
    return false
  }

  // 步骤2：Header验证
  try {
    const headerJson = atob(parts[0].replace(/-/g, '+').replace(/_/g, '/'))
    const header = JSON.parse(headerJson)
    if (!header.alg || typeof header.alg !== 'string') return false
  } catch {
    return false
  }

  // 步骤3：Payload验证
  try {
    const payloadJson = atob(parts[1].replace(/-/g, '+').replace(/_/g, '/'))
    JSON.parse(payloadJson)
  } catch {
    return false
  }

  return true // 通过所有检查
}
