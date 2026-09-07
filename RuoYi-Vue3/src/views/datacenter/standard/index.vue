<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="标准名称" prop="tradeStandardName">
        <el-input
            v-model="queryParams.tradeStandardName"
            placeholder="请输入标准名称"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标准类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择标准类型" clearable style="width: 200px">
          <el-option
              v-for="dict in data_standard_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="提取类型" prop="extractType">
        <el-select v-model="queryParams.extractType" placeholder="请选择提取类型" clearable style="width: 200px">
          <el-option
              v-for="dict in data_extract_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input
            v-model="queryParams.description"
            placeholder="请输入描述"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据源IP" prop="ip">
        <el-input
            v-model="queryParams.ip"
            placeholder="请输入数据源IP"
            clearable
            style="width: 200px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 388px">
        <el-date-picker
            v-model="dateRange"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
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
            type="info"
            plain
            icon="Upload"
            @click="handleImport"
            v-hasPermi="['datacenter:standard:import']"
        >导入</el-button>
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
      <el-table-column label="ID" align="center" prop="id" width="80" />
      <el-table-column label="标准名称" align="center" prop="tradeStandardName" :show-overflow-tooltip="true" />
      <el-table-column label="标准类型" align="center" prop="type" width="100">
        <template #default="scope">
          <dict-tag :options="data_standard_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="提取类型" align="center" prop="extractType" width="120">
        <template #default="scope">
          <dict-tag :options="data_extract_type" :value="scope.row.extractType" />
        </template>
      </el-table-column>
      <el-table-column label="数据源ID" align="center" prop="dataSourceId" width="100" />
      <el-table-column label="文件ID" align="center" prop="forldId" width="100" />
      <el-table-column label="数据源IP" align="center" prop="ip" width="140" />
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="创建人" align="center" prop="creatorName" width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
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
    <!-- 行业标准管理导入对话框 -->
    <excel-import-dialog
        ref="importStandardRef"
        title="行业标准导入"
        action="/datacenter/standard/importData"
        template-action="/datacenter/standard/importTemplate"
        template-file-name="standard_template"
        update-support-label="是否更新已经存在的标准数据"
        @success="getList"
    />
    <!-- 添加或修改行业标准管理对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="standardRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="标准名称" prop="tradeStandardName">
              <el-input v-model="form.tradeStandardName" placeholder="请输入标准名称" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标准类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择标准类型" style="width: 100%">
                <el-option
                    v-for="dict in data_standard_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提取类型" prop="extractType">
              <el-select v-model="form.extractType" placeholder="请选择提取类型" style="width: 100%">
                <el-option
                    v-for="dict in data_extract_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源ID" prop="dataSourceId">
              <el-input v-model.number="form.dataSourceId" placeholder="请输入数据源ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文件ID" prop="forldId">
              <el-input v-model.number="form.forldId" placeholder="请输入文件ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据源IP" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入数据源IP，如 192.168.1.1" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入描述" :rows="3" maxlength="500" show-word-limit />
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
import ExcelImportDialog from "@/components/ExcelImportDialog"

const { proxy } = getCurrentInstance()
const { data_standard_type, data_extract_type } = useDict("data_standard_type", "data_extract_type")

const standardList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const dateRange = ref([])

/** IP 格式校验（IPv4） */
function validateIp(rule, value, callback) {
  if (!value) {
    callback()
    return
  }
  // IPv4 简单正则校验
  const ipReg = /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)){3}$/
  if (!ipReg.test(value)) {
    callback(new Error('数据源IP格式不正确'))
  } else {
    callback()
  }
}

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tradeStandardName: undefined,
    type: undefined,
    extractType: undefined,
    description: undefined,
    ip: undefined,
  },
  rules: {
    tradeStandardName: [
      { required: true, message: "标准名称不能为空", trigger: "blur" },
      { max: 100, message: "标准名称长度不能超过100个字符", trigger: "blur" }
    ],
    type: [
      { required: true, message: "标准类型不能为空", trigger: "change" }
    ],
    ip: [
      { validator: validateIp, trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询行业标准管理列表 */
function getList() {
  loading.value = true
  // 时间区间参数（RuoYi 约定放在 params 内）
  listStandard(proxy.addDateRange(queryParams.value, dateRange.value, "CreateTime")).then(response => {
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

/** 表单重置（仅业务字段，审计字段由后端自动填充） */
function reset() {
  form.value = {
    id: null,
    tradeStandardName: null,
    type: null,
    dataSourceId: null,
    forldId: null,
    extractType: null,
    ip: null,
    description: null
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
  dateRange.value = []
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
  title.value = "添加行业标准"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getStandard(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改行业标准"
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

/** 删除按钮操作（后端为软删除，前端交互不变） */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除行业标准编号为"' + _ids + '"的数据项？').then(function() {
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

/** 导入按钮操作 */
function handleImport() {
  proxy.$refs["importStandardRef"].open()
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('datacenter/standard/export', {
    ...queryParams.value
  }, `standard_${new Date().getTime()}.xlsx`)
}

getList()
</script>
