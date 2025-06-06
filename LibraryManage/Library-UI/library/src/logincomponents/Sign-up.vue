<!-- sign_up -->

<template>
  <div class="container a-container" id="a-container">
    <!--         添加用户表单 -->
    <form class="form" id="a-form" method="" action="">
      <h2 class="form_title title">注册账号</h2>
      <div class="form__icons"></div>
      <span class="form__span"></span>
      <el-form
        ref="addUserFormRef"
        :model="addUserForm"
        :rules="userRules"
        class="add-user-form"
        @keyup.enter="addUserButton(addUserFormRef)"
      >
        <el-form-item label="所属组" prop="groups">
          <el-select v-model="addUserForm.groups" placeholder="请选择所属组">
            <el-option
              v-for="item in UserType"
              :key="item.typeId"
              :label="item.label"
              :value="item.typeName"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input
            class="form__input"
            v-model="addUserForm.username"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            class="form__input"
            v-model="addUserForm.password"
            type="password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input
            class="form__input"
            v-model="addUserForm.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="addUserForm.gender" placeholder="请选择性别">
            <el-option
              v-for="item in GenderType"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeName"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="借书卡号" prop="idCard">
          <el-input
            class="form__input"
            v-model.number="addUserForm.idCard"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            class="form__input"
            v-model.number="addUserForm.phone"
            autocomplete="off"
            maxlength="11"
          ></el-input>
        </el-form-item>
        <el-form-item label="身份" prop="identity">
          <el-select v-model="addUserForm.identity" placeholder="请选择身份">
            <el-option
              v-for="item in IdentityType"
              :key="item.typeId"
              :label="item.typeName"
              :value="item.typeName"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="addUserButton(addUserFormRef)"
            ref="addUserButtonRef"
            :disabled="registerState"
            class="form__button button submit"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from "vue";
import useEmitter from "@/hooks/useloginEmitter.js";
import type { FormInstance, FormRules } from "element-plus";
import { ElMessageBox } from "element-plus";
import axios from "axios";

const emitter = useEmitter();
const isOpen = ref(false); // 初始状态

onMounted(() => {
  emitter.on("change", (newIsOpen: boolean) => {
    isOpen.value = newIsOpen; // 更新响应式状态

    // 切换样式
    const aContainer = document.querySelector("#a-container") as HTMLElement;
    if (aContainer) {
      aContainer.classList.toggle("is-txl", isOpen.value); // 根据状态添加或移除类
    }
  });
});

//注册逻辑
const addUserButtonRef = ref();
let registerState = ref(false);

const addUserFormRef = ref<FormInstance>();
const addUserForm = reactive({
  groups: "",
  username: "",
  password: "",
  name: "",
  gender: "",
  idCard: "",
  phone: "",
  identity: "",
});

const userRules = reactive<FormRules>({
  groups: [{ required: true, message: "请选择组名称", trigger: "change" }],
  username: [{ required: true, message: "请输入用户名称", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  name: [{ required: true, message: "请输入用户姓名", trigger: "blur" }],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  idCard: [{ required: true, message: "请输入借书卡号", trigger: "blur" }],
  phone: [{ required: true, message: "请输入手机号", trigger: "blur" }],
  identity: [{ required: true, message: "请选择身份", trigger: "change" }],
});
const addUserButton = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      axios
        .post("http://202.194.7.29:22223/user/save", addUserForm)
        .then((resp) => {
          const statusCode = resp.data.statusCode;

          // 添加失败
          if (statusCode == 0) {
            ElMessageBox.alert("注册失败，请重试", "信息", {
              confirmButtonText: "确认",
            });
          }
          // 添加成功
          if (statusCode == 1) {
            ElMessageBox.alert("注册成功,请返回登录界面", "信息", {
              confirmButtonText: "确认",
            });
          }
          // 用户名存在
          if (statusCode == 2) {
            ElMessageBox.alert("添加失败，此用户名已存在", "信息", {
              confirmButtonText: "确认",
            });
          }
          // 借书卡存在
          if (statusCode == 3) {
            ElMessageBox.alert("添加失败，此借书卡已存在", "信息", {
              confirmButtonText: "确认",
            });
          }
          // 手机号存在
          if (statusCode == 4) {
            ElMessageBox.alert("添加失败，此手机号已存在", "信息", {
              confirmButtonText: "确认",
            });
          }
        });
    } else {
      return false;
    }
  });
};

// 选项框属性
const UserType = ref([
  {
    typeId: 1,
    typeName: "admin",
    label: "管理员",
  },
  {
    typeId: 2,
    typeName: "user",
    label: "用户",
  },
]);
const GenderType = ref([
  {
    typeId: 1,
    typeName: "男",
  },
  {
    typeId: 2,
    typeName: "女",
  },
]);
const IdentityType = ref([
  {
    typeId: 1,
    typeName: "管理员",
  },
  {
    typeId: 2,
    typeName: "学生",
  },
  {
    typeId: 3,
    typeName: "教师",
  },
]);
</script>
<style lang="scss">
@import "../assets/css/user";
</style>
<style scoped>
@import "./login.css";
/* 将源码中的css样式单独存放,在各组件中导入就可以。*/
</style>

<style scoped>
.form__button {
  margin-top: 0; /* 移除按钮顶部的默认间距 */
}
.el-select {
  width: 100%; /* 确保下拉框宽度一致 */
}

.el-select .el-input {
  width: 100%; /* 确保输入框宽度一致 */
}

/* 可选：增加性别和身份的下拉框宽度 */
.gender-select,
.identity-select {
  min-width: 200px; /* 设置最小宽度，您可以根据需要调整 */
}
.container {
  display: flex;
  justify-content: center; /* 使按钮居中 */
  align-items: center;
  flex-direction: column; /* 垂直排列 */
}

.form__input {
  width: 100%; /* 使输入框宽度一致 */
}

.form__button {
  width: 100%; /* 使按钮宽度一致 */
}
</style>
