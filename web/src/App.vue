<template>
  <NavBar />
  <router-view/>
</template>

<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from 'jquery'

export default{
  components: {
    NavBar,
  },
  //专门用来调试
  setup() {
    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmZGJhMDk0YjNmNGY0YjcyOGZhODA4MzQyNTEwYWM5MCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTc1MjcyNjgwNiwiZXhwIjoxNzUzOTM2NDA2fQ.JaAC0hoB9dK-bJAr-fs89YrwXxMpYS3nz1p7-RSn15U"
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // });

    // $.ajax({
    //   url:"http://127.0.0.1:3000/user/account/register/",
    //   type:"post",
    //   data: {
    //     username: "hk",
    //     password: "pxjw3",
    //     confirmedPassword: "pxjw3",
    //   },
      
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   }
    // })
  }
}
</script>

<style>
body {
  background-image: url("./assets/background.png");
  background-size: cover;
}
</style>
