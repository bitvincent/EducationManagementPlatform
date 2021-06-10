<template>
  <div class="publish-certificate-container">
    <div class="title">发布证书</div>
    <div class="panel">
      <el-table
        v-if="tableCol.length !== 0"
        :data="tableData"
        style="width: 100%"
        max-height="250"
      >
        <el-table-column
          fixed
          prop="studentname"
          label="学生姓名"
          width="120"
        />
        <el-table-column
          v-for="(grade, index) in tableCol[0].grade"
          :key="grade"
          :prop="`grade-${index}`"
          :label="`第${Number(index) + 1}成绩`"
        >
          <template slot-scope="aver">
            <div>
              {{ aver.row[`grade-${index}`] || "/" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          prop="average"
          label="最终成绩"
          width="100"
        >
          <template slot-scope="aver">
            <el-input
              v-if="
                aver.row.finalgrade === null ||
                  aver.row.finalgrade === undefined
              "
              v-model="aver.row.average"
              class="aver-grade"
            >{{ aver.row.average || 0 }}</el-input>
            <div v-else class="aver-grade">{{ aver.row.finalgrade }}</div>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="是否发放证书" width="150">
          <template slot-scope="scope">
            <div
              v-if="
                scope.row.finalgrade === null ||
                  scope.row.finalgrade === undefined
              "
              class="operate"
            >
              <div class="confirm-button" @click="offer(scope.row, 0)">是</div>
              <div class="cancel-button" @click="offer(scope.row, 1)">否</div>
            </div>
            <div v-else-if="scope.row.pass === 1">未通过</div>
            <div v-else>已发布证书</div>
          </template>
        </el-table-column>
      </el-table>
      <div v-else class="no-data">暂无学生成绩</div>
    </div>
  </div>
</template>
<script>
import { getAllGrade, setFinalGrade } from '@/api/certificate';
export default {
  data() {
    return {
      tableData: [],
      tableCol: []
    };
  },
  computed: {},
  async mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.tableData = [];
      const res = await getAllGrade({ classid: this.$route.params.id });
      const { data = [] } = res;
      if (data.length === 0) {
        return;
      }
      this.tableData = data.map((item) => {
        item.grade.forEach((grade, index) => {
          item[`grade-${index}`] = grade;
        });
        item.average =
          Math.floor(
            (item.grade.reduce((acc, val) => acc + val, 0) /
              item.grade.length) *
              100
          ) / 100;
        return item;
      });
      this.tableCol = data.sort((a, b) => b.grade.length - a.grade.length);
    },
    async offer(row, pass) {
      try {
        const res = await setFinalGrade({
          classid: this.$route.params.id,
          finalgrade: row.average || 0,
          pass,
          studentid: row.studentid
        });
        const { data } = res;
        data.status === 0 &&
          this.$message({
            message: '打分成功',
            type: 'success'
          });
      } catch (e) {
        console.log(e);
      }
      await this.init();
    }
  }
};
</script>
<style lang="scss" scoped>
.publish-certificate-container {
  width: 1200px;
  margin: 0 auto;
  .title {
    margin: 20px 0;
    font-size: 18px;
  }
  .panel {
    min-height: calc(100vh - 60px - 80px - 20px);
    background: #fff;

    .aver-grade {
      border: 1px solid #ccc;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .operate {
      display: flex;
      align-items: center;

      .confirm-button {
        width: 50px;
        height: 20px;
        background: #1746a2;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        margin-right: 20px;
        cursor: pointer;
      }
      .cancel-button {
        width: 50px;
        height: 20px;
        background: #e8554e;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #fff;
        cursor: pointer;
      }
    }

    .no-data {
      text-align: center;
      padding: 20px;
    }
  }
}
</style>
