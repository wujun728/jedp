<template>
  <a-modal
    title="操作"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
  >
    <a-spin :spinning="spinning">
      <a-form :form="form">
        <a-form-item style="display:none">
          <a-input v-decorator="['userId']"/>
        </a-form-item>
        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户名" >
          <a-input placeholder="用户名" v-decorator="['loginName', {rules: [{ required: true, message: '请输入用户名' }]}]" />
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="昵称">
          <a-input v-decorator="['userName', {rules: [{ required: true, message: '请输入昵称' }]}] "placeholder="起一个名字"/>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="手机" >
          <a-input placeholder="手机" v-decorator="['mobile', {rules: [{ required: true, message: '请输入手机' }]}]" />
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户类型">
          <a-select placeholder="类型" v-decorator="['userType', {rules: [{ required: true, message: '请选择用户类型' }]}]">
            <a-select-option v-for="item in userTypeData" :key="item.value">{{ item.text }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="状态">
          <a-select v-decorator="['status', {initialValue:'0',rules: [{ required: true, message: '请选择状态' }]}]">
            <a-select-option :value="'0'">正常</a-select-option>
            <a-select-option :value="'1'">禁用</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="部门">
          <a-tree-select
            v-decorator="['deptId', {rules: [{ required: true, message: '请选择部门' }]}]"
            :dropdownStyle="{ maxHeight: '400px', overflow: 'auto' }"
            :treeData="deptTree"
            @change="onChange"
            placeholder="部门"
            treeDefaultExpandAll
          >
          </a-tree-select>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属园区">
          <a-select v-decorator="['parkId', {rules: [{ required: true, message: '请选择所属园区' }]}]" @change="onChangeRole">
            <a-select-option v-for="item in parkData" :key="item.value">{{ item.text }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="拥有角色" hasFeedback>
          <a-select style="width: 100%" mode="multiple" v-decorator="['roleIds', {rules: [{ required: true, message: '请选择角色' }]}]" :allowClear="true" >
            <a-select-option v-for="(action) in roleAll" :key="action.roleId" >{{ action.roleName }}</a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
          <a-textarea :rows="3" placeholder="..." v-decorator="['remark']"/>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
import { getRoleAll, saveUser, getUser, getUserType } from '@/api/system'
import { getParkList } from '@/api/admin/park'
import pick from 'lodash.pick'
export default {
  name: 'UserModal',
  props: {
    deptTree: {
      type: Array,
      required: true
    }
  },
  components: {
  },
  data () {
    return {
      description: '列表使用场景：后台管理中的权限管理以及角色管理，可用于基于 RBAC 设计的角色权限控制，颗粒度细到每一个操作类型。',
      visible: false,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      confirmLoading: false,
      parkData: [],
      roleAll: [],
      userTypeData: [],
      mdl: {},
      deptCheck: true,
      spinning: false,
      form: this.$form.createForm(this)
    }
  },
  beforeCreate () {
  },
  created () {
    // 园区
    getParkList().then(res => {
      if (res.code === 0) {
        const result = res.rows
        result.forEach(r => {
          this.parkData.push({ value: r.id, text: r.name })
        })
      }
    })
    // 获取用户类型
    getUserType().then(res => {
      if (res.code === 0) {
        const result = res.data
        result.forEach(r => {
          this.userTypeData.push({ value: r.value, text: r.text })
        })
      }
    })
  },
  methods: {
    add () {
      this.form.resetFields()
      this.mdl = Object.assign({}, { userId: 0, deptId: '' })
      this.visible = true
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'userId', 'loginName', 'userName', 'userType', 'status', 'mobile', 'roleIds', 'remark', 'deptId', 'parkId'))
      })
    },
    edit (record) {
      if (record.userId > 0) {
        this.spinning = true
        getUser(record.userId).then(res => {
          this.mdl = Object.assign({}, res)
          this.visible = true
          this.$nextTick(() => {
            this.form.setFieldsValue(pick(this.mdl, 'userId', 'loginName', 'userType', 'userName', 'status', 'mobile', 'roleIds', 'remark', 'deptId', 'parkId'))
            this.spinning = false
            // this.form.setFieldsValue({ ...record })
          })
          // 根据园区显示角色
          this.loadRoleAll(this.mdl.parkId)
        })
      }
    },
    onChange (value, label, extra) {
      if (extra.triggerNode.$children.length > 0) {
        this.$message.error('不能选择父节点' + `${label}`)
        this.deptCheck = false
      } else {
        this.deptCheck = true
      }
    },
    onChangeRole (value) {
      this.loadRoleAll(value)
    },
    loadRoleAll (parkId) {
      getRoleAll({ parkId: parkId }).then(res => {
        this.roleAll = res.rows
      })
    },
    handleSubmit (e) {
      e.preventDefault()
      if (!this.deptCheck) {
        this.$message.error('不能选择父节点')
        return
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log('Received values of form: ', values)
          this.confirmLoading = true
          saveUser(values).then(res => {
            if (res.code === 0) {
              this.$message.success('保存成功')
              this.$emit('ok')
              this.visible = false
            } else {
              this.$message.success(res.msg)
            }
          }).catch(() => {
            this.$message.error('系统错误，请稍后再试')
          }).finally(() => {
            this.confirmLoading = false
          })
        }
      })
    }
  },
  watch: {
    /*
      'selectedRows': function (selectedRows) {
        this.needTotalList = this.needTotalList.map(item => {
          return {
            ...item,
            total: selectedRows.reduce( (sum, val) => {
              return sum + val[item.dataIndex]
            }, 0)
          }
        })
      }
      */
  }
}
</script>
