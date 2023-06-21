<template>
  <div class="login-container"
       v-bind:style="{backgroundImage:'url(' + bg + ')',
        backgroundRepeat:'no-repeat',
        backgroundSize:'100% 100%'}"
       style="padding: 10px" >
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增图书</el-button>
<!--      <el-button type="primary">导入</el-button>-->
<!--      <el-button type="primary">导出</el-button>-->
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入图书名" style = "width: 20%" clearable/>
      <el-button type="primary" style = "margin-left: 5px" @click="load">查询</el-button>
    </div>
    <div class="table" style="margin-top:20px;">
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column label="图片">
        <template #default = "scope">
          <el-image
            style="width: 100px; height: 90px;"
            :src="scope.row.picture"
            :preview-src-list="[scope.row.picture]">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="书名"/>
      <el-table-column prop="kind" label="分类"/>
      <el-table-column prop="author" label="作者"/>
      <el-table-column prop="press" label="出版社"/>
      <el-table-column prop="introductory" label="图书简介"/>
      <el-table-column prop="totalcount" label="总数量"/>
      <el-table-column prop="surpluscount" label="剩余数量"/>
      <el-table-column prop="weedingcount" label="剔旧数量"/>

      <el-table-column label="操作" >
        <template #default = "scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑图书</el-button>

          <el-popconfirm title="确认删除吗" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除图书</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    </div>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="HandleSizeChange"
          @current-change="HandleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 15]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog v-model="dialogVisible" title="新增和修改">
        <el-form :model="form" label-width="120px">
          <el-form-item label="书名">
            <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="分类">
            <el-radio-group v-model="form.kind">
              <el-radio label="文学类">文学类</el-radio>
              <el-radio label="历史类">历史类</el-radio>
              <el-radio label="科研类">科研类</el-radio>
              <el-radio label="艺术类">艺术类</el-radio>
              <el-radio label="小说类">小说类</el-radio>
              <el-radio label="军事类">军事类</el-radio>
              <el-radio label="哲学类">哲学类</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="作者">
            <el-input v-model="form.author" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="出版社">
            <el-input v-model="form.press" style="width: 80%"/>
          </el-form-item>

          <el-form-item label="图书简介">
            <el-input type="textarea" v-model="form.introductory" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="总数量">
            <el-input v-model="form.totalcount" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="剩余数量">
            <el-input v-model="form.surpluscount" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="剔旧数量">
            <el-input v-model="form.weedingcount" style="width: 80%"/>
          </el-form-item>

          <el-form-item label="入库时间">
            <el-date-picker v-model="form.createtime" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable>
            </el-date-picker>
          </el-form-item>
          <el-form-item label="图片">
            <el-upload ref="upload"
                action="http://localhost:9090/files/upload" :on-success="filesUploadSuccess">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>

        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
        </template>
      </el-dialog>
    </div>
  </div>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Book',
  components: {},
  data() {
    return {
      form:{},
      dialogVisible:false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total:0,
      tableData: [],
      bg: require('./1_book.png')     //背景
    }
  },
  created() {
    this.load()
  },
  methods: {
    filesUploadSuccess(res){
      console.log(res)
      this.form.picture=res.data
    },
    load(){
      request.get("/book",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search: this.search,
        }
      }).then(res => {
        console.log(res)
        this.tableData=res.data.records;
        this.total=res.data.total;
      })
    },
    save(){
      if(this.form.id){  //更新
        request.put("/book",this.form).then(res => {
          console.log(res)
          if(res.code === '200'){
            this.$message({
              type: "success",
              message: "更新成功",
            })
          }else {
            this.$message({
              type: "error",
              message: "更新失败",
            })
          }
          this.load()//刷新
          this.dialogVisible =false  //关闭弹窗
        })
      }else {
        //新增
        request.post("/book",this.form).then(res => {
          console.log(res)
          if(res.code === '200'){
            this.$message({
              type: "success",
              message: "更新成功",
            })
          }else {
            this.$message({
              type: "error",
              message: "更新失败",
            })
          }
          this.load()//刷新
          this.dialogVisible =false  //关闭弹窗
        })
      }
    },
    add(){
      this.dialogVisible=true
      this.form={}
      this.$nextTick(() => {
        // this.$refs['upload'].clearFiles()  //清除上传文件历史
        if (this.$refs.upload) {
          this.$refs.upload.clearFiles()
        }
      })
    },

    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible=true
      this.$nextTick(() => {
        // this.$refs['upload'].clearFiles()  //清除上传文件历史
        if (this.$refs.upload) {
          this.$refs.upload.clearFiles()
        }
      })
    },
    handleDelete(id){
      console.log(id)
      request.delete("/book/"+id).then(res => {
        if(res.code === '200'){
          this.$message({
            type: "success",
            message: "删除成功",
          })
        }else {
          this.$message({
            type: "error",
            message: "删除失败",
          })
        }
        this.load()//刷新
      })
    },
    HandleSizeChange(pageSize){  //改变页码
      this.pageSize=pageSize
      this.load()
    },
    HandleCurrentChange(pageNum){  //该表当前页触发
      this.currentPage =pageNum
      this.load()
    },
  }
}
</script>
