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
//增加用户
router.post('/user/add', async (ctx) => {
  ctx.body = await authController.addUser(ctx.request.body)
})

//删除用户
router.delete('/user/delete', async (ctx) => {
  ctx.body = await authController.deleteUser(ctx.request.body)
})

//修改用户信息
router.put('/user/update', async (ctx) => {
  console.log('打印到了修改User', '')
  ctx.body = await authController.updateUser(ctx.request.body)
})

//查询用户
router.post('/user/find', async (ctx) => {
  ctx.body = await authController.findUser(ctx.request.body)
})


//增加关联人
router.post('/role/add', async (ctx) => {
  ctx.body = await authController.addRole(ctx.request.body)
})

//删除关联人
router.delete('/role/delete', async (ctx) => {
  ctx.body = await authController.deleteRole(ctx.request.body)
})

//修改关联人信息
router.put('/role/update', async (ctx) => {
  ctx.body = await authController.updateRole(ctx.request.body)
})

//查询关联人信息
router.post('/role/find', async (ctx) => {
  ctx.body = await authController.findRole(ctx.request.body)
})
export default router