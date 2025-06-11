<!-- sign_in -->
<template>
  <div class="container b-container" id="b-container">
    <!-- 用户登录表单内容 -->
    <form class="form">
      <h2 class="form_title title">图书管理系统</h2>

      <!-- 登录表单 -->

      <div class="form__icons"></div>
      <span class="form__span"></span>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        @keyup.enter="login(loginFormRef)"
      >
        <el-form-item label="用户名">
          <el-input
            class="form__input"
            v-model="loginForm.username"
            placeholder="用户名"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码">
          <el-input
            type="password"
            v-model="loginForm.password"
            placeholder="密码"
            class="form__input"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证码" label-width="70px" prop="code">
          <div class="code-container">
            <el-input
              v-model="dynamicValidateForm.code"
              placeholder="不区分大小写 点击刷新"
              class="form__input"
              style="flex: 1"
            ></el-input>
            <img
              :src="captchaImage"
              alt="图片无法加载"
              style=""
              @click="changeVerify()"
              class="captcha-img"
            />
          </div>
        </el-form-item>

        <el-form-item>
          <div class="button-container">
            <el-button
              type="primary"
              @click="login(loginFormRef)"
              ref="loginButtonRef"
              :disabled="loginState"
              class="form__button button submit"
              >登录</el-button
            >
          </div>
        </el-form-item>
      </el-form>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from "vue";
import useEmitter from "@/hooks/useloginEmitter.js";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessageBox } from "element-plus";
import axios from "axios";
axios.defaults.withCredentials = true;
import jsCookie from "js-cookie";
import router from "@/router";

const captchaImage = ref("");
const dynamicValidateForm = ref({
  code: "", // 用于接收验证码输入
});
// 状态管理
const emitter = useEmitter();
const isOpen = ref(false); // 初始状态

// 监听事件
onMounted(() => {
  emitter.on("change", (newIsOpen: boolean) => {
    isOpen.value = newIsOpen; // 更新响应式状态
    const bContainer = document.querySelector("#b-container") as HTMLElement;
    if (bContainer) {
      bContainer.classList.toggle("is-txr", isOpen.value);
    }
  });
});

// 登录表单
const loginButtonRef = ref();
let loginState = ref(false);

// 登陆表单
const loginFormRef = ref<FormInstance>();
const loginForm = reactive({
  username: "Admin",
  password: "123456",
});

// 表单校验
const loginRules = reactive<FormRules>({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
});

// 登录操作
const login = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate(async (valid) => {
    if (valid && !loginState.value) {
      loginState.value = true;

      try {
        // 验证验证码
        const captchaValid = await checkCaptcha(dynamicValidateForm.value.code);
        if (!captchaValid) {
          await ElMessageBox.alert("验证码错误", "信息", {
            confirmButtonText: "重试",
          });
          loginState.value = false;
          return;
        }

        // 发送客户端请求，用户信息作为请求体
        const resp = await axios.post(
          "http://localhost:8080/login/admin",
          loginForm,
          {
            params: { code: dynamicValidateForm.value.code }, // 如果需要将验证码作为查询参数发送
          }
        );

        const statusCode = resp.data.statusCode;

        switch (statusCode) {
          case 0:
            await ElMessageBox.alert("登录失败，账号或密码错误", "信息", {
              confirmButtonText: "重试",
            });
            break;
          case 1:
            await ElMessageBox.alert("登录成功", "信息", {
              confirmButtonText: "确认",
              callback: () => {
                jsCookie.set("username", resp.data.username, {
                  expires: 1,
                  path: "/",
                });
                router.push("/home");
              },
            });
            break;
          case 2:
            await ElMessageBox.alert("登录失败，账号已被冻结", "信息", {
              confirmButtonText: "确定",
            });
            break;
        }
      } catch (error) {
        console.error("登录请求出错:", error);
      } finally {
        loginState.value = false;
      }
    }
  });
};
const checkCaptcha = async (captcha: string) => {
  try {
    const resp = await axios.post("http://localhost:8080/code/verify/check", {
      code: captcha,
    });
    return resp.data; // 返回布尔值
  } catch (error) {
    ElMessageBox.alert("验证码验证请求失败，请稍后再试", "错误", {
      confirmButtonText: "确定",
    });
    return false;
  }
};
const changeVerify = () => {
  axios
    .get("http://localhost:8080/code/verify", {
      responseType: "arraybuffer",
    })
    .then((response) => {
      const imageBase64 = btoa(
        new Uint8Array(response.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          ""
        )
      );
      captchaImage.value = `data:image/jpeg;base64,${imageBase64}`;
    })
    .catch((error) => {
      console.error("Error fetching CAPTCHA:", error);
      ElMessageBox.alert("无法加载验证码，请重试", "错误", {
        confirmButtonText: "确定",
      });
    });
};
// 初始化页面时获取验证码
changeVerify();
</script>
<style lang="scss">
@import "../assets/css/user";
</style>
<style scoped>
@import "./login.css";
/* 将源码中的css样式单独存放,在各组件中导入就可以。*/
.el-select {
  width: 100%; /* 确保下拉框宽度一致 */
}

.el-select .el-input {
  width: 100%; /* 确保输入框宽度一致 */
}
.code-container {
  justify-content: center; /* 使按钮居中 */
  align-items: center;
  display: flex;
  align-items: center; /* 垂直对齐 */
}

.captcha-img {
  margin-left: 15px; /* 根据需要调整间距 */
  width: 100px; /* 设置你想要的宽度 */
  height: auto; /* 保持图片的比例 */
  border: 2px solid hsl(210, 100%, 63%); /* 添加红色边框，边框宽度为2px */
  cursor: pointer; /* 确保图片可以被点击 */
}
.button-container {
  display: flex;
  justify-content: flex-end; /* 水平居中 */
  height: 100%; /* 如果希望容器高度充满其父元素，可以添加此行（根据需要调整） */
}
.form__button.submit {
  margin-left: 100px; /* 根据需要调整数值，以在按钮和容器右侧之间留出空间 */
}
</style>
