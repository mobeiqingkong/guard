import Koa from 'koa'
import koaBody from 'koa-body'
import router from './routes'
import qsJSON from './middlewares/QueryStringJSON'
const logger = require('./middlewares/Logger')
require('./models')
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
    ctx.throw(err.status || 500, err.message, {
      expose: true
    })
  }
})

// 日志中间件
app.use(logger.intercept())

// http请求解析中间件
app.use(koaBody({
  multipart: true
}))

app.use(cors({
  credentials: true,
  exposeHeaders: 'Token'
}))

app.use(async (ctx, next) => {
  ctx.params = ctx.request.body.fields
  await next()
})

// 路由中间件
app.use(router.routes(), router.allowedMethods())

module.exports = app
