<template>
  <div class="app-container">
    <h2>教育过程</h2>
    <div style="background: white;padding: 30px 30px 200px 30px">
      <el-table id="table" :header-cell-style="{'background-color':'#5893f9','color':'white'}" :data="tableData" row-key="homeworknumber" :expand-row-keys="expand">
        <el-table-column align="center" label="作业次数" prop="homeworknumber" />
        <el-table-column align="center" label="作业题目" prop="homeworkname" />
        <el-table-column align="center" label="上传时间" prop="uploadtime" />
        <el-table-column align="center" label="批改时间" prop="marktime" />
        <el-table-column align="center" label="成绩" prop="grade" />
        <el-table-column width="100" type="expand" label="查看详情" exp>
          <template slot-scope="{row}">
            <div style="margin-left: 200px">
              <el-form label-position="left">
                <el-form-item label="交易哈希">
                  <span>{{ row.tx_hash }}</span>
                </el-form-item>
                <el-form-item label="块哈希">
                  <span>{{ row.block_hash }}</span>
                </el-form-item>
                <el-form-item label="链上二维码">
                  <img :src="genQrCode(row.tx_hash)" style="margin-left: 100px">
                </el-form-item>
              </el-form>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: center">
        <el-button type="primary" style="width: 100px;margin-top: 20px" @click="exportExcel">导出</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getLearnProcess
} from '@/api/homework';
import { export_json_to_excel } from '@/vendor/Export2Excel';

export default {
  name: 'Student',
  props: {
    id: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.getData()
  },
  methods: {
    async getData() {
      const res = await getLearnProcess(this.id)
      this.tableData = res.data
      this.tableData.sort((a, b) => a.homeworknumber - b.homeworknumber)
      this.tableData.forEach(t => { t.url = 'http://8.131.96.156:8081/transaction/' + t.tx_hash })
    },
    genQrCode(src) {
      const jrQrcode = require('jr-qrcode')
      const url = `http://8.131.96.156:8081/transaction/${src}`
      console.log(url)
      return jrQrcode.getQrBase64(url, { padding: 5, width: 200, height: 200, correctLevel: 0 })
    },
    exportExcel() {
      const tHeader = ['作业次数', '作业题目', '上传时间', '批改时间', '成绩', '交易哈希', '块哈希', '交易链接'];
      const filterVal = ['homeworknumber', 'homeworkname', 'uploadtime', 'marktime', 'grade', 'tx_hash', 'block_hash', 'url'];
      const filename = '教育过程';
      const data = this.formatJson(filterVal, this.tableData);
      export_json_to_excel({
        header: tHeader,
        data,
        filename
      })
    },
    formatJson(filterVal, tableData) {
      return tableData.map(v => {
        return filterVal.map(j => {
          return v[j]
        })
      }
      )
    }

  }
}
</script>

<style scoped>

</style>
