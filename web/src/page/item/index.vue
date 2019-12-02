<template>
<!-- 商品分类 -->
  <div>
    <h4 class="title">商品搜索测试页面</h4>
    <el-form :model="params" label-width="1em" ref="form">
      <el-form-item prop="keyword" required
                    :rules="[{ required: true, message: '请输入商品名称进行搜索', trigger: ['blur', 'change'] }]">
        <el-input v-model="params.keyword" placeholder="请输入商品名称进行搜索" style="display: inline-block;width: 70%" @keypress.enter="search"></el-input>
        <el-button type="primary" @click="search" style="display: inline-block" icon="el-icon-search">搜索</el-button>
        <el-input placeholder="请输入商品名称进行搜索" style="display: none" ></el-input>
      </el-form-item>
      <el-form-item>
        <div class="mobile">
          <div class="content">
            <header class="search-header">
              <div class="">
                <div style="text-indent: initial;">搜索结果</div>
              </div>
            </header>
            <div class="search-article">
              <div v-if="items && items.length > 0" v-for="(item, index) of items" v-bind:key="index" class="cmui-list-item pos-r paddingt10 paddingh20 borderb" style="width: 100%; padding-right: 5px; padding-bottom: 5px;">
                <div class="cmui-list-item-container">
                  <div class="bg-white margint10 flex-container top paddingb20" style="overflow: hidden">
                    <div class="pos-r img-container border-radius" style="border:0">
                      <img :src="(item.mainImages || '').split(',')[0]">
                    </div>
                    <div class="paddingh20 flex1">
                      <div class="title1 text-fixed2 text-darker fs-11 margint10" style="height: 59px">
                        <span class="text-bolder fs-13 lh-20">{{item.name}}</span>
                      </div>
                      <div class="flex-container left" style="border: 0">
                        <p class="text-red fs-19 lh-28">￥{{item.price}}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { searchApi } from '@/config/api/item-api'

export default {
  name: 'itemsearch',
  props: {
    value: {
      type: String
    }
  },
  data () {
    return {
      params: {
        keyword: null
      },
      items: []
    }
  },
  methods: {
    search () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          searchApi(this.params)
            .then(d => {
              this.items = d || []
            })
            .catch(d => {
              console.log(d)
            })
        }
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
