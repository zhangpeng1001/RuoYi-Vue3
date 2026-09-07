<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="tradeStandardName">
        <el-input
            v-model="queryParams.tradeStandardName"
            placeholder="请输入名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
            v-model="queryParams.description"
            placeholder="请输入描述"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['datacenter:standard:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['datacenter:standard:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['datacenter:standard:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['datacenter:standard:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="standardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="tradeStandardName" />
      <el-table-column label="标准类型" align="center" prop="type" />
      <el-table-column label="数据源ID" align="center" prop="dataSourceId" />
      <el-table-column label="文件ID" align="center" prop="forldId" />
      <el-table-column label="数据源ip" align="center" prop="ip" />
      <el-table-column label="描述" align="center" prop="description" />
      <el-table-column label="创建人编码" align="center" prop="creator" />
      <el-table-column label="修改人编码" align="center" prop="modifier" />
      <el-table-column label="创建人名称" align="center" prop="creatorName" />
      <el-table-column label="修改人名称" align="center" prop="modifierName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleViewData(scope.row)" v-hasPermi="['datacenter:standard:query']">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['datacenter:standard:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['datacenter:standard:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 行业标准管理详情抽屉 -->
    <standard-view-drawer ref="standardViewRef" />
    <!-- 添加或修改行业标准管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="standardRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="名称" prop="tradeStandardName">
              <el-input v-model="form.tradeStandardName" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据源ID" prop="dataSourceId">
              <el-input v-model="form.dataSourceId" placeholder="请输入数据源ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="文件ID" prop="forldId">
              <el-input v-model="form.forldId" placeholder="请输入文件ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据源ip" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入数据源ip" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入描述" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="逻辑删除标识 0 未删除 1 删除" prop="delFlag">
              <el-input v-model="form.delFlag" placeholder="请输入逻辑删除标识 0 未删除 1 删除" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="创建人编码" prop="creator">
              <el-input v-model="form.creator" placeholder="请输入创建人编码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="修改人编码" prop="modifier">
              <el-input v-model="form.modifier" placeholder="请输入修改人编码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="创建人名称" prop="creatorName">
              <el-input v-model="form.creatorName" placeholder="请输入创建人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="修改人名称" prop="modifierName">
              <el-input v-model="form.modifierName" placeholder="请输入修改人名称" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Standard">
import { listStandard, getStandard, delStandard, addStandard, updateStandard } from "@/api/datacenter/standard"
import StandardViewDrawer from "./view"

const { proxy } = getCurrentInstance()

const standardList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tradeStandardName: undefined,
    type: undefined,
    description: undefined,
  },
  rules: {
    tradeStandardName: [
      { required: true, message: "名称不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询行业标准管理列表 */
function getList() {
  loading.value = true
  listStandard(queryParams.value).then(response => {
    standardList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    tradeStandardName: null,
    type: null,
    dataSourceId: null,
    forldId: null,
    extractType: null,
    ip: null,
    description: null,
    delFlag: null,
    creator: null,
    modifier: null,
    createTime: null,
    updateTime: null,
    creatorName: null,
    modifierName: null
  }
  proxy.resetForm("standardRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加行业标准管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getStandard(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改行业标准管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["standardRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateStandard(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addStandard(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除行业标准管理编号为"' + _ids + '"的数据项？').then(function() {
    return delStandard(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 详情按钮操作 */
function handleViewData(row) {
  proxy.$refs["standardViewRef"].open(row.id)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('datacenter/standard/export', {
    ...queryParams.value
  }, `standard_${new Date().getTime()}.xlsx`)
}

getList()
</script>
