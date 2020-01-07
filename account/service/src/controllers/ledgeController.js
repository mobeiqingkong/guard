const enrollAdminLedge=require('../dbhelper/enrollAdmin')
const registerUserLedge=require('../dbhelper/registerUser')
const queryLedge=require('../dbhelper/query')
const invokeLedge=require('../dbhelper/invoke')
const dbHelper = require('../dbhelper/dbHelper')
const model = 'ledge'
//注册管理员admin接口
exports.enrollAdmin = async (params) => {
  return await enrollAdminLedge.enrollAdminMain(params.certificateAuthoritiesCustom,params.adminCustom,params.enrollmentIDCustom,params.enrollmentSecretCustom) 
}
//注册普通用户接口
exports.registerUser = async (params) => {
  return await registerUserLedge.registerMain(params.userCustom,params.adminCustom,params.affiliationCustom,params.enrollmentIDCustom,params.roleCustom)
}
//查询账本接口
exports.query = async (params) => {
  return await queryLedge.queryMain(params.userCustom ,params.channelCustom,params.contractCustom,params.params)
}
//增加/更新账本信息接口
exports.invoke = async (params) => {
  return await invokeLedge.invokeMain(params.userCustom,params.channelCustom,params.contractCustom,params.params)
}
