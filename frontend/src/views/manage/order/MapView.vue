<template>
  <a-drawer
    placement="right"
    width="100%"
    :closable="false"
    :visible="orderShow"
    destroyOnClose
    wrapClassName="aa"
    :getContainer="false"
  >
    <div style="width: 100%">
      <a-icon type="arrow-left" style="position: absolute;z-index: 999;color: red;font-size: 20px;margin: 15px"
              @click="home"/>
      <a-row style="height:100vh;font-family: SimHei">
        <a-col :span="9" style="height: 100%;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);color:#fff">
          <div>
            <div class="scenicInfo" style="height: 100vh; overflow-y: auto;overflow-x: hidden">
              <div v-if="orderInfo != null">
                <a-carousel autoplay style="height: 300px;" v-if="orderInfo.images !== null && orderInfo.images !== ''">
                  <div style="width: 100%;height: 300px" v-for="(item, index) in orderInfo.images.split(',')"
                       :key="index">
                    <img :src="'http://127.0.0.1:9527/imagesWeb/' + item" style="width: 100%;height: 100%">
                  </div>
                </a-carousel>
                <a-card :title="orderInfo.orderName" :bordered="false">
                  <div style="text-align: center;margin-top: 20px;font-family: SimHei;">
                    <a-icon type="smile" theme="twoTone" style="font-size: 80px"/>
                    <p v-if="orderInfo.status == 0" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">
                      等待报价中！</p>
                    <p v-if="orderInfo.status == 1" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">
                      已确认，等待订单支付！</p>
                    <p v-if="orderInfo.status == 2" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">
                      装修中！</p>
                    <p v-if="orderInfo.status == 3" style="font-size: 20px;margin-top: 15px;margin-bottom: 15px">
                      订单已完成！</p>
                  </div>
                  <div style="padding-left: 24px;padding-right: 24px;margin-bottom: 50px;margin-top: 50px"
                       v-if="orderInfo != null">
                    <a-steps :current="orderInfo.status" progress-dot size="small">
                      <a-step title="等待报价"/>
                      <a-step title="未支付"/>
                      <a-step title="装修中"/>
                      <a-step title="已完成"/>
                    </a-steps>
                  </div>
                  <div v-if="orderData.status == 2 || orderData.status == 3">
                    <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">地址</h3>
                    <div id="areas" style="width: 100%;height: 350px;box-shadow: 3px 3px 3px rgba(0, 0, 0, .2);background:#ec9e3c;color:#fff"></div>
                  </div>
                </a-card>
              </div>
              <div style="font-size: 12px;font-family: SimHei;color: #404040;margin-top: 15px">
                <div style="font-size: 12px;font-family: SimHei" v-if="userInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">用户信息</span>
                    </a-col>
                    <a-col :span="8"><b>用户编号：</b>
                      {{ userInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>用户姓名：</b>
                      {{ userInfo.name ? userInfo.name : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>邮箱地址：</b>
                      {{ userInfo.mail ? userInfo.mail : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>联系电话：</b>
                      {{ userInfo.phone }}
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="orderInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单信息</span>
                    </a-col>
                    <a-col :span="8"><b>订单编号：</b>
                      {{ orderInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>订单名称：</b>
                      {{ orderInfo.orderName ? orderInfo.orderName : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>预估价格：</b>
                      {{ orderInfo.forecastPrice ? orderInfo.forecastPrice + '元' : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>总价格：</b>
                      {{ orderInfo.orderPrice ? orderInfo.orderPrice + '元' : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>创建时间：</b>
                      {{ orderInfo.createDate }}
                    </a-col>
                    <a-col :span="8"><b>付款时间：</b>
                      {{ orderInfo.payDate }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>折扣后价格：</b>
                      {{ orderInfo.afterOrderPrice ? orderInfo.afterOrderPrice : '- -' }}元
                    </a-col>
                    <a-col :span="8"><b>装修难度：</b>
                      <span v-if="orderData.fixDifficulty == 1">轻度</span>
                      <span v-if="orderData.fixDifficulty == 2">中度</span>
                      <span v-if="orderData.fixDifficulty == 3">复杂</span>
                    </a-col>
                    <a-col :span="8"><b>订单类型：</b>
                      <span v-if="orderData.orderType == 1">全包</span>
                      <span v-if="orderData.orderType == 2">半包</span>
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>订单方式：</b>
                      <span v-if="orderData.orderMethod == 1">零售单</span>
                      <span v-if="orderData.orderMethod == 2">工程单</span>
                    </a-col>
                    <a-col :span="8"><b>总面积：</b>
                      {{ orderInfo.weight ? orderInfo.weight : '- -' }}㎡
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="8"><b>卫生间数量：</b>
                      {{ orderInfo.height ? orderInfo.height : '- -' }}个
                    </a-col>
                    <a-col :span="8"><b>房间数量：</b>
                      {{ orderInfo.width ? orderInfo.width : '- -' }}个
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span
                    style="font-size: 15px;font-weight: 650;color: #000c17">装修描述</span></a-col>
                  <a-col :span="24">
                    {{ orderData.content ? orderData.content : '- -' }}
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col style="margin-bottom: 15px"><span
                    style="font-size: 15px;font-weight: 650;color: #000c17">装修图册</span></a-col>
                  <a-col :span="24">
                    <a-upload
                      name="avatar"
                      action="http://127.0.0.1:9527/file/fileUpload/"
                      list-type="picture-card"
                      :file-list="fileList"
                      @preview="handlePreview"
                      @change="picHandleChange"
                    >
                    </a-upload>
                    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
                      <img alt="example" style="width: 100%" :src="previewImage"/>
                    </a-modal>
                  </a-col>
                </a-row>
                <br/>
                <a-row style="padding-left: 24px;padding-right: 24px;" v-if="orderData && orderData.video != null">
                  <a-col style="margin-bottom: 15px"><span
                    style="font-size: 15px;font-weight: 650;color: #000c17">视频</span></a-col>
                  <a-col :span="24">
                    <video width="100%" height="100%" controls>
                      <source :src="'http://127.0.0.1:9527/imagesWeb/' + orderData.video" type="video/mp4">
                    </video>
                  </a-col>
                </a-row>
                <br/>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="startAddressInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">装修地址</span>
                    </a-col>
                    <a-col :span="24"><b>详细地址：</b>
                      {{ startAddressInfo.address }}
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="8"><b>联系人：</b>
                      {{ startAddressInfo.contactPerson ? startAddressInfo.contactPerson : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>联系方式：</b>
                      {{ startAddressInfo.contactMethod ? startAddressInfo.contactMethod : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="endAddressInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">详细地址</span>
                    </a-col>
                    <a-col :span="24"><b>详细地址：</b>
                      {{ endAddressInfo.address }}
                    </a-col>
                    <br/>
                    <br/>
                    <a-col :span="8"><b>联系人：</b>
                      {{ endAddressInfo.contactPerson ? endAddressInfo.contactPerson : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>联系方式：</b>
                      {{ endAddressInfo.contactMethod ? endAddressInfo.contactMethod : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="discountInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">优惠信息</span>
                    </a-col>
                    <a-col :span="8"><b>优惠券编号：</b>
                      {{ discountInfo.code }}
                    </a-col>
                    <a-col :span="8"><b>优惠券名称：</b>
                      {{ discountInfo.couponName ? discountInfo.couponName : '- -' }}
                    </a-col>
                    <a-col :span="8"><b>优惠券类型：</b>
                      <span v-if="discountInfo.type == 1">满减</span>
                      <span v-if="discountInfo.type == 2">折扣</span>
                    </a-col>
                    <a-col :span="8"><b>发放时间：</b>
                      {{ discountInfo.createDate }}
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 1">
                    <a-col :span="8"><b>满减金额：</b>
                      {{ discountInfo.discountPrice }} 元
                    </a-col>
                    <a-col :span="8"><b>门槛金额：</b>
                      {{ discountInfo.threshold }} 元
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;" v-if="discountInfo.type == 2">
                    <a-col :span="8"><b>折扣：</b>
                      {{ discountInfo.rebate }} 折
                    </a-col>
                  </a-row>
                  <br/>
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col :span="24"><b>备注：</b>
                      {{ discountInfo.content }}
                    </a-col>
                  </a-row>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="staffInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">设计师信息</span>
                    </a-col>
                    <a-col :span="8"><b>设计师姓名：</b>
                      {{ staffInfo.name }}
                    </a-col>
                    <a-col :span="8"><b>性别：</b>
                      <span v-if="staffInfo.sex == '1'">男</span>
                      <span v-if="staffInfo.sex == '2'">女</span>
                    </a-col>
                    <a-col :span="8"><b>设计师工号：</b>
                      {{ staffInfo.code }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
                <br/>
                <div style="font-size: 13px;font-family: SimHei" v-if="evaluateInfo !== null">
                  <a-row style="padding-left: 24px;padding-right: 24px;">
                    <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span>
                    </a-col>
                    <a-col :span="8"><b>评价分数：</b>
                      <a-rate :default-value="evaluateInfo.score" disabled/>
                    </a-col>
                    <a-col :span="8"><b>评价内容：</b>
                      <a-tooltip>
                        <template slot="title">
                          {{ evaluateInfo.content }}
                        </template>
                        {{ evaluateInfo.content.slice(0, 8) }} ...
                      </a-tooltip>
                    </a-col>
                    <a-col :span="8"><b>评价时间：</b>
                      {{ evaluateInfo.createDate ? evaluateInfo.createDate : '- -' }}
                    </a-col>
                  </a-row>
                  <br/>
                </div>
              </div>
            </div>
          </div>
        </a-col>
        <a-col :span="15" style="height: 100vh;background: #f8f8f8;overflow-y: auto;overflow-x: hidden">
          <a-row :gutter="15" style="padding: 20px" v-if="orderData != null">
            <a-col :span="24" style="margin-top: 15px;background: #fff;padding: 20px" v-if="orderData.status == 0">
              <div v-if="quotationList && quotationList.length > 0">
                <h3
                  style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                  报价信息</h3>
                <a-list :data-source="quotationList" item-layout="vertical">
                  <a-list-item slot="renderItem" slot-scope="item"
                               style="padding: 20px 0; border-bottom: 1px dashed #e8e8e8;">
                    <a-card style="width: 100%; border-radius: 2px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                      <a-row :gutter="16">
                        <a-col :span="24">
                          <div style="display: flex; align-items: center; margin-bottom: 15px;">
                            <a-avatar
                              :src="'http://127.0.0.1:9527/imagesWeb/' + (item.staffImages ? item.staffImages.split(',')[0] : '')"
                              size="large" style="margin-right: 12px;"
                            />
                            <div>
                              <div style="font-size: 16px; font-weight: 600; color: #000c17;">{{ item.staffName }}</div>
                              <div style="font-size: 12px; color: #8c8c8c;">
                                <a-rate :default-value="item.staffScore" disabled style="font-size: 12px;"/>
                              </div>
                            </div>
                            <div style="margin-left: auto; text-align: right;">
                              <div style="font-size: 20px; font-weight: 700; color: #ff4d4f;">¥{{ item.price }}</div>
                              <div style="font-size: 12px; color: #8c8c8c; margin-top: 4px;">报价时间：{{
                                  item.createDate
                                }}
                              </div>
                            </div>
                          </div>
                        </a-col>
                      </a-row>

                      <a-row :gutter="16" style="margin-top: 15px;">
                        <a-col :span="8">
                          <div style="display: flex; align-items: center;">
                            <a-icon type="phone" style="color: #1890ff; margin-right: 8px;"/>
                            <span>{{ item.staffPhone }}</span>
                          </div>
                        </a-col>
                        <a-col :span="8">
                          <div style="display: flex; align-items: center;">
                            <a-icon type="clock-circle" style="color: #52c41a; margin-right: 8px;"/>
                            <span>预计工时：{{ item.workHour }} 小时</span>
                          </div>
                        </a-col>
                        <a-col :span="8">
                          <div style="display: flex; justify-content: flex-end;">
                            <a-tag color="blue">设计师报价</a-tag>
                          </div>
                        </a-col>
                      </a-row>

                      <a-row style="margin-top: 15px;">
                        <a-col :span="24">
                          <div style="font-size: 14px; color: #595959;">
                            <div style="font-weight: 600; margin-bottom: 8px;">报价说明：</div>
                            <div style="background: #fafafa; padding: 12px;border-left: 3px solid #1890ff;">
                              {{ item.content }}
                            </div>
                          </div>
                        </a-col>
                      </a-row>

                    </a-card>
                  </a-list-item>
                </a-list>
              </div>
              <div v-else>
                <a-empty description="暂无报价信息" style="padding: 40px 0;">
                  <div slot="image" style="fontSize: 48px; color: #bfbfbf;"></div>
                </a-empty>
              </div>
            </a-col>
            <a-col :span="24" style="background: #fff;padding: 20px" v-if="orderData.status >= 2">
              <div v-if="quotationList && quotationList.length > 0">
                <h3
                  style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                  报价信息</h3>
                <a-list :data-source="quotationList" item-layout="vertical">
                  <a-list-item slot="renderItem" slot-scope="item"
                               style="padding: 20px 0; border-bottom: 1px dashed #e8e8e8;"
                               v-if="item.id == orderInfo.quotationId">
                    <a-card style="width: 100%; border-radius: 2px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                      <a-row :gutter="16">
                        <a-col :span="24">
                          <div style="display: flex; align-items: center; margin-bottom: 15px;">
                            <a-avatar
                              :src="'http://127.0.0.1:9527/imagesWeb/' + (item.staffImages ? item.staffImages.split(',')[0] : '')"
                              size="large" style="margin-right: 12px;"
                            />
                            <div>
                              <div style="font-size: 16px; font-weight: 600; color: #000c17;">{{ item.staffName }}</div>
                              <div style="font-size: 12px; color: #8c8c8c;">
                                <a-rate :default-value="item.staffScore" disabled style="font-size: 12px;"/>
                              </div>
                            </div>
                            <div style="margin-left: auto; text-align: right;">
                              <div style="font-size: 20px; font-weight: 700; color: #ff4d4f;">¥{{ item.price }}</div>
                              <div style="font-size: 12px; color: #8c8c8c; margin-top: 4px;">报价时间：{{
                                  item.createDate
                                }}
                              </div>
                            </div>
                          </div>
                        </a-col>
                      </a-row>

                      <a-row :gutter="16" style="margin-top: 15px;">
                        <a-col :span="8">
                          <div style="display: flex; align-items: center;">
                            <a-icon type="phone" style="color: #1890ff; margin-right: 8px;"/>
                            <span>{{ item.staffPhone }}</span>
                          </div>
                        </a-col>
                        <a-col :span="8">
                          <div style="display: flex; align-items: center;">
                            <a-icon type="clock-circle" style="color: #52c41a; margin-right: 8px;"/>
                            <span>预计工时：{{ item.workHour }} 小时</span>
                          </div>
                        </a-col>
                        <a-col :span="8">
                          <div style="display: flex; justify-content: flex-end;">
                            <a-tag color="blue">设计师报价</a-tag>
                          </div>
                        </a-col>
                      </a-row>

                      <a-row style="margin-top: 15px;">
                        <a-col :span="24">
                          <div style="font-size: 14px; color: #595959;">
                            <div style="font-weight: 600; margin-bottom: 8px;">报价说明：</div>
                            <div style="background: #fafafa; padding: 12px;border-left: 3px solid #1890ff;">
                              {{ item.content }}
                            </div>
                          </div>
                        </a-col>
                      </a-row>

                    </a-card>
                  </a-list-item>
                </a-list>
              </div>
              <div v-if="endAddressInfo != null && orderData.deliveryDate == null" style="margin-top: 15px">
                <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                  详细地址
                </h3>
                <a-row style="padding-left: 24px;padding-right: 24px;">
                  <a-col :span="24">
                    <b>详细地址：</b>
                    {{ endAddressInfo.address }}
                  </a-col>
                  <br/>
                  <br/>
                  <a-col :span="8"><b>联系人：</b>
                    {{ endAddressInfo.contactPerson ? endAddressInfo.contactPerson : '- -' }}
                  </a-col>
                  <a-col :span="8"><b>联系方式：</b>
                    {{ endAddressInfo.contactMethod ? endAddressInfo.contactMethod : '- -' }}
                  </a-col>
                </a-row>
                <br/>
              </div>
            </a-col>
            <a-col :span="24" style="margin-top: 15px;background: #fff;padding: 20px" v-if="repairSteps.length !== 0">
              <h3 style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                装修流程
              </h3>
              <a-timeline style="margin-top: 20px;">
                <a-timeline-item
                  v-for="(step, index) in repairSteps"
                  :key="step.id"
                  :color="getStepColor(step.status)">
                  <p style="font-size: 16px; font-weight: 500; color: #000c17;">{{ step.title }}</p>
                  <p style="font-size: 13px; color: #8c8c8c;">{{ step.description }}</p>

                  <!-- 预算花费展示 -->
                  <div v-if="step.itemPrice || step.workHours" style="margin-top: 8px;">
                    <div v-if="step.itemPrice" style="display: inline-block; margin-right: 8px;">
                      <span
                        style="font-size: 14px; font-weight: 500; color: #ff4d4f; padding: 4px 8px; background-color: #fff1f0; border-radius: 4px; display: inline-block;">
                        预算费用: {{ step.itemPrice }} 元
                      </span>
                    </div>

                    <div v-if="step.workHours" style="display: inline-block;">
                      <span
                        style="font-size: 14px; font-weight: 500; color: #1890ff; padding: 4px 8px; background-color: #e6f7ff; border-radius: 4px; display: inline-block;">
                        预计工时: {{ step.workHours }} 时
                      </span>
                    </div>

                    <!-- 实际花费展示 -->
                    <div v-if="step.actualItemPrice !== undefined && step.actualItemPrice !== null"
                         style="margin-top: 8px;display: inline-block;margin-left: 8px">
                      <span
                        style="font-size: 14px; font-weight: 500; color: #52c41a; padding: 4px 8px; background-color: #f6ffed; border-radius: 4px; display: inline-block;">
                        实际费用: {{ step.actualItemPrice }} 元
                      </span>
                    </div>
                  </div>

                  <!-- 花费对比 -->
                  <div v-if="step.itemPrice && step.actualItemPrice !== undefined && step.actualItemPrice !== null"
                       style="margin-top: 10px; padding: 12px; border-radius: 8px; background-color: #f5f5f5;">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                      <div style="font-size: 14px; font-weight: 500;">
                        <span style="color: #666;">费用对比</span>
                      </div>
                      <div :style="{
      fontSize: '14px',
      fontWeight: '600',
      color: step.actualItemPrice > step.itemPrice ? '#ff4d4f' : '#52c41a'
    }">
                        {{ step.actualItemPrice > step.itemPrice ? '↑ 超支' : '↓ 节约' }}
                      </div>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-top: 8px;">
                      <div style="font-size: 13px;">
                        <div style="color: #8c8c8c;">预算费用</div>
                        <div style="color: #ff4d4f; font-weight: 500;">¥{{ step.itemPrice }}</div>
                      </div>
                      <div style="font-size: 13px;">
                        <div style="color: #8c8c8c;">实际费用</div>
                        <div style="color: #1890ff; font-weight: 500;">¥{{ step.actualItemPrice }}</div>
                      </div>
                      <div style="font-size: 13px;">
                        <div style="color: #8c8c8c;">差额</div>
                        <div :style="{
        color: step.actualItemPrice > step.itemPrice ? '#ff4d4f' : '#52c41a',
        fontWeight: '500'
      }">
                          ¥{{ Math.abs(step.actualItemPrice - step.itemPrice).toFixed(2) }}
                        </div>
                      </div>
                    </div>

                    <a-progress
                      :percent="(Math.min(step.actualItemPrice, step.itemPrice) / Math.max(step.actualItemPrice, step.itemPrice)) * 100"
                      :status="step.actualItemPrice > step.itemPrice ? 'exception' : 'success'"
                      :show-info="false" style="margin-top: 8px;"
                    />

                    <div style="text-align: right; margin-top: 4px; font-size: 12px; color: #8c8c8c;">
                      占比: {{
                        ((Math.min(step.actualItemPrice, step.itemPrice) / Math.max(step.actualItemPrice, step.itemPrice)) * 100).toFixed(1)
                      }}%
                    </div>
                  </div>
                </a-timeline-item>
              </a-timeline>
            </a-col>
            <a-col :span="24" style="margin-top: 15px;background: #fff;padding: 20px"
                   v-if="orderData.status == 3 && series.length > 0">
              <h3
                style="font-size: 18px; font-weight: 650; color: #000c17; margin-bottom: 20px; border-left: 4px solid #1890ff; padding-left: 10px;">
                装修费用对比分析
              </h3>
              <apexchart type="bar" height="350" :options="chartOptions" :series="series"></apexchart>

              <!-- 添加费用汇总信息 -->
              <div style="margin-top: 20px; display: flex; justify-content: space-around; text-align: center;">
                <div style="padding: 15px; background-color: #fff1f0; border-radius: 4px;">
                  <div style="font-size: 14px; color: #ff4d4f;">总预算费用</div>
                  <div style="font-size: 20px; font-weight: bold; color: #ff4d4f;">
                    ¥{{ repairSteps.reduce((sum, step) => sum + (parseFloat(step.itemPrice) || 0), 0).toFixed(2) }}
                  </div>
                </div>
                <div style="padding: 15px; background-color: #f0f5ff; border-radius: 4px;">
                  <div style="font-size: 14px; color: #1890ff;">总实际费用</div>
                  <div style="font-size: 20px; font-weight: bold; color: #1890ff;">
                    ¥{{
                      repairSteps.reduce((sum, step) => sum + (parseFloat(step.actualItemPrice) || 0), 0).toFixed(2)
                    }}
                  </div>
                </div>
                <div :style="{
      padding: '15px',
      backgroundColor: getTotalCostDifference() >= 0 ? '#fff1f0' : '#f6ffed',
      borderRadius: '4px'
    }">
                  <div :style="{fontSize: '14px', color: getTotalCostDifference() >= 0 ? '#ff4d4f' : '#52c41a'}">
                    {{ getTotalCostDifference() >= 0 ? '总超支' : '总节约' }}
                  </div>
                  <div
                    :style="{fontSize: '20px', fontWeight: 'bold', color: getTotalCostDifference() >= 0 ? '#ff4d4f' : '#52c41a'}">
                    ¥{{ Math.abs(getTotalCostDifference()).toFixed(2) }}
                  </div>
                </div>
              </div>
            </a-col>
            <a-col :span="12" style="margin-top: 15px;background: #fff;padding: 20px"
                   v-if="orderData.status == 3 && series1.length > 0">
              <apexchart type="pie" width="380" :options="chartOptions1" :series="series1"></apexchart>
            </a-col>
            <a-col :span="12" style="margin-top: 15px;background: #fff;padding: 20px"
                   v-if="orderData.status == 3 && series2.length > 0">
              <apexchart type="pie" width="380" :options="chartOptions2" :series="series2"></apexchart>
            </a-col>
          </a-row>
        </a-col>
      </a-row>
    </div>
  </a-drawer>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'
import {mapState} from 'vuex'

function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}

export default {
  name: 'Map',
  props: {
    orderShow: {
      type: Boolean,
      default: false
    },
    orderData: {
      type: Object
    }
  },
  data () {
    return {
      series: [],
      chartOptions: {
        chart: {
          height: 350,
          type: 'bar',
          stacked: false
        },
        dataLabels: {
          enabled: true
        },
        stroke: {
          width: 2
        },
        xaxis: {
          categories: [] // 将根据 repairSteps 动态填充
        },
        yaxis: [
          {
            axisTicks: {
              show: true
            },
            axisBorder: {
              show: true,
              color: '#008FFB'
            },
            labels: {
              style: {
                colors: '#008FFB'
              }
            },
            title: {
              text: '金额 (元)',
              style: {
                color: '#008FFB'
              }
            }
          }
        ],
        tooltip: {
          y: {
            formatter: function (val) {
              return val + ' 元'
            }
          }
        },
        legend: {
          position: 'top',
          horizontalAlign: 'right'
        }
      },
      series1: [], // 用于预算费用饼图
      chartOptions1: {
        chart: {
          width: 380,
          type: 'pie'
        },
        labels: [], // 将根据 repairSteps 动态填充
        title: {
          text: '预算费用分布',
          align: 'center'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      series2: [], // 用于实际费用饼图
      chartOptions2: {
        chart: {
          width: 380,
          type: 'pie'
        },
        labels: [], // 将根据 repairSteps 动态填充
        title: {
          text: '实际费用分布',
          align: 'center'
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      },
      repairSteps: [],
      logisticsForm: this.$form.createForm(this),
      rowId: null,
      quoteForm: this.$form.createForm(this),
      addressList: [],
      fileList: [],
      flawFileList: [],
      previewVisible: false,
      previewImage: '',
      previewVisibleFlaw: false,
      previewImageFlaw: '',
      userInfo: null,
      orderInfo: null,
      startAddressInfo: null,
      endAddressInfo: null,
      discountInfo: null,
      staffInfo: null,
      evaluateInfo: null,
      communityRent: 0,
      rentShow: false,
      communityShow: false,
      communityData: null,
      checkedList: ['Apple', 'Orange'],
      indeterminate: true,
      checkAll: false,
      plainOptions: ['Apple', 'Pear', 'Orange'],
      visible: false,
      rentList: [],
      communityList: [],
      community: null,
      nowPoint: null,
      roadData: [],
      checkLoading: false,
      echartsShow: false,
      getShop: null,
      putShop: null,
      quotationList: [],
      series: [{
        name: '得分',
        data: []
      }],
      chartOptions: {
        chart: {
          height: 380,
          type: 'radar'
        },
        title: {
          text: ''
        },
        xaxis: {
          categories: ['交付得分', '价格得分', '质量得分', '准时得分', '服务得分', '综合得分']
        }
      }
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user,
      repairStepsWithCosts () {
        if (!this.repairSteps || this.repairSteps.length === 0) {
          return []
        }

        return this.repairSteps.map(step => {
          const item = {...step}

          // 计算预算花费总额（如果有多个费用项）
          if (step.itemPrice) {
            item.budgetTotal = parseFloat(step.itemPrice)
          }

          // 实际花费总额
          if (step.actualItemPrice) {
            item.actualTotal = parseFloat(step.actualItemPrice)
          }

          return item
        })
      }
    })
  },
  watch: {
    'orderShow': function (value) {
      if (value) {
        this.dataInit(this.orderData.id)
      }
    }
  },
  methods: {
    updateCostPieCharts () {
      if (!this.repairSteps || this.repairSteps.length === 0) {
        return
      }
      // 过滤出有预算或实际花费的步骤
      const stepsWithData = this.repairSteps.filter(step =>
        (step.itemPrice && parseFloat(step.itemPrice) > 0) ||
        (step.actualItemPrice !== undefined && step.actualItemPrice !== null && parseFloat(step.actualItemPrice) > 0)
      )
      if (stepsWithData.length === 0) {
        return
      }
      // 准备预算费用饼图数据
      const budgetLabels = stepsWithData
        .filter(step => step.itemPrice && parseFloat(step.itemPrice) > 0)
        .map(step => step.title)
      const budgetSeries = stepsWithData
        .filter(step => step.itemPrice && parseFloat(step.itemPrice) > 0)
        .map(step => parseFloat(step.itemPrice))

      // 准备实际费用饼图数据
      const actualLabels = stepsWithData
        .filter(step => step.actualItemPrice !== undefined && step.actualItemPrice !== null && parseFloat(step.actualItemPrice) > 0)
        .map(step => step.title)
      const actualSeries = stepsWithData
        .filter(step => step.actualItemPrice !== undefined && step.actualItemPrice !== null && parseFloat(step.actualItemPrice) > 0)
        .map(step => parseFloat(step.actualItemPrice))
      // 更新图表数据
      this.series1 = budgetSeries
      this.chartOptions1 = {
        ...this.chartOptions1,
        labels: budgetLabels
      }
      this.series2 = actualSeries
      this.chartOptions2 = {
        ...this.chartOptions2,
        labels: actualLabels
      }
    },
    updateCostChart () {
      if (!this.repairSteps || this.repairSteps.length === 0) {
        return
      }
      // 过滤出有预算或实际花费的步骤
      const stepsWithData = this.repairSteps.filter(step =>
        (step.itemPrice && parseFloat(step.itemPrice) > 0) ||
        (step.actualItemPrice !== undefined && step.actualItemPrice !== null && parseFloat(step.actualItemPrice) >= 0)
      )
      if (stepsWithData.length === 0) {
        return
      }
      // 准备图表数据
      const categories = stepsWithData.map(step => step.title)
      const budgetData = stepsWithData.map(step =>
        step.itemPrice ? parseFloat(step.itemPrice) : 0
      )
      const actualData = stepsWithData.map(step =>
        (step.actualItemPrice !== undefined && step.actualItemPrice !== null)
          ? parseFloat(step.actualItemPrice) : 0
      )
      // 更新图表数据
      this.series = [
        {
          name: '预算费用',
          type: 'column',
          data: budgetData
        },
        {
          name: '实际费用',
          type: 'column',
          data: actualData
        }
      ]
      // 更新图表选项中的分类
      this.chartOptions = {
        ...this.chartOptions,
        xaxis: {
          categories: categories
        }
      }
    },
    getTotalCostDifference () {
      if (!this.repairStepsWithCosts || this.repairStepsWithCosts.length === 0) {
        return 0
      }
      const totalBudget = this.repairStepsWithCosts.reduce((sum, step) =>
        sum + (parseFloat(step.itemPrice) || 0), 0)
      const totalActual = this.repairStepsWithCosts.reduce((sum, step) =>
        sum + (parseFloat(step.actualItemPrice) || 0), 0)

      return totalActual - totalBudget
    },
    getStepColor (status) {
      switch (status) {
        case 'completed':
          return 'green'
        case 'in-progress':
          return 'blue'
        case 'pending':
          return 'gray'
        default:
          return 'gray'
      }
    },
    queryRepairStep (orderId) {
      this.$get(`/cos/order-info/queryRepairStep/${orderId}`).then((r) => {
        if (r.data.msg) {
          let repairStep = JSON.parse(r.data.msg)
          this.repairSteps = repairStep

          if (this.orderData.status == 3) {
            // 更新费用图表
            this.updateCostChart()
            // 更新费用饼图
            this.updateCostPieCharts()
          }
        } else {
          this.repairStep = []
        }
      })
    },
    submitLogisticsInfo () {
      this.logisticsForm.validateFields((err, values) => {
        if (!err) {
          // 构造物流信息对象
          let logistics = {
            company: values.company,
            trackingNumber: values.trackingNumber,
            remark: values.remark
          }
          const logisticsData = {
            id: this.orderInfo.id,
            logisticsInfo: JSON.stringify(logistics)
          }
          // 发送请求更新订单的物流信息
          this.$put(`/cos/order-info/updateLogisticsInfo`, logisticsData).then(response => {
            this.$message.success('物流信息提交成功')
            // 通知父组件更新订单状态
            this.$emit('orderChange')
          }).catch(error => {
            this.$message.error('物流信息提交失败: ' + (error.message || '系统错误'))
          })
        }
      })
    },
    goToChat (schedule) {
      this.$post('/cos/chat-record/defaultChat', {
        staffId: schedule.staffId,
        userId: this.currentUser.userId,
        senderType: 0,
        content: '你好'
      }).then((r) => {
        // 跳转到聊天页面，并传递默认消息
        this.$router.push({
          path: '/user/chat'
        })
      })
    },
    confirmQuotation (quotation) {
      this.$get(`/cos/order-info/checkQuotation`, {
        quotationId: quotation.id
      }).then(response => {
        this.$message.success('报价确认成功')
        // 更新订单状态
        this.$emit('orderChange')
      })
    },
    submitQuote () {
      this.quoteForm.validateFields((err, values) => {
        if (!err) {
          const quoteData = {
            orderId: this.orderInfo.id,
            addressId: values.addressId,
            price: values.price,
            workHour: values.workHour,
            content: values.content,
            staffId: this.currentUser.userId
          }

          if (this.rowId != null) {
            quoteData.id = this.rowId
            this.$put('/cos/order-quotation', quoteData).then(response => {
              this.$message.success('报价提交成功')
              // 可以在此处添加成功后的操作，例如刷新数据或关闭表单
            }).catch(error => {
              this.$message.error('报价提交失败: ' + error.message)
            })
          } else {
            this.$post('/cos/order-quotation', quoteData).then(response => {
              this.$message.success('报价提交成功')
              // 可以在此处添加成功后的操作，例如刷新数据或关闭表单
            }).catch(error => {
              this.$message.error('报价提交失败: ' + error.message)
            })
          }
        }
      })
    },
    queryQuotationByOrder () {
      this.$get('/cos/order-quotation/queryQuotationByOrder', {
        orderId: this.orderInfo.id
      }).then(response => {
        this.quotationList = response.data.data
      })
    },
    setFormValues ({...quotation}) {
      this.rowId = quotation.id
      let fields = ['addressId', 'price', 'workHour', 'content']
      let obj = {}
      Object.keys(quotation).forEach((key) => {
        if (fields.indexOf(key) !== -1) {
          this.quoteForm.getFieldDecorator(key)
          obj[key] = quotation[key]
        }
      })
      this.quoteForm.setFieldsValue(obj)
    },
    queryStaffAddress () {
      this.$get(`/cos/address-info/listByStaffId/${this.currentUser.userId}`).then((r) => {
        this.addressList = r.data.data
      })
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    flawImagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.flawFileList = imageList
      }
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
    picHandleChange ({fileList}) {
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
    picHandleChangeFlaw ({fileList}) {
      this.flawFileList = fileList
    },
    dataInit (orderId) {
      this.$get(`/cos/order-info/${orderId}`).then((r) => {
        this.userInfo = r.data.user
        this.orderInfo = r.data.order
        this.startAddressInfo = r.data.startAddress
        this.endAddressInfo = r.data.endAddress
        this.discountInfo = r.data.discount
        this.staffInfo = r.data.staff
        this.evaluateInfo = r.data.evaluate
        this.imagesInit(this.orderInfo.images)
        this.queryQuotationByOrder()
        this.queryRepairStep(orderId)
        if (this.endAddressInfo != null && this.orderData.logisticsInfo != null) {
          // 设置表单值
          setTimeout(() => {
            this.$nextTick(() => {
              let logisticsInfo = JSON.parse(this.orderData.logisticsInfo)
              this.logisticsForm.setFieldsValue({
                company: logisticsInfo.company || '',
                trackingNumber: logisticsInfo.trackingNumber || '',
                remark: logisticsInfo.remark || ''
              })
            })
          }, 200)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          if (this.startAddressInfo != null) {
            this.local(this.startAddressInfo)
          }
          if (this.endAddressInfo != null) {
            this.local(this.endAddressInfo)
          }
        }, 200)
      })
    },
    local (merchantData) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(merchantData.longitude, merchantData.latitude)
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    navigation (address, merchant) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(merchant.longitude, merchant.latitude), new BMap.Point(address.longitude, address.latitude))
      // this.getRoadData()
    },
    getRoadData () {
      let options = {
        onSearchComplete: results => {
          // eslint-disable-next-line eqeqeq,no-undef
          if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
            // 获取第一条方案
            let plan = results.getPlan(0)
            // 获取方案的驾车线路
            // eslint-disable-next-line no-unused-vars
            let route = plan.getRoute(0)
            // 获取每个关键步骤,并输出到页面
            let s = []
            for (let j = 0; j < plan.getNumRoutes(); j++) {
              let route = plan.getRoute(j)
              for (let i = 0; i < route.getNumSteps(); i++) {
                let step = route.getStep(i)
                s.push((i + 1) + '. ' + step.getDescription())
              }
            }
            this.roadData = s
          }
        }
      }
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), options)
      // eslint-disable-next-line no-undef
      let start = new BMap.Point(this.nowPoint.lng, this.nowPoint.lat)
      let end = null
      if (this.rentShow) {
        end = new BMap.Point(this.rentData.longitude, this.rentData.latitude)
      } else {
        end = new BMap.Point(this.communityData.longitude, this.communityData.latitude)
      }
      // eslint-disable-next-line no-undef
      driving.search(start, end)
    },
    checkEvaluate (score) {
      let data = [score.deliverScore, score.priceScore, score.qualityScore, score.scheduleScore, score.serviceScore, score.overScore, 100]
      this.series[0].data = data
    },
    home () {
      this.$emit('close')
    },
    gisOnChange (e) {
      let data = null
      switch (e.target.value) {
        case '1':
          data = this.getShop
          break
        case '2':
          data = this.putShop
          break
      }
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions: {map: baiduMap.rMap(), autoViewport: true}})
      // eslint-disable-next-line no-undef
      driving.search(new BMap.Point(this.nowPoint.lng, this.nowPoint.lat), new BMap.Point(data.longitude, data.latitude))
    },
    getLocal () {
      // eslint-disable-next-line no-undef
      let geolocation = new BMap.Geolocation()
      geolocation.getCurrentPosition(r => {
        this.nowPoint = r.point
      }, {enableHighAccuracy: true})
    }
  }
}
</script>

<style scoped>
>>> .ant-drawer-body {
  padding: 0 !important;
}

>>> .ant-card-meta-title {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-card-meta-description {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-divider-with-text-left {
  margin: 0;
}

>>> .ant-card-head-title {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-card-extra {
  font-size: 13px;
  font-family: SimHei;
}

>>> .ant-radio-button-wrapper {
  border-radius: 0;
}
</style>
