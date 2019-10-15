import Router from 'koa-router'
const jwt = require('jsonwebtoken');
const moment = require('moment')
import * as id_tokenController from '../controllers/id_tokenController'
import * as clientController from '../controllers/clientController'
const router = new Router({
  prefix: '/auth'
})
//查询id_token
router.post('/id_token/find', async (ctx) => {
  ctx.body = await id_tokenController.findOne(ctx.request.body)
})

//增加id_token
router.post('/id_token/add', async (ctx) => {
  ctx.body = await id_tokenController.add(ctx.request.body)
})

//修改id_token
router.put('/id_token/update', async (ctx) => {
  ctx.body = await id_tokenController.update(ctx.request.body.filter, ctx.request.body.params)
})


//删除id_token
router.delete('/id_token/delete/:_id', async (ctx) => {
  ctx.body = await id_tokenController.delete(ctx.params)
})



//查询client
router.post('/client_token/find', async (ctx) => {
  ctx.body = await clientController.findOne(ctx.request.body)
})

//增加client
router.post('/client_token/add', async (ctx) => {
  ctx.body = await clientController.add(ctx.request.body)
})

//修改client
router.put('/client_token/update', async (ctx) => {
  ctx.body = await clientController.update(ctx.request.body.filter, ctx.request.body.params)
})

//删除client
router.delete('/client_token/delete/:_id', async (ctx) => {
  ctx.body = await clientController.delete(ctx.params)
})

export default router