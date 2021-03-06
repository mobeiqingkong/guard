import assert from 'assert'
import moment from 'moment'
import { createContext } from 'vm'
const dbHelper = require('../dbhelper/dbHelper')
const model = 'client'


//根据条件查询client
exports.findOne = async (params) => {
  return await dbHelper.findOne(model, params)
}

//增加一条client数据
exports.add = async (params) => {
  params.created_time = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
  return await dbHelper.add(model, params)
}
  
//修改一条client数据
exports.update = async (filter,params) => {  
return await dbHelper.update(model, filter, params)
}

//删除一条client数据
exports.delete = async (filter) => {
  return await dbHelper.delete(model, filter)
}