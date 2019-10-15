import Router from 'koa-router'
import * as authController from '../controllers/authController'
const redis = require('../utils/redis')

const router = new Router({
    prefix: '/auth'
})

//验证用户名，密码和client_token来颁发id_token
//必要参数：userName,phoneNumber,email,password,token
router.post('/id_token', async (ctx) => {
  ctx.body = await authController.getIdToken(ctx.request.body,ctx)
})

//动态校验id_token（是否存在/过期）
router.get('/id_token/:value', async (ctx) => {
  ctx.body = await authController.checkIdToken(ctx.params)
})

//根据id_token的refresh_token找到数据并更新它的value（将id_token的token值和过期时间更新）
router.get('/id_refresh_token/:refresh_token', async (ctx)=>{
  ctx.body = await authController.refreshIdToken(ctx.params)
})

//验证client_id和sign来获取或颁发client_token（如果验证成功返回token，如果不存在token则生成并返回）
router.post('/client_token', async (ctx) => {
  ctx.body = await authController.checkClientToken(ctx.request.body)
})

//验证client_token是否存在
router.get('/client_token/:client_token', async (ctx) => {
  ctx.body = await authController.findClientToken(ctx.params)
})

export default router