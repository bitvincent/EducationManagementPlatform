<template>
  <div class="my-certificate-container">
    <div class="title">我的证书</div>
    <div class="panel">
      <div class="certificate">
        <div>学习中</div>
        <div v-if="doingList.length !== 0" class="list">
          <div
            v-for="{ classname, passtime, classid } in doingList"
            :key="classid"
            class="item doing"
          >
            <div class="info-title">{{ classname }}</div>
            <div class="info">
              <div class="time">{{ passtime }}</div>
            </div>
          </div>
        </div>
        <div v-else class="no-data">暂无证书</div>
      </div>
      <div class="certificate">
        <div>已通过</div>
        <div v-if="doneList.length !== 0" class="list">
          <div
            v-for="{ classname, passtime, classid } in doneList"
            :key="classid"
            class="item done"
          >
            <div class="info-title">{{ classname }}</div>
            <div class="info">
              <div class="time">{{ passtime }}</div>
              <div class="detail" @click="goDetail(classid)">查看</div>
            </div>
          </div>
        </div>
        <div v-else class="no-data">暂无证书</div>
      </div>
      <div class="certificate">
        <div>未通过</div>
        <div v-if="failedList.length !== 0" class="list">
          <div
            v-for="{ classname, passtime, classid } in failedList"
            :key="classid"
            class="item failed"
          >
            <div class="info-title">{{ classname }}</div>
            <div class="info">
              <div class="time">{{ passtime }}</div>
            </div>
          </div>
        </div>
        <div v-else class="no-data">暂无证书</div>
      </div>
    </div>
  </div>
</template>

<script>
import { getAllCertification } from '@/api/certificate';
export default {
  data() {
    return {
      doingList: [],
      doneList: [],
      failedList: []
    };
  },
  async mounted() {
    const res = await getAllCertification();
    const { data } = res;
    data.forEach((element) => {
      switch (element.classstatus) {
        case 0:
          this.doingList.push(element);
          break;
        case 1:
          this.doneList.push(element);
          break;
        case 2:
          this.failedList.push(element);
          break;
        default:
          break;
      }
    });
  },
  methods: {
    goDetail(id) {
      this.$router.push(`certificate-detail/${id}`);
    }
  }
};
</script>

<style lang="scss" scoped>
.my-certificate-container {
  width: 1200px;
  margin: 0 auto;

  .title {
    margin: 20px 0;
    font-size: 18px;
  }

  .panel {
    min-height: calc(100vh - 60px - 80px - 20px);
    background: #fff;
    padding: 20px 50px;

    .certificate {
      margin: 10px 0;
      font-size: 22px;

      .list {
        display: flex;
        flex-wrap: wrap;
        .item {
          width: 300px;
          height: 180px;
          border-radius: 12px;
          background: #f2f2f2;
          overflow: hidden;
          margin-top: 20px;
          margin-right: 20px;

          .info-title {
            height: 120px;
            display: flex;
            justify-content: center;
            align-items: center;
            color: #fff;
            background: #a0a0a0;
            font-size: 26px;
          }

          .info {
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 60px;
            margin: 0 20px;

            .time {
              font-size: 14px;
              color: #636363;
            }
            .detail {
              font-size: 16px;
              color: #fff;
              background: #17479e;
              width: 56px;
              height: 28px;
              display: flex;
              align-items: center;
              justify-content: center;
              cursor: pointer;
            }
          }
        }
        .done {
          .info-title {
            background: #70ae34;
          }
        }
        .failed {
          .info-title {
            background: #e9675f;
          }
        }
      }

      .no-data {
        margin: 20px;
        font-size: 18px;
        height: 200px;
        display: flex;
        align-items: center;
        color: #ccc;
      }
    }
  }
}
</style>
