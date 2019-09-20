import axios from 'axios'
import qs from 'qs'
import guardServConfig from '../../config/guard.serv.config'
import {logger} from '../middlewares/Logger'

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


	export default {
	guardService
}

