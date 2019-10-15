import Router from 'koa-router'
import fs from 'fs'
import path from 'path'

const router = new Router({
  prefix: '/auth'
})

/**
 * 扫描目录，挂载路由
 */
const addRoutes = (routesUrl) => {
  // 同步方法无所谓的，因为是在服务器跑起来之前就完成映射，不会有任何性能影响
  const routesDir = fs.readdirSync(routesUrl)
  routesDir.forEach((filename) => {
    const file = path.join(routesUrl, '/' + filename)
    if (fs.lstatSync(file).isDirectory()) {
      const tdir = path.join(routesUrl, filename)
      addRoutes(tdir)
      return
    }
    const routerModel = require(path.join(routesUrl, filename))
    router.use(routerModel.routes(), routerModel.allowedMethods())
  })
}

const routePath = path.join(__dirname, '../../routes')
addRoutes(routePath)

export default router
