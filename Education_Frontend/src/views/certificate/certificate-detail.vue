/* eslint-disable no-undef */
<template>
  <div class="certificate-detail-container">
    <div class="title">教育证书</div>
    <div class="panel">
      <div id="capture" class="certificate">
        <img
          class="img"
          src="@/assets/certificate.jpg"
          width="496"
          height="700"
        >
        <div class="studentname">{{ studentname }}</div>
        <div class="schoolspecial">{{ schoolspecial }}</div>
        <div class="institute">{{ institute }}</div>
        <div class="sno">{{ sno }}</div>
        <div class="classname">{{ classname }}</div>
        <div class="teachername">{{ teachername }}</div>
        <div class="finalgrade">{{ finalgrade }}</div>
        <div class="block_number">{{ block_number }}</div>
        <div class="blockchainid">{{ blockchainid }}</div>
        <div class="tx_hash">{{ tx_hash }}</div>
        <div class="passtime">{{ passtime }}</div>
        <canvas id="qrcode" ref="canvas" class="canvas" />
      </div>
      <div class="download" @click="downloadImg">下载</div>
    </div>
  </div>
</template>

<script>
import qrCode from 'qrcode';
import { getCertification } from '@/api/certificate';
import html2canvas from 'html2canvas';
import canvas2image from '@/utils/canvas2image';

export default {
  data() {
    return {
      studentname: '',
      schoolspecial: '',
      institute: '',
      sno: '',
      classname: '',
      teachername: '',
      finalgrade: '',
      blockchainid: '',
      block_number: '',
      tx_hash: '',
      tx_url: '',
      passtime: ''
    };
  },
  async mounted() {
    const res = await getCertification({
      classid: this.$route.params.certificateId
    });
    const { data = {}} = res;
    const {
      studentname,
      schoolspecial,
      institute,
      sno,
      classname,
      teachername,
      finalgrade,
      blockchainid,
      block_number,
      tx_hash,
      tx_url,
      passtime
    } = data;
    this.studentname = studentname;
    this.schoolspecial = schoolspecial;
    this.institute = institute;
    this.sno = sno;
    this.classname = classname;
    this.teachername = teachername;
    this.finalgrade = finalgrade;
    this.blockchainid = blockchainid;
    this.block_number = block_number;
    this.tx_hash = tx_hash;
    this.tx_url = tx_url;
    this.passtime = passtime;

    const canvas = this.$refs.canvas;
    qrCode.toCanvas(canvas, this.tx_url);
  },
  methods: {
    downloadImg() {
      const dom = document.querySelector('#capture');
      const width = dom.offsetWidth;
      const height = dom.offsetHeight + 20;
      const scale = window.devicePixelRatio;
      html2canvas(dom, { width, height, scale, useCORS: true, imageTimeout: 2000, y: 150 }).then((canvas) => {
        canvas2image.saveAsPNG(canvas).getAttribute('src');
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.certificate-detail-container {
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
    display: flex;
    align-items: center;
    flex-direction: column;
    margin-bottom: 20px;

    .certificate {
      position: relative;

      .canvas {
        position: absolute;
        left: 204px;
        bottom: 96px;
        width: 88px !important;
        height: 88px !important;
      }

      div {
        position: absolute;
        font-size: 13px;
        word-break: break-all;
      }
      .studentname {
        top: 253px;
        left: 235px;
      }
      .schoolspecial {
        left: 235px;
        top: 275px;
      }
      .institute {
        left: 235px;
        top: 298px;
      }
      .sno {
        left: 235px;
        top: 322px;
      }
      .classname {
        left: 255px;
        top: 344px;
      }
      .teachername {
        left: 255px;
        top: 368px;
      }
      .finalgrade {
        left: 255px;
        top: 392px;
      }
      .block_number {
        left: 235px;
        top: 477px;
        font-size: 12px;
      }
      .blockchainid {
        left: 135px;
        top: 172px;
        width: 225px;
      }
      .tx_hash {
        left: 135px;
        top: 487px;
        width: 224px;
        font-size: 12px;
      }
      .passtime {
        left: 225px;
        bottom: 34px;
      }
    }

    .download {
      margin-top: 50px;
      background: #1746a2;
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 80px;
      height: 30px;
      cursor: pointer;
    }
  }
}
</style>
