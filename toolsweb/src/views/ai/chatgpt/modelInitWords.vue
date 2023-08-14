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
    <div class="chat-opera">
      <el-button size="small" type="info" @click="updateDataDialog = true">添加</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="tableData.records"
      border
      element-loading-text="Loading"
      fit
      highlight-current-row
    >
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)"
          >编辑
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="ID" prop="id" />
      <el-table-column align="center" label="模型ID" prop="modelId" />
      <el-table-column align="center" label="prompts" prop="prompts" />

      <el-table-column align="center" label="创建时间" prop="createTime">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :visible.sync="updateDataDialog"
      center
      title="提示"
      top="8vh"
      width="500px"
      @close="updateDataDialogClose"
    >
      <div class="chat-add-from">
        <el-form :model="addRuleForm" label-position="left" label-width="80px">
          <el-form-item v-for="(item, index) in addRuleFormConfig.field" :key="index" :label="item.label">
            <el-input
              v-if="item.type === 'input'"
              v-model="addRuleForm[item.prop]"
              clearable
            />
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="handleEditSubmit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { error, success } from '@/utils/message'
import { remove, get, page, update, add } from '@/api/chat/modelInitWords'

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
      addRuleFormConfig: {
        field: [
          {
            type: 'input',
            label: '模型ID',
            prop: 'modelId'
          },
          {
            type: 'input',
            label: 'prompts',
            prop: 'prompts'
          }
        ]
      },
      addRuleForm: {
      },
      listLoading: true,
      updateDataDialog: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    updateDataDialogClose() {
      this.addRuleForm = {
      }
    },
    handleEditSubmit() {
      if (this.addRuleForm.id) {
        update(this.addRuleForm)
      } else {
        add(this.addRuleForm)
      }
      this.updateDataDialog = false
      this.addRuleForm = {
      }
      success('修改成功')
      this.fetchData()
    },
    handleEdit(index, row) {
      this.addRuleForm.id = row.id
      get({ id: row.id }).then(response => {
        if (!response.data) {
          error('数据不存在')
          return
        }
        this.addRuleForm = response.data
        this.updateDataDialog = true
      })
    },
    handleDelete(index, row) {
      remove({ id: row.id })
      success('删除成功')
      this.fetchData()
    },
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
