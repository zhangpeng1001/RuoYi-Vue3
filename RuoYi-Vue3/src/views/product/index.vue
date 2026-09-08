<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="82px">
      <el-form-item label="操作时间" style="width: 388px">
        <el-date-picker
          v-model="dateRange"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item label="所属省" prop="provinceCode">
        <el-select
          v-model="queryParams.provinceCode"
          placeholder="请选择省"
          clearable
          filterable
          style="width: 180px"
          @change="handleProvinceChange"
        >
          <el-option
            v-for="p in provinceList"
            :key="p.regionCode"
            :label="p.regionName"
            :value="p.regionCode"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属市" prop="cityCode">
        <el-select
          v-model="queryParams.cityCode"
          placeholder="请选择市"
          clearable
          filterable
          style="width: 180px"
          :disabled="!queryParams.provinceCode"
        >
          <el-option
            v-for="c in cityList"
            :key="c.regionCode"
            :label="c.regionName"
            :value="c.regionCode"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="代理商" prop="agentName">
        <el-autocomplete
          v-model="queryParams.agentName"
          :fetch-suggestions="queryAgent"
          placeholder="请输入代理商关键字"
          clearable
          style="width: 200px"
          @select="handleAgentSelect"
        >
          <template #default="{ item }">
            <div>{{ item.agentName }}</div>
          </template>
        </el-autocomplete>
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          style="width: 200px"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['product:product:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['product:product:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表表格：前4数据列 + selection 冻结左，前4数据列黄底 -->
    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" fixed="left" />
      <el-table-column label="省" prop="provinceName" align="left" fixed="left" class-name="frozen-col" width="90" />
      <el-table-column label="市" prop="cityName" align="left" fixed="left" class-name="frozen-col" width="90" />
      <el-table-column label="代理商" prop="agentName" align="left" fixed="left" class-name="frozen-col" :show-overflow-tooltip="true" min-width="160" />
      <el-table-column label="商品名称" align="left" fixed="left" class-name="frozen-col" min-width="160">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">{{ scope.row.productName }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="金额" prop="amount" align="right" width="120" :formatter="formatAmount" />
      <el-table-column label="状态" align="center" width="90">
        <template #default="scope">
          <dict-tag :options="product_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="left" prop="createByName" width="110" />
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width" fixed="right">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleView(scope.row)" v-hasPermi="['product:product:query']">预览</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['product:product:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增商品弹窗 -->
    <edit-product ref="editRef" @success="getList" />
    <!-- 预览商品弹窗 -->
    <view-product ref="viewRef" />
  </div>
</template>

<script setup name="Product">
import { listProduct, delProduct } from "@/api/product/product"
import { listProvinces, listCities } from "@/api/product/region"
import { searchAgent } from "@/api/product/agent"
import EditProduct from "./edit"
import ViewProduct from "./view"

const { proxy } = getCurrentInstance()
const { product_status } = useDict("product_status")

const productList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const dateRange = ref([])
const provinceList = ref([])
const cityList = ref([])

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    provinceCode: undefined,
    cityCode: undefined,
    agentName: undefined,
    productName: undefined
  }
})
const { queryParams } = toRefs(data)

/** 金额格式化：保留2位小数 */
function formatAmount(row) {
  if (row.amount == null) return ""
  return Number(row.amount).toFixed(2)
}

/** 时间格式化（沿用 RuoYi 全局方法） */
function parseTime(time) {
  return proxy.parseTime(time)
}

/** 查询商品列表 */
function getList() {
  loading.value = true
  listProduct(proxy.addDateRange(queryParams.value, dateRange.value, "CreateTime")).then(res => {
    productList.value = res.rows
    total.value = res.total
    loading.value = false
  })
}

/** 加载省份 */
function loadProvinces() {
  listProvinces().then(res => {
    provinceList.value = res.data || []
  })
}

/** 省变化：联动加载市 */
function handleProvinceChange(provinceCode) {
  queryParams.value.cityCode = undefined
  cityList.value = []
  if (provinceCode) {
    listCities(provinceCode).then(res => {
      cityList.value = res.data || []
    })
  }
}

/** 代理商联想查询 */
function queryAgent(queryString, cb) {
  searchAgent(queryString).then(res => {
    cb(res.data || [])
  }).catch(() => cb([]))
}

/** 代理商选中（保留关键字用于查询） */
function handleAgentSelect(item) {
  queryParams.value.agentName = item.agentName
}

/** 搜索 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置 */
function resetQuery() {
  dateRange.value = []
  cityList.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  multiple.value = !selection.length
}

/** 新增 */
function handleAdd() {
  proxy.$refs["editRef"].open()
}

/** 预览 */
function handleView(row) {
  proxy.$refs["viewRef"].open(row.id)
}

/** 删除（软删除） */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除选中的商品数据项？').then(function () {
    return delProduct(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

// 初始化
loadProvinces()
getList()
</script>

<style lang="scss" scoped>
/* 冻结列黄底样式 */
:deep(.el-table .frozen-col) {
  background: #FFF9C4;
}
/* 冻结列 hover 时仍保持黄底 */
:deep(.el-table__body tr:hover > td.frozen-col) {
  background: #FFF59D !important;
}
</style>
