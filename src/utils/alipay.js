// utils/alipay.js
import { nextTick } from 'vue'

export function handleAlipayForm(formHtml) {
  // 1. 清理旧容器
  document.querySelectorAll('.alipay-container').forEach((el) => el.remove())

  // 2. 创建新容器并注入消毒后的 HTML
  const container = document.createElement('div')
  container.className = 'alipay-container'
  container.style.cssText = 'position:absolute; left:-9999px;'
  container.innerHTML = formHtml
  document.body.appendChild(container)

  // 3. 获取表单并设置跳转方式
  const form = container.querySelector('form')
  if (!form) throw new Error('未找到支付宝表单')
  form.setAttribute('target', '_blank') // 解决 Safari/微信内兼容性问题[1,3](@ref)

  // 4. 提交表单（双保险）
  nextTick(() => {
    try {
      form.submit() // 主提交方式
    } catch (e) {
      console.error(e)
      setTimeout(() => form.submit(), 100) // 延迟重试
    }
  })
}
