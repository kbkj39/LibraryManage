<template>
  <div class="body">
    <div class="header">
      <h1 class="title">{{ title }}</h1>
      <!-- 使用动态数据绑定 -->
      <div class="identity-switch">
        <button @click="switchToReader">切换到读者</button>
        <button @click="switchToAdmin">切换到管理员</button>
      </div>
    </div>

    <div class="main">
      <SwitchView />
      <SignIn :role="userRole" />
      <SignUp />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"; // 引入 ref
import SwitchView from "@/logincomponents/SwitchView.vue";
import SignIn from "@/logincomponents/Sign-in-user";
import SignUp from "@/logincomponents/Sign-up-user.vue";
import router from "@/router";
// 定义响应式数据
const title = ref("你好，读者"); // 初始标题
const userRole = ref("reader");
const switchToReader = () => {
  title.value = "你好，读者";
  router.push("/loginUser");
  userRole.value = "reader";

  console.log("切换到读者");
};

const switchToAdmin = () => {
  router.push("/LoginTest");
  userRole.value = "admin";
  title.value = "您好，管理员"; // 切换到管理员时更新标题
  console.log("切换到管理员");
};
</script>

<style scoped>
*,
*::after,
*::before {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  user-select: none;
}

.body {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column; /* 使内容垂直排列 */
  justify-content: flex-start; /* 从顶部开始对齐 */
  font-family: "Montserrat", sans-serif;
  font-size: 12px;
  color: #a0a5a8;
  background: url("~@/assets/images/backgroud.jpg") 100% no-repeat;
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 5px; /* 加一些内边距 */
  background-color: hsl(210, 100%, 63%); /* 背景颜色 */
}

.title {
  color: white; /* 字体颜色 */
  margin: 0; /* 去掉默认边距 */
}

.identity-switch button {
  background-color: white;
  color: hsl(210, 100%, 63%);
  border: none;
  padding: 10px 15px;
  margin-left: 10px;
  cursor: pointer;
  border-radius: 5px; /* 圆角按钮 */
}

.identity-switch button:hover {
  background-color: #e0e0e0; /* 悬停时的背景颜色 */
}

.main {
  margin: 40px auto;
  background-color: rgba(236, 240, 243, 0.7);
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: none;
  border-radius: 20px;
  overflow: hidden;
}

@media (max-width: 1200px) {
  .main {
    transform: scale(0.7);
  }
}
@media (max-width: 1000px) {
  .main {
    transform: scale(0.6);
  }
}
@media (max-width: 800px) {
  .main {
    transform: scale(0.5);
  }
}
@media (max-width: 600px) {
  .main {
    transform: scale(0.4);
  }
}
</style>
