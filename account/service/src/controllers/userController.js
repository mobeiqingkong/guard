import assert from 'assert'
import moment from 'moment'
const dbHelper = require('../dbhelper/dbHelper')

const md5 = require('md5')
const model = 'user'

//根据条件查询user
exports.findOne = async (params) => {
  return await dbHelper.findOne(model, params)
}

//增加一条user数据
exports.add = async (params) => {
  params.created_time = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
  return await dbHelper.add(model, params)

}
  
//修改一条user数据
exports.update = async (filter,params) => {  
return await dbHelper.update(model, filter, params)
}

//删除一条user数据
exports.delete = async (filter) => {
  return await dbHelper.delete(model, filter)
}

//用户登录
exports.login = async (body) => {
  let username = body.username
  let password = md5(body.password)
  let phonenumberRule=/^1[0-9]{10}$/
  let emailRule=/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
  let user
 if(phonenumberRule.test(username)){
    user = await dbHelper.findOne(model, {tel: username, deleted: false})
 }else if(emailRule.test(username)){
  user = await dbHelper.findOne(model, {email: username, deleted: false})
 }else{
  user = await dbHelper.findOne(model, {username: username, deleted: false})
 }
  assert(user, '账号/邮箱/电话号码不存在！')
  assert(user.enable === true, '该账号没有使用权限！')
  assert(user.password === password, '密码不正确！')
  return user
}
