
import moment from 'moment'
const guardService = require('../services/guardService')
const redis = require('../utils/redis')
//登录方法
exports.login = async (body) => {
  console.log('打印到了1')
  let user = await guardService.login(body)
  console.log('user.relative是'+user.relative)
  let rolesId = user.relative
  let roles=[]
  //遍历获取关联的角色
  for(var n=0;n<rolesId.length;n++){
    roles[n]=await findRoleMethod(rolesId[n])
    console.log('roles[n]是'+JSON.stringify(roles[n]))
  }
  //保存登录后的信息
  let key = user._id + "guard"
  redis.set(key, JSON.stringify({
  _id: user._id,
  user: user,
  roles: roles
  }), 'iows:guard:')
  redis.expire(key, 86400, 'iows:guard:')
  user.relative=roles
  return user
}

//增加用户
exports.addUser = async (body) => {
  console.log("打印到增加用户,body是"+JSON.stringify(body))
  return await guardService.addUser(body)
}
//增加角色
exports.addRole = async (body) => {return await guardService.addRole(body)}
//删除用户
exports.deleteUser = async (body) => {return await guardService.deleteUser(body)}
//删除角色
exports.deleteRole = async (body) => {return await guardService.deleteRole(body)}
//修改用户
exports.updateUser = async (body) => {return await guardService.updateUser(body)}
//修改角色
exports.updateRole = async (body) => {return await guardService.updateRole(body)}
//查询用户
exports.findUser = async (body) => {return await guardService.findUser(body)}
//查询角色
exports.findRole = async (body) => {return await guardService.findRole(body)}

//注册管理员
exports.enrollAdmin = async (body) => {return await guardService.enrollAdmin(body)}
//注册普通用户
exports.registerUser = async (body) => {return await guardService.registerUser(body)}
//查询账本
exports.query = async (body) => {return await guardService.query(body)}
//更新账本
exports.invoke = async (body) => {return await guardService.invoke(body)}





//根据id查询角色方法
function findRoleMethod(roleId){
  return guardService.findRole({"_id":roleId})
}

