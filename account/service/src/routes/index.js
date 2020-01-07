import Router from 'koa-router'
import * as userController from '../controllers/userController'
import * as roleController from '../controllers/roleController'
import * as ledgeController from '../controllers/ledgeController'
const md5 = require('md5')
const router = new Router({
  prefix: '/guard'
})

//用户登录
router.post('/user/login', async (ctx) => {
  console.log('登录请求体部是'+JSON.stringify(ctx.request.body))
  ctx.body = await userController.login(ctx.request.body)
})

//查询user
router.post('/user/find', async (ctx) => {
  ctx.body = await userController.findOne(ctx.request.body)
})

//增加user
router.post('/user/add', async (ctx) => {
  ctx.request.body.password=md5(ctx.request.body.password)
  ctx.body = await userController.add(ctx.request.body)
})

//修改user
router.put('/user/update', async (ctx) => {
  ctx.body = await userController.update(ctx.request.body.filter, ctx.request.body.params)
})


//删除user
router.delete('/user/delete/:_id', async (ctx) => {
  ctx.body = await userController.delete(ctx.params)
})



//查询role
router.post('/role/find', async (ctx) => {
  ctx.body = await roleController.findOne(ctx.request.body)
})

//增加role
router.post('/role/add', async (ctx) => {
  console.log('增加角色接口:'+JSON.stringify(ctx.request.body))
  ctx.body = await roleController.add(ctx.request.body)
})

//修改role
router.put('/role/update', async (ctx) => {
  ctx.body = await roleController.update(ctx.request.body.filter, ctx.request.body.params)
})

//删除role
router.delete('/role/delete/:_id', async (ctx) => {
  ctx.body = await roleController.delete(ctx.params)
})





//查询账本
router.post('/ledget/query', async (ctx) => {
  ctx.body = await ledgeController.query(ctx.request.body)
})

//增加账本
router.post('/ledget/invoke', async (ctx) => {
  ctx.body = await ledgeController.invoke(ctx.request.body)
})

//注册普通用户
router.post('/ledget/registerUser', async (ctx) => {
  ctx.body = await ledgeController.registerUser(ctx.request.body)
})

//注册管理员
router.post('/ledget/enrollAdmin', async (ctx) => {
  ctx.body = await ledgeController.enrollAdmin(ctx.request.body)
})
export default router