<template>
  <el-drawer title="行业标准管理详情" v-model="visible" direction="rtl" size="60%" append-to-body :before-close="handleClose" class="detail-drawer">
    <div v-loading="loading" class="drawer-content">
      <h4 class="section-header">基本信息</h4>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">名称：</label>
            <span class="info-value plaintext">
              {{ info.tradeStandardName }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">标准类型 1行业标准、2自定义标准：</label>
            <span class="info-value plaintext">
              {{ info.type }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">数据源ID：</label>
            <span class="info-value plaintext">
              {{ info.dataSourceId }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">文件的ID：文件夹的ID对应package_storage的id：</label>
            <span class="info-value plaintext">
              {{ info.forldId }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">提取标准类型 1:file 2:建模数据源：</label>
            <span class="info-value plaintext">
              {{ info.extractType }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">数据源ip：</label>
            <span class="info-value plaintext">
              {{ info.ip }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">描述：</label>
            <span class="info-value plaintext">
              {{ info.description }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">创建人编码。创建人指运营系统管理人员：</label>
            <span class="info-value plaintext">
              {{ info.creator }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">修改人编码。修改人指运营系统人员：</label>
            <span class="info-value plaintext">
              {{ info.modifier }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">创建人名称：</label>
            <span class="info-value plaintext">
              {{ info.creatorName }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">修改人名称：</label>
            <span class="info-value plaintext">
              {{ info.modifierName }}
            </span>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-drawer>
</template>

<script setup name="StandardViewDrawer">
import { getStandard } from '@/api/datacenter/standard'


const visible = ref(false)
const loading = ref(false)
const info = reactive({})

const open = async (id) => {
  visible.value = true
  loading.value = true
  try {
    const res = await getStandard(id)
    Object.assign(info, res.data || {})
  } catch (error) {
    console.error('获取行业标准管理信息失败:', error)
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
