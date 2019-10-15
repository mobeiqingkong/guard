const logger = require('../middlewares/Logger').getLogger('ioam')
var redis = require('redis')
const redisConfig = require('../../config/redis.config').config
const uuid = require('node-uuid')

class Redis {
  constructor () {
    this.id = uuid.v4().toString()
  }
  connect () {
    this.prefix = redisConfig.prefix
    this.subclient = redis.createClient(redisConfig)
    this.pubclient = redis.createClient(redisConfig)
    delete redisConfig.prefix
    this.client = redis.createClient(redisConfig)
    this.client.on('error', (err) => {
      logger.info('connect to redis error')
      logger.info(err)
    })
    let that = this
    this.client.on('ready', async () => {
      console.log('connect to redis succ')
    })
    this.subscribes = {}
    this.subclient.on('message', (channel, message) => {
      logger.info(`message: ${message} from channel: ${channel}`)
      that.subscribes[channel].forEach(cb => {
        cb(message)
      })
    })
  }
  del (key, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.del(`${prefixed}${key}`, (err, num) => {
        if (err) reject(err)
        resolve({count: num, success: true})
      })
    })
  }
  set (key, content, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.set(`${prefixed}${key}`, content, (err, succ) => {
        if (err) reject(err)
        if (succ === 'OK') {
          resolve({success: true})
        } else {
          resolve({success: false})
        }
      })
    })
  }
  get (key, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.get(`${prefixed}${key}`, (err, value) => {
        if (err) reject(err)
        resolve(value)
      })
    })
  }
  /**
   * set if not exist
   * key不存在，则成功加入缓存并且返回1，
   * key存在，返回0
   */
  setnx (key, value, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.setnx(`${prefixed}${key}`, value, (err, ret) => {
        if (err) reject(err)
        resolve(ret)
      })
    })
  }
  /**
   * 先获取key对应的value值，若不存在则返回nil，然后将旧的value更新为新的value
   * @param {*} key 
   * @param {*} value 
   */
  getset (key, value, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.getset(`${prefixed}${key}`, value, (err, ret) => {
        if (err) reject(err)
        resolve(ret)
      })
    })
  }
  /**
   * 设置key超时时间
   * @param {*} key 
   * @param {*} timeoutSeconds 单位秒
   */
  expire (key, timeoutSeconds, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.expire(`${prefixed}${key}`, timeoutSeconds, (err, value) => {
        if (err) reject(err)
        resolve(true)
      })
    })
  }
  /**
   * 
   * @param {*} pattern 
   */
  keys (pattern) {
    return new Promise((resolve, reject) => {
      this.client.keys(pattern, (err, keys) => {
        if (err) reject(err)
        resolve(keys)
      })
    })
  }
  /**
   * set add 命令
   */
  sadd (key, value, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.sadd(`${prefixed}${key}`, value, (err, value) => {
        if (err) reject(err)
        resolve(value)
      })
    })
  }
  spop (key, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.spop(`${prefixed}${key}`, (err, value) => {
        if (err) reject(err)
        resolve(value)
      })
    })
  }
  /**
   * srem 命令，移除set集合中的某个值value
   * @param {*} set 
   * @param {*} value 
   */
  srem (key, value, prefix) {
    let prefixed = prefix ? prefix : this.prefix
    return new Promise((resolve, reject) => {
      this.client.srem(`${prefixed}${key}`, value, (err, value) => {
        if (err) reject(err)
        resolve(value)
      })
    })
  }
  publish (channel, message) {
    this.pubclient.publish(channel, message, () => {
      logger.info('publish succ')
    })
  }
  /**
   * @param {*} channel 
   * @param {*} callback
   * 
   */
  subscribe (channel, callback) {
    this.subclient.subscribe(channel)
    if (!this.subscribes[channel]) {
      this.subscribes[channel] = []
    }
    this.subscribes[channel].push(callback)
  }
  /**
   * @returns true/false 获取锁成功/获取锁失败
   * @param {*} key
   * @param {*} expireTime // 超时时间 
   */
  async lock (key, expireTime) {
    let flag = false
    try {
      let v = await this.getset(key, this.id)
      // 如果获取锁成功，则设置过期时间，以免发生死锁
      this.expire(key, expireTime)
      if (!v) {
        flag = true
      } else {
        if (v === this.id) {
          flag = true
        }
      }
    } catch (err) {
      logger.error(`redis lock err key: ${key}, expireTime: ${expireTime}`)
      logger.error(err)
      flag = false
    }
    return flag
  }
  async unlock (key) {
    return this.del(key)
  }
}

module.exports = new Redis()
