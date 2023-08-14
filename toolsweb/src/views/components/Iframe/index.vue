<template>
  <div id="show-iframe-div">
    <iframe v-show="iframeState" id="show-iframe" frameborder="0" name="showHere" allowfullscreen="true" allowtransparency="true" :src="url" />
  </div>
</template>

<script>
export default {
  name: 'WebIframe',
  props: {
    url: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      iframeState: false,
      goBackState: false
    }
  },
  mounted() {
    const oIframe = document.getElementById('show-iframe')
    const deviceHeight = document.documentElement.clientHeight
    oIframe.style.width = '100%'
    oIframe.style.height = deviceHeight - 50 + 'px'
    this.iframeState = true
  },
  methods: {
    goBack() {
      this.goBackState = false
      this.iframeState = false
    },
    showIframe() {
      this.goBackState = true
      this.iframeState = true
    }
  }
}

</script>
<style scoped>

</style>
