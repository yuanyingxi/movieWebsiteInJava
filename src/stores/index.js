import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getHomeData } from '@/axios/axiosInterface'

export const useMainStore = defineStore(
  'main',
  () => {
    // 创建主页电影信息的list
    /**
     * 每一个电影信息对象为
     * {
     *   title: '电影名',
     *   imgUrl: '电影图片地址',
     *   score: '电影评分',
     *   discription: '电影简介',
     *   isVip: '是否会员',
     * }
     * @param {string} title 电影名
     * @param {string} imgUrl 电影图片地址
     * @param {number} score 电影评分
     * @param {string} discription 电影简介
     * @param {boolean} isVip 是否会员
     */
    const obj = {
      normal: [], // 主页电影的数据(包含会员和免费电影的数据)
      vip: [], // 会员电影的数据
      free: [], // 免费电影的数据
    }
    /**
     *
     * @param {string} type 首页的类型，normal(返回会员和免费电影列表)、vip(返回会员电影列表)、free(返回免费电影列表)
     * @return {Array<{
     *   title: string, // 电影名
     *   imgUrl: string, // 电影图片地址
     *   score: number, // 电影评分
     *   discription: string, // 电影简介
     *   isVip: boolean // 是否会员
     *   id: number // 电影id
     * }>} 电影列表
     */
    async function getMovieList(type) {
      if (obj[type].length > 0) {
        return obj[type]
      }
      // 没有电影数据需要从后端获取并设置
      try {
        const response = await getHomeData(type)
        if (response.code === 200) {
          // 将数据存起来
          obj[type] = response.data.list.map((item) => {
            item.id = +item.id
            return item
          })
          return obj[type]
        } else {
          throw new Error('获取电影列表失败', response.msg)
        }
      } catch (error) {
        console.error(error)
      }
    }

    const m = ref({}) // 用于存储movieId和对应的电影信息对象
    // 用于获取电影信息对象
    function getMovieInfo(movieId) {
      if (m.value[movieId]) {
        return m.value[movieId]
      }
      return null
    }
    function setMovieInfo(movieId, movieInfo) {
      m.value[movieId] = movieInfo
    }
    return { getMovieList, getMovieInfo, setMovieInfo, m }
  },
  {
    persist: {
      storage: sessionStorage,
    },
  },
)
