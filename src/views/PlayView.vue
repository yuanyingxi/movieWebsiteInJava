<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElAvatar, ElScrollbar } from 'element-plus'
import { useRoute } from 'vue-router'
import { useMainStore } from '@/stores'
import { getPlayerInfoByMovieId } from '@/axios/axiosInterface'
import router from '@/router'
const mainStore = useMainStore()
const route = useRoute()
// 通过路由参数获取电影数据
// 电影数据
/**
{
  title: '盗梦空间',
  videoUrl: 'https://example.com/movie.mp4',
  synopsis: `多米尼克·科布是一位经验老到的盗梦师，能够潜入人们精神最为脆弱的梦境中窃取秘密...`,
  directors: [{ id: 1, name: '克里斯托弗·诺兰', avatar: 'https://example.com/director.jpg' }],
  actors: [
    { id: 101, name: '莱昂纳多·迪卡普里奥', avatar: 'https://example.com/actor1.jpg' },
    { id: 102, name: '约瑟夫·高登-莱维特', avatar: 'https://example.com/actor2.jpg' },
    { id: 103, name: '艾伦·佩吉', avatar: 'https://example.com/actor3.jpg' },
  ],
}
 */
const movie = reactive({})

onMounted(() => {
  // 为当前页面设置电影配置
  const movieInfo = mainStore.getMovieInfo(+route.query.movieId)
  if (!movieInfo) {
    throw new Error('电影信息不存在')
  } else {
    console.log(movieInfo)
    movie.title = movieInfo.title
    movie.videoUrl = movieInfo.url
    movie.synopsis = movieInfo.description
  }
  // 通过电影ID获取电影相关任务的信息
  getPlayerInfoByMovieId(+route.query.movieId).then((res) => {
    console.log(res)
    const director = res.shift()
    movie.directors = []
    movie.directors.push({
      id: director.id,
      name: director.name,
      avatar: `/api/movieCover/creator?movieNo=${+route.query.movieId}&name=${director.name}`,
    })
    movie.actors = res.map((actor) => {
      return {
        name: actor.name,
        avatar: `/api/movieCover/creator?movieNo=${+route.query.movieId}&name=${actor.name}`,
        id: actor.id,
      }
    })
    console.log(movie)
  })
})

// 播放控制
const videoPlayer = ref(null)

const progress = ref(0)

const updateProgress = () => {
  if (videoPlayer.value) {
    progress.value = (videoPlayer.value.currentTime / videoPlayer.value.duration) * 100
  }
}

function toMovieAboutPlayer(movieNo, actorId, actorName) {
  // 跳转到电影相关人员页面
  router
    .push({
      path: '/movieAboutPlayer',
      query: {
        movieNo,
        actorId,
        actorName,
      },
    })
    .then(() => {
      window.location.reload()
    })
}
</script>

<template>
  <div class="movie-player-container">
    <!-- 顶部导航 -->
    <header class="player-header">
      <div class="movie-title">{{ movie.title }}</div>
    </header>

    <!-- 主内容区 -->
    <main>
      <div class="player-main">
        <!-- 左侧播放区 -->
        <section class="video-section">
          <video
            ref="videoPlayer"
            :src="movie.videoUrl"
            controls
            class="video-player"
            @timeupdate="updateProgress"
          ></video>
        </section>

        <!-- 右侧信息区 -->
        <section class="info-section">
          <!-- 演职人员卡片 -->
          <div class="crew-card">
            <h3>导演</h3>
            <div class="crew-list">
              <div
                v-for="director in movie.directors"
                :key="director.id"
                class="crew-item"
                @click="toMovieAboutPlayer(+route.query.movieId, director.id, director.name)"
              >
                <el-avatar :size="60" :src="director.avatar" />
                <div class="crew-name">{{ director.name }}</div>
              </div>
            </div>

            <h3>演员</h3>
            <div class="crew-list">
              <div
                v-for="actor in movie.actors"
                :key="actor.id"
                class="crew-item"
                @click="toMovieAboutPlayer(+route.query.movieId, actor.id, actor.name)"
              >
                <el-avatar :size="60" :src="actor.avatar" />
                <div class="crew-name">{{ actor.name }}</div>
              </div>
            </div>
          </div>
        </section>
      </div>
      <!-- 电影简介 -->
      <div class="synopsis-card">
        <h3>剧情简介</h3>
        <el-scrollbar height="200px">
          <p class="synopsis-text">{{ movie.synopsis }}</p>
        </el-scrollbar>
      </div>
    </main>
  </div>
</template>

<style scoped>
.movie-player-container {
  margin: 0 auto;
  padding: 20px;
  background: #1a1a1a;
  color: #fff;
  font-family: 'Helvetica Neue', sans-serif;
  min-height: 100vh;
}

.player-header {
  text-align: center;
  padding: 15px 0;
  border-bottom: 1px solid #333;
}

.movie-title {
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.player-main {
  display: flex;
  gap: 30px;
  margin-top: 20px;
}

.video-section {
  flex: 3;
}

.video-player {
  width: 100%;
  aspect-ratio: 16 / 9;
  min-height: 400px;
  background: #000;
  border-radius: 8px;
}

.custom-controls {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: 15px;
  padding: 10px;
  background: #222;
  border-radius: 4px;
}

.progress-slider {
  flex: 1;
}

.info-section {
  flex: 1;
}

.crew-card,
.synopsis-card {
  background: #222;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
}

.crew-card h3,
.synopsis-card h3 {
  margin-top: 0;
  font-size: 18px;
  color: #e50914; /* 主题红色 */
  border-left: 3px solid #e50914;
  padding-left: 10px;
}

.crew-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin: 15px 0;
}

.crew-item {
  text-align: center;
  width: 80px;
}

.crew-name {
  margin-top: 8px;
  font-size: 13px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.synopsis-text {
  line-height: 1.8;
  color: #ccc;
  margin: 0;
}
</style>
