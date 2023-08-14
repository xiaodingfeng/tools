<template>
  <div class="app-container">
    <div class="image-preview-div" style="overflow: auto">
      <el-card v-for="(item, index) in tableData.records" :key="index" :body-style="{ padding: '0px' }">
        <el-image :preview-src-list="filterPreviewList()" :src="item.url" class="image" />
        <div style="padding: 14px;">
          <span>{{ item.description + '-' + item.category }}</span>
          <div class="bottom clearfix">
            <time class="time">{{ item.createTime }}</time>
            <el-link class="image-download-link" :href="item.url">下载</el-link>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getList } from '@/api/images'

export default {
  data() {
    return {
      queryForm: {
        page: 1,
        pageSize: 999999
      },
      noMore: false,
      tableData: {
        records: [],
        total: 0
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    load() {
      this.queryForm.page += 1
      this.fetchData()
    },
    filterPreviewList() {
      return this.tableData.records.map(item => {
        return item.url
      })
    },
    fetchData() {
      getList(this.queryForm).then(response => {
        if (!response.data.records) {
          this.noMore = true
        } else {
          this.tableData.records = response.data.records
        }
      })
    }
  }
}
</script>
<style>
.image-preview-div {
  display: flex;
  flex-direction: row;
  align-content: center;
  flex-wrap: wrap;
  align-items: flex-start;
  justify-content: space-around;
}
.image-preview-div .el-card {
  width: 350px;
  margin: 15px;
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.image-download-link {
  padding: 0;
  float: right;
}

.el-image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
