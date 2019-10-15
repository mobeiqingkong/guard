import moment from 'moment'
const tokenService = require('../services/tokenService')
const guardService = require('../services/guardService')
const md5 = require('md5')
const jwt = require('jsonwebtoken')
const privateSecret = 'Id_token12345'
var body

//颁发id_token，采用更新的方式，每次登录成功通过登录用户的user_id查询id_token表，找到符合的条目
//根据符合的条目校验id_token和refresh_token是否过期
//如果id_token过期，则更新id_token，如果refresh_token过期，则id_token，refresh_token都更新

exports.getIdToken = async(params,ctx) => {
  console.log('params.username是：'+params.username)
  console.log('params.password是：'+params.password)
  //验证账户必填的字段
  if(isEmpty(params.username) && isEmpty(params.password)){
    body={
      status: 200,
      code: 100006,
      message:'账户名/手机号/邮箱三选一填，密码必填'
    }
    return body
  }
    //验证账户+密码是否正确,等待user验证
    let user = await guardService.login({'username':params.username,'password':params.password})
    console.log('user是:'+JSON.stringify(user))
    //登录失败则user.isSuccess为false
    if(!isEmpty(user) && user.isSuccess === false){
      body={
        status: 200,
        code: 100005,
        message:user.message
      }
      return body
    }
    //登录校验成功
    var idTokenExpiredTime=moment(new Date(new Date().getTime() + 6 * 60 * 60 * 1000)).format('YYYY-MM-DD HH:mm:ss')
    let idTokenPayload = {
        user_id: user._id,
        name: user.name,
        tel:user.tel,
        email:user.email,
        expired_time:idTokenExpiredTime
    }
    // var newIdToken = jwt.sign(idTokenPayload, privateSecret,{ expiresIn: '1h' })
    var newIdToken = jwt.sign(idTokenPayload, privateSecret)
    //登录如果过期就更新refreshToken，默认7天
    var refreshTokenExpiredTime=moment(new Date(new Date().getTime() + 7 * 24 * 60 * 60 * 1000)).format('YYYY-MM-DD HH:mm:ss')
    let refreshTokenPayload = {
      user_id: user._id,
      name: user.name,
      tel:user.tel,
      email:user.email,
      expired_time:refreshTokenExpiredTime
    }
    //var newRefreshToken = jwt.sign(refreshTokenPayload, privateSecret ,{ expiresIn: '1h' } )
    var newRefreshToken = jwt.sign(refreshTokenPayload, privateSecret)

    //根据user_id来查询这条id_token记录
    var id_Token = await tokenService.findIdToken({'user_id':user._id})
    
    console.log('id_Token是：', id_Token)
    //如果id_Token(对应id_token表的value)存在
    if(!isEmpty(id_Token)){
      //校验refresh_token和id_token是否过期
        //如果id_token过期了，则更新id_token
        if(CheckIsExpire(id_Token.expired_time)){
          console.log("id_token过期了")
          tokenService.updateIdToken({"user_id":user._id},{"value":newIdToken,"expired_time":idTokenExpiredTime})
        }
        //如果refresh_token过期了，则即更新id_token，也更新refresh_token
        if(CheckIsExpire(id_Token.refresh_expired_time)){
          console.log("refresh_token过期了")
          tokenService.updateIdToken({"user_id":user._id},
          {
            "value":newIdToken,
            "expired_time":idTokenExpiredTime,
            "refresh_token":newRefreshToken,
            "refresh_expired_time":refreshTokenExpiredTime}
          )
        }
        var id_Token = await tokenService.findIdToken({'user_id':user._id})
        body = {
          status: 200,
          code: 100001,
          id_token:id_Token.value,
          refresh_token:id_Token.refresh_token
        }
        return body
    }else{
    //如果id_Token不存在，创建新的id_Token
    let createId_Token ={
        "client_id":ctx.Client_tokenId,
        "user_id":user._id,
        "refresh_expired_time":refreshTokenExpiredTime,
        "refresh_token":newRefreshToken,
        //默认有效期为当前时间加6小时
        "expired_time":idTokenExpiredTime,
        "value":newIdToken
      }
      console.log("createId_Token是："+JSON.stringify(createId_Token))
      let addId_Token = await tokenService.addIdToken(createId_Token)
      body = {
        status: 200,
        code: 100002,
        message: "id_Token增加成功",
        "id_token ":addId_Token.value,
        "refresh_token": addId_Token.refresh_token
      }
      // body={addId_Token,...body}
      return body
    }
}




//动态校验
exports.checkIdToken = async (query) => {
  var id_token = await tokenService.findIdToken(query)
  if (isEmpty(id_token)) {
    body = {
      status: 200,
      code: 100002,
      message: "id_token不存在"
    }
  } else {
    if (!CheckIsExpire(id_token.expired_time)) {
      body = {
        status: 200,
        code: 100001,
        message:"验证成功"
      }
    } else {
      body = {
        status: 200,
        code: 100008,
        message: "id_token已经过期"
      }
    }
  }
  return body
}

//刷新/获取新令牌
exports.refreshIdToken = async (query) => {
  var id_token = await tokenService.findIdToken(query)
  if (id_token === undefined) {
    body = {
      status: 200,
      code: 100002,
      message: "refresh_token不存在"
    }
    return body
  } else {
    let decoded = jwt.verify(id_token.value,privateSecret,(error,decoded)=>{
      if(error){
        console.log(error)
        return error
      }
      return decoded
    })
    console.log("mydecoded校验",decoded)
    let payload={
      user_id: decoded.user_id,
      name: decoded.name,
      tel:decoded.tel,
      email:decoded.email,
      expired_time:moment(new Date(new Date().getTime() + 1 * 60 * 60 * 1000)).format('YYYY-MM-DD HH:mm:ss')//刷新后expired_time增加一小时，重新生成id_token
    }
    
    console.log("payload是：",payload)
    const newToken = jwt.sign(payload, privateSecret)
    //将id_token的token和过期时间更新
    id_token.value = newToken
    id_token.expired_time=moment(new Date(new Date().getTime() + 1 * 60 * 60 * 1000)).format('YYYY-MM-DD HH:mm:ss'),
    await tokenService.updateIdToken({ "refresh_token": query.refresh_token }, id_token)
    return body ={
      status: 200,
      code: 100001,
      token: id_token.value
    }
  }
}

//校验client_token
exports.checkClientToken = async (query) => {
  if (query.client_id === ''|| query.client_id === undefined) {
    return body = {
      status: 200,
      code: 200001,
      message:'格式错误'
    }
  }
  console.log('query.client_id是：'+query.client_id)
  let client = await tokenService.findClientToken({"client_id":query.client_id})
  let client1 = await tokenService.findClientToken({"client_id":"13"})
  
  //没有获取到client
  if (client === undefined|client===null) {
    return body = {
      status: 200,
      code: 200002,
      message:'client不存在'
    }
  }
  //验证sign
  // console.log('client是：'+JSON.parse(client))
  console.log('client的类型是：'+typeof(client))
  console.log('client是：'+JSON.stringify(client))
  console.log('client.secret是：'+client1.secret)
  if (md5(client.secret)===query.sign) {
    //如果client_token不存在则添加
    if (!client.token||client.token==='') {
      const newToken = jwt.sign({
        client_id: client.client_id,
        secret:client.secret
      }, 'client_token')
      client.token = newToken
      await tokenService.updateClientToken({ "client_id": client.client_id },client)
    }
    //返回client的token
    return body = {
      client_token:client.token
    }
  } else {
    return body = {
      status: 200,
      code: 200003,
      message:'sign验证失败'
    }
  }
}

exports.findClientToken = async (query) => {
  let client = await tokenService.findClientToken({ "token": query.client_token })
  if (client === undefined) {
    return body = {
      status: 200,
      code: 200002,
      message:'client_token不存在'
    }
  }
  return body = {
    status: 200,
    code: 200000,
    message:'验证成功'
  }
}

//返回true为过期，false为未过期
const CheckIsExpire = (expired_time) => {
  const nowTime = Number(new Date()) ;
  var date = new Date(expired_time)
  var expired_time = (new Date(date)).getTime()
  return (expired_time < nowTime)
}

//判断字符是否为空
function isEmpty(obj){
	return (typeof obj === 'undefined' || obj === null || obj === "");
}