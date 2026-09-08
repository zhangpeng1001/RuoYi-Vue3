<template>
  <el-dialog title="新增商品" v-model="visible" width="1100px" append-to-body :close-on-click-modal="false">
    <el-form ref="productRef" :model="form" :rules="rules" label-width="100px">
      <!-- 第一行：省 / 市 / 代理商 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="所属省" prop="provinceCode">
            <el-select v-model="form.provinceCode" placeholder="请选择省" filterable style="width: 100%" @change="handleProvinceChange">
              <el-option v-for="p in provinceList" :key="p.regionCode" :label="p.regionName" :value="p.regionCode" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="所属市" prop="cityCode">
            <el-select v-model="form.cityCode" placeholder="请选择市" filterable style="width: 100%" :disabled="!form.provinceCode">
              <el-option v-for="c in cityList" :key="c.regionCode" :label="c.regionName" :value="c.regionCode" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="代理商" prop="agentId">
            <el-autocomplete
              v-model="agentKeyword"
              :fetch-suggestions="queryAgent"
              placeholder="请输入代理商关键字"
              style="width: 100%"
              @select="handleAgentSelect"
            >
              <template #default="{ item }">
                <div>{{ item.agentName }}</div>
              </template>
            </el-autocomplete>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 第二行：商品名称 / 金额 -->
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="商品名称" prop="productName">
            <el-input v-model="form.productName" placeholder="请输入商品名称" maxlength="20" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="金额" prop="amount">
            <el-input v-model="form.amount" placeholder="请输入金额" @input="formatAmountInput">
              <template #append>元</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 商品类型（单选） -->
      <el-form-item label="商品类型" prop="productType">
        <el-radio-group v-model="form.productType">
          <el-radio v-for="d in product_type" :key="d.value" :label="d.value">{{ d.label }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <!-- 商品颜色（多选） -->
      <el-form-item label="商品颜色" prop="productColorArr">
        <el-checkbox-group v-model="form.productColorArr">
          <el-checkbox v-for="d in product_color" :key="d.value" :label="d.value">{{ d.label }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <!-- 第三行：发布时间 / 状态 / 所属人 -->
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="发布时间" prop="publishTime">
            <el-date-picker v-model="form.publishTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择发布时间" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option v-for="d in product_status" :key="d.value" :label="d.label" :value="Number(d.value)" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="所属人">
            <el-button type="primary" plain icon="Plus" @click="openUserSelect">选择用户</el-button>
            <div class="user-tags" v-if="selectedUsers.length">
              <el-tag v-for="(u, i) in selectedUsers" :key="u.userId" closable @close="removeUser(i)" class="mr5">{{ u.nickName }}({{ u.userName }})</el-tag>
            </div>
            <span class="tip-text">最少1人，最多3人</span>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 商品描述（富文本） -->
      <el-form-item label="商品描述" prop="description">
        <editor v-model="form.description" :min-height="200" />
      </el-form-item>

      <!-- 预览图 1/2/3 -->
      <el-form-item label="预览图">
        <el-row :gutter="20" style="width: 100%">
          <el-col :span="8" v-for="(img, i) in previewImages" :key="i">
            <div class="preview-slot">
              <el-upload
                action="#"
                :http-request="(opts) => uploadImage(opts, i)"
                :show-file-list="false"
                :before-upload="beforeImageUpload"
                accept="image/*"
              >
                <div class="upload-box" v-if="!img.url">
                  <el-icon><Plus /></el-icon>
                </div>
                <img v-else :src="resolveImg(img.url)" class="preview-img" @click.stop="showBigImage(img.url)" />
              </el-upload>
              <div class="preview-label">预览图{{ i + 1 }}</div>
              <el-button v-if="img.url" link type="danger" size="small" @click="img.url = ''">删除</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form-item>

      <!-- 联系方式动态子表 -->
      <el-form-item label="联系方式">
        <div style="width: 100%">
          <el-row :gutter="10" v-for="(c, idx) in form.contacts" :key="idx" class="contact-row">
            <el-col :span="6">
              <el-input v-model="c.name" placeholder="姓名（最多5汉字）" />
            </el-col>
            <el-col :span="7">
              <el-input v-model="c.phone" placeholder="手机号" />
            </el-col>
            <el-col :span="8">
              <el-input v-model="c.email" placeholder="邮箱（可空）" />
            </el-col>
            <el-col :span="3">
              <el-button link type="danger" icon="Delete" @click="removeContact(idx)">删除</el-button>
            </el-col>
          </el-row>
          <el-button type="primary" plain icon="Plus" size="small" @click="addContact">新增联系方式</el-button>
        </div>
      </el-form-item>

      <!-- 备注 -->
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" maxlength="255" show-word-limit />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>

    <!-- 所属人选择弹窗 -->
    <user-select-dialog ref="userSelectRef" @confirm="handleUserConfirm" />
    <!-- 图片大图预览 -->
    <el-dialog v-model="bigImgVisible" append-to-body>
      <img :src="bigImgUrl" style="width: 100%" />
    </el-dialog>
  </el-dialog>
</template>

<script setup name="EditProduct">
import request from '@/utils/request'
import { addProduct } from "@/api/product/product"
import { listProvinces, listCities } from "@/api/product/region"
import { searchAgent } from "@/api/product/agent"
import UserSelectDialog from "./components/UserSelectDialog.vue"

const { proxy } = getCurrentInstance()
const { product_status, product_type, product_color } = useDict("product_status", "product_type", "product_color")

const visible = ref(false)
const provinceList = ref([])
const cityList = ref([])
const agentKeyword = ref("")
const selectedUsers = ref([])
// 3 个预览图位
const previewImages = ref([{ url: "" }, { url: "" }, { url: "" }])
const bigImgVisible = ref(false)
const bigImgUrl = ref("")

/** 校验：金额仅数字+2位小数 */
function validateAmount(rule, value, callback) {
  if (value === "" || value == null) {
    callback(new Error("金额不能为空"))
    return
  }
  const reg = /^\d+(\.\d{1,2})?$/
  if (!reg.test(value)) {
    callback(new Error("金额只能为数字且保留2位小数"))
  } else {
    callback()
  }
}

const data = reactive({
  form: {
    provinceCode: undefined,
    provinceName: undefined,
    cityCode: undefined,
    cityName: undefined,
    agentId: undefined,
    agentName: undefined,
    productName: undefined,
    amount: undefined,
    productType: undefined,
    productColorArr: [],
    publishTime: undefined,
    status: undefined,
    description: "",
    remark: undefined,
    contacts: []
  },
  rules: {
    provinceCode: [{ required: true, message: "请选择所属省", trigger: "change" }],
    cityCode: [{ required: true, message: "请选择所属市", trigger: "change" }],
    agentId: [{ required: true, message: "请选择代理商", trigger: "change" }],
    productName: [
      { required: true, message: "商品名称不能为空", trigger: "blur" },
      { max: 20, message: "商品名称长度不能超过20个字符", trigger: "blur" }
    ],
    amount: [{ required: true, validator: validateAmount, trigger: "blur" }],
    publishTime: [{ required: true, message: "请选择发布时间", trigger: "change" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }],
    description: [{ required: true, message: "商品描述不能为空", trigger: "blur" }]
  }
})
const { form, rules } = toRefs(data)

/** 金额输入：过滤非数字 */
function formatAmountInput(val) {
  if (val == null) return
  // 仅允许数字和小数点
  let v = String(val).replace(/[^\d.]/g, "")
  // 仅保留第一个小数点
  const parts = v.split(".")
  if (parts.length > 2) {
    v = parts[0] + "." + parts.slice(1).join("")
  }
  // 小数位最多2位
  if (parts.length === 2 && parts[1].length > 2) {
    v = parts[0] + "." + parts[1].substring(0, 2)
  }
  form.value.amount = v
}

/** 打开弹窗 */
function open() {
  reset()
  visible.value = true
  loadProvinces()
}

/** 表单重置 */
function reset() {
  agentKeyword.value = ""
  selectedUsers.value = []
  previewImages.value = [{ url: "" }, { url: "" }, { url: "" }]
  cityList.value = []
  form.value = {
    provinceCode: undefined,
    provinceName: undefined,
    cityCode: undefined,
    cityName: undefined,
    agentId: undefined,
    agentName: undefined,
    productName: undefined,
    amount: undefined,
    productType: undefined,
    productColorArr: [],
    publishTime: undefined,
    status: undefined,
    description: "",
    remark: undefined,
    contacts: []
  }
  proxy.resetForm("productRef")
}

/** 加载省份 */
function loadProvinces() {
  if (provinceList.value.length) return
  listProvinces().then(res => {
    provinceList.value = res.data || []
  })
}

/** 省变化联动市，并同步省名 */
function handleProvinceChange(provinceCode) {
  form.value.cityCode = undefined
  form.value.cityName = undefined
  cityList.value = []
  if (provinceCode) {
    const p = provinceList.value.find(x => x.regionCode === provinceCode)
    form.value.provinceName = p ? p.regionName : undefined
    listCities(provinceCode).then(res => {
      cityList.value = res.data || []
    })
  }
}

/** 市变化同步市名（watch 城市选择） */
watch(() => form.value.cityCode, (code) => {
  if (code) {
    const c = cityList.value.find(x => x.regionCode === code)
    form.value.cityName = c ? c.regionName : undefined
  }
})

/** 代理商联想 */
function queryAgent(queryString, cb) {
  searchAgent(queryString).then(res => {
    cb(res.data || [])
  }).catch(() => cb([]))
}

/** 代理商选中：记录 agentId + agentName */
function handleAgentSelect(item) {
  agentKeyword.value = item.agentName
  form.value.agentId = item.id
  form.value.agentName = item.agentName
}

/** 打开所属人选择 */
function openUserSelect() {
  proxy.$refs["userSelectRef"].open()
}

/** 所属人选择确认 */
function handleUserConfirm(users) {
  // 合并去重，最多3人
  const map = new Map()
  ;[...selectedUsers.value, ...users].forEach(u => map.set(u.userId, u))
  selectedUsers.value = Array.from(map.values()).slice(0, 3)
}

/** 移除所属人 */
function removeUser(idx) {
  selectedUsers.value.splice(idx, 1)
}

/** 新增联系方式 */
function addContact() {
  form.value.contacts.push({ name: "", phone: "", email: "" })
}

/** 移除联系方式 */
function removeContact(idx) {
  form.value.contacts.splice(idx, 1)
}

/** 图片上传前校验 */
function beforeImageUpload(file) {
  const isImg = file.type.indexOf("image") > -1
  if (!isImg) {
    proxy.$modal.msgError("只能上传图片格式文件!")
    return false
  }
  const isLt = file.size / 1024 / 1024 < 5
  if (!isLt) {
    proxy.$modal.msgError("上传图片大小不能超过 5MB!")
    return false
  }
  return true
}

/** 自定义图片上传：调用通用上传接口，存储返回的 url */
function uploadImage(options, index) {
  proxy.$modal.loading("正在上传图片，请稍候...")
  const formData = new FormData()
  formData.append("file", options.file)
  request({
    url: '/common/upload',
    method: 'post',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  }).then(res => {
    proxy.$modal.closeLoading()
    // RuoYi 通用上传返回 { fileName, url, newFileName }
    previewImages.value[index].url = res.fileName || res.url
    proxy.$modal.msgSuccess("上传成功")
  }).catch(() => {
    proxy.$modal.closeLoading()
  })
}

/** 解析图片地址（相对路径补全 baseURL） */
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

/** 提交 */
function submitForm() {
  proxy.$refs["productRef"].validate(valid => {
    if (!valid) return
    // 校验联系方式（非必填，但填了需符合格式）
    const nameReg = /^[\u4e00-\u9fa5]{1,5}$/
    const phoneReg = /^1[3-9]\d{9}$/
    const emailReg = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z]{2,6}$/
    for (const c of form.value.contacts) {
      if (!c.name && !c.phone && !c.email) continue
      if (c.name && !nameReg.test(c.name)) {
        proxy.$modal.msgError("联系方式姓名只能为1-5个汉字")
        return
      }
      if (c.phone && !phoneReg.test(c.phone)) {
        proxy.$modal.msgError("联系方式手机号格式不正确")
        return
      }
      if (c.email && !emailReg.test(c.email)) {
        proxy.$modal.msgError("联系方式邮箱格式不正确")
        return
      }
    }
    // 组装提交数据
    const imageUrls = previewImages.value.map(i => i.url).filter(u => u)
    const submitData = {
      provinceCode: form.value.provinceCode,
      provinceName: form.value.provinceName,
      cityCode: form.value.cityCode,
      cityName: form.value.cityName,
      agentId: form.value.agentId,
      agentName: form.value.agentName,
      productName: form.value.productName,
      amount: form.value.amount,
      productType: form.value.productType,
      productColor: form.value.productColorArr.join(","),
      publishTime: form.value.publishTime,
      status: form.value.status,
      description: form.value.description,
      remark: form.value.remark,
      imageUrls: imageUrls,
      userIds: selectedUsers.value.map(u => u.userId),
      contacts: form.value.contacts.filter(c => c.name || c.phone || c.email)
    }
    addProduct(submitData).then(() => {
      proxy.$modal.msgSuccess("新增成功")
      visible.value = false
      emit("success")
    })
  })
}

/** 取消 */
function cancel() {
  visible.value = false
  reset()
}

const emit = defineEmits(["success"])
defineExpose({ open, setSelectedUsers: (users) => { selectedUsers.value = users || [] } })
</script>

<style lang="scss" scoped>
.user-tags {
  margin-top: 6px;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}
.mr5 {
  margin-right: 5px;
}
.tip-text {
  color: #909399;
  font-size: 12px;
  margin-left: 6px;
}
.contact-row {
  margin-bottom: 10px;
}
.preview-slot {
  text-align: center;
}
.upload-box {
  width: 100%;
  height: 140px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8c939d;
  font-size: 28px;
  cursor: pointer;
}
.upload-box:hover {
  border-color: #409eff;
  color: #409eff;
}
.preview-img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 6px;
  cursor: pointer;
}
.preview-label {
  margin-top: 6px;
  color: #606266;
  font-size: 13px;
}
</style>
