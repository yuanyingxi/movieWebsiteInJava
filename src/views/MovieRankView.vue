<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import * as echarts from 'echarts'
import { ElTabs, ElTabPane, ElTable, ElTableColumn, ElRate, ElButton } from 'element-plus'
import { getMovieListByTabType } from '@/axios/axiosInterface'
// 响应式数据
const activeTab = ref('weekly')
const chartRef = ref(null)
let chartInstance = null

// 模拟数据
// { id: 1, title: '阿凡达', rating: 8.8, boxOffice: 212.0, releaseDate: '2009-12-18' }
const dataSources = ref({
  weekly: [],
  monthly: [],
  all: [],
  rating: [],
})

// 计算当前显示的数据
const currentData = computed(() => dataSources.value[activeTab.value])

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  updateChart()
}

// 更新图表数据
const updateChart = () => {
  const data = currentData.value.slice(0, 10)

  const option = {
    title: {
      text: `${getTabLabel(activeTab.value)}TOP10`,
      left: 'center',
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>{a}: {c}',
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true,
    },
    xAxis: {
      type: 'category',
      data: data.map((item) => item.title),
      axisLabel: {
        interval: 0,
        rotate: 30,
      },
    },
    yAxis: {
      type: 'value',
      name: activeTab.value === 'rating' ? '评分' : '票房(亿)',
    },
    series: [
      {
        name: activeTab.value === 'rating' ? '评分' : '票房',
        type: 'bar',
        data: data.map((item) => (activeTab.value === 'rating' ? item.rating : item.boxOffice)),
        itemStyle: {
          color: function (params) {
            const colorList = [
              '#E86452', // 第一名
              '#FF9845', // 第二名
              '#F6BD16', // 第三名
              '#5B8FF9', // 其他
              '#5B8FF9',
              '#5B8FF9',
              '#5B8FF9',
              '#5B8FF9',
              '#5B8FF9',
              '#5B8FF9',
            ]
            return colorList[params.dataIndex]
          },
        },
      },
    ],
  }

  if (chartInstance) {
    chartInstance.setOption(option)
  }
}

// 表格行样式
const tableRowClassName = ({ rowIndex }) => {
  if (rowIndex === 0) return 'top-1-row'
  if (rowIndex === 1) return 'top-2-row'
  if (rowIndex === 2) return 'top-3-row'
  return ''
}

// 辅助函数
const getTabLabel = (tab) => {
  const labels = {
    weekly: '本周',
    monthly: '本月',
    all: '全部',
    rating: '好评',
  }
  return labels[tab] || ''
}

const formatBoxOffice = (value) => {
  return `${value.toFixed(2)}`
}

const viewDetail = (movie) => {
  console.log('查看详情:', movie)
  // 实际项目中跳转到详情页
}

const handleTabChange = () => {
  // console.log(tab);

  // if (tab.props.label !== '热播排行') {
  // updateChart()
  // }
  updateChart()
}

function getRankDataByTab() {
  getMovieListByTabType(activeTab.value)
    .then((res) => {
      // 将数据保存到dataSources中
      // dataSources 的子项结构为 { id: 1, title: '阿凡达', rating: 8.8, boxOffice: 212.0, releaseDate: '2009-12-18' }
      //item = {title: '无间道', playCount: 3012928, rating: 9.3, date: '2003-09-05', rank: null}
      dataSources.value[activeTab.value] = res.data.map((item) => {
        return {
          title: item.title,
          rating: item.rating,
          boxOffice: item.playCount / 100000,
          releaseDate: item.date,
          id: item.id,
        }
      })
      console.log(res)
      updateChart()
    })
    .catch((err) => {
      console.error(err)
    })
}

// 生命周期钩子
onMounted(() => {
  getRankDataByTab()
  initChart()
  window.addEventListener('resize', () => {
    if (chartInstance) chartInstance.resize()
  })
})

// 监听数据变化
watch(activeTab, getRankDataByTab)
</script>

<template>
  <div class="movie-ranking-container">
    <!-- 导航选项卡 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabChange">
      <el-tab-pane label="本周排行" name="weekly"></el-tab-pane>
      <el-tab-pane label="本月排行" name="monthly"></el-tab-pane>
      <el-tab-pane label="全部排行" name="all"></el-tab-pane>
      <el-tab-pane label="好评排行" name="rating"></el-tab-pane>
    </el-tabs>

    <!-- 图表区域 -->
    <div class="chart-container">
      <div ref="chartRef" style="width: 100%; height: 400px"></div>
    </div>

    <!-- 数据表格 -->
    <el-table
      :data="currentData"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      :header-cell-style="{ 'text-align': 'center' }"
    >
      <el-table-column type="index" label="排名" width="80">
        <template #default="scope">
          <span v-if="scope.$index < 3" :class="`top-${scope.$index + 1}`">
            {{ scope.$index + 1 }}
          </span>
          <span v-else>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="电影名称"></el-table-column>
      <el-table-column prop="rating" label="评分" sortable min-width="250">
        <template #default="scope">
          <el-rate
            v-model="scope.row.rating"
            disabled
            :max="10"
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            show-score
            :score-template="scope.row.rating + '分'"
          />
        </template>
      </el-table-column>
      <el-table-column prop="boxOffice" label="票房(亿)" sortable>
        <template #default="scope">
          {{ formatBoxOffice(scope.row.boxOffice) }}
        </template>
      </el-table-column>
      <el-table-column prop="releaseDate" label="上映时间" sortable></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.movie-ranking-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.chart-container {
  margin: 20px 0;
}

.top-1 {
  color: #e86452;
  font-weight: bold;
  font-size: 18px;
}

.top-2 {
  color: #ff9845;
  font-weight: bold;
  font-size: 16px;
}

.top-3 {
  color: #f6bd16;
  font-weight: bold;
  font-size: 15px;
}

:deep(.top-1-row) {
  --el-table-tr-bg-color: #fff8f7;
}

:deep(.top-2-row) {
  --el-table-tr-bg-color: #fffaf2;
}

:deep(.top-3-row) {
  --el-table-tr-bg-color: #fffdf2;
}
</style>
