import assert from 'assert'
import moment from 'moment'
const dbHelper = require('../dbhelper/dbHelper')
const model = 'id_token'


//查询接口
exports.findOne = async (params) => {
  return await dbHelper.findOne(model, params)
}

//增加一条id_token数据
exports.add = async (params) => {
  return await dbHelper.add(model, params)
}

//修改一条id_token数据
exports.update = async (filter,params) => {
  return await dbHelper.update(model, filter, params)
}

//删除一条id_token数据
exports.delete = async (filter) => {
  return await dbHelper.delete(model, filter)
}