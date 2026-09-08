<template>
  <el-dialog title="选择所属人" v-model="visible" width="720px" append-to-body :before-close="handleClose">
    <div class="user-select-wrapper">
      <!-- 关键字搜索 -->
      <el-form :inline="true" class="mb8">
        <el-form-item label="关键字">
          <el-input
            v-model="keyword"
            placeholder="账号/昵称/手机号"
            clearable
            style="width: 220px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 用户列表（多选，限制 1-3 人由父组件校验） -->
      <el-table
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
        :row-key="rowKey"
        max-height="360"
      >
        <el-table-column type="selection" width="50" align="center" :reserve-selection="true" />
        <el-table-column label="用户ID" prop="userId" width="80" align="center" />
        <el-table-column label="账号" prop="userName" align="center" />
        <el-table-column label="昵称" prop="nickName" align="center" />
        <el-table-column label="部门" prop="deptName" align="center" />
        <el-table-column label="手机号" prop="phonenumber" width="130" align="center" />
        <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" align="center" />
      </el-table>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="confirm">确 定</el-button>
        <el-button @click="handleClose">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup name="UserSelectDialog">
import { listProductUsers } from "@/api/product/user"

const { proxy } = getCurrentInstance()

const visible = ref(false)
const loading = ref(false)
const keyword = ref("")
const userList = ref([])
const selectedRows = ref([])

/** 行 key（保留选中状态） */
function rowKey(row) {
  return row.userId
}

/** 打开弹窗 */
function open() {
  visible.value = true
  selectedRows.value = []
  keyword.value = ""
  loadList()
}

/** 加载用户列表 */
function loadList() {
  loading.value = true
  listProductUsers(keyword.value).then(res => {
    userList.value = res.data || []
    // 回显已选中行
    nextTick(() => {
      // 父组件传入的已选 ids 通过 ref 调用时设置
    })
  }).finally(() => {
    loading.value = false
  })
}

/** 搜索 */
function handleSearch() {
  loadList()
}

/** 重置 */
function resetSearch() {
  keyword.value = ""
  loadList()
}

/** 选中变化 */
function handleSelectionChange(selection) {
  selectedRows.value = selection
}

/** 确认：最多3人校验 */
function confirm() {
  if (selectedRows.value.length < 1) {
    proxy.$modal.msgWarning("请至少选择 1 个所属人")
    return
  }
  if (selectedRows.value.length > 3) {
    proxy.$modal.msgWarning("最多只能选择 3 个所属人")
    return
  }
  // 向父组件抛出选中结果
  emit("confirm", selectedRows.value)
  visible.value = false
}

function handleClose() {
  visible.value = false
  selectedRows.value = []
}

const emit = defineEmits(["confirm"])
defineExpose({ open, clearSelection: () => proxy.$refs?.userTable?.clearSelection?.() })
</script>

<style lang="scss" scoped>
.user-select-wrapper {
  padding: 0 4px;
}
</style>
