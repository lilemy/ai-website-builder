<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.svg" alt="Logo" />
            <div class="title">小新应用生成</div>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <a-button type="primary">登录</a-button>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'

const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const menuItems = ref([
  {
    key: '/',
    label: '首页',
    title: '首页',
  },
])

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  padding-inline: 20px;
  height: 48px;
  line-height: 48px;
  border-bottom: 1px solid #f0f0f0;
}

.header-row {
  max-width: 1200px;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
}

.logo-link {
  text-decoration: none;
}

.title {
  color: #000;
  font-size: 18px;
  font-weight: 600;
  margin-left: 12px;
}

.logo {
  height: 32px;
}

.header-menu {
  border-bottom: none !important;
  background: transparent;
}

.header-right {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
</style>
