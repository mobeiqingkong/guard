const mongoose = require('mongoose')
const Schema = mongoose.Schema
const userSchema = Schema({
    deleted: {
        type: Boolean, // 是否删除,如果已删除，后续不检索
        default: false
      },
    relative : [{
        type: Schema.Types.ObjectId, ref: 'role'//关系，关联role表
      }],
    username : {type: String, required: true },//用户账户名
    password : {type: String, required: true },//用户密码
    name : {type: String, required: true},//用户姓名
    tel : {type: String, required: true},//用户手机号
    email : {type: String, required: true},//用户Email
    enable : {
        type: Boolean, // 该用户是否可用
        default: true
      },
    remark: String,  // 备注
    workplaceMenus: Object,
    createUser: String,             // 创建人
    updateUser: String,             // 更新人
    createTime: String,             // 创建时间
    updateTime: String,             // 更新时间
})
var user = mongoose.model('user',userSchema)

module.exports = user