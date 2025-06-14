<template>
  <div class="navUser">
    <el-container class="layout-container-demo" style="height: 100%">
      <el-aside>
        <!-- 导航栏 -->
        <el-scrollbar>
          <div class="title"><p>图书管理系统</p></div>
          <el-menu router :default-active="currentRoute.path" unique-opened>
            <el-menu-item
              v-for="(item, index) in routes"
              :key="index"
              :index="item.path"
              @click="addTab(item.name, item.path)"
            >
              <el-icon v-if="item.name === '图书信息'"><Reading /></el-icon>
              <el-icon v-if="item.name === '用户信息'"><User /></el-icon>
              <p>{{ item.name }}</p>
            </el-menu-item>
            <el-menu-item @click="loginOut">
              <el-icon><Back /></el-icon>退出系统
            </el-menu-item>
          </el-menu>
        </el-scrollbar>
      </el-aside>

      <el-main>
        <!-- 用户信息工具栏 -->
        <div class="toolbar">
          <span>{{ userInfo.username }}</span>
          <el-dropdown>
            <el-icon><Setting /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="loginOut">登出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <!-- 标签栏 -->
        <div class="tabs">
          <el-tabs
            v-model="editableTabsValue"
            @tab-click="tabClick"
            @tab-remove="tabRemove"
          >
            <el-tab-pane
              v-for="item in editableTabs"
              :label="item.name"
              :key="item.path"
              :name="item.path"
              :closable="item.closable"
            >
              <router-view :key="currentRoute.fullPath"></router-view>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Reading, User, Setting, Back } from "@element-plus/icons-vue";
import { ElMessageBox } from "element-plus";
import jsCookie from "js-cookie";

// 获取路由实例和当前路由
const router = useRouter();
const currentRoute = useRoute();

// 判断用户登录状态
const userLoginState = jsCookie.get("username");
if (userLoginState == undefined) {
  ElMessageBox.alert("登录信息已过期，请重新登录", "信息", {
    confirmButtonText: "确认",
    callback: () => {
      // 页面跳转
      router.push("/login");
    },
  });
}

// 用户信息
const userInfo = ref({
  username: jsCookie.get("username"),
});

// 退出系统
const loginOut = () => {
  ElMessageBox.alert("您已成功登出", "信息", {
    confirmButtonText: "确认",
    callback: () => {
      // 页面跳转
      router.push("/loginUser");
      // 删除cookie
      jsCookie.remove("username");
    },
  });
};

// 路由配置
const routes = [
  // 假设这是从某个地方获取的路由配置
  { name: "图书信息", path: "/bookUser" },
  { name: "用户信息", path: "/userUser" },
];

// 添加标签栏
const addTab = (name: string, path: string) => {
  let isExist = false;
  for (const item of editableTabs.value) {
    if (item.name === name && item.path === path) {
      isExist = true;
      break;
    }
  }
  if (!isExist) {
    editableTabs.value.push({
      name: name,
      path: path,
      closable: true,
    });
  }
  editableTabsValue.value = path;
};

// 标签栏
let editableTabsValue = ref("/bookUser");
const editableTabs = ref([
  {
    name: "图书信息",
    path: "/bookUser",
    closable: false,
  },
]);

// 点击标签栏
const tabClick = (tab: any) => {
  router.push(tab.props.name);
};

// 移除标签栏
const tabRemove = (targetName: string) => {
  const tabs = editableTabs.value;
  let activeName = editableTabsValue.value;
  if (activeName === targetName) {
    tabs.forEach((tab, index) => {
      if (tab.path === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1];
        if (nextTab) {
          activeName = nextTab.path;
        }
      }
    });
  }
  router.push(activeName);
  editableTabsValue.value = activeName;
  editableTabs.value = tabs.filter((tab) => tab.path !== targetName);
};

// 标签初始化
onMounted(() => {
  addTab(currentRoute.name as string, currentRoute.path);
});
</script>

<style lang="scss">
@import "../assets/css/navUser";
</style>
