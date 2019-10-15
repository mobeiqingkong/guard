const mongoose = require('mongoose')
const Schema = mongoose.Schema
const idtokenSchema = Schema({
    value: {type: String, required: true},//id_token的值
    client_id : {type: String, required: true },//应用客户端id
    user_id : {type: String, required: true},//用户id
    expired_time : {type: String, required: true},//过期时间
    refresh_token : {type: String, required: true},//更新token
    refresh_expired_time : {type: String , required: true}//更新token的过期时间，默认是6小时
})

var Idtoken = mongoose.model('Id_token',idtokenSchema)

module.exports = Idtoken