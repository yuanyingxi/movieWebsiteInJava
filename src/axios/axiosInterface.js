import request from './request'

// 登录接口
export async function login(data) {
  const a = await request.post('/user/login', data)
  return a
}

// 注册接口
export async function register(user) {
  Reflect.deleteProperty(user, 'confirmPassword')
  const a = await request.post('/user/register', user)
  return a
}

// 支付接口
export async function payByZFB(data) {
  try {
    const a = await request.post('/payment/create', data)
    return a
  } catch (error) {
    console.log(error)
    return Promise.reject(error)
  }
}

const moiveDataTypeMap = {
  normal: 0b10,
  vip: 0b01,
  free: 0b00,
}
// 获取主页数据
export async function getHomeData(type) {
  // 获取主页数据
  try {
    const a = await request.get('/home', {
      params: {
        movieType: moiveDataTypeMap[type],
        pageNum: 1,
        pageSize: 50,
      },
    })
    return a
  } catch (error) {
    return Promise.reject(error)
  }
}

// 分页数据请求接口

// 根据类型获取电影列表
export async function getMovieListByType(type, page = 1, size = 8) {
  try {
    // 根据类型获取电影列表
    const a = await request.get('/movie/list/type', {
      params: {
        type,
        page,
        size,
      },
    })
    return a
  } catch (error) {
    return Promise.reject(error)
  }
}

// 根据地区获取电影列表
export async function getMovieListByRegion(region, page = 1, size = 8) {
  try {
    // 根据地区获取电影列表
    const a = await request.get('/movie/list/region', {
      params: {
        region,
        page,
        size,
      },
    })
    return a
  } catch (err) {
    return Promise.reject(err)
  }
}

const rankTypeMap = {
  weekly: 1,
  monthly: 2,
  all: 3,
  rating: 4,
  hot: 5,
}

// 根据热度等获得电影排行列表
export async function getMovieListByTabType(rankType) {
  rankType = rankTypeMap[rankType]
  try {
    // 根据rankType获取电影排行列表
    const a = await request.get('/rank/list', {
      params: {
        rankType,
      },
    })
    return a
  } catch (error) {
    return Promise.reject(error)
  }
}

// 根据电影id获取电影相关任务的信息
export async function getPlayerInfoByMovieId(movieId) {
  try {
    const a = await request.get('/movieCover/creatorsName', {
      params: {
        movieNo: movieId,
      },
    })
    return a.data
  } catch (error) {
    console.error(error)
    return Promise.reject(error)
  }
}

// 主创作品接口
export async function getCreatorWorks(creator_id) {
  try {
    const a = await request.get('/creatorMovie', {
      params: {
        creator_id,
      },
    })
    return a.data
  } catch (error) {
    console.error(error)
    return Promise.reject(error)
  }
}
