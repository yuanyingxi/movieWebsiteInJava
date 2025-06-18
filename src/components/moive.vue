<script setup>
import router from '@/router'
import { useTemplateRef, onMounted } from 'vue'
import { useMainStore } from '@/stores'
defineOptions({
  name: 'MovieCard',
})

const store = useMainStore()
defineProps(['movie'])
const img = useTemplateRef('img')
onMounted(() => {
  let ob = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        console.log('hane')
        img.value.src = img.value.dataset.src
        ob.disconnect()
        ob = null
      }
    },
    {
      root: null,
      rootMargin: '-100px',
      threshold: 0,
    },
  )
  ob.observe(img.value)
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
  <div class="movie-card">
    <a @click="toPlay(movie)">
      <div style="position: relative">
        <!-- https://picsum.photos/200/300,请求随机图片的地址 -->
        <img
          ref="img"
          src="https://picsum.photos/200/300"
          :data-src="'/api/movieCover?movieNo=' + movie.id"
          style="border-radius: 7px; width: 200px; min-height: 50px; height: 300px"
        />
        <div v-if="movie.isVip" class="vip">
          <svg
            t="1749327524275"
            viewBox="0 0 1389 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="17153"
            width="200"
            height="200"
          >
            <path
              d="M0 0m275.335801 0l757.173452 0q275.335801 0 275.335801 275.335801l0 413.003701q0 275.335801-275.335801 275.335801l-757.173452 0q-275.335801 0-275.335801-275.335801l0-413.003701q0-275.335801 275.335801-275.335801Z"
              fill="#1F232E"
              p-id="17154"
            ></path>
            <path
              d="M328.200275 688.339502h96.918201l125.002454-407.496985H466.969518l-52.864474 198.241776c-12.114775 45.705743-20.925521 85.90477-34.141639 132.161185h-2.753358c-12.665447-46.256415-22.026864-86.455441-34.692311-132.161185L289.102591 280.842517H203.197821L328.200275 688.339502z m269.347247 0h81.499397V280.842517H597.547522V688.339502z m181.721628 0h81.568231V543.512871h54.516489c87.556785 0 156.941406-41.30037 156.941406-134.363871 0-96.918202-69.384622-128.306483-159.144093-128.306483h-133.813199V688.339502z m81.568231-209.255209V345.821766h46.256415c56.168503 0 85.90477 15.418805 85.90477 63.327234 0 46.256415-26.982908 69.935293-83.151412 69.935293h-49.009773z"
              fill="#F2D375"
              p-id="17155"
            ></path>
          </svg>
        </div>
        <span style="position: absolute; right: 3px; bottom: 5px; color: yellow; font-size: 18px">{{
          movie.averageRating
        }}</span>
      </div>
      <div class="text" style="font-size: 16px">{{ movie.title }}</div>
      <div
        class="text"
        style="
          font-weight: 500;
          opacity: 0.5;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
          line-clamp: 2;
        "
      >
        {{ movie.description }}
      </div>
    </a>
  </div>
</template>
<style scoped>
.movie-card {
  --img-width: 200px;
  --img-height: 300px;
  --card-width: 200px;
  width: var(--card-width);
}
.vip {
  width: 40px;
  height: 25px;
  position: absolute;
  right: -4px;
  top: 1px;
}
.vip svg {
  max-width: 100%;
  max-height: 100%;
}
.text {
  color: #fff;
  font-weight: 700;
  letter-spacing: 0;
  opacity: 0.72;
}
</style>
