<template>
  <div class="field">
    <div class="top-container px-5 py-5">
      <div class="profile-logo">
        <div>
          <h2>음성변환</h2>
        </div>
        <div>
          <h2 @click="$router.push('/Home')">PAGO BOOKS</h2>
        </div>
        <div @click="loginModal = true">
          <img src="@/assets/weblogin1.png" alt="profile-logo" />
        </div>
      </div>
      <!--profile-logo-end-->
    </div>
    <!--top-container-end-->

    <div class="stt-bottom-container px-5 pt-5 pb-2">
      <div class="stt-ts-container">
        <div class="ts-output-cont">
          <div>
            <textarea
              class="stt-ts-box"
              placeholder="변환할 내용을 입력하세요"
            ></textarea>
          </div>
          <div style="display: flex">
            <div class="stt-ts-lg-ch mt-3">
              <select name="ts-lg" id="ts-lg">
                <option value="kr">한국어</option>
                <option value="en">영어</option>
                <option value="jp">일본어</option>
                <option value="cn">중국어</option>
                <option value="gm">독일어</option>
                <option value="sp">스페인어</option>
              </select>
            </div>
            <div class="stt-trans-btn mt-3">
              <button @click="tts">음성으로 변환하기</button>
            </div>
          </div>
        </div>
        <!--ts-output-cont-end-->

        <div class="stt-ts-input-cont">
          <div class="stt-ts-box">
            <img :src="image" alt="" />
          </div>
          <div class="stt-cf-btn mt-4">
            <label for="chooseFile">파일 내려받기</label>
            <form method="post" enctype="multipart/form-data">
              <input
                ref="image"
                @change="uploadImg()"
                type="file"
                id="chooseFile"
                name="chooseFile"
                accept="image/*"
                style="display: none"
              />
            </form>
          </div>
        </div>
        <!--ts-input-cont-end-->
      </div>
      <!--ts-container-end-->

      <br />

      <div
        style="
          text-align: center;
          font-weight: bold;
          margin-top: 22px;
          margin-bottom: 22px;
        "
      >
        <p>[ Tip : 로그인을 하시면 자료를 보관하고 내려받을 수 있습니다 ]</p>
      </div>
    </div>
    <!--stt-bottom-container-end-->
  </div>
  <!--field_end-->

  <div class="px-5"><hr /></div>

  <div class="footer container">
    <p class="mx-3">파고북스 이용약관</p>
    <p class="mx-3">의견제안</p>
    <p class="mx-3">개인정보처리방침</p>
    <p class="mx-3">책임의 한계와 법적고지</p>
    <p class="mx-3">준수사항</p>
  </div>

  <Modal @closeModal="loginModal = false" :loginModal="loginModal"/>

</template>

<script>
// import $ from 'jquery'
import Modal from "@/components/Modal.vue"
import axios from "axios";
export default {
  name: "papagoPage",
  data() {
    return {
      image: "",
      loginModal: false,
    };
  },
  components: {
    Modal,
  },

  methods: {
    uploadImg() {
      console.log("들어왔다");
      var image = this.$refs["image"].files[0];

      const url = URL.createObjectURL(image);
      this.image = url;
      console.log(url);
      console.log(this.image);
    },
    async tts() {
      await axios
        .post(
          "/api/tts/server",
          {
            data1: "반가워요",
            data2: "njihwan",
            data3: "1",
            data4: "1",
          },
          {
            headers: {
              "Content-Type": "application/json",
              Accept: "application/json",
            },
          }
        )
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },

  mounted() {},
};
</script>

<style>
body {
  margin: 0;
}

textarea {
  resize: none;
}

.profile-logo > div > h2 {
  color: white;
  font-weight: bold;
}

.stt-bottom-container {
  background: white;
  border-radius: 100px 0px 0px 0px;
}

.stt-ts-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.top-content {
  display: flex;
  justify-content: space-between;
  padding: 2rem 4rem 2rem 4rem;
  color: white;
}

.stt-ts-lg-ch {
  margin-right: 1rem;
  text-align: left;
}

.stt-ts-lg-ch > select {
  font-size: 1rem;
  padding: 0.5rem;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
}

select:hover {
  border-color: none;
}

select:focus {
  outline: none;
}

.stt-ts-input-cont {
  width: 35%;
  margin: 1rem;
  padding: 2rem 3rem 2rem 3rem;
  border: 1px solid #dbdbdb;
  border-radius: 25px;
  text-align: center;
}

.stt-ts-box {
  width: 100%;
  height: 400px;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  padding: 1rem;
  text-align: left;
}

.stt-cf-btn {
  padding: 0.5rem;
  background: white;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  font-size: 1rem;
}

.stt-trans-btn {
  width: 100%;
  margin-left: 1rem;
  padding: 0.5rem;
  background: #0d66ff;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  font-size: 1rem;
}

.stt-trans-btn > button {
  width: 100%;
  color: white;
  background: #0d66ff;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
}

.ts-output-cont {
  width: 35%;
  margin: 1rem;
  padding: 2rem 3rem 2rem 3rem;
  border: 1px solid #dbdbdb;
  border-radius: 25px;
}

.output-record {
  text-align: end;
}

.output-record > button {
  text-align: center;
  width: 20%;
  padding: 1.25rem;
  background: #0d66ff;
  border-radius: 50px 0px 0px 0px;
}
</style>