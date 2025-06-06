<template>
  <div class="switch" id="switch-cnt">
    <div class="switch__container is-hidden" id="switch-c1">
      <h2 class="switch__title title">欢迎回来 !</h2>

      <button class="switch__button button switch-btn" @click="change">
        登录
      </button>
    </div>

    <div class="switch__container" id="switch-c2">
      <h2 class="switch__title title">你好，朋友 !</h2>

      <button class="switch__button button switch-btn" @click="change">
        注册
      </button>
    </div>
  </div>
</template>
<!-- 界面移动逻辑 -->
<script setup lang="ts">
import { ref } from "vue";
import useEmitter from "@/hooks/useloginEmitter.js";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessageBox } from "element-plus";

const sidebarOpen = ref(false);
const emitter = useEmitter();

const change = () => {
  const switchC1 = document.querySelector("#switch-c1") as HTMLDivElement;
  const switchC2 = document.querySelector("#switch-c2") as HTMLDivElement;
  const switchCtn = document.querySelector("#switch-cnt") as HTMLDivElement;

  switchCtn.classList.toggle("is-txr");
  switchC1.classList.toggle("is-hidden");
  switchC2.classList.toggle("is-hidden");
  sidebarOpen.value = !sidebarOpen.value;
  emitter.emit("change", sidebarOpen.value);
};
</script>

<style scoped>
@import "./login.css";
/* 将源码中的css样式单独存放,在各组件中导入就可以。*/
</style>
