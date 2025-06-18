<script setup>
import { ref, watch } from 'vue'
import { register } from '@/axios/axiosInterface'
import { ElMessage } from 'element-plus'
import router from '@/router'
const showPassword = ref(false)
const agreed = ref(false)

const fromInputList = ref(['', '', '', ''])
watch(
  fromInputList,
  (newVal) => {
    for (let i = 0; i < newVal.length; i++) {
      if (newVal[i] === '') {
        agreed.value = false
        return
      }
    }
    agreed.value = true
  },
  { deep: true },
)

async function handleRegister(e) {
  const formData = Object.fromEntries(new FormData(e.target)) // 表单数据对象
  // 密码一致性验证
  if (formData.password !== formData.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  // 这里调用API发送注册请求
  const res = await register(formData)
  if (res.code === 200) {
    ElMessage.success('注册成功')
    setTimeout(() => {
      router.push('login') // 注册成功后跳转到登录页
    }, 500)
  } else {
    ElMessage.error(res.message)
  }
}
</script>

<template>
  <div class="login-view">
    <div class="login-container">
      <h1>注册新账号</h1>
      <form @submit.prevent="handleRegister">
        <div style="margin-bottom: 1.5rem">
          <label>用户名</label>
          <input name="username" type="text" placeholder="2-16个字符" v-model="fromInputList[0]" />
        </div>

        <div style="margin-bottom: 1.5rem">
          <label>邮箱</label>
          <input
            name="email"
            type="email"
            placeholder="请输入有效邮箱"
            v-model="fromInputList[1]"
          />
        </div>

        <div style="margin-bottom: 1.5rem">
          <label>密码</label>
          <div style="position: relative">
            <input
              name="password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="8-20位,包含字母和数字"
              v-model="fromInputList[2]"
            />
            <span
              style="position: absolute; right: 16px; top: 13px; cursor: pointer"
              @click="showPassword = !showPassword"
            >
              <!-- 复用登录页的密码可见性切换图标 -->
              <svg
                v-show="!showPassword"
                t="1747651561463"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="13996"
                width="16"
              >
                <path
                  d="M128.256 67.648l263.296 263.36 301.696 301.696L956.608 896l-60.352 60.352-126.528-126.528A533.568 533.568 0 0 1 4.096 524.992L0 512l4.16-12.992a534.144 534.144 0 0 1 178.112-256.64L67.904 128 128.256 67.648z m114.816 235.52A449.152 449.152 0 0 0 89.728 512a448.192 448.192 0 0 0 616.704 254.528l-76.544-76.544a213.312 213.312 0 0 1-295.68-295.68L243.136 303.232z m153.6 153.6a128 128 0 0 0 170.752 170.752L396.736 456.832zM512.256 213.376c-24.576 0-48.704 1.984-72.192 5.76l-42.112 6.848-13.632-84.224 42.112-6.848a533.568 533.568 0 0 1 593.984 364.16l4.16 12.992-4.16 12.992a531.712 531.712 0 0 1-77.504 152.384l-25.216 34.432-68.864-50.368 25.216-34.432c25.472-34.88 46.08-73.6 60.736-115.008A448.192 448.192 0 0 0 512.256 213.312z m21.888 82.24l40.832 12.48a213.76 213.76 0 0 1 141.248 141.312l12.544 40.768-81.6 25.024-12.48-40.768a128.384 128.384 0 0 0-84.8-84.736l-40.768-12.544 25.024-81.536z"
                  p-id="13997"
                ></path>
              </svg>
              <svg
                v-show="showPassword"
                t="1747653563295"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="14290"
                width="16"
              >
                <path
                  d="M89.792 512a448.192 448.192 0 0 0 845.056 0A448.192 448.192 0 0 0 89.792 512zM4.16 499.008a533.568 533.568 0 0 1 1016.32 0l4.16 12.992-4.16 12.992a533.568 533.568 0 0 1-1016.32 0L0 512l4.16-12.992zM512.32 384a128 128 0 1 0 0 256 128 128 0 0 0 0-256zM299.008 512a213.312 213.312 0 1 1 426.624 0 213.312 213.312 0 0 1-426.624 0z"
                  p-id="14291"
                ></path>
              </svg>
            </span>
          </div>
        </div>

        <div style="margin-bottom: 1.5rem">
          <label>确认密码</label>
          <input
            name="confirmPassword"
            :type="showPassword ? 'text' : 'password'"
            placeholder="请再次输入密码"
            v-model="fromInputList[3]"
          />
        </div>
        <button type="submit">立即注册</button>

        <div class="links">
          <span>已有账号?</span>
          <a href="/login">立即登录</a>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* 复用登录页样式 */
.login-view {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0;
  padding: 0;
}
.login-container {
  background-color: white;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 400px;
}
h1 {
  color: #1a73e8;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 1.8rem;
}
label {
  display: block;
  color: #5f6368;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}
input {
  width: 100%;
  padding: 12px;
  border: 1px solid #dadce0;
  border-radius: 6px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}
input:focus {
  outline: none;
  border-color: #1a73e8;
  box-shadow: 0 0 0 2px rgba(26, 115, 232, 0.2);
}
button {
  width: 100%;
  background: #1a73e8;
  color: white;
  padding: 14px;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s ease;
}
button:hover {
  background: #1557b0;
}
button:disabled {
  background: #a0c0f0;
  cursor: not-allowed;
}
.links {
  margin-top: 1.5rem;
  text-align: center;
}
a {
  color: #1a73e8;
  text-decoration: none;
  font-size: 0.9rem;
}

/* 用户协议样式 */
.agreement {
  display: flex;
  align-items: center;
  margin-bottom: 1.5rem;
  font-size: 0.85rem;
  color: #5f6368;
}
.agreement input {
  width: auto;
  margin-right: 8px;
}
.agreement a {
  margin: 0 4px;
}

@media (max-width: 480px) {
  .login-container {
    width: 90%;
    padding: 1.5rem;
  }
  .agreement {
    flex-wrap: wrap;
    line-height: 1.6;
  }
}
</style>
