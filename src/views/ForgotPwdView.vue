<template>
  <div class="login-view">
    <div class="login-container">
      <h1>找回密码</h1>
      <form @submit.prevent="handleResetPassword">
        <div style="margin-bottom: 1.5rem">
          <label>注册邮箱</label>
          <input name="email" type="email" placeholder="请输入注册邮箱" />
        </div>

        <div style="margin-bottom: 1.5rem">
          <label>验证码</label>
          <div class="code-input">
            <input name="code" type="text" placeholder="请输入验证码" />
            <button
              type="button"
              class="code-btn"
              :disabled="countdown > 0"
              @click="sendVerificationCode"
            >
              {{ countdown > 0 ? `${countdown}s后重新发送` : '获取验证码' }}
            </button>
          </div>
        </div>

        <button type="submit">重置密码</button>

        <div class="links">
          <a href="/login">返回登录</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const countdown = ref(0)

function handleResetPassword(e) {
  const formData = new FormData(e.target)
  const obj = Object.fromEntries(formData)
  console.log('重置密码数据:', obj)
  // 这里调用API发送重置密码请求
}

function sendVerificationCode() {
  // 发送验证码逻辑
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
}
</script>

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
.links {
  margin-top: 1.5rem;
  text-align: center;
}
a {
  color: #1a73e8;
  text-decoration: none;
  font-size: 0.9rem;
}

/* 验证码输入框样式 */
.code-input {
  display: flex;
  gap: 10px;
}
.code-input input {
  flex: 1;
}
.code-btn {
  width: 120px;
  background: #e8f0fe;
  color: #1a73e8;
  white-space: nowrap;
}
.code-btn:disabled {
  background: #f1f3f4;
  color: #5f6368;
  cursor: not-allowed;
}

@media (max-width: 480px) {
  .login-container {
    width: 90%;
    padding: 1.5rem;
  }
  .code-btn {
    font-size: 0.8rem;
    padding: 12px 8px;
  }
}
</style>
