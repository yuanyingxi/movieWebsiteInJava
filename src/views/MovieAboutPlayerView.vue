<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ElAvatar,
  ElTag,
  ElImage,
  ElRate,
  ElButton,
  ElTable,
  ElPagination,
  ElCard,
  ElTableColumn,
} from 'element-plus'
import { getCreatorWorks } from '@/axios/axiosInterface'
// 路由管理
const route = useRoute()
const router = useRouter()

// 演员数据
const actor = ref({
  id: 1,
  name: '汤姆·汉克斯',
  avatar: 'https://example.com/avatar.jpg',
  //   gender: '男',
  //   birthday: '1956-07-09',
  //   country: '美国',
  //   bio: '两次获得奥斯卡最佳男主角奖的传奇演员...',
})

// 电影列表数据
const movieList = ref([
  {
    id: 1,
    title: '阿甘正传',
    year: 1994,
    rating: 9.5,
    poster: 'https://example.com/poster1.jpg',
    genres: ['剧情', '爱情'],
  },
  {
    id: 2,
    title: '拯救大兵瑞恩',
    year: 1998,
    rating: 8.9,
    poster: 'https://example.com/poster2.jpg',
    genres: ['战争', '历史'],
  },
])

// 分页配置
const pagination = ref({
  total: 0,
  pageSize: 10,
  currentPage: 1,
})

// 加载电影数据
const loadMovies = () => {
  // 实际项目中替换为API请求
  getCreatorWorks(actor.value.id).then((res) => {
    pagination.value.total = res.total
    console.log(res)
    // debugger
    movieList.value = res.list.map((item, index) => {
      return {
        id: item.movieNo,
        title: item.title,
        rating: item.averageRating,
        poster: `/api/movieCover?movieNo=${item.movieNo}`,
        year: res.movieDetails[index].releaseDate.split('T')[0],
        isVip: item.isVip,
        genres: res.movieDetails[index].genres,
      }
    })
    console.log(movieList.value)
  })
}


onMounted(() => {
  actor.value.name = route.query.actorName
  actor.value.id = route.query.actorId
  actor.value.avatar = `/api/movieCover/creator?movieNo=${route.query.movieNo}&name=${actor.value.name}`
  loadMovies()
})

// 分页事件处理
const handleSizeChange = (newSize) => {
  pagination.value.pageSize = newSize
  loadMovies()
}

const handleCurrentChange = (newPage) => {
  pagination.value.currentPage = newPage
  loadMovies()
}

// 跳转到电影详情页
const goToMovieDetail = (movieId) => {
  router.push({
    path: '/moviePlay',
    query: {
      movieId: movieId,
    },
  })
}
</script>

<template>
  <div class="container">
    <!-- 演员信息卡片 -->
    <el-card class="actor-card" v-if="actor">
      <div class="actor-header">
        <el-avatar :size="100" :src="actor.avatar" />
        <div class="actor-info">
          <h1>{{ actor.name }}</h1>
        </div>
      </div>
    </el-card>

    <!-- 电影列表表格 -->
    <el-table
      :data="movieList"
      :row-key="(row) => row.id"
      style="width: 100%"
      height="500"
      empty-text="暂无相关电影数据"
    >
      <el-table-column label="海报" width="120">
        <template #default="scope">
          <el-image
            :src="scope.row.poster"
            fit="cover"
            style="width: 80px; height: 120px"
            :preview-src-list="[scope.row.poster]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="片名" sortable />
      <el-table-column prop="year" label="年份" width="100" sortable />
      <el-table-column prop="rating" label="评分" min-width="300">
        <template #default="scope">
          <el-rate
            v-model="scope.row.rating"
            disabled
            show-score
            :max="10"
            :score-template="`${scope.row.rating.toFixed(1)}`"
          />
        </template>
      </el-table-column>
      <el-table-column prop="genres" label="类型" width="180">
        <template #default="scope">
          <div class="genre-tags">
            <el-tag v-for="genre in scope.row.genres" :key="genre" size="small">
              {{ genre }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="primary" size="small" @click="goToMovieDetail(scope.row.id)">
            详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页控件 -->
    <el-pagination
      background
      layout="total, sizes, prev, pager, next"
      :total="pagination.total"
      :page-sizes="[5, 10, 20]"
      :page-size="pagination.pageSize"
      :current-page="pagination.currentPage"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      class="pagination"
    />
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.actor-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.actor-header {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

.actor-info {
  flex: 1;
}

.actor-info h1 {
  margin-top: 0;
  margin-bottom: 10px;
}

.meta {
  margin: 15px 0;
  display: flex;
  gap: 10px;
}

.bio {
  color: #666;
  line-height: 1.6;
}

.genre-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
