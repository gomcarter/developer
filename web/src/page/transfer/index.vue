<template>
  <div>
    <h4 class="title">操作结果</h4>
    <hr>
    <div class="container">
      <div class="result-container">
        <span v-if="data.result" class="el-icon-success success"></span>
        <span v-else class="el-icon-error failed"></span>
      </div>
      <br/>
      <br/>
      <h4 class="center"><b>{{ data.title || '操作成功！' }}</b></h4>
      <div v-if="data.subtitle" class="subtitle center">{{ data.subtitle }}</div>
      <br/>
      <br/>
      <div style="margin: 10px">
        <el-button type="primary" v-for="(btn, key) of data.buttons" @click="to(btn.link)" :key="key">{{ btn.text }}</el-button>
        <el-button type="info" v-if="data.back != false " @click="$router.go(-1)">{{ data.back || '返回' }}</el-button>
      </div>
    </div>
  </div>
</template>

/**
*
* 调用方式：
*
* this.$transfer({
*   result: true/false; 如果为true显示成功样式，false显示失败样式  默认为true
*   back: '返回', // 如果不需要这个按钮，传false来， 默认为返回上一级
*   title: '操作成功！', // 默认显示  操作成功！
*   subtitle: 'xxxx', // 默认不显示
*   buttons: [{
*     text: '去列表',
*     link: '/system/role',
*   }, {}],
* });
*
*/
<script>
export default {
  name: 'transfer',
  data () {
    return {
      data: {
        result: true,
        title: '操作成功！',
        subtitle: null,
        back: '返回',
        buttons: []
      }
    }
  },
  methods: {
    to (link) {
      this.$router.push(link)
    }
  },
  mounted () {
    let data
    try {
      const params = this.$route.params.data
      if (params && typeof params === 'string') {
        data = JSON.parse(decodeURIComponent(params))
      } else {
        data = data || ({})
      }
    } catch (ignore) {
      // do nothing
    }
    this.data = Object.assign({}, this.data, data)
    document.documentElement.scrollTop = 0
    window.screenTop = 0
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
