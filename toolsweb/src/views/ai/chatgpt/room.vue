<template>
  <div class="app-container">
    <div class="chat-search-title">
      <el-row>
        <el-col :span="12">
          <div class="chat-search-from">
            <el-form :inline="true" :model="queryForm" class="demo-form-inline">
              <el-form-item label="关键词">
                <el-input v-model="queryForm.keywords" clearable placeholder="关键词" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="fetchData">查询</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chat-search-pagination">
            <el-pagination
              :current-page="queryForm.pageNum"
              :page-size="queryForm.pageSize"
              :page-sizes="[10, 20, 50]"
              :total="tableData.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <el-table
      v-loading="listLoading"
      :data="tableData.records"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" prop="id" />
      <el-table-column align="center" label="名称" prop="title" />
      <el-table-column align="center" label="描述" prop="description" />
      <el-table-column align="center" label="api类型" prop="apiType" />
      <el-table-column align="center" label="会话ID" prop="conversationId" />
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="创建时间" prop="createTime">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { error, success } from '@/utils/message'
import { get, page } from '@/api/chat/room'

export default {
  data() {
    return {
      queryForm: {
        pageNum: 1,
        pageSize: 10,
        keywords: ''
      },
      tableData: {
        records: [],
        total: 0
      },
      listLoading: true
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      page(this.queryForm).then(response => {
        this.tableData.records = response.data.records
        this.tableData.total = response.data.total * 1
        this.listLoading = false
      })
    }
  }
}
</script>
