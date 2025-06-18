<script setup>
import { ref, computed } from 'vue'
import { payByZFB } from '@/axios/axiosInterface'

const plans = [
  { id: 1, name: '月度会员', price: 30 },
  { id: 2, name: '季度会员', price: 80 },
  { id: 3, name: '年度会员', price: 288 },
]

const selectedPlan = ref(2) // 默认选中季度会员

const selectedPlanName = computed(() => {
  return plans.find((p) => p.id === selectedPlan.value)?.name || ''
})

const selectedPlanPrice = computed(() => {
  return plans.find((p) => p.id === selectedPlan.value)?.price || ''
})
const handlePayment = async () => {
  // 如果没有选择任何套餐，则不执行支付流程
  if (!selectedPlan.value) return
  console.log('支付流程启动', {
    plan: selectedPlanName.value,
    amount: selectedPlanPrice.value,
  })
  // 调用支付接口，完成支付流程
  try {
    const result = await payByZFB({
      productName: selectedPlanName.value,
      amount: selectedPlanPrice.value,
    })
    if (result.code === 200) {
      console.log('支付成功', result)
      // 创建一个div
      const div = document.createElement('div')
      // 向div中插入支付宝支付表单
      div.innerHTML = result.data
      // 将div添加到body中
      document.body.appendChild(div)
      // 处理支付宝支付表单
      // 触发from的提交事件
      const from = div.querySelector('form')
      from.submit()
    } else {
      console.log('支付失败', result)
    }
  } catch (e) {
    console.log('出现错误', e)
  }
}
</script>

<template>
  <div class="vip-payment-container">
    <!-- 主内容区 -->
    <div class="payment-card">
      <!-- 标题区 -->
      <div class="header-section">
        <div class="premium-badge">
          <svg class="crown-icon" viewBox="0 0 24 24">
            <path d="M5 16L3 5l5.5 5L12 4l3.5 6L21 5l-2 11H5z" />
            <path d="M4 16h16v3H4z" fill="#FFD700" />
          </svg>
          <h1>VIP会员开通</h1>
        </div>
        <p class="subtitle">开通即享无广告观影·4K画质·专属内容</p>
      </div>

      <div class="content-wrapper">
        <!-- 套餐选择卡片 -->
        <div class="plan-section">
          <h2 class="section-title">选择套餐</h2>
          <div class="plan-options">
            <div
              v-for="plan in plans"
              :key="plan.id"
              :class="['plan-card', { active: selectedPlan === plan.id }]"
              @click="selectedPlan = plan.id"
            >
              <div class="plan-header">
                <span class="plan-name">{{ plan.name }}</span>
              </div>
              <div class="price-display">
                <span class="currency">¥</span>
                <span class="amount">{{ plan.price }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 订单与支付区域 -->
        <div class="payment-summary-wrapper">
          <!-- 支付方式 -->
          <div class="payment-section">
            <h2 class="section-title">支付方式</h2>
            <div class="alipay-card">
              <div class="alipay-logo">
                <div class="alipay-icon">
                  <svg
                    t="1749400338134"
                    viewBox="0 0 1024 1024"
                    version="1.1"
                    xmlns="http://www.w3.org/2000/svg"
                    p-id="26915"
                    width="200"
                    height="200"
                  >
                    <path
                      d="M230.771014 576.556522c-12.614493 9.646377-25.228986 23.744928-28.93913 42.295652-5.194203 24.486957-0.742029 55.652174 22.26087 80.13913 28.93913 28.93913 72.718841 37.101449 92.011594 38.585508 51.2 3.710145 106.110145-22.26087 147.663768-50.457971 16.324638-11.130435 43.77971-34.133333 70.492754-69.750725-59.362319-30.423188-133.565217-64.556522-212.22029-61.588406-41.553623 1.484058-70.492754 9.646377-91.269566 20.776812zM983.188406 712.347826c25.971014-61.588406 40.811594-129.113043 40.811594-200.347826 0-281.971014-230.028986-512-512-512S0 230.028986 0 512s230.028986 512 512 512c170.666667 0 321.298551-83.849275 414.794203-212.22029C838.492754 768.742029 693.797101 696.023188 604.011594 652.985507c-42.295652 48.973913-105.368116 97.205797-176.602898 117.982609-44.521739 13.356522-85.333333 18.550725-126.886957 9.646377-42.295652-8.904348-72.718841-28.197101-90.527536-47.489855-8.904348-10.388406-19.292754-22.26087-27.455073-37.843479 0.742029 0.742029 0.742029 2.226087 0.742029 2.968116 0 0-4.452174-7.42029-7.420289-19.292753-1.484058-5.936232-2.968116-11.872464-3.710145-17.808696-0.742029-4.452174-0.742029-8.904348 0-12.614493-0.742029-7.42029 0-15.582609 1.484058-23.744927 4.452174-20.776812 12.614493-43.77971 35.617391-65.298551 48.973913-48.231884 115.014493-50.457971 149.147826-50.457971 50.457971 0.742029 138.017391 22.26087 212.22029 48.973913 20.776812-43.77971 34.133333-89.785507 42.295652-121.692754H304.973913v-33.391304h158.052174v-66.782609H272.324638v-34.133333h190.701449v-66.782609c0-8.904348 2.226087-16.324638 16.324638-16.324637h74.944927v83.107246h207.026087v33.391304H554.295652v66.782609H719.768116S702.701449 494.933333 651.501449 586.202899c115.014493 40.811594 277.518841 104.626087 331.686957 126.144927z m0 0"
                      fill="#06B4FD"
                      p-id="26916"
                    ></path>
                  </svg>
                </div>
                <span>支付宝安全支付</span>
              </div>
            </div>
          </div>

          <!-- 订单摘要 -->
          <div class="summary-section">
            <h2 class="section-title">订单摘要</h2>
            <div class="summary-content">
              <div class="summary-item">
                <span>套餐：</span>
                <strong>{{ selectedPlanName }}</strong>
              </div>
              <div class="summary-item">
                <span>金额：</span>
                <div class="total-amount">
                  <span class="currency">¥</span>
                  {{ selectedPlanPrice }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 支付按钮 -->
      <button class="payment-btn" :class="{ active: selectedPlan }" @click="handlePayment">
        确认支付
      </button>
    </div>

    <!-- 底部保障声明 -->
    <div class="guarantee-section">
      <div class="guarantee-item">
        <svg class="check-icon" viewBox="0 0 24 24">
          <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
        </svg>
        <span>7天无理由退款</span>
      </div>
      <div class="guarantee-item">
        <svg class="check-icon" viewBox="0 0 24 24">
          <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
        </svg>
        <span>隐私保护协议</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* ===== PC端优化设计 ===== */
/* 整体容器 - 符合PC端尺寸规范[2](@ref) */
.vip-payment-container {
  max-width: 1200px; /* PC端推荐宽度1200-1440px[2](@ref) */
  margin: 40px auto;
  padding: 40px;
  background: #fff;
  color: #333;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

/* 主卡片 */
.payment-card {
  padding: 0;
}

/* 标题区 */
.header-section {
  text-align: center;
  margin-bottom: 40px;
  padding-bottom: 25px;
  border-bottom: 1px solid #f0f0f0;
}

.premium-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.crown-icon {
  width: 48px;
  height: 48px;
  fill: #ffd700;
}

h1 {
  font-size: 36px; /* PC端标题字体更大[2](@ref) */
  color: #222;
  margin: 0;
  font-weight: 700;
}

.subtitle {
  color: #666;
  margin-top: 12px;
  font-size: 18px;
}

/* 内容布局 - 使用网格布局实现响应式[6](@ref) */
.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

/* 套餐选择 */
.section-title {
  font-size: 22px;
  margin-bottom: 20px;
  color: #444;
  font-weight: 600;
  position: relative;
  padding-bottom: 10px;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 50px;
  height: 3px;
  background: #1890ff;
}

.plan-options {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.plan-card {
  background: #f9f9f9;
  border-radius: 12px;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.plan-card:hover {
  background: #f5f5f5;
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.plan-card.active {
  background: #f0f8ff;
  border: 2px solid #1890ff;
  box-shadow: 0 5px 20px rgba(24, 144, 255, 0.2);
}

.plan-name {
  font-weight: 500;
  font-size: 18px;
  color: #333;
}

.price-display {
  display: flex;
  align-items: baseline;
}

.currency {
  font-size: 22px;
  color: #ff6a00;
  margin-right: 5px;
}

.amount {
  font-size: 32px;
  font-weight: 700;
  color: #ff6a00;
}

/* 支付与摘要区域 */
.payment-summary-wrapper {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* 支付方式 */
.payment-section {
  background: #f8fbff;
  border-radius: 12px;
  padding: 25px;
  border: 1px solid #e6f7ff;
}

.alipay-logo {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 18px;
  font-weight: 500;
  color: #1890ff;
}

.alipay-icon svg {
  width: 42px;
  height: 42px;
}

/* 订单摘要 */
.summary-section {
  background: #fff;
  border-radius: 12px;
  padding: 25px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.summary-content {
  padding: 15px 0;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  padding: 15px 0;
  font-size: 18px;
  border-bottom: 1px dashed #eee;
}

.summary-item:last-child {
  border-bottom: none;
}

.total-amount {
  font-size: 28px;
  font-weight: 700;
  color: #ff6a00;
}

/* 支付按钮 */
.payment-btn {
  width: 100%;
  padding: 20px;
  font-size: 22px;
  font-weight: 600;
  background: #1890ff;
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 40px;
  letter-spacing: 2px;
  opacity: 0.8;
}

.payment-btn.active {
  opacity: 1;
  box-shadow: 0 8px 25px rgba(24, 144, 255, 0.4);
}

.payment-btn.active:hover {
  background: #096dd9;
  transform: translateY(-3px);
  box-shadow: 0 12px 30px rgba(24, 144, 255, 0.5);
}

/* 保障声明 */
.guarantee-section {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-top: 40px;
  padding-top: 25px;
  border-top: 1px solid #f0f0f0;
  font-size: 16px;
  color: #666;
}

.guarantee-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.check-icon {
  width: 20px;
  height: 20px;
  fill: #52c41a;
}

/* ===== 响应式设计 - 适应不同PC屏幕尺寸[3,6](@ref) ===== */
@media (max-width: 1440px) {
  .vip-payment-container {
    max-width: 1000px;
    padding: 30px;
  }
}

@media (max-width: 1200px) {
  .vip-payment-container {
    max-width: 900px;
  }

  .content-wrapper {
    gap: 30px;
  }
}

@media (max-width: 992px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }

  .payment-summary-wrapper {
    flex-direction: row;
    gap: 20px;
  }

  .payment-section,
  .summary-section {
    flex: 1;
  }
}

@media (max-width: 768px) {
  .vip-payment-container {
    margin: 20px;
    padding: 25px;
  }

  .payment-summary-wrapper {
    flex-direction: column;
  }

  h1 {
    font-size: 32px;
  }

  .section-title {
    font-size: 20px;
  }
}
</style>
