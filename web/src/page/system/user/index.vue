<template>
  <div v-if="setting.remote != null">
    <h4 class="title" v-if="isAdmin">设置</h4>
    <hr v-if="isAdmin"/>
    <el-form label-width="10em">
      <el-form-item label="是否开启外部登录" class="label" v-if="isAdmin">
        <el-switch v-model="setting.remote" @change="remoteChanged"></el-switch>
      </el-form-item>
    </el-form>
    <div v-if="setting.remote === true">
      <el-form :model="setting" label-width="10em" v-if="isAdmin" ref="settingForm">
        <el-form-item label="登录URL" :rules="[{ required: true, message: '请输入登录URL', trigger: ['blur', 'change'] }]" prop="url">
          <el-input v-model="setting.url" placeholder="请输入登录URL" ></el-input>
        </el-form-item>
        <el-form-item>
          <div class="mark">设置url需支持POST请求，且登录用户名参数为：username，密码参数为：password；返回数据结果需要如下：</div>
          <v-jsonformatter :width="600" :json="{code: '0 —— 表示登录成功，其他 —— 表示登录失败', message: '登录失败原因'}" :minHeight="100"></v-jsonformatter>
          <div class="mark red">注意：如果开启了外部登录，但是登录不成功，您依然可以是使用admin账户登录到本系统</div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save" :icon="disabled?'el-icon-loading':'el-icon-check'" :disabled="disabled">保存设置</el-button>
          <el-button type="success" @click="openTestDialog" icon="el-icon-s-promotion" :disabled="disabled">测试登录</el-button>
        </el-form-item>
      </el-form>
      <el-form label-width="10em" v-else>
        <el-form-item class="label" >
          <h3>当前已开启外部登录，用户管理功能被禁用。</h3>
        </el-form-item>
      </el-form>
    </div>
    <div v-else-if="setting.remote === false" :style="{ 'margin-left': isAdmin ? '4em' : '' }">
      <h4 class="title">筛选条件</h4>
      <hr/>
      <div class="filters">
        <el-form :inline="true" :model="filter" label-width="6em">
          <el-form-item label="编号">
            <el-input v-model="filter.id" placeholder="请输入编号" @keypress.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item label="账号">
            <el-input v-model="filter.username" placeholder="请输入账号" @keypress.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="filter.name" placeholder="请输入姓名" @keypress.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="filter.mail" placeholder="请输入邮箱" @keypress.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="filter.cellphone" placeholder="请输入联系电话" @keypress.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker v-model="filter.createTimeGE" value-format="yyyy-MM-dd" type="date" placeholder="选择日期" @keypress.enter.native="search"></el-date-picker>
            至
            <el-date-picker v-model="filter.createTimeLE" value-format="yyyy-MM-dd" type="date"  placeholder="选择日期" @keypress.enter.native="search"></el-date-picker>
          </el-form-item>
          <el-form-item label="修改时间">
            <el-date-picker v-model="filter.modifyTimeGE" value-format="yyyy-MM-dd" type="date" placeholder="选择日期" @keypress.enter.native="search"></el-date-picker>
            至
            <el-date-picker v-model="filter.modifyTimeLE" value-format="yyyy-MM-dd" type="date"  placeholder="选择日期" @keypress.enter.native="search"></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
            <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
          </el-form-item>
        </el-form>
      </div>
      <h4 class="title">自定义函数列表</h4>
      <hr/>
      <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>
    </div>
    <v-dialog ref="testDialog" label-width="6em" title="测试登录" :ok="doTest" ok-text="开始测试" cancel-text="关闭">
      <el-form slot="body" :model="test" ref="testForm" label-width="6em" >
        <el-form-item label="账号" prop="username" :rules="[{ required: true, message: '请输入账号', trigger: ['blur', 'change'] }]">
          <el-input v-model="test.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: ['blur', 'change'] }]">
          <el-input v-model="test.password" placeholder="请输入新密码，长度随便设，只要你记得住"></el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { userListApi, userCountApi, deleteUserApi, getUserSettingApi, updateUserSettingApi, testRemoteLoginApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'
import { isAdmin, user } from '@/config/login'

export default {
  data () {
    return {
      isAdmin: isAdmin(),
      currentUser: user(),
      disabled: false,
      setting: {
        remote: null,
        url: null
      },
      test: {
        username: 'admin',
        password: '3e27f65cdf23407d9c3a72297567c5fa'
      },
      filter: {
        id: null,
        username: null,
        name: null,
        mail: null,
        cellphone: null,
        createTimeGE: null,
        createTimeLE: null,
        modifyTimeGE: null,
        modifyTimeLE: null
      },
      dataUrl: userListApi,
      countUrl: userCountApi,
      params: {},
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: () => this.$router.push(`/system/user/edit`)
      }],
      columns: [
        {field: 'id', header: '编号', sort: 'id', width: 100},
        {field: 'username', header: '账号', sort: 'username', width: 140},
        {field: 'name', header: '姓名', sort: 'name', width: 140},
        {field: 'mail', header: '邮箱', sort: 'mail', width: 200},
        {field: 'cellphone', header: '联系电话', sort: 'cellphone', width: 140},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 230, formatter: (row, index, value) => formatDate(value)},
        {field: 'modifyTime', header: '修改时间', sort: 'modify_time', width: 230, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 230,
          actions: [{
            text: '【编辑】',
            // admin或者是自己才有权限，并且admin不接受编辑
            show: (r) => (this.isAdmin || r.username === this.currentUser) && !isAdmin(r.username),
            handler: (row) => this.$router.push(`/system/user/edit/${row.id}`)
          }, {
            text: '【删除】',
            // admin才有权限，并且admin不接受编辑
            show: (r) => this.isAdmin && !isAdmin(r.username),
            handler: (row) => this.delete(row.id)
          }]
        }
      ]
    }
  },
  mounted () {
    this.load()
  },
  methods: {
    openTestDialog () {
      this.$refs.testDialog.open()
    },
    load () {
      getUserSettingApi()
        .then((d) => {
          this.setting.remote = d.remote || false
          this.setting.url = d.url
        })
        .catch(() => {
          this.$alert('读取配置数据失败！', '提示', {type: 'error'})
        })
    },
    remoteChanged () {
      if (this.setting.remote === false) {
        updateUserSettingApi(this.setting)
      }
    },
    doTest () {
      testRemoteLoginApi({
        url: this.setting.url,
        username: this.test.username,
        password: this.test.password
      }).then(d => {
        this.$alert('测试登录成功！', '提示', {type: 'success'})
      }).catch(() => {})
    },
    save () {
      this.$refs.settingForm.validate((valid) => {
        if (valid) {
          this.disabled = true
          updateUserSettingApi(this.setting)
            .then(d => {
              this.disabled = false
              this.$success(`保存成功！`)
              this.disabled = false
            })
            .catch(() => {
              this.disabled = false
            })
        }
      })
    },
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteUserApi(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    search () {
      this.params = removeBlank(this.filter)
    },
    clear () {
      this.params = {}
      this.filter = {}
    }
  },
  components: {
    'v-dialog': () => import('@/components/dialog'),
    'v-datagrid': () => import('@/components/datagrid'),
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
