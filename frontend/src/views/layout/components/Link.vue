<template>
  <component :is="type" v-bind="linkProps(to)" @click.native="handleClick">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: String,
      required: true
    }
  },
  computed: {
    // 判断是内部链接还是外部链接
    type() {
      if (isExternal(this.to)) {
        return 'a'
      }
      return 'router-link'
    }
  },
  methods: {
    linkProps(url) {
      if (isExternal(url)) {
        return {
          href: url,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        to: url
      }
    },
    handleClick(e) {
      // 阻止特定页面的默认请求行为
      if (this.to === '/student/create' || this.to === '/course-selection/create') {
        // 阻止可能的默认行为，让Vue Router自己处理跳转
        e.preventDefault();
      }
    }
  }
}
</script> 