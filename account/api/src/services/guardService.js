import {guardService} from '../utils/axios'


//用户登录
exports.login = (body) => guardService.post(`/user/login`, body)

//增加user
exports.addUser = (body) => guardService.post(`/user/add`, body)
//增加role
exports.addRole = (body) => guardService.post(`/role/add`, body)

//根据条件查询user
exports.findUser = (body) => guardService.post('/user/find', body)
//根据条件查询role
exports.findRole = (body) => guardService.post('/role/find', body)

//修改user
exports.updateUser = (body) => guardService.put(`/user/update`, body)
//修改role
exports.updateRole = (body) => guardService.put(`/role/update`,body)

//根据条件删除user
exports.deleteUser = (ctx) => guardService.delete(`/user/delete/${ctx.params._id}`, ctx)
//根据条件删除role
exports.deleteRole = (ctx) => guardService.delete(`/role/delete/${ctx.params._id}`, ctx)