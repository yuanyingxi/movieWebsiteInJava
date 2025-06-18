<script setup>
import { ref, watch } from 'vue'
import { ElMenu, ElSubMenu, ElMenuItem, ElRow, ElCol, ElEmpty, ElPagination } from 'element-plus'
import MovieCard from '@/components/MovieCard.vue'
import { getMovieListByType, getMovieListByRegion } from '@/axios/axiosInterface'

// 分类数据结构（写死）
const categories = ref([
  {
    id: 'type',
    name: '电影类型',
    children: [
      { id: 'action', name: '动作' },
      { id: 'comedy', name: '喜剧' },
      { id: 'sci_fi', name: '科幻' },
      { id: 'romance', name: '爱情' },
      { id: 'thriller', name: '悬疑' },
      { id: 'animation', name: '动画' },
    ],
  },
  {
    id: 'region',
    name: '地区',
    children: [
      { id: 'china', name: '中国' },
      { id: 'usa', name: '美国' },
      { id: 'korea', name: '韩国' },
      { id: 'japan', name: '日本' },
      { id: 'europe', name: '欧洲' },
      { id: 'india', name: '印度' },
      { id: 'thailand', name: '泰国' },
      { id: 'other', name: '其他地区' },
    ],
  }
])

// averageRating: 9.1
// coverUrl: "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p451926968.webp"
// description: "不久的将来，网络黑客尼奥（基奴李维斯 饰）对这个看似正常的现实世界产生了怀疑。他结识了黑客崔妮蒂（凯莉·安·摩丝 饰），并见到了黑客组织的首领墨菲斯（劳伦斯·菲什伯恩 饰）。墨菲斯告诉他，现实世界其实是由一个名叫“母体”的计算机人工智能系统控制，人们就像他们饲养的动物，没有自由和思想，而尼奥就是能够拯救人类的救世主。可是，救赎之路从来都不会一帆风顺，到底哪里才是真实的世界？如何才能打败那些超人一样的特勤？尼奥是不是人类的希望？这是黑客的帝国，程序和代码欢迎大家的到来。."
// id: 54
// isVip: 1
// region: "美国"
// title: "黑客帝国"
// url: "http://b7b8a6d4.natappfree.cc/AveMujica.mp4"

// 电影数据（写死）
const movies = ref([])

// 当前选中分类
const activeCategory = ref(null)
const currentCategory = ref('')

// 分页相关状态
const currentPage = ref(1) // 当前页码
const pageSize = ref(8) // 每页显示的电影数量
const pageSizes = ref([8, 12, 24, 48]) // 可选每页显示数量
// 总电影数量
const totalMovies = ref(0)

function getMovies(page) {
  if (page) {
    currentPage.value = page
  }
  if (activeCategory.value.main === 'type') {
    // 电影类型分类,请求电影列表(根据类型)
    getMovieListByType(activeCategory.value.sub, currentPage.value, pageSize.value)
      .then((res) => {
        if (res.code === 200) {
          movies.value = res.data.list.map((item) => (item.type = activeCategory.value.sub) && item)
          totalMovies.value = res.data.total // 更新总电影数量
        } else {
          new Error(res.msg)
        }
      })
      .catch((err) => {
        console.error(err)
      })
  } else if (activeCategory.value.main === 'region') {
    // 电影地区分类，请求电影列表(根据地区)
    getMovieListByRegion(activeCategory.value.sub, currentPage.value, pageSize.value)
      .then((res) => {
        if (res.code === 200) {
          movies.value = res.data.list.map((item) => (item.type = activeCategory.value.sub) && item)
          totalMovies.value = res.data.total // 更新总电影数量
        } else {
          new Error(res.msg)
        }
      })
      .catch((err) => {
        console.error(err)
      })
  }
}

// 处理分类选择
const handleCategorySelect = (indexPath) => {
  const [mainCat, subCat] = indexPath.split('-')

  const main = categories.value.find((c) => c.id === mainCat)
  const sub = main.children.find((s) => s.id === subCat)
  currentCategory.value = `${main.name} - ${sub.name}`
  activeCategory.value = { main: mainCat, sub: sub.name }
  // 切换分类时重置到第一页
  currentPage.value = 1

  // 请求当前页的电影数据
  getMovies(1)
}

watch(currentPage, () => {
  // 切换页码时，请求当前页的电影数据
  getMovies()
})

watch(pageSize, () => {
  getMovies()
})

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  // 滚动到顶部，提升用户体验
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 处理每页数量变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
}
</script>

<template>
  <div class="movie-category-container">
    <!-- 分类导航区 -->
    <div class="category-nav">
      <el-menu default-active="type" class="category-menu" @select="handleCategorySelect">
        <el-sub-menu v-for="category in categories" :key="category.id" :index="category.id">
          <template #title>
            <span class="category-title">{{ category.name }}</span>
          </template>
          <el-menu-item
            v-for="sub in category.children"
            :key="sub.id"
            :index="`${category.id}-${sub.id}`"
          >
            {{ sub.name }}
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>

    <!-- 电影展示区 -->
    <div class="movie-display">
      <h2 v-if="currentCategory" class="current-category">
        当前分类：{{ currentCategory }}
        <span class="movie-count">(共 {{ totalMovies }} 部电影)</span>
      </h2>

      <div v-if="movies.length > 0">
        <el-row :gutter="20">
          <el-col v-for="movie in movies" :key="movie.id" :xs="12" :sm="8" :md="6" :lg="4">
            <movie-card :movie="movie" />
          </el-col>
        </el-row>
        <!-- 分页控件 -->
        <div class="pagination-container">
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="totalMovies"
            v-model:page-size="pageSize"
            :page-sizes="pageSizes"
            v-model:current-page="currentPage"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>

      <el-empty v-else description="暂无该分类下的电影" />
    </div>
  </div>
</template>

<style scoped>
.movie-category-container {
  display: flex;
  min-height: 80vh;
}

.category-nav {
  width: 220px;
  border-right: 1px solid #eee;
}

.category-menu {
  border-right: none;
}

.category-title {
  font-weight: 600;
  font-size: 16px;
}

.movie-display {
  flex: 1;
  padding: 0 20px;
}

.current-category {
  color: var(--el-color-primary);
  padding-bottom: 15px;
  border-bottom: 1px dashed #eee;
  margin-bottom: 20px;
}

.movie-count {
  font-size: 14px;
  color: #666;
  font-weight: normal;
  margin-left: 10px;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
  padding: 20px 0;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .movie-category-container {
    flex-direction: column;
  }

  .category-nav {
    width: 100%;
    border-right: none;
    border-bottom: 1px solid #eee;
  }

  .pagination-container {
    overflow-x: auto;
  }
}
</style>
