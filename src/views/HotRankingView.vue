<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { ElMessage, ElRadioGroup, ElButton, ElCard, ElRadioButton } from 'element-plus'
import { getMovieListByTabType } from '@/axios/axiosInterface'

// 初始电影数据
const movieData = ref([
  { name: '流浪地球3', value: 9850 },
  { name: '封神第二部', value: 8760 },
  { name: '长津湖：冰雕连', value: 7650 },
  { name: '哪吒之魔童闹海', value: 6980 },
  { name: '战狼3', value: 6320 },
  { name: '唐人街探案4', value: 5870 },
  { name: '红海行动2', value: 5120 },
  { name: '你好，李焕英2', value: 4870 },
  { name: '孤注一掷2', value: 4230 },
  { name: '消失的她2', value: 3980 },
])

getMovieListByTabType('hot').then((res) => {
  movieData.value = res.data.map((item) => {
    return {
      name: item.title,
      value: item.playCount / 10000, // 单位换算为万热度
    }
  })
  updateChart()
})

// 图表类型,bar或pie
const chartType = ref('bar')
const chartDom = ref(null)
let chartInstance = null // 图表实例

// 根据排名获取颜色
const getRankColor = (index) => {
  const colors = [
    '#ff4d4f',
    '#ff7d45',
    '#ffa940',
    '#ffc53d',
    '#73d13d',
    '#36cfc9',
    '#40a9ff',
    '#597ef7',
    '#9254de',
    '#f759ab',
  ]
  return colors[index % colors.length]
}

// 横向柱状图配置
const getBarOption = (data) => {
  return {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}: {c}万热度',
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true,
    },
    xAxis: {
      type: 'value', // 数值轴
      name: '热度指数',
      nameLocation: 'middle',
      axisLabel: {
        formatter: '{value} 万',
      },
    },
    yAxis: {
      type: 'category', // 类目轴
      data: data.map((item) => item.name),
      axisLabel: {
        formatter: (value, index) => {
          const rank = index + 1
          return `{rank|TOP${rank}}{value|${value}}`
        },
        rich: {
          rank: {
            color: '#ffd04b',
            fontWeight: 'bold',
            padding: [0, 10, 0, 0],
          },
          value: {
            color: '#606266',
          },
        },
      },
    },
    series: [
      {
        name: '热度',
        type: 'bar',
        data: data,
        label: {
          show: true,
          position: 'right',
          formatter: '{c}万',
          color: '#909399',
        },
        itemStyle: {
          borderRadius: [0, 20, 20, 0],
        },
      },
    ],
    dataZoom: [
      {
        type: 'inside',
        yAxisIndex: 0,
        start: 0,
        end: 50,
      },
    ],
  }
}

// 饼图配置
const getPieOption = (data) => {
  return {
    tooltip: {
      trigger: 'item', // 鼠标悬停触发提示框
      formatter: '{b}: {c}万热度 ({d}%)',
    },
    legend: {
      // 图例
      type: 'scroll',
      orient: 'vertical',
      right: 10,
      top: 20,
      bottom: 20,
      formatter: (name) => {
        const item = data.find((d) => d.name === name)
        const index = data.indexOf(item) + 1
        return `TOP${index} ${name}`
      },
    },
    series: [
      {
        name: '热度占比',
        type: 'pie',
        radius: ['33%', '70%'],
        center: ['40%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2,
        },
        label: {
          show: true,
          // 'TOP{index|{a}}\n{percent|{d}%}'
          formatter: (item) => {
            return `TOP{index|${item.dataIndex + 1}}\n{percent|${item.percent}%}`
          },
          rich: {
            index: {
              color: '#ffd04b',
              fontSize: 14,
              lineHeight: 26,
            },
            percent: {
              color: '#409eff',
              fontSize: 18,
              fontWeight: 'bold',
            },
          },
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 18,
            fontWeight: 'bold',
          },
        },
        labelLine: {
          length: 20,
          length2: 30,
        },
        data: data,
      },
    ],
  }
}

// 更新图表配置
const updateChart = () => {
  if (!chartInstance) return

  const sortedData = [...movieData.value]
    .sort((a, b) => b.value - a.value)
    .map((item, index) => ({
      ...item,
      itemStyle: {
        color: getRankColor(index),
      },
    }))

  const option = chartType.value === 'bar' ? getBarOption(sortedData) : getPieOption(sortedData)

  chartInstance.setOption(option)
}

// 初始化图表
const initChart = () => {
  if (!chartDom.value) return
  chartInstance = echarts.init(chartDom.value)
  updateChart()
  // 响应窗口大小变化
  window.addEventListener('resize', handleResize)
}

// 刷新数据
const refreshData = () => {
  // 模拟数据更新
  movieData.value.forEach((item) => {
    item.value += Math.floor(Math.random() * 500 - 200)
  })
  movieData.value.sort((a, b) => b.value - a.value)
  ElMessage.success('数据已刷新')
  updateChart()
}

// 响应窗口大小变化
const handleResize = () => {
  chartInstance?.resize()
}

// 生命周期钩子
onMounted(initChart)
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chartInstance?.dispose() // 销毁图表实例
})

// 监听图表类型变化
watch(chartType, updateChart)
</script>

<template>
  <div class="movie-rank-container">
    <el-card shadow="hover" class="chart-card">
      <template #header>
        <div class="card-header">
          <span>电影热播排行榜</span>
          <div>
            <el-radio-group v-model="chartType" size="small">
              <el-radio-button value="bar">排名视图</el-radio-button>
              <el-radio-button value="pie">占比视图</el-radio-button>
            </el-radio-group>
            <el-button size="small" icon="Refresh" @click="refreshData" class="refresh-btn" />
          </div>
        </div>
      </template>

      <div ref="chartDom" style="height: 400px"></div>
    </el-card>
  </div>
</template>

<style scoped>
.movie-rank-container {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.chart-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.refresh-btn {
  margin-left: 10px;
}
</style>
