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
  }
}
</script>

<style>
body {
  background-image: url("./assets/background.png");
  background-size: cover;
}
</style>
