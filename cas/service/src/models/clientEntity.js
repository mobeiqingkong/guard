const mongoose = require('mongoose')
const Schema = mongoose.Schema
const clientSchema = Schema({ 
    name: {type: String, required: true},//系统名称
    client_id : {type: String, unique: true, required: true },//应用客户端id
    secret : {type: String, required: true},//密钥
    token : {type: String, required: true},//token
    isInternal : {type: Boolean, required: true},//是否为内部系统 client
    created_time : {type: String, required: true}//创建时间
})

var Client = mongoose.model('Client',clientSchema)

module.exports = Client