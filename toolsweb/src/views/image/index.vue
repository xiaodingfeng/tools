<template>
  <div class="app-container">
    <div class="image-search-title">
      <el-row>
        <el-col :span="12">
          <div class="image-search-from">
            <el-form :inline="true" :model="queryForm" class="demo-form-inline">
              <el-form-item label="分类">
                <el-select v-model="queryForm.category" filterable clearable placeholder="请选择">
                  <el-option
                    v-for="item in categoryOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
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
          <div class="image-search-pagination">
            <el-pagination
              :current-page="queryForm.pageNum"
              :page-sizes="[20, 50, 100, 400]"
              :page-size="queryForm.pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData.total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="image-opera">
      <el-button size="small" type="info" @click="addImageDialog = true">添加图片</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="tableData.records"
      element-loading-text="Loading"
      border
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
      <el-table-column align="center" label="序号" width="50">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center">
        <template slot-scope="scope">
          {{ scope.row.category }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文件名" align="center">
        <template slot-scope="scope">
          {{ scope.row.realFileName }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="createTime" label="创建时间">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="url" align="center">
        <template slot-scope="scope">
          <el-link type="primary" :href="scope.row.url" target="_blank">{{ scope.row.url }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="预览" align="center">
        <template slot-scope="scope">
          <div style="min-width: 50px;max-height: 100px">
            <el-image
              fit="scale-down"
              placeholder="加载中"
              :preview-src-list="new Array(scope.row.url)"
              :src="scope.row.url"
              lazy
            />
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="提示"
      :visible.sync="addImageDialog"
      top="8vh"
      width="500px"
      center
    >
      <div class="image-add-from">
        <el-form label-position="left" :model="addRuleForm" label-width="80px">
          <el-form-item label="分类">
            <el-select
              v-model="addRuleForm.category"
              filterable
              allow-create
              default-first-option
              placeholder="请选择分类"
            >
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input
              v-model="addRuleForm.description"
              type="textarea"
              autosize
              clearable
              placeholder="描述"
            />
          </el-form-item>
          <el-form-item label="文件">
            <el-upload
              ref="upload"
              :data="addRuleForm"
              drag
              accept=".jpg,.jpeg,.gif,.png"
              :auto-upload="false"
              :on-success="uploadSuccess"
              :multiple="true"
              :action="uploadUrl"
            >
              <i class="el-icon-upload" />
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <el-button size="small" @click="addImageDialog = false">关闭</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="updateImageDialog"
      top="8vh"
      width="500px"
      center
    >
      <div class="image-add-from">
        <el-form label-position="left" :model="addRuleForm" label-width="80px">
          <el-form-item label="分类">
            <el-select
              v-model="addRuleForm.category"
              filterable
              allow-create
              default-first-option
              placeholder="请选择分类"
            >
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input
              v-model="addRuleForm.description"
              type="textarea"
              autosize
              clearable
              placeholder="描述"
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
import { category, deleteImage, getList, imageDetail, updateImage } from '@/api/images'
import { error, success } from '@/utils/message'

export default {
  data() {
    return {
      queryForm: {
        pageNum: 1,
        pageSize: 20,
        keywords: '',
        category: ''
      },
      categoryOptions: [],
      tableData: {
        records: [],
        total: 0
      },
      uploadUrl: process.env.VUE_APP_BASE_API + '/images/upload',
      addRuleForm: {
        description: '',
        category: ''
      },
      listLoading: true,
      addImageDialog: false,
      updateImageDialog: false
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleEditSubmit() {
      updateImage(this.addRuleForm)
      this.updateImageDialog = false
      this.addRuleForm = {
        description: '',
        category: ''
      }
      success('修改成功')
      this.fetchData()
    },
    handleEdit(index, row) {
      this.addRuleForm.id = row.id
      imageDetail(row.id).then(response => {
        if (!response.data) {
          error('数据不存在')
          return
        }
        this.addRuleForm = response.data
        this.updateImageDialog = true
      })
    },
    handleDelete(index, row) {
      deleteImage({ id: row.id })
      success('删除成功')
      this.fetchData()
    },
    submitUpload() {
      // this.$refs.upload.action = process.env.VUE_APP_BASE_API + 'images/upload'
      this.$refs.upload.submit()
    },
    uploadSuccess(response, file, fileList) {
      if (response.code !== 200) {
        error(response.msg)
        return
      }
      success('上传成功')
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
      this.initOptions()
      this.listLoading = true
      getList(this.queryForm).then(response => {
        this.tableData.records = response.data.records
        this.tableData.total = response.data.total * 1
        this.listLoading = false
      })
    },
    initOptions() {
      category().then(response => {
        this.categoryOptions = response.data.map(item => {
          const v = {}
          v.label = item
          v.value = item
          return v
        })
      })
    }
  }
}
</script>
