<template>
  <component :is="type" v-bind="linkProps(to)">
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
    }
  }
}
</script> 