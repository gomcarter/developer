webpackJsonp([5],{"1Xk0":function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n=a("3cXf"),i=a.n(n),r=a("aA9S"),l=a.n(r),o=a("0Njg"),s={data:function(){return{title:"项目修改",args:[],data:{name:null,prefix:null,mark:null},header:[],config:{}}},methods:{init:function(){var e=this;this.$route.params.id&&Object(o.F)(this.$route.params.id).then(function(t){l()(e.data,t),e.config=t.config?JSON.parse(t.config):{},console.log(JSON.parse(t.header)),t.header&&(e.header=JSON.parse(t.header)),console.log(e.header)}).catch(function(e){console.log(e)}),this.title=this.$route.params.id?"修改前台项目":"添加前台项目"},add:function(){var e=this;this.$refs.edit.validate(function(t){t&&e.$confirm("确定保存？","提示",{type:"info"}).then(function(){e.data.config=i()(e.config),e.data.header=i()(e.header),e.$route.params.id?Object(o._18)(e.$route.params.id,e.data).then(function(t){e.$transfer({back:"返回编辑",buttons:[{text:"去列表",link:"/consumer/list"}]})}).catch(function(e){console.log(e)}):Object(o.b)(e.data).then(function(t){e.$transfer({back:"继续添加",buttons:[{text:"去列表",link:"/consumer/list"}]})}).catch(function(e){console.log(e)})})})},edit:function(){this.$refs.interfacesSelector.open(this.config)},saveNode:function(e){this.$set(this,"config",e)},addHeader:function(){this.header.push({key:"",value:""}),console.log(this.header)},delHeader:function(e){this.header.splice(e,1)}},mounted:function(){this.init()},components:{"v-interfaces-selector":function(){return a.e(36).then(a.bind(null,"vTTj"))}}},c={render:function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("h4",{staticClass:"title"},[e._v(e._s(e.title))]),e._v(" "),a("hr"),e._v(" "),a("el-form",{ref:"edit",attrs:{model:e.data,"label-width":"8em"}},[a("el-form-item",{staticClass:"min_width",attrs:{label:"项目名称:",rules:[{required:!0,message:"请输入项目名称",trigger:["blur","change"]}],prop:"name"}},[a("el-input",{attrs:{placeholder:"输入项目名称"},model:{value:e.data.name,callback:function(t){e.$set(e.data,"name",t)},expression:"data.name"}})],1),e._v(" "),a("el-form-item",{staticClass:"min_width",attrs:{label:"项目前缀:",rules:[{required:!0,message:"请输入项目前缀",trigger:["blur","change"]}],prop:"prefix"}},[a("el-input",{attrs:{placeholder:"输入项目前缀，给不同的前台统一一个接口的前缀"},model:{value:e.data.prefix,callback:function(t){e.$set(e.data,"prefix",t)},expression:"data.prefix"}}),e._v(" "),a("div",{staticClass:"mark"},[e._v("示例：/end1/order/{id}中end1定义为某个前台系统，所有这个前台系统的接口都以end1开头")])],1),e._v(" "),a("el-form-item",{attrs:{label:"鉴权Headers:"}},[a("el-button",{attrs:{type:"primary",icon:"el-icon-plus",circle:"",size:"small"},on:{click:function(t){return e.addHeader()}}})],1),e._v(" "),e._l(e.header,function(t,n){return a("el-form-item",{key:n},[a("el-input",{staticClass:"param-key",attrs:{placeholder:"请输入header名"},model:{value:t.key,callback:function(a){e.$set(t,"key",a)},expression:"h.key"}}),e._v(" "),a("span",[e._v("=")]),e._v(" "),a("el-input",{staticClass:"param-value",attrs:{placeholder:"header的默认值，如果没有则不填写"},model:{value:t.value,callback:function(a){e.$set(t,"value",a)},expression:"h.value"}}),e._v(" "),a("el-button",{attrs:{type:"danger",icon:"el-icon-delete",circle:"",size:"small"},on:{click:function(t){return e.delHeader(n)}}})],1)}),e._v(" "),a("el-form-item",{attrs:{label:"鉴权接口设置:"}},[e.config.interfaceId?a("span",[e._v(e._s(e.config.interfaceName))]):a("span",[e._v("未设置")]),e._v(" "),a("el-button",{attrs:{type:"primary",icon:"el-icon-setting",circle:"",size:"small"},on:{click:e.edit}}),e._v(" "),a("div",{staticClass:"mark"},[e._v("设置之后，系统在单接口测试且需要鉴权时自动完成鉴权")])],1),e._v(" "),a("el-form-item",{staticClass:"min_width",attrs:{label:"备注:"}},[a("el-input",{attrs:{type:"textarea",rows:"5",placeholder:"示例：\n1，调用此接口必须在header中加入：platform=airmall\n2，必须登录，且在cookie或者header中传airmallLoginToken=登录获取到的token"},model:{value:e.data.mark,callback:function(t){e.$set(e.data,"mark",t)},expression:"data.mark"}})],1),e._v(" "),a("el-form-item",[a("el-button",{attrs:{type:"primary",icon:"el-icon-success"},on:{click:e.add}},[e._v("提交")]),e._v(" "),a("el-button",{attrs:{type:"info",icon:"el-icon-back"},on:{click:function(t){return e.$router.go(-1)}}},[e._v("返回")])],1)],2),e._v(" "),a("v-interfaces-selector",{ref:"interfacesSelector",attrs:{"on-ok":e.saveNode}})],1)},staticRenderFns:[]};var d=a("C7Lr")(s,c,!1,function(e){a("kZyu")},"data-v-2b6c9890",null);t.default=d.exports},kZyu:function(e,t){}});
//# sourceMappingURL=5.10fa64bc04f4ee384a68.js.map