import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'

import zhCn from 'element-plus/es/locale/lang/zh-cn'
import ElementPlus from 'element-plus'
import * as ElIconModules from '@element-plus/icons'
import 'element-plus/dist/index.css'
import '@/assets/icon/iconfont.js' // 图标
import '@/assets/icon/iconfont.css'
const app =createApp(App)

for(let iconName in ElIconModules){
    app.component(iconName,ElIconModules[iconName])
}
app.use(router).use(ElementPlus,{locale: zhCn,size:"small"}).mount('#app')
