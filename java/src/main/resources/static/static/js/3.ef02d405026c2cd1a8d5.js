webpackJsonp([3],{"1BRe":function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=r("3cXf"),s=r.n(a),n=r("0Njg"),i=r("R2SV"),l={data:function(){return{width:1e3,height:800,title:"新增用例",disabled:!1,functionListApi:n.p,id:this.$route.params.id,packageId:null,form:{name:null,workflow:null},presetParams:[]}},computed:{},methods:{addPresetParams:function(){this.presetParams.push({key:"",value:"",functionId:"",fix:!0})},delPresetParams:function(e){this.presetParams.splice(e,1)},save:function(){var e=this;this.$refs.edit.validate(function(t){if(t){var r=e.$refs.workflow.validate();r.result?e.$confirm("确定保存？","提示",{type:"info"}).then(function(){e.disabled=!0,e.id?Object(n.T)(e.id,e.generateParams()).then(function(t){e.$transfer({back:"再次编辑",buttons:[{text:"去列表",link:"/flow/testCase"}]})}).catch(function(){e.disabled=!1}):Object(n.h)(e.generateParams()).then(function(t){e.$transfer({back:"继续添加",buttons:[{text:"去列表",link:"/flow/testCase"}]})}).catch(function(){e.disabled=!1})}):e.$message({message:r.message,type:"warning"})}else e.$message({message:"请输入用例名称",type:"warning"})})},generateParams:function(){return{workflow:s()(this.$refs.workflow.workflow()),name:this.form.name,presetParams:s()(this.presetParams),packageId:this.packageId}},test:function(){var e=this,t=this.$refs.workflow.validate();if(t.result){this.$refs.runnerDialog.open();var r={workflow:this.$refs.workflow.workflow(),presetParams:this.presetParams};setTimeout(function(){return e.$refs.runner.setData(r)},500)}else this.$message({message:t.message,type:"warning"})},runTest:function(){this.$refs.runner.run()},beautify:function(){this.$refs.workflow.beautify()}},components:{"v-runner":function(){return r.e(31).then(r.bind(null,"V7zm"))},"v-dialog":function(){return r.e(27).then(r.bind(null,"j9Pd"))},"v-selector":function(){return r.e(29).then(r.bind(null,"qNHp"))},"v-workflow":function(){return r.e(32).then(r.bind(null,"2fbX"))}},mounted:function(){var e=this,t=Object(i.g)().packageId;t?(this.disabled=!0,this.packageId=t,Object(n.w)(t).then(function(t){e.form.name=t.name,Object(n.C)({rows:1e3,idList:t.interfacesIdList}).then(function(t){if(t&&t.length>0){for(var r=[],a=e.width/2-200,s=0;s<t.length;++s){var n=t[s],i=n.name;i.length>16?i=i.substr(0,8)+"\r\n"+i.substr(8,7)+"...":i.length>8&&(i=i.substr(0,8)+"\r\n"+i.substr(8,i.length));var l={interfaceId:n.id,interfaceName:i,history:!1,hash:n.hash,headers:[],javascript:null,java:n.java,url:n.url,sleep:null,returns:JSON.parse(n.returns),method:n.method,parameters:JSON.parse(n.parameters)},o="g"+s,c={id:o,shape:"rect",label:"（"+o+"）"+i,x:a+s%3*190,y:100*(parseInt(s/3)+1),data:l};r.push(c)}e.form.workflow={nodes:r}}else e.form.workflow={};e.disabled=!1})})):this.id&&(this.title="修改用例",Object(n.x)(this.id).then(function(t){e.form.name=t.name,e.form.workflow=JSON.parse(t.workflow),e.presetParams=JSON.parse(t.presetParams)}).catch(function(e){console.log(e)}))}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",[r("h4",{staticClass:"title"},[e._v(e._s(e.title))]),e._v(" "),r("hr"),e._v(" "),r("el-form",{ref:"edit",attrs:{model:e.form,"label-width":"8em"}},[r("el-form-item",{attrs:{label:"用例名称:",prop:"name",required:"",rules:[{required:!0,message:"请输入用例名称",trigger:["blur","change"]}]}},[r("el-input",{attrs:{placeholder:"请输入用例名称"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),e._v(" "),r("el-form-item",{attrs:{label:"预置参数:"}},[r("el-button",{attrs:{type:"primary",icon:"el-icon-plus",circle:"",size:"small"},on:{click:function(t){return e.addPresetParams()}}}),e._v("\n      在后续脚本中使用，如设置了参数名为：name=value，使用 $name 即可获得此参数对应的值value，不要重复命名\n      "),e.presetParams&&e.presetParams.length>0?r("el-form",e._l(e.presetParams,function(t,a){return r("el-form-item",{key:a},[r("el-input",{staticClass:"preset_params_key",attrs:{placeholder:"请输入预置参数名"},model:{value:t.key,callback:function(r){e.$set(t,"key",r)},expression:"preset.key"}},[r("template",{slot:"prepend"},[r("span",{staticClass:"table_title"},[e._v("参数名")])])],2),e._v(" "),r("div",{staticClass:"el-input-group__prepend preset_params_value_title"},[r("span",[e._v("参数值")])]),e._v(" "),t.fix?r("el-input",{staticClass:"preset_params_value",attrs:{placeholder:"请输入一个固定值"},model:{value:t.value,callback:function(r){e.$set(t,"value",r)},expression:"preset.value"}}):r("v-selector",{staticClass:"preset_params_value",attrs:{id:"id",text:"name",onSelectionChanged:function(e){return t.functionId=(e[0]||{}).id},filterable:!0,remote:!0,placeholder:"请输选择一个脚本，输入名称进行搜索",load:[t.functionId],url:e.functionListApi}}),e._v(" "),r("el-checkbox",{model:{value:t.fix,callback:function(r){e.$set(t,"fix",r)},expression:"preset.fix"}},[e._v("固定值")]),e._v(" "),r("el-button",{attrs:{type:"danger",icon:"el-icon-delete",size:"small",circle:""},on:{click:function(t){return e.delPresetParams(a)}}})],1)}),1):e._e()],1),e._v(" "),r("el-form-item",{attrs:{label:"配置流程:","label-width":"8em"}},[r("div",{staticClass:"container",style:{width:e.width+2+"px"}},[e.form.workflow?r("v-workflow",{ref:"workflow",attrs:{"data-list":e.form.workflow,width:e.width,height:e.height}}):e._e()],1)]),e._v(" "),r("el-form-item",[r("el-button",{attrs:{type:"info",icon:"el-icon-back"},on:{click:function(t){return e.$router.go(-1)}}},[e._v("返回")]),e._v(" "),r("el-button",{attrs:{type:"primary",icon:e.disabled?"el-icon-loading":"el-icon-success",disabled:e.disabled},on:{click:e.save}},[e._v("提交")]),e._v(" "),r("el-button",{attrs:{type:"success",icon:"el-icon-s-grid"},on:{click:e.beautify}},[e._v("美化")]),e._v(" "),r("el-button",{attrs:{type:"success",icon:"el-icon-magic-stick"},on:{click:e.test}},[e._v("测试")])],1)],1),e._v(" "),r("v-dialog",{ref:"runnerDialog",attrs:{title:"运行用例","ok-text":"开始执行","cancel-text":"关闭",ok:e.runTest,width:1200}},[r("div",{attrs:{slot:"body"},slot:"body"},[r("v-runner",{ref:"runner",attrs:{width:1100,height:518}})],1)])],1)},staticRenderFns:[]};var c=r("C7Lr")(l,o,!1,function(e){r("T7hM")},"data-v-64aed249",null);t.default=c.exports},T7hM:function(e,t){}});
//# sourceMappingURL=3.ef02d405026c2cd1a8d5.js.map