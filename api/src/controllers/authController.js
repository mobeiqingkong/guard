
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
  return user
}

//查询用户
exports.findUser = async (body) => {return await guardService.findUser(body)}
//查询角色
exports.findRole = async (body) => {return await guardService.findRole(body)}

//根据id查询角色方法
function findRoleMethod(roleId){
  return guardService.findRole({"_id":roleId})
}
