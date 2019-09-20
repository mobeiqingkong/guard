import Koa from 'koa'
import koaBody from 'koa-body'
import router from './routes/'


import qsJSON from './middlewares/QueryStringJSON'
const logger = require('./middlewares/Logger')
const session = require('koa-session')
var cors = require('kcors')
const app = new Koa()

// queryString解析中间件
// 将QueryString中JSON字符串解析为Object,默认参数为ctx.query.params
qsJSON(app)

// try-catch 中间件
app.use(async (ctx, next) => {
  try {
    await next()
  } catch (err) {
    // 系统内自定义异常
    if (err.ercode) {
      ctx.response.body = err
    } else {
      if (err.status = 401) {
        ctx.response.status = 200
        ctx.response.body = {
          status: err.status,
          message: err.message,
          isSuccess: false
        }
      } else {
        ctx.response.status = 200
        ctx.response.body = {
          status: 200,
          message: err.message,
          isSuccess: false
        }
      }
    }
  }
})

// 日志中间件
app.use(logger.intercept())

app.use(cors({
  credentials: true,
  exposeHeaders: 'Token'
}))

app.keys = ['some secret hurr']
const CONFIG = {
  key: 'session',
  maxAge: 2 * 24 * 3600 * 1000,  // cookie的过期时间 毫秒
  overwrite: true,
  httpOnly: true, // true表示只有服务器端可以获取cookie
  signed: true,
  rolling: false,  // 在每次请求时强行设置 cookie，这将重置 cookie 过期时间（默认：false）
  renew: false
}
app.use(session(CONFIG, app))
//app.use(login())

// http请求解析中间件
app.use(koaBody())

// 分页中间件
// app.use(pagination())

app.use(async (ctx, next) => {
  ctx.params = ctx.request.body.fields
  await next()
})

// 路由中间件
app.use(router.routes(), router.allowedMethods())
module.exports = app
