<template>
  <div class="field">
    <div class="top-container px-5 py-5">
      <div class="profile-logo">
        <div>
          <h2>문서변환</h2>
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

    <div class="ocr-bottom-container px-5 pt-5 pb-2">
      <div class="ocr-ts-container">
        <div class="ocr-ts-input-cont">
          <div class="ocr-img-box">
            <img :src="image" class="ocr-img-fit" />
          </div>
          <div class="ocr-cf-btn mt-4">
            <label for="chooseFile">파일 가져오기</label>
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

        <div class="ts-output-cont">
          <div class="ocr-ts-box">
            <form action="#">
              <output name="result" for="text">
                <div v-html="content"></div>
              </output>
            </form>
          </div>
          <div style="display: flex">
            <div class="ocr-ts-lg-ch mt-4">
              <select name="ts-lg" id="ts-lg">
                <option value="kr">한국어</option>
                <option value="en">영어</option>
                <option value="jp">일본어</option>
                <option value="cn">중국어</option>
                <option value="gm">독일어</option>
                <option value="sp">스페인어</option>
              </select>
            </div>
            <button @click="translation" class="ocr-trans-btn mt-4">
              번역하기
            </button>
          </div>
        </div>
        <!--ts-output-cont-end-->
      </div>
      <!--ts-container-end-->
      <br />
      <div
        style="
          text-align: center;
          font-weight: bold;
          margin-top: 21px;
          margin-bottom: 21px;
        "
      >
        <p>[ Tip : 로그인을 하시면 자료를 보관하고 내려받을 수 있습니다 ]</p>
      </div>
    </div>
    <!--ocr-bottom-container-end-->
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

  <div class="login-modal px-5 py-5" v-if="loginModal == true">
    <h4>로그인</h4>
    <div class="loginBtns">
      <div>
        <img src="@/assets/naverLogo.png" alt="" /> 네이버 아이디로 로그인
      </div>
      <div>
        <img src="@/assets/kakaoLogo.png" v-on:click="kakaoLogin" />카카오
        아이디로 로그인
      </div>
      <div>
        <img src="@/assets/googleLogo.png" v-on:click="googleLogin" />구글
        아이디로 로그인
      </div>
      <div id="my-signin2" style="display: none"></div>
      <div>
        <img src="@/assets/githubLogo.png" alt="" />깃허브 아이디로 로그인
      </div>
    </div>
    <div>
      <button @click="loginModal = false">닫기</button>
    </div>
  </div>
  <!--login-modal-end-->
</template>

<script>
// import $ from 'jquery'
import axios from "axios";
export default {
  name: "papagoPage",
  data() {
    return {
      googleAuth: null,
      image: "",
      text: "",
      loginModal: false,
    };
  },
  components: {},

  methods: {
    async translation() {
      let form = new FormData();
      form.append("text", this.text);
      await axios
        .post("/api/user/papago/json", form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((res) => {
          console.log(res);
          this.text = res.data;
          console.log(this.text);
        })
        .catch((err) => {
          console.log("refreshToken error : ", err.config);
        });
    },
    async uploadImg() {
      console.log("들어왔다");
      var image = this.$refs["image"].files[0];
      const url = URL.createObjectURL(image);
      this.image = url;
      let form = new FormData();
      form.append("file", image);

      await axios
        .post("/api/ocr", form, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((res) => {
          console.log(res);
          this.text = res.data;
          console.log(this.text);
        })
        .catch((err) => {
          console.log("refreshToken error : ", err.config);
        });
    },
    kakaoLogin() {
      // console.log(window.Kakao);
      window.Kakao.Auth.login({
        scope: "profile_image, account_email",
        success: this.kakaoInfo,
      });
    },
    async kakaoInfo(authObj) {
      console.log(authObj);
      const userInfo = {
        email: null,
        profile: null,
      };
      await window.Kakao.API.request({
        url: "/v2/user/me",
        success: (res) => {
          const kakao_account = res.kakao_account;
          userInfo.email = kakao_account.email;
          userInfo.profile = kakao_account.profile.thumbnail_image_url;

          if (userInfo.email != null || userInfo.email != "") {
            axios
              .post("/api/user/save/normal", JSON.stringify(userInfo), {
                headers: { "Content-Type": `application/json` },
              })
              .then((res) => {
                res;
                console.log("가입성공");
              })
              .catch((err) => {
                err;
                console.log("기존가입");
              });
          }
          alert("로그인 성공!");
        },
        fail: (error) => {
          this.$router.push("/errorPage");
          console.log(error);
        },
      });
      let form = new FormData();
      form.append("email", userInfo.email);
      form.append("password", "DMTT");
      this.$store.dispatch("getToken", form);
      this.$store.dispatch("setUserInfo", userInfo);
    },
    kakaoLogout() {
      // eslint-disable-next-line
      if (!window.Kakao.Auth.getAccessToken()) {
        console.log("Not logged in.");
        return;
      }
      window.Kakao.Auth.logout(function (response) {
        alert(response + "logout");
        window.location.href = "/";
      });
      localStorage.clear(); // 전체삭제
    },
    //구글 버튼
    googleLogin() {
      var self = this;
      window.gapi.signin2.render("my-signin2", {
        scope: "profile email",
        width: 240,
        height: 50,
        longtitle: true,
        theme: "dark",
        onsuccess: this.googleInfo,
        onfailure: this.googleLogout,
      });
      setTimeout(function () {
        if (!self.googleLoginCheck) {
          const auth = window.gapi.auth2.getAuthInstance();
          auth.isSignedIn.get();
          document.querySelector(".abcRioButton").click();
        }
      }, 500);
    },
    //구글 로그인 이후 실행되는 콜백함수(성공)
    async googleInfo(googleUser) {
      //const user_join_type = "g"
      const profile = googleUser.getBasicProfile();
      const googleEmail = profile.getEmail();
      const googleProfile = profile.getImageUrl();
      let googleData = {};
      googleData.email = googleEmail;
      googleData.profile = googleProfile;
      if (googleData.email != null || googleData.profile != "") {
        await axios
          .post("/api/user/save/normal", JSON.stringify(googleData), {
            headers: { "Content-Type": `application/json` },
          })
          .then((res) => {
            res;
            console.log("가입성공");
          })
          .catch((err) => {
            err;
            console.log("가입에러");
          });
      }
      let form = new FormData();
      form.append("email", googleData.email);
      form.append("password", "DMTT");
      this.$store.dispatch("getToken", form);
      this.$store.dispatch("setUserInfo", googleData);
    },
    googleLogout() {
      // eslint-disable-next-line
      const auth = gapi.auth2.getAuthInstance();
      auth.signOut().then(function () {
        console.log("User signed out.");
      });
      localStorage.clear();
    },
  },
  mounted() {},
  computed: {
    content() {
      return this.text.replace(/(?:\r\n|\r|\n)/g, "<br />");
    },
  },
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

.ocr-bottom-container {
  background: white;
  border-radius: 100px 0px 0px 0px;
}

.ocr-ts-container {
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

.ocr-ts-lg-ch {
  margin-right: 1rem;
  text-align: left;
}

.ocr-ts-lg-ch > select {
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

.ocr-ts-input-cont {
  width: 35%;
  margin: 1rem;
  padding: 2rem 3rem 2rem 3rem;
  border: 1px solid #dbdbdb;
  border-radius: 25px;
  text-align: center;
}

.ocr-img-box {
  width: 100%;
  height: 400px;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  overflow: auto;
}

.ocr-img-box::-webkit-scrollbar {
  display: none;
}

.ocr-img-fit {
  width: 100%;
  border-radius: 10px;
  object-fit: contain;
}

.ocr-ts-box {
  width: 100%;
  height: 400px;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  padding: 1rem;
  text-align: left;
  overflow: auto;
}

.ocr-cf-btn {
  padding: 0.5rem;
  background: white;
  border: 1px solid #dbdbdb;
  border-radius: 10px;
  font-size: 1rem;
}

.ocr-trans-btn {
  width: 100%;
  margin-left: 1rem;
  padding: 0.5rem;
  color: white;
  background: #0d66ff;
  border: 1px solid #dbdbdb;
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
</style>