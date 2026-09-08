<template>
  <el-dialog title="商品预览" v-model="visible" width="1100px" append-to-body :before-close="handleClose">
    <div v-loading="loading" class="view-wrapper">
      <!-- 基本信息 -->
      <h4 class="section-header">基本信息</h4>
      <el-row :gutter="20" class="mb8">
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">所属省：</label>
            <span class="info-value">{{ info.provinceName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">所属市：</label>
            <span class="info-value">{{ info.cityName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">代理商：</label>
            <span class="info-value">{{ info.agentName }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">商品名称：</label>
            <span class="info-value">{{ info.productName }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">金额：</label>
            <span class="info-value">{{ info.amount != null ? Number(info.amount).toFixed(2) : '' }} 元</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">发布时间：</label>
            <span class="info-value">{{ parseTime(info.publishTime) }}</span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">商品类型：</label>
            <span class="info-value">{{ info.productType || '—' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">商品颜色：</label>
            <span class="info-value">{{ info.productColor || '—' }}</span>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-item">
            <label class="info-label">状态：</label>
            <span class="info-value">
              <dict-tag :options="product_status" :value="info.status" />
            </span>
          </div>
        </el-col>
      </el-row>

      <!-- 所属人 -->
      <h4 class="section-header">所属人</h4>
      <div class="info-item" v-if="info.userList && info.userList.length">
        <el-tag v-for="u in info.userList" :key="u.userId" class="mr5" type="info">
          {{ u.nickName }}({{ u.userName }})<template v-if="u.deptName"> - {{ u.deptName }}</template>
        </el-tag>
      </div>
      <div class="empty-text" v-else>无</div>

      <!-- 商品描述 -->
      <h4 class="section-header">商品描述</h4>
      <div class="description-box" v-html="info.description"></div>

      <!-- 预览图 -->
      <h4 class="section-header">预览图</h4>
      <el-row :gutter="20" v-if="info.imageList && info.imageList.length">
        <el-col :span="8" v-for="(img, i) in info.imageList" :key="i">
          <img :src="resolveImg(img.imageUrl)" class="preview-img" @click="showBigImage(img.imageUrl)" />
        </el-col>
      </el-row>
      <div class="empty-text" v-else>无</div>

      <!-- 联系方式 -->
      <h4 class="section-header">联系方式</h4>
      <el-table :data="info.contactList" border v-if="info.contactList && info.contactList.length">
        <el-table-column label="姓名" prop="name" align="center" />
        <el-table-column label="电话" prop="phone" align="center" />
        <el-table-column label="邮箱" prop="email" align="center" :show-overflow-tooltip="true" />
      </el-table>
      <div class="empty-text" v-else>无</div>

      <!-- 备注 -->
      <h4 class="section-header">备注</h4>
      <div class="info-item">
        <span class="info-value">{{ info.remark || '—' }}</span>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关 闭</el-button>
      </div>
    </template>

    <!-- 图片大图预览 -->
    <el-dialog v-model="bigImgVisible" append-to-body>
      <img :src="bigImgUrl" style="width: 100%" />
    </el-dialog>
  </el-dialog>
</template>

<script setup name="ViewProduct">
import { getProduct } from "@/api/product/product"

const { proxy } = getCurrentInstance()
const { product_status } = useDict("product_status")

const visible = ref(false)
const loading = ref(false)
const info = reactive({})
const bigImgVisible = ref(false)
const bigImgUrl = ref("")

/** 打开预览 */
function open(id) {
  visible.value = true
  loading.value = true
  // 清空旧数据
  Object.keys(info).forEach(k => delete info[k])
  getProduct(id).then(res => {
    Object.assign(info, res.data || {})
  }).catch(() => {
    proxy.$modal.msgError("获取商品详情失败")
  }).finally(() => {
    loading.value = false
  })
}

/** 时间格式化 */
function parseTime(time) {
  return proxy.parseTime(time)
}

/** 解析图片地址 */
function resolveImg(url) {
  if (!url) return ""
  if (url.startsWith("http") || url.startsWith("data:")) return url
  return import.meta.env.VITE_APP_BASE_API + url
}

/** 大图预览 */
function showBigImage(url) {
  bigImgUrl.value = resolveImg(url)
  bigImgVisible.value = true
}

/** 关闭 */
function handleClose() {
  visible.value = false
  Object.keys(info).forEach(k => delete info[k])
}

defineExpose({ open })
</script>

<style lang="scss" scoped>
.view-wrapper {
  padding: 0 12px;
}
.section-header {
  margin: 16px 0 12px;
  padding-bottom: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  border-bottom: 1px solid #ebeef5;
}
.info-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 8px;
  font-size: 13px;
  line-height: 28px;
}
.info-label {
  flex-shrink: 0;
  min-width: 80px;
  color: #909399;
}
.info-value {
  flex: 1;
  color: #303133;
  word-break: break-all;
}
.description-box {
  padding: 8px 12px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  min-height: 60px;
  color: #303133;
  line-height: 1.6;
  :deep(p) {
    margin: 4px 0;
  }
  :deep(img) {
    max-width: 100%;
  }
}
.preview-img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
}
.mr5 {
  margin-right: 5px;
  margin-bottom: 5px;
}
.empty-text {
  color: #909399;
  font-size: 13px;
  padding: 4px 0;
}
</style>
