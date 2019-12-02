<template>
  <div class="imageuploader-container">
    <v-big-img v-if="isShowBigImg" :imgSrc="bigImgUrl" @hideBigImg="closeBigImg" />
    <div class="imageuploader-viewer-container" v-show="imageUrls.length > 0">
      <div class="imageuploader-viewer-cell" v-for="(image, index) of imageUrls" :key="index"
           :style="{ width: `${width}px`, height: `${height}px`}">
        <span  v-if="!image.loading" class="imageuploader-viewer-delete-btn el-icon-error" @click="imageUrls.splice(index, 1); notifyImageChanged();"></span>
        <div v-if="!image.loading" class="imageuploader-viewer-show-big" @click="showBigImg(image.url)">
          <span class="el-icon-zoom-in"></span>
        </div>
        <img class="imageuploader-loading" v-if="image.loading" :src="loading"/>
        <img v-else :src="getPictureUrl(image.url, {q : quality, w: width, h: height})" @click="showBigImg(image.url)"/>
      </div>
    </div>
    <div class="imageuploader-background-container" v-show="imageUrls.length < multiple">
      <div class="imageuploader-background-shower" :style="{ width: `${width}px`, height: `${height}px`}">
        <img :src="background">
        <input @change="fileChanged($event)" class="imageuploader-file-input" type="file" :multiple="multiple > 1"
               accept="image/*"/>
        <p class="multiple-title">{{ title }}</p>
      </div>
    </div>
    <div class="cropper-box" v-show="isCropperShow">
      <div class="cropper-box_container">
        <div class="header">
          <span>请选择合适的区域</span>
          <i class="el-icon-close" @click="closeCropper"></i>
        </div>
        <div class="cropper">
          <vue-cropper
            ref="cropper"
            :img="option.img"
            :outputSize="option.size"
            :autoCrop="true"
            :centerBox="true"
            :canMove="true"
            :fixed="true"
            :fixedNumber="fixedNumberArr"
            :original="true"
            :autoCropWidth="autoCropWidth"
            :autoCropHeight="autoCropHeight"
            :fixedBox="true"
          ></vue-cropper>
        </div>
        <p class="cropper-title">在非裁剪区域可上下移动图片</p>
        <ul class="option">
          <li @click="scaleIncrement">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAh0lEQVRYR2NkGGDAOMD2M4w6YDQEKA2BKcBE/B2IS8lNzJQ64D/Q4k9AzD+QDvgMtJxv1AGjITAaArQIgalAQ7PINRhNXyeQX4HNLHwF0TSghkwqOaALaE45qQ4gxm5QSThaEI2GwGgIDHgIDGiDBFRafhvIJhkxhRVeNZS2CUcdMBoCFIcAAF6xKCGqjKLPAAAAAElFTkSuQmCC" alt="">
          </li>
          <li @click="scaleDecrement">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAW0lEQVRYR+3VwQkAIAhAUd2kTatNGyWFbkUEBXb4gkfNHoIqwaHB7wsDIIAAAggg8LVAsUuZH13Lan283xQ7gfABfNo08gaiWbHnMr7egZtfH9cigAACCCCAQAexYQYhaaM/hAAAAABJRU5ErkJggg==" alt="">
          </li>
          <li @click="turnRight">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABxElEQVRYR+2VOyiFcRjGHffFpixyWZRcUkaLGJTFSAYWpQxuA5HL2SxSxCCDY1AGKZMsFKFYZBDF4pYoZVEu4ffUd+p0dL7z3c75SuetX993Ov/vfZ7/7X0DaT5HwGf9tJSBf7UCpZynF3i1c668WIFMBIdgEhZgMJkGKhBbhUqYgXF4T4aBDESGIQhX0A6ndoTDY51uQQEJbiEL3uACQrAIH3aMODUgjWnohzb4hi6oggFYt2rCjYE8RK5hCzoNwSaea7AEOphxw40BJe+AFaiDQ0Otmuc+jIJuhWm4NaDkx6BDWRuh1GxsQzHPZzMHXhgoQaAeQlFCO/zeg2CiDcTKr+1RUarxy0A5wueQDZ+xTHixBbFyq1Y8QiHc+2GgDNFLULH68sNAC6KzoEMaMxK5BaqGT9DjxoDut3p8H4TMEkX9p5pwAOqSalauVmCCr8dA1e7EgokixhzBPEzFG29lC9KN2ajEqu1umiRVM9oGdcdG+PHCgHLkGLNR99sF1fgzuAOVW935BuiGZegFS23ZygpETkIzHIFW0MqEQ/V+A+ZAxcdy2DUQTpzLiwpMPtzAg2XFqIFODTjV+/NdykBqBX4BpUZFIXSCdMMAAAAASUVORK5CYII=" alt="">
          </li>
          <li @click="turnLeft">
            <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABxElEQVRYR+2WyytEURzHZxYIGxuJLCmUwkYUpVgrZeFRSnltJMVCymOjFAslCyLksfEPKEWyYYXkUULKQlaS8orPr87UUPfeM+fO7aTm1KeZe+/vnO/3/uac32/CIcsjbFk/lDDwbzOQw955iMf+McnAEMIjMAaT8OnHiImBVATHoR9OoQXOTE2YGIholfBlHfJgFCbgO1YjJgYKEWmDOiiANPgA2RdPQRpIZvFp6IFn2IArmFIMxiou8boZSCF2ByTtfbAKb7ACtepneA3SwDyLN0ENHCmhSj4PoBXWTMR1M5BP4CU0wlaU0KHKQpWpuK6BOQLLoPyPUBfX23ATtIF7BIZh2Y+Q01yvTZjOxBeQo3dhw0AuopKBLHi0YSAJ0XcognMbBkTzFqT2L9oy4HQK4uLHaxOKSDGcQDNI+dUd7QRK6c5wm6BjQOYvgBQiqQfXGg4qiNmDDnA9vroGpBfsgmzGAZDS7NR663m2CdKkpH64Dl0Dsoh0wxnohGOYhX24AzmupdAN1SpmyUtcnsdiILKeZKEXGiBT3fxSpqRXSKeU2qE1TAxEL5yt3l7+kgXajrXexiTIbwZMNH/NSRiwnoEfg6dGIeEaZ9gAAAAASUVORK5CYII=" alt="">
          </li>
        </ul>
        <div class="footer">
          <el-button @click="closeCropper">取消</el-button>
          <el-button type="primary" @click="cropperSubmit">上传图片</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

/**
* @author ： 李银 on 2018年6月19日 21:11:04
*
* 入参：
* multiple:Number     - 最多上传多少张图片，默认为1
* background:image     - 图片选择器背景图
* title:String        - 图片选择器背景提示文字
* width:Number        - 图片显示宽度，默认200px
* height:Number       - 图片显示高度，默认200px
* quality:Number      - 小图显示的质量，默认50（即原图50%的压缩质量）
* fileSize:Number     - 图片最大上传KB数，默认2MB（即1024*1024*2 KBytes）
* initImageUrls:Array - 初始化图片列表，外部传入图片URL
*
* api:
* getImageUrls()                - 获取当前已经上传的图片url；
*
* 回调：
* onImageChanged(urls)          - 当前图片的url发生改变时（如删除，上传成功）出发此回调，urls为所有图片urls；
**/
<script>
import uploadImage from '@/assets/img/upload.png'
import loading from '@/assets/img/loading.gif'
import {uploadFile} from '@/config/api/base-api'
import {getPictureUrl} from '@/config/utils'
import { VueCropper } from 'vue-cropper'

export default {
  name: 'imageuploader',
  props: {
    multiple: {
      type: Number,
      default: 1
    },
    background: {
      type: String,
      default: uploadImage
    },
    title: {
      type: String,
      default: '请选择需要上传的图片'
    },
    width: {
      type: Number,
      default: 200
    },
    height: {
      type: Number,
      default: 200
    },
    mark: {
      type: Boolean,
      default: true
    },
    quality: {
      type: Number,
      default: 50
    },
    // 默认仅支持最大10M文件
    fileSize: {
      type: Number,
      default: 1024 * 1024 * 2
    },
    initImageUrls: {
      type: Array,
      default: () => []
    },
    onImageChanged: {
      type: Function,
      default: null
    },
    isCropper: { // 是否能够截图
      type: Boolean,
      default: false
    },
    fixedNumberArr: { // 截图框比例
      type: Array,
      default: () => [4, 3]
    },
    autoCropWidth: { // 截图框宽
      type: Number,
      default: 400
    },
    autoCropHeight: { // 截图框高
      type: Number,
      default: 300
    }
  },
  data () {
    return {
      loading,
      bigImgUrl: '',
      isShowBigImg: false,
      // 格式为 {url: '图片url', loading: true/false - 是否在上传中？};
      imageUrls: [],
      // 支持上传的图片类型
      support: ['gif', 'jpeg', 'jpg', 'png', 'svg', 'bmp'],
      option: {
        size: 0.9,
        img: ''
      },
      isCropperShow: false,
      imgName: ''
    }
  },
  watch: {
    initImageUrls: 'setImageUrls'
  },
  mounted () {
    // 初始化外部传入的图片资源
    this.setImageUrls()
  },
  methods: {
    setImageUrls () {
      let init = this.initImageUrls || []
      if (typeof init === 'string') {
        init = [init]
      }
      this.imageUrls = init.filter(url => url).map(url => ({ url, loading: false }))
    },
    getPictureUrl,
    fileChanged (e) {
      // 判断文件是否存在
      const files = e.target.files

      // 判断是否为图片文件
      const temp = []
      const legalFiles = []
      if (!this.isCropper) {
        for (let i = 0; i < files.length; i += 1) {
          const f = files[i]
          // 获取文件上传的后缀名
          const splits = f.name.split('.')
          const type = splits[splits.length - 1].toLowerCase()
          if (this.support.indexOf(type) === -1) {
            this.$alert(`您这个[${f.name}]上传类型不符合`, '提示', {type: 'error'})
          } else if (f.size >= this.fileSize) {
            this.$alert(`您这个[${f.name}]文件太大了`, '提示', {type: 'error'})
          } else {
            temp.push({ loading: true })
            legalFiles.push(f)
          }
        }
      } else {
        if (files.length > 1) {
          this.$alert(`请选择单张图片上传`, '提示', {type: 'error'})
          return false
        }
        const f = files[0]
        // 获取文件上传的后缀名
        const splits = f.name.split('.')
        const type = splits[splits.length - 1].toLowerCase()
        if (this.support.indexOf(type) === -1) {
          this.$alert(`您这个[${f.name}]上传类型不符合`, '提示', {type: 'error'})
        } else if (f.size >= this.fileSize) {
          this.$alert(`您这个[${f.name}]文件太大了`, '提示', {type: 'error'})
        } else {
          temp.push({ loading: true })
          legalFiles.push(f)
          this.imgName = f.name
        }
      }

      // 上传图片是否超限
      if (this.imageUrls.length + legalFiles.length > this.multiple) {
        this.$alert(`最多只能上传${this.multiple}张图片`, '提示', {type: 'error'})
      } else {
        this.imageUrls = this.imageUrls.concat(temp)
        this.upload(legalFiles)
      }
      e.target.value = ''
    },
    async upload (files) {
      const param = new FormData()
      files.forEach((f) => {
        if (this.isCropper) {
          let render = new FileReader()
          render.onload = (e) => {
            this.isCropperShow = true
            this.option.img = e.target.result
          }
          render.readAsDataURL(f)
        } else {
          param.append('fileList', f)
        }
      })
      if (!this.mark) {
        param.append('mark', this.mark)
      }
      if (!this.isCropper) {
        uploadFile(param).then((res) => {
          let index = 0
          this.imageUrls = this.imageUrls.map((image) => {
            if (image.loading) {
              image.url = res[index]
              image.loading = false
              index += 1
            }
            return image
          })
          this.notifyImageChanged()
        })
      }
    },
    notifyImageChanged () {
      if (this.onImageChanged) {
        this.onImageChanged(this.getImageUrls())
      }
    },
    getImageUrls () {
      return this.imageUrls.filter(i => !i.loading).map(i => i.url)
    },
    showBigImg (url) {
      this.bigImgUrl = this.getPictureUrl(url)
      this.isShowBigImg = true
    },
    closeBigImg () {
      this.isShowBigImg = false
    },
    clear () {
      this.imageUrls = []
    },
    // 截图
    closeCropper () {
      this.isCropperShow = false
      this.option.img = ''
      this.imageUrls = this.imageUrls.splice(0, this.imageUrls.length - 1)
    },
    cropperSubmit (v) {
      const param = new FormData()
      this.$refs.cropper.getCropData((data) => {
        param.append('fileList', this.dataURLtoFile(data, this.imgName))
        uploadFile(param).then((res) => {
          this.isCropperShow = false
          let index = 0
          this.imageUrls = this.imageUrls.map((image) => {
            if (image.loading) {
              image.url = res[index]
              image.loading = false
              index += 1
            }
            return image
          })
          this.notifyImageChanged()
        }).catch(() => {
          this.$alert('上传失败', '提示', {type: 'error'})
        })
      })
    },
    turnRight () {
      this.$refs.cropper.rotateRight()
    },
    turnLeft () {
      this.$refs.cropper.rotateLeft()
    },
    scaleIncrement () {
      this.$refs.cropper.changeScale(1)
    },
    scaleDecrement () {
      this.$refs.cropper.changeScale(-1)
    },
    dataURLtoFile (dataurl, filename) { // 将base64转换为文件
      let arr = dataurl.split(',')
      let mime = arr[0].match(/:(.*?);/)[1]
      let bstr = atob(arr[1])
      let n = bstr.length
      let u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], filename, {type: mime})
    }
  },
  components: {
    'v-big-img': () => import('@/components/image/big'),
    'vue-cropper': VueCropper
  }
}
</script>

<style lang="scss" scoped>
  @import 'index';
</style>
