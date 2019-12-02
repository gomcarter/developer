<template>
  <div id="app">
    <v-frame ref="frames" v-if="$route.name != null && $route.meta.menu !== false"/>

    <div class="index_more" v-if="$route.name != null && $route.meta.menu !== false">
      <div class="index_chunk">
        <keep-alive>
          <router-view v-if="$route.meta.keepAlive"></router-view>
        </keep-alive>
        <router-view v-if="!$route.meta.keepAlive"></router-view>
      </div>
    </div>
    <keep-alive v-if="$route.meta.menu === false">
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="$route.meta.menu === false && !$route.meta.keepAlive"></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {
    'v-frame': () => import('@/components/frame')
  },
  updated () {
    if (this.$refs.frames) {
      // this.$refs.frames.selectMenuByRoute()
    }
  }
}
</script>

<style lang="scss">
  @import 'assets/css/mixin.scss';
  @import 'assets/css/common.scss';
</style>
