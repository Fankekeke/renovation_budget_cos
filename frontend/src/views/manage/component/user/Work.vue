<template>
  <div style="min-height: 85vh; padding: 20px;">
    <div class="main-container">
      <div class="header-section">
        <div class="title">家装预算分析</div>
        <div class="subtitle">上传房屋图片分析</div>
      </div>

      <a-card class="main-card">
        <a-row :gutter="24">
          <a-col :span="24" class="upload-section">
            <a-upload-dragger
              name="avatar"
              :multiple="true"
              accept=".png, .jpg"
              action="http://127.0.0.1:9527/cos/ai/recognitionImage"
              @change="aiHandleChange"
              class="upload-dragger"
            >
              <p class="ant-upload-drag-icon">
                <a-icon type="camera" theme="twoTone" twoToneColor="#5092d7" class="upload-icon" />
              </p>
              <p class="ant-upload-text">点击或拖拽图片到此区域上传</p>
              <p class="ant-upload-hint">支持PNG、JPG格式图片，用于识别待装修房间</p>
            </a-upload-dragger>

            <!-- AI识别结果展示区域 -->
            <div v-if="showAiResult && aiRecognitionResult" class="ai-result-section">
              <h3 class="result-title">识别结果</h3>
              <div class="result-content">
                <div class="markdown-content" v-html="renderMarkdown(aiRecognitionResult)"></div>
              </div>
            </div>
          </a-col>

          <a-col :span="24" class="form-section">
            <div v-if="nextFlag == 1" class="form-container">
              <a-form :form="form" layout="vertical">

                <a-row :gutter="20">
                  <a-col :span="6">
                    <a-form-item label='家装标题' v-bind="formItemLayout">
                      <a-input v-decorator="[
                                'orderName',
                                { rules: [{ required: true, message: '请输入家装标题!' }] }
                                ]"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="6">
                    <a-form-item label='总面积' v-bind="formItemLayout">
                      <a-input-number style="width: 100%" v-decorator="[
                              'weight',
                              { rules: [{ required: true, message: '请输入总面积!' }] }
                              ]" :min="0.1" :step="0.1"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="6">
                    <a-form-item label='卫生间数量' v-bind="formItemLayout">
                      <a-input-number style="width: 100%" v-decorator="[
                              'height',
                              { rules: [{ required: true, message: '请输入卫生间数量!' }] }
                              ]" :min="0.1" :step="0.1"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="6">
                    <a-form-item label='房间数量' v-bind="formItemLayout">
                      <a-input-number style="width: 100%" v-decorator="[
                              'width',
                              { rules: [{ required: true, message: '请输入房间数量!' }] }
                              ]" :min="0.1" :step="0.1"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="6">
                    <a-form-item label='订单类型' v-bind="formItemLayout">
                      <a-select v-decorator="[
        'orderType',
        { rules: [{ required: true, message: '请选择订单类型!' }] }
        ]">
                        <a-select-option value="1">全包</a-select-option>
                        <a-select-option value="2">半包</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>

                  <!-- 装修难度 -->
                  <a-col :span="6" v-if="form.getFieldValue('orderType') === '1'">
                    <a-form-item label='装修难度' v-bind="formItemLayout">
                      <a-select v-decorator="['fixDifficulty']">
                        <a-select-option value="1">轻度</a-select-option>
                        <a-select-option value="2">中度</a-select-option>
                        <a-select-option value="3">复杂</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>

                  <!-- 预估价格 -->
                  <a-col :span="6">
                    <a-form-item label='预估价格' v-bind="formItemLayout">
                      <a-input-number
                        style="width: 100%"
                        v-decorator="['forecastPrice']"
                        :min="0"
                        :step="0.01"
                        placeholder="元"/>
                    </a-form-item>
                  </a-col>

                  <!-- 订单方式 -->
                  <a-col :span="6">
                    <a-form-item label='订单方式' v-bind="formItemLayout">
                      <a-select v-decorator="[
        'orderMethod',
        { rules: [{ required: true, message: '请选择订单方式!' }] }
        ]">
                        <a-select-option value="1">零售单</a-select-option>
                        <a-select-option value="2">工程单</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>

                  <a-col :span="6">
                    <a-form-item label='装修地址' v-bind="formItemLayout">
                      <a-select style="width: 100%" v-decorator="[
                                'startAddressId',
                                { rules: [{ required: true, message: '请输入装修地址!' }] }
                                ]">
                        <a-select-option v-for="(item, index) in addressList" :value="item.id" :key="index">{{ item.address }}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24"></a-col>
                  <a-col :span="24">
                    <a-form-item label='订单备注' v-bind="formItemLayout">
                      <a-textarea :rows="3" v-decorator="[
                              'content',
                               { rules: [{ required: true, message: '请输入订单备注!' }] }
                              ]"/>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item label='装修图片' v-bind="formItemLayout">
                      <a-upload
                        name="avatar"
                        action="http://127.0.0.1:9527/file/fileUpload/"
                        list-type="picture-card"
                        :file-list="fileList"
                        @preview="handlePreview"
                        @change="picHandleChange"
                      >
                        <div v-if="fileList.length < 8">
                          <a-icon type="plus" />
                          <div class="ant-upload-text">
                            Upload
                          </div>
                        </div>
                      </a-upload>
                      <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                        <img alt="example" style="width: 100%" :src="previewImage" />
                      </a-modal>
                    </a-form-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-item label='装修视频' v-bind="formItemLayout">
                      <a-upload
                        name="avatar"
                        action="http://127.0.0.1:9527/file/fileUpload/"
                        list-type="picture-card"
                        :file-list="videoFileList"
                        @preview="handlePreviewVideo"
                        @change="picHandleChangeVideo"
                      >
                        <div v-if="videoFileList.length < 8">
                          <a-icon type="plus" />
                          <div class="ant-upload-text">
                            Upload
                          </div>
                        </div>
                      </a-upload>
                      <a-modal :visible="previewVisibleVideo" :footer="null" @cancel="handleCancelVideo">
                        <img alt="example" style="width: 100%" :src="previewImageVideo" />
                      </a-modal>
                    </a-form-item>
                  </a-col>
                </a-row>
                <div style="text-align: center; margin-top: 20px;">
                  <a-button
                    type="primary"
                    @click="fetch"        style="border-radius: 20px; background: linear-gradient(45deg, #5092d7, #4b7def); border: none; padding: 0 40px; height: 40px;"
                  >
                    发布
                  </a-button>
                </div>

              </a-form>
            </div>
          </a-col>
        </a-row>
      </a-card>
    </div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
import VehicleView from './VehicleView.vue'
import Map from './Map.vue'
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'Work',
  components: {Map, VehicleView},
  data () {
    return {
      nextFlag: 1,
      fileList: [],
      flawFileList: [],
      videoFileList: [],
      addressList: [],
      previewVisible: false,
      previewImage: '',
      previewVisibleFlaw: false,
      previewImageFlaw: '',
      previewVisibleVideo: false,
      previewImageVideo: '',
      formItemLayout,
      form: this.$form.createForm(this),
      orderMapView: {
        merchantInfo: null,
        visiable: false
      },
      key: '',
      roomList: [],
      roomTypeList: [],
      loading: false,
      vehicleView: {
        visiable: false,
        data: null
      },
      rentView: {
        visiable: false,
        data: null
      },
      startDate: null,
      endDate: null,
      orderInfo: null,
      discountId: null,
      aiRecognitionResult: null,
      showAiResult: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  mounted () {
    this.selectAddress()
    // 确保加载marked.js库
    if (typeof window.marked === 'undefined') {
      const script = document.createElement('script')
      script.src = 'https://cdn.jsdelivr.net/npm/marked/marked.min.js'
      document.head.appendChild(script)
    }
  },
  methods: {
    renderMarkdown (content) {
      if (!content) {
        return ''
      }
      if (window.marked) {
        try {
          return window.marked.parse(content)
        } catch (error) {
          console.error('Markdown解析错误:', error)
          return content
        }
      }
      return 'Marked.js 库未加载！'
    },
    aiHandleChange ({ file }) {
      if (file.response !== undefined) {
        console.log(file.response.code)
        console.log(file.status)
        if (file.response.code === 500 && file.status === 'done') {
          this.$message.error(file.response.msg)
        } else if (file.response.code === 0 && file.status === 'done') {
          // 存储识别结果并在界面展示
          this.aiRecognitionResult = file.response.data
          this.showAiResult = true
          this.$message.success('装修识别成功，请确认识别信息')

          // 自动填充表单字段（如果有返回的数据）
          if (file.response.msg) {
            const data = file.response.msg
            this.aiRecognitionResult = data
          }
        }
      }
    },
    orderPay () {
      let data = this.orderInfo
      data.discountId = this.discountId
      delete data.discountInfos
      this.$post('/cos/pay/alipay', data).then((r) => {
        // console.log(r.data.msg)
        // 添加之前先删除一下，如果单页面，页面不刷新，添加进去的内容会一直保留在页面中，二次调用form表单会出错
        const divForm = document.getElementsByTagName('div')
        if (divForm.length) {
          document.body.removeChild(divForm[0])
        }
        const div = document.createElement('div')
        div.innerHTML = r.data.msg // data就是接口返回的form 表单字符串
        // console.log(div.innerHTML)
        document.body.appendChild(div)
        document.forms[0].setAttribute('target', '_self') // 新开窗口跳转
        document.forms[0].submit()
      })
    },
    handleChange (value) {
      let afterOrderPrice = this.orderInfo.orderPrice
      if (!value) {
        this.orderInfo.afterOrderPrice = afterOrderPrice
      } else {
        this.orderInfo.discountInfos.forEach(e => {
          if (e.id === value) {
            if (e.type == 1) {
              this.orderInfo.afterOrderPrice = afterOrderPrice - e.discountPrice
            } else {
              this.orderInfo.afterOrderPrice = afterOrderPrice * e.rebate / 10
            }
          }
        })
      }
      // this.discountId = value
      // console.log(this.discountId)
    },
    selectAddress () {
      this.$get(`/cos/address-info/listByUserId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    handleCancelFlaw () {
      this.previewVisibleFlaw = false
    },
    async handlePreviewFlaw (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImageFlaw = file.url || file.preview
      this.previewVisibleFlaw = true
    },
    picHandleChangeFlaw ({ fileList }) {
      this.flawFileList = fileList
    },
    handleCancelVideo () {
      this.previewVisibleVideo = false
    },
    async handlePreviewVideo (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImageVideo = file.url || file.preview
      this.previewVisibleVideo = true
    },
    picHandleChangeVideo ({ fileList }) {
      this.videoFileList = fileList
    },
    handlevehicleViewClose () {
      this.vehicleView.visiable = false
    },
    handlevehicleViewSuccess () {
      this.vehicleView.visiable = false
      this.$message.success('添加订单成功')
    },
    orderSuccess () {
      this.rentView.visiable = false
      this.$message.success('添加订单成功')
      this.fetch()
    },
    collectDelete (id) {
      this.$delete(`/cos/collect-info/${id}`).then((r) => {
        this.$message.success('取消收藏成功')
        this.fetch()
      })
    },
    collectAdd (roomCode) {
      let data = { userId: this.currentUser.userId, roomCode }
      this.$post(`/cos/collect-info`, data).then((r) => {
        this.$message.success('成功')
        this.fetch()
      })
    },
    view (record) {
      // if (record.currentStatus === '0') {
      //   this.$message.warn('此商家不在营业时间内')
      //   return false
      // }
      this.orderMapView.merchantInfo = record
      this.orderMapView.visiable = true
    },
    handleorderMapViewClose () {
      this.orderMapView.visiable = false
    },
    getWorkStatusList () {
      this.$get(`/cos/order-info/selectMerchantList`, { key: this.key }).then((r) => {
        this.roomList = r.data.data
      })
    },
    fetch () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })

      let flawImages = []
      this.flawFileList.forEach(image => {
        flawImages.push(image.response)
      })

      let videoList = []
      this.videoFileList.forEach(image => {
        videoList.push(image.response)
      })

      this.form.validateFields((err, values) => {
        // 当订单方式为零售单(1)时，装修地址为必填项
        if (values.orderMethod === '1' && !values.startAddressId) {
          this.$message.warn('请选择装修地址')
          return false
        }
        values.flawImages = flawImages.length > 0 ? flawImages.join(',') : null
        values.video = videoList.length > 0 ? videoList.join(',') : null
        values.images = images.length > 0 ? images.join(',') : null
        values.userId = this.currentUser.userId
        if (!err) {
          this.loading = true
          this.$post('/cos/order-info', {
            ...values
          }).then((r) => {
            this.$message.success('添加订单成功')
            setTimeout(() => {
              this.$router.push('/user/order')
            }, 500)
          })
        }
      })
    }
  }
}
</script>

<style scoped>.main-container {
  max-width: 1200px;
  margin: 0 auto;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.title {
  font-size: 36px;
  font-weight: 600;
  color: #5d4037;
  font-family: 'STSong', SimHei;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 20px;
  font-weight: 500;
  color: #5092d7;
  font-family: 'STSong', SimHei;
}

.main-card {
  border-radius: 15px;
  box-shadow: 0 8px 16px rgba(121, 85, 72, 0.2);
  border: none;
}

.upload-section {
  margin-bottom: 20px;
}

.upload-dragger {
  border-radius: 10px;
  background-color: #fffcf5;
  border: 2px dashed #d9d9d9;
  transition: all 0.3s;
}

.upload-dragger:hover {
  border-color: #5092d7;
}

.upload-icon {
  font-size: 48px;
}

.ant-upload-text {
  font-size: 18px;
  color: #5d4037;
  font-weight: 500;
}

.ant-upload-hint {
  color: #5092d7;
}

.ai-result-section {
  margin-top: 20px;
  border-radius: 10px;
  background: #fffaf0;
  border: 1px solid #e8e0d2;
  padding: 20px;
}

.result-title {
  color: #5d4037;
  margin-bottom: 15px;
  text-align: center;
  font-size: 20px;
  font-weight: 500;
}

.result-content {
  background: white;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #e0d6cc;
  max-height: 500px;
  overflow-y: auto;
}

.form-section {
  margin-top: 15px;
}

.form-container {
  background: #fbfbfb;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(249, 249, 249, 0.8);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-section {
    margin-bottom: 20px;
  }

  .title {
    font-size: 28px;
  }

  .subtitle {
    font-size: 16px;
  }

  .main-card {
    padding: 15px;
  }
}
</style>
<style scoped>/* Markdown内容样式优化 */
.markdown-content {
  word-wrap: break-word;
  word-break: break-word;
  overflow-wrap: break-word;
  line-height: 1.6;
  color: #333;
  font-family: 'Noto Serif SC', SimHei;
}

.markdown-content h1,
.markdown-content h2,
.markdown-content h3 {
  color: #5d4037;
  margin-top: 1em;
  margin-bottom: 0.5em;
}

.markdown-content h1 {
  font-size: 1.8em;
  border-bottom: 2px solid #e0d6cc;
  padding-bottom: 0.3em;
}

.markdown-content h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #e0d6cc;
  padding-bottom: 0.3em;
}

.markdown-content p {
  margin: 0.8em 0;
}

.markdown-content ul,
.markdown-content ol {
  padding-left: 2em;
  margin: 0.8em 0;
}

.markdown-content li {
  margin: 0.3em 0;
}

.markdown-content code {
  background-color: #f5f0e6;
  padding: 0.2em 0.4em;
  border-radius: 3px;
  font-family: monospace;
  word-break: break-all;
}

.markdown-content pre {
  background-color: #f5f0e6;
  padding: 1em;
  border-radius: 5px;
  overflow-x: auto;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.markdown-content pre code {
  background: none;
  padding: 0;
  white-space: pre-wrap;
}

.markdown-content blockquote {
  border-left: 4px solid #d7ccc8;
  padding-left: 1em;
  margin: 1em 0;
  color: #666;
}

.markdown-content table {
  border-collapse: collapse;
  width: 100%;
  margin: 1em 0;
  table-layout: fixed;
}

.markdown-content th,
.markdown-content td {
  border: 1px solid #d7ccc8;
  padding: 0.5em;
  text-align: left;
  word-wrap: break-word;
}

.markdown-content th {
  background-color: #f0e6d2;
}
</style>
