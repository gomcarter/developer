<template>
  <div class="img-box">
    <v-big-img v-if="isShowBigImg" :imgSrc="getPictureUrl(imgSrc)" @hideBigImg="closeBigImg"></v-big-img>
    <div class="imgW">
      <img style="border: 1px solid #CCC; border-radius: 5px" :src="getPictureUrl(imgSrc, {w: w, h: h})" :alt="altTitle">
      <div class="resize">
        <span @click="showBigImg"></span>
      </div>
    </div>
  </div>
</template>
<script>
import { getPictureUrl } from '@/config/utils'
import vBigImg from '@/components/image/big'

export default {
  name: 'mimg',
  props: {
    imgSrc: {
      type: String,
      default: ''
    },
    altTitle: {
      type: String,
      default: '图片未找到'
    },
    w: {
      type: String,
      default: ''
    },
    h: {
      type: Number,
      default: 200
    }
  },
  data () {
    return {
      getPictureUrl,
      isShowBigImg: false
    }
  },
  components: {
    'v-big-img': vBigImg
  },
  methods: {
    showBigImg () {
      this.isShowBigImg = true
    },
    closeBigImg () {
      this.isShowBigImg = false
    }
  }
}

</script>
<style lang="scss" scoped>
.img-box{
  float: left;
  margin-right: 20px;
  >.imgW {
    position: relative;
    width: auto;
    >img {
      max-width: 100%;
    }
    .resize {
      width: 100%;
      height: 100%;
      position: absolute;
      right: 0px;
      bottom: 0px;
      background-color: #646a7f;
      color: #fff;
      text-align: center;
      opacity: 0;
      z-index: 10;
      cursor: pointer;
      span{
        display: block;
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>
