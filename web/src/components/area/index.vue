<template>
  <div class='area-container'>
    <select v-show="provinces.length > 0" class='el-input__inner' v-model="province"
            @change=" city = ''; district = ''; notifyAreaChanged()" :disabled="disabled">
      <option value="">请选择省份</option>
      <option v-for="(item, i) in provinces" :value="item.code" :key="i" :disabled="disabledCodes.indexOf(item.code) > -1">{{item.text}}</option>
    </select>
    <select v-show="level >= 2 && (cityMap[province] || []).length > 0" class='el-input__inner' v-model="city"
            @change=" district = ''; notifyAreaChanged()" :disabled="disabled">
      <option value="">请选择市</option>
      <option v-for="(item, i) in (cityMap[province] || [])" :value="item.code" :key="i" :disabled="disabledCodes.indexOf(item.code) > -1">{{item.text}}</option>
    </select>
    <select v-show="level >= 3 && (districtMap[city] || []).length > 0" class='el-input__inner' v-model="district"
            @change="notifyAreaChanged" :disabled="disabled">
      <option value="">请选择区/县</option>
      <option v-for="(item, i) in (districtMap[city] || [])" :value="item.code" :key="i" :disabled="disabledCodes.indexOf(item.code) > -1">{{item.text}}</option>
    </select>
  </div>
</template>

/**
* @author ： 李银 on 2018年10月10日 09:26:24
*
* 数据结构：
* code: "110000"
* nodes: [{id: 2, text: "市辖区", code: "110100",…}]
* text: "北京市"
* disabled: true/false -- 是否屏蔽选择框
* disabledCodes: Array - 需要灰掉的选项
*
* 入参：
* code:String    - 是否支持默认被选择的数据， 默认为空
* level:Number   - 支持选到第几级1-表示只选择省、2-表示可选择到市、3-表示可选择到区， 默认为3
*
* API:
* getCode        - 获取最终用户选择的区域code
*
* 回调：
* onAreaChanged(code, address)  - 用户修改了选择项触发此回调，code为最终选择的area code, address为最终地址中文信息；
**/
<script>
import { getAreaTree } from '@/config/api/base-api'

export default {
  name: 'geoArea',
  props: {
    code: {
      type: String,
      default: null
    },
    onAreaChanged: {
      type: Function,
      default: null
    },
    level: {
      type: Number,
      default: 3
    },
    disabled: {
      type: Boolean,
      default: false
    },
    disabledCodes: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      province: '',
      city: '',
      district: '',
      provinces: [],
      cityMap: {},
      districtMap: {}
    }
  },
  watch: {
    code: 'load'
  },
  methods: {
    async init () {
      getAreaTree().then((d) => {
        this.provinces = d
        // 获取省级数据
        const provinces = []
        // key = provinceId
        const cityMap = {}
        // key = cityid
        const districtMap = {}
        d.forEach((p) => {
          provinces.push({ code: p.code, text: p.text, disabled: p.disabled })

          cityMap[p.code] = [];
          (p.nodes || []).forEach((c) => {
            cityMap[p.code].push({ code: c.code, text: c.text, disabled: c.disabled })

            districtMap[c.code] = [];
            (c.nodes || []).forEach((d) => {
              districtMap[c.code].push({ code: d.code, text: d.text, disabled: d.disabled })
            })
          })
        })

        this.provinces = provinces
        this.cityMap = cityMap
        this.districtMap = districtMap

        delete d.data
        this.load()
      })
    },
    // 通知选择项改变
    notifyAreaChanged () {
      if (this.onAreaChanged) {
        const code = this.getCode()
        const provinceCode = `${code.substr(0, 2)}0000`
        const cityCode = `${code.substr(0, 4)}00`
        const districtCode = code

        const province = this.provinces.filter(p => p.code === provinceCode).map(p => p.text)[0] || ''
        const city = (this.cityMap[provinceCode] || []).filter(c => c.code === cityCode).map(c => c.text)[0] || ''
        const district = (this.districtMap[cityCode] || []).filter(d => d.code === districtCode).map(d => d.text)[0] || ''

        this.onAreaChanged(code, province + city + district)
      }
    },
    getCode () {
      return this.district || this.city || this.province
    },
    load () {
      if (this.code && this.code.length === 6) {
        const provinceCode = `${this.code.substr(0, 2)}0000`
        const cityCode = `${this.code.substr(0, 4)}00`
        const districtCode = this.code

        // 初始化省份 通过code获取到实际的code；
        const province = this.provinces.filter(p => p.code === provinceCode).map(p => p.code)[0] || ''
        const city = (this.cityMap[province] || []).filter(c => c.code === cityCode).map(c => c.code)[0] || ''
        const district = (this.districtMap[city] || []).filter(d => d.code === districtCode).map(d => d.code)[0] || ''

        if (province !== this.province) {
          this.province = province
        }

        if (city !== this.city) {
          this.city = city
        }

        if (district !== this.district) {
          this.district = district
        }
        return true
      }
      this.province = ''
      this.city = ''
      this.district = ''
      return true
    }
  },
  mounted () {
    this.init()
  }
}
</script>

<style lang='scss' scoped>
  @import 'index';
</style>
