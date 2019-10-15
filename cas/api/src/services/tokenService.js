import {casService} from '../utils/axios'


//增加id_token
exports.addIdToken = (query) => casService.post(`/auth/id_token/add`, query)
//增加client_token
exports.addClientToken = (query) => casService.post(`/auth/client_token/add`, query)

//根据条件查询id_token
exports.findIdToken = (query) => casService.post('/auth/id_token/find', query)
//根据条件查询client_token
exports.findClientToken = (query) => casService.post('/auth/client_token/find', query)

//修改id_token
exports.updateIdToken = (filter,query) => casService.put(`/auth/id_token/update`, {filter,params:query})
//修改client_token
exports.updateClientToken = (filter,query) => casService.put(`/auth/client_token/update`,{filter,params:query})

//根据条件删除id_token
exports.deleteIdToken = (query) => casService.delete('/auth/id_token/delete', query)
//根据条件删除client_token
exports.deleteClientToken = (query) => casService.delete('/auth/client_token/delete', query)