import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import mitt from "mitt";

const app = createApp(App);
app.config.globalProperties.emitter = mitt();
//Element Plus
import ElementPlus from "element-plus";
import "element-plus/theme-chalk/index.css";
import zhCn from "element-plus/es/locale/lang/zh-cn";
app.use(ElementPlus, { locale: zhCn });

app.use(store).use(router).mount("#app");
