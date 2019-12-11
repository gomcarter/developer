<template>
    <!-- 过渡动画 -->
    <transition name="fade">
        <div class="img-view">
            <!-- 遮罩层 -->
            <div class="img-layer" @click="close"></div>
            <div class="img">
                <img :src="imgSrc" class="bigimg" :style="{ 'max-width': width }">
            </div>
            <span class="upload_warp_img_div_top el-icon-error img_div_del" @click="close"></span>
        </div>
    </transition>
</template>
<script>

export default {
  name: 'bigImg',
  props: {
    imgSrc: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '100%'
    }
  },
  methods: {
    close () {
      // 发送事件
      this.$emit('hideBigImg')
    }
  }
}

</script>
<style scoped>
    .img_div_del {
        position: fixed;
        top: 20px;
        right: 20px;
        color: #fff;
        z-index: 1000;
        font-size: 30px;
        cursor: pointer;
    }
    /*动画*/
    .fade-enter-active,
    .fade-leave-active {
        transition: all .2s linear;
        transform: translate3D(0, 0, 0);
    }
    .fade-enter,
    .fade-leave-active {
        transform: translate3D(100%, 0, 0);
    }
    /* bigimg */
    .img-view {
        position: relative;
        width: 100%;
        height: 100%;
    }
    /*遮罩层样式*/
    .img-view .img-layer {
        position: fixed;
        z-index: 999;
        top: 0;
        left: 0;
        background: rgba(0, 0, 0, 0.7);
        width: 100%;
        height: 100%;
        overflow: hidden;
    }
    /*不限制图片大小，实现居中*/
    .img-view .img img {
        max-width: 100%;
        max-height: 100%;
        display: block;
        position: fixed;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        margin: auto;
        z-index: 1000;
    }
</style>
