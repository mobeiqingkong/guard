import {guardService} from '../utils/axios'
  
//   exports.query = (query) => guardService.get('/user', {
//     params: query
//   })
  
//   exports.queryByUserName = (query) => guardService.get('/user/userName', {
//     params: query
//   })
  
//   exports.userAll = (query) => guardService.get('/user/userAll', {
//     params: query
//   })
  
//   exports.findOne = (query) => guardService.get(`/user/${query._id}`)
  
//   exports.add = (userId, body) => guardService.post(`/user/${userId}`, body)

exports.login = (body) => guardService.post('/user/login', body)
  
//   exports.update = (user) => guardService.put('/user', user)
  
//   exports.delete = (params) => guardService.delete(`/user/${params._id}`)
  