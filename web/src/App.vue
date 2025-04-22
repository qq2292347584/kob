<template>
  <div>{{ bot_name }}</div>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';

export default{
  name:"App",
  setup: ()=> { //程序的入口
    let bot_name = ref("");
    let bot_rating = ref("");

    $.ajax({
      url: "http://127.0.0.1:3000/pk/getbotinfo/",
      type: "get",
      success: (resp) => {
        bot_name.value = resp;
      }
    })

    return {
      bot_name,
      bot_rating,
    }
  }
}
</script>

<style>
body {
  background-image: url("./assets/background.png");
  background-size: cover;
}
</style>
