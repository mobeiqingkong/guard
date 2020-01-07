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

  console.log("ctx.request.body.filter是："+JSON.stringify(ctx.request.body.filter))
  console.log("ctx.request.body.params是："+JSON.stringify(ctx.request.body.params))
  console.log("ctx.request.body是："+JSON.stringify(ctx.request.body))
  ctx.body = await authController.updateUser({"filter":JSON.parse(ctx.request.body.filter),"params":JSON.parse(ctx.request.body.params)})
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
router.delete('/role/delete/:_id', async (ctx) => {
  console.log('删除的用户id是'+ctx.params._id)
  ctx.body = await authController.deleteRole(ctx)
})

//修改关联人信息
router.put('/role/update', async (ctx) => {
  console.log("ctx.request.body.filter是："+JSON.stringify(ctx.request.body.filter))
  console.log("ctx.request.body.params是："+JSON.stringify(ctx.request.body.params))
  console.log("ctx.request.body是："+JSON.stringify(ctx.request.body))
  ctx.body = await authController.updateRole({"filter":JSON.parse(ctx.request.body.filter),"params":JSON.parse(ctx.request.body.params)})
})
//查询关联人信息
router.post('/role/find', async (ctx) => {
  ctx.body = await authController.findRole(ctx.request.body)
})


//查询账本
router.post('/ledget/query', async (ctx) => {
  ctx.body = await authController.query(ctx.request.body)
})

//增加账本
router.post('/ledget/invoke', async (ctx) => {
  ctx.body = await authController.invoke(ctx.request.body)
})

//注册普通用户
router.post('/ledget/registerUser', async (ctx) => {
  ctx.body = await authController.registerUser(ctx.request.body)
})

//注册管理员
router.post('/ledget/enrollAdmin', async (ctx) => {
  ctx.body = await authController.enrollAdmin(ctx.request.body)
})

export default router