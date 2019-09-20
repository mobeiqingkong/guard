import Router from 'koa-router'
import * as authController from '../controllers/authController'
const redis = require('../utils/redis')

const router = new Router({
    prefix: '/guard'
})

//登录接口
router.post('/user/login', async (ctx) => {
  ctx.body = await authController.login(ctx.request.body)
})
//查询用户
router.post('/user/findUser', async (ctx) => {
  ctx.body = await authController.findUser(ctx.request.body)
})

//增加用户
router.post('/user/add', async (ctx) => {
  ctx.body = await authController.add(ctx.params)
})

//根据
router.get('/id_refresh_token/:refresh_token', async (ctx)=>{
  ctx.body = await authController.refreshIdToken(ctx.params)
})

//验证
router.post('/client_token', async (ctx) => {
  ctx.body = await authController.checkClientToken(ctx.request.body)
})

//验证
router.get('/client_token/:client_token', async (ctx) => {
  ctx.body = await authController.findClientToken(ctx.params)
})

export default router