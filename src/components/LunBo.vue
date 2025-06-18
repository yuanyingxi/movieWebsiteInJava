<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElCarousel, ElCarouselItem, ElButton } from 'element-plus'

// 电影数据示例（可替换为API动态获取）
const movies = ref([
  {
    id: 1,
    title: '白雪公主',
    description:
      '当至纯至善的白雪公主（瑞秋·齐格勒 Rachel Zegler 饰），遭遇童话届令人胆寒的邪恶皇后（盖尔·加朵 Gal Gadot 饰），一场关于纯真与邪恶、光明与黑暗的终极对决正式上演。',
    image: '/白雪公主.jpg',
  },
  {
    id: 2,
    title: '拼出未来',
    description:
      '用一块一块积木搭建出“菲董”Pharrell Williams的人生以及音乐、艺术和时尚方面的创作才华。',
    image: '/拼出未来.jpg',
  },
  {
    id: 3,
    title: '雄狮少年2',
    description:
      '2009年前后，阿娟（李昕 配）不得不背井离乡前往上海打工，以赚取父亲的医疗费用。但他的舞狮技艺并没有多少用武之地。为了生存下去，他误打误撞学格斗、参加格斗比赛，以获取高额的奖金。看似公平的擂台背后，隐藏着资本的操控和舆论的暴力。资本试图收编阿娟，垄断行业，当阿娟等人选择拒绝后，对手利用网络的力量，对阿娟进行肆意的诽谤和污蔑，将他塑造成地痞流氓。面对铺天盖地的负面舆论，阿娟有口难辩，他的努力和坚持被无情地抹黑',
    image: '/雄狮少年2.jpg',
  },
  {
    id: 3,
    title: '阳光照耀青春里',
    description: `年轻有才的程序员何立为（肖央 饰）供职于家园软件公司，并独自开发了一款游戏。他的偏执和作为扰乱了公司的融资计划，被公司送进了“青春里”精神康复院。在康复院里，何立为认识了形形色色的病友，也经历了不少啼笑皆非的故事。其中女病人林春春（春夏 饰）总是与何立为发生种种纠葛，两颗心逐渐走近。何立为的哥哥何立刚（王迅 饰）拒绝接何立为出院，医院总务主任洪兆庆（陈明昊 饰）也不断压制着何立为的种种出格行为。何立为为了证明自己没有得病，百般尝试，希望早日逃出青春里。直到有一天，他看到了春春脑中的幻觉，而随后发生的一系列事情也让他开始迷惑，究竟什么才是正常的世界，自己想“逃出”的到底又是什么……`,
    image: '/阳光照耀青春里.jpg',
  },
  {
    id: 3,
    title: '通往夏天的隧道，再见的出口',
    description: `我去见那天的你。浦岛隧道——传闻只要进入那个隧道，就可以得到任何想要的东西。但是，作为交换……
看似个性捉摸不定，其实是因过去的意外内心怀有伤痛的高中生塔野薰，与坚定自我的态度，私下则因和自己的人生理想不同而苦恼的转学生花城杏子，他们两人为了调查隧道，并实现各自的愿望，而缔结了合作关系……
这是一个发生在某个偏僻乡村的令人难忘的夏日乡愁和疾驰的故事。`,
    image: '/通往夏天的隧道，再见的出口.jpg',
  },
])

// 轮播控制逻辑
const carouselRef = ref(null)
let autoplayTimer = null

// 启动自动轮播
const startAutoplay = () => {
  if (carouselRef.value?.next) {
    autoplayTimer = setInterval(() => {
      carouselRef.value.next()
    }, 2000)
  }
}

// 暂停自动轮播
const pauseAutoplay = () => {
  if (autoplayTimer) {
    clearInterval(autoplayTimer)
    autoplayTimer = null
  }
}

// 跳转到电影详情页
const goToMovie = (id) => {
  console.log(`跳转到电影ID: ${id}`)
  // 实际项目中用 router.push(`/movie/${id}`)
}

// 生命周期管理
onMounted(startAutoplay)
onUnmounted(pauseAutoplay)
</script>

<template>
  <div class="carousel-container">
    <!-- 轮播图主体 -->
    <el-carousel
      ref="carouselRef"
      :interval="3000"
      indicator-position="outside"
      arrow="always"
      height="500px"
      @mouseenter="pauseAutoplay"
      @mouseleave="startAutoplay"
    >
      <!-- 遍历电影数据生成轮播项 -->
      <el-carousel-item v-for="(movie, index) in movies" :key="index">
        <div class="movie-slide">
          <!-- 电影封面 -->
          <img :src="movie.image" :alt="movie.title" class="movie-image" />
          <!-- 电影信息叠加层 -->
          <div class="movie-info">
            <h2 class="movie-title">{{ movie.title }}</h2>
            <p class="movie-desc">{{ movie.description }}</p>
            <el-button type="primary" @click="goToMovie(movie.id)"> 立即观看 </el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<style scoped>
.carousel-container {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  margin: 20px;
}

.movie-slide {
  position: relative;
  height: 100%;
}

.movie-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s;
}

.movie-slide:hover .movie-image {
  transform: scale(1.03);
}

.movie-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  padding: 30px;
  color: white;
}

.movie-title {
  font-size: 2.2rem;
  margin-bottom: 12px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.7);
}

.movie-desc {
  font-size: 1rem;
  max-width: 60%;
  margin-bottom: 20px;
  line-height: 1.6;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .movie-title {
    font-size: 1.5rem;
  }
  .movie-desc {
    max-width: 100%;
    font-size: 0.9rem;
  }
  .el-carousel {
    height: 350px !important;
  }
}
</style>
