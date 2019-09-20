#!/usr/bin/env node

var currentPath = process.cwd()

require('runkoa')(currentPath + '/config/koa.js')
