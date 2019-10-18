import axios from 'axios'
import qs from 'qs'
import casServConfig from '../../config/cas.serv.config'
import guardServConfig from '../../config/guard.serv.config'
import {logger} from '../middlewares/Logger'

const casService = axios.create({
  baseURL: casServConfig.cas_serv_dns,
  paramsSerializer: (params) => {
    return qs.stringify({params: params})
  }
})

casService.interceptors.response.use(
	(response) => {
		if (response.status === 200) {
			return response.data
		}
	},
	(e) => {
		logger.error('axios response', e.response.data)
		throw new Error(e.response.data)
	})
	casService.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
	casService.interceptors.request.use(config => {
		var token= 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiNWI5NzI3MWExZGIyNGQ1NzQxYzEyNDc3IiwibmFtZSI6IueMq-Wwj-m7kSIsInRlbCI6IjE1MTAwMDAwMDAwIiwiZW1haWwiOiIxQDkwOTAuY29tIiwiZXhwaXJlZF90aW1lIjoiMjAxOS0xMC0yMiAyMDoxMDoxNSIsImlhdCI6MTU3MTE0MTQxNX0.lljPsncNF5Juh2jl2L9a9QEnNIqul_rdKIt8b2r2L2s'
		config.headers.common['Authorization'] = 'Bearer ' + token;
		return config;
	})
	
const guardService = axios.create({
	baseURL: guardServConfig.guard_serv_dns,
	paramsSerializer: (params) => {
	  return qs.stringify({params: params})
	}
  })
  
  guardService.interceptors.response.use(
	  (response) => {
		  if (response.status === 200) {
			  return response.data
		  }
	  },
	  (e) => {
		  logger.error('axios response', e.response.data)
		  throw new Error(e.response.data)
	  })
	guardService.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
  	guardService.interceptors.request.use(config => {
    var token= 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGllbnRfaWQiOiIxMjM0NTYiLCJzZWNyZXQiOiIxMjM0NSIsImlhdCI6MTU3MTE0MTc1NX0.J7uQinrpzuAuA4sRbQkWqyaoGQYV8sXXluK83c5ZFr0'
    config.headers.common['Authorization'] = 'Bearer ' + token;
    return config;
})
export default {
	casService,guardService
}

