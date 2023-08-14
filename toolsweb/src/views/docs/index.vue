<template>
  <div style="width: 100%; height: 1400px">
    <WebIframe url="https://tools.fengzhengx.cn/swagger-ui.html" />
  </div>
</template>

<script>

import WebIframe from '@/views/components/Iframe'

export default {
  name: 'DocsSwagger',
  components: {
    WebIframe
  }
}
</script>
