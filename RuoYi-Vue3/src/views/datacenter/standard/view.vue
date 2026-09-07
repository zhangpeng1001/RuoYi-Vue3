<template>
  <el-drawer title="行业标准详情" v-model="visible" direction="rtl" size="55%" append-to-body :before-close="handleClose" class="detail-drawer">
    <div v-loading="loading" class="drawer-content">
      <h4 class="section-header">基本信息</h4>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">标准名称：</label>
            <span class="info-value plaintext">
              {{ info.tradeStandardName }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">标准类型：</label>
            <span class="info-value">
              <dict-tag :options="data_standard_type" :value="info.type" />
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">提取类型：</label>
            <span class="info-value">
              <dict-tag :options="data_extract_type" :value="info.extractType" />
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">数据源ID：</label>
            <span class="info-value plaintext">
              {{ info.dataSourceId }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">文件ID：</label>
            <span class="info-value plaintext">
              {{ info.forldId }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">数据源IP：</label>
            <span class="info-value plaintext">
              {{ info.ip }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="24">
          <div class="info-item">
            <label class="info-label">描述：</label>
            <span class="info-value plaintext">
              {{ info.description }}
            </span>
          </div>
        </el-col>
      </el-row>

      <h4 class="section-header">审计信息</h4>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">创建人：</label>
            <span class="info-value plaintext">
              {{ info.creatorName }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">修改人：</label>
            <span class="info-value plaintext">
              {{ info.modifierName }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">创建时间：</label>
            <span class="info-value plaintext">
              {{ parseTime(info.createTime) }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">修改时间：</label>
            <span class="info-value plaintext">
              {{ parseTime(info.updateTime) }}
            </span>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-drawer>
</template>

<script setup name="StandardViewDrawer">
import { getStandard } from '@/api/datacenter/standard'

const { proxy } = getCurrentInstance()
const { data_standard_type, data_extract_type } = useDict("data_standard_type", "data_extract_type")

const visible = ref(false)
const loading = ref(false)
const info = reactive({})

/** 时间格式化（沿用 RuoYi 全局方法，缺失时降级） */
function parseTime(time) {
  if (proxy.parseTime) {
    return proxy.parseTime(time)
  }
  return time
}

const open = async (id) => {
  visible.value = true
  loading.value = true
  try {
    const res = await getStandard(id)
    Object.assign(info, res.data || {})
  } catch (error) {
    console.error('获取行业标准详情失败:', error)
  } finally {
    loading.value = false
  }
}

function handleClose() {
  visible.value = false
  Object.keys(info).forEach(key => delete info[key])
}

defineExpose({ open })
</script>

<style lang="scss" scoped>
.detail-drawer {
  .drawer-content {
    padding: 0 20px;
  }
  .section-header {
    margin: 12px 0 16px;
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
    line-height: 24px;
  }
  .info-label {
    flex-shrink: 0;
    min-width: 84px;
    color: #909399;
  }
  .info-value {
    flex: 1;
    color: #303133;
    word-break: break-all;
  }
  .info-value.plaintext {
    white-space: pre-wrap;
  }
}
</style>
