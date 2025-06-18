<script setup>
import { ElImage, ElTag } from 'element-plus'
import { onMounted } from 'vue'
import router from '@/router'
import { useMainStore } from '@/stores'
const store = useMainStore()
defineProps(['movie'])

// 类型ID转中文（示例）
const getTypeName = (typeId) => {
  const map = {
    action: '动作',
    comedy: '喜剧',
    // 其他类型映射...
  }
  return map[typeId] || typeId
}
onMounted(() => {
  console.log('mounted')
})

function toPlay(movie) {
  // debugger
  store.setMovieInfo(movie.id, movie)
  console.log(store.getMovieInfo(movie.id))

  router
    .push({
      path: '/moviePlay',
      query: {
        movieId: movie.id,
      },
    })
    .then(() => {
      // 刷新页面
      window.location.reload()
    })
}
</script>

<template>
  <a @click="toPlay(movie)">
    <div class="movie-card">
      <div class="poster-container">
        <el-image :src="'/api/movieCover?' + `movieNo=${movie.id}`" fit="cover" class="poster">
          <template #error>
            <div class="poster-error">
              <i class="el-icon-picture-outline"></i>
            </div>
          </template>
        </el-image>
      </div>
      <div class="movie-info">
        <h3 class="title">{{ movie.title }}</h3>
        <div class="meta">
          <el-tag v-for="t in [movie.type]" :key="t" size="small" class="tag">
            {{ getTypeName(t) }}
          </el-tag>
        </div>
      </div>
    </div>
  </a>
</template>

<style scoped>
.movie-card {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s;
  margin-bottom: 20px;
  background: white;
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.poster-container {
  aspect-ratio: 2/3;
  background: #f5f7fa;
}

.poster {
  width: 100%;
  height: 100%;
}

.poster-error {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
  font-size: 30px;
}

.movie-info {
  padding: 12px;
}

.title {
  font-size: 14px;
  margin: 0 0 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tag {
  margin-right: 5px;
  margin-bottom: 5px;
}
</style>
