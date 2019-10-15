const jwt = require("jsonwebtoken");
const secret = 'client_token'

module.exports.checkToken = function () {
  return async (ctx, next) => {
    var url = ctx.request.url;
    // 登录登出 不用检查
    if (url.indexOf("/account/login")!= -1 || url.indexOf("/account/logout")!= -1 ) {
      await next();
    }else {
      if (ctx.request.headers.authorization) {
        console.log("每一次都会调用token验证，url是"+url)
        const parts = ctx.request.headers.authorization.split(' ');
        const client_token = parts[1];
        //校验token
        await jwt.verify(client_token, secret, (error,decoded)=>{
          if (error) {
            error.status=401
            throw error
          }
          return decoded
        })
        await next();
      }else {
        ctx.body = {
          status: 401,
          message: '没有client token,请先认证'
        }
        return ctx.body
      }
    }
  }
}
