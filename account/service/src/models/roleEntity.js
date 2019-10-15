const mongoose = require('mongoose')
const Schema = mongoose.Schema
const roleSchema = Schema({ 
    deleted: {
        type: Boolean, // 是否删除,如果已删除，后续不检索
        default: false
      },
    name : {type: String, required: true},//用户姓名
    tel : {type: String, required: true},//用户手机号
    email : {type: String, required: true},//用户Email
    relationship : {type: String, required: true},//与关联人的关系
})

var role = mongoose.model('role',roleSchema)

module.exports = role