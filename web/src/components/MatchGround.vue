<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4" style="text-align: center; margin-top: 10vh;">
                <img :src="$store.state.user.photo" alt="">
                <div class="username">{{ $store.state.user.username }}</div> 
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select v-model="select_bot" class="form-select" aria-label="Default select example">
                        <option value="-1" selected>亲自上阵</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{bot.title}}</option>
                    </select>
                </div>
            </div>
            <div class="col-4" style="text-align: center; margin-top: 10vh;">
                <img :src="$store.state.pk.opponent_photo" alt="">
                <div class="opponent-username">{{ $store.state.pk.opponent_username }}</div>
            </div>
        </div>
        <button @click="match_btn_click()" type="button" class="btn btn-warning match-btn">{{ match_btn_info }}</button>

    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    name: "MatchGround",
    setup() {
        let match_btn_info = ref("开始匹配");
        const store = useStore();
        let bots = ref([]);
        let select_bot = ref("-1");

        const match_btn_click = ()=> {
            if(match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }))
            } else {
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }))
            }
        }
        const refresh_bots = () => {
            $.ajax({
                url:"http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        }

        refresh_bots(); //从云端动态获取bots
        return {
            match_btn_info,
            match_btn_click,
            refresh_bots,
            bots,
            select_bot,
        }
    }
}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 70vh;
    background-color: rgba(50, 50, 50, 0.5);
    border-radius: 10px;
    margin: 40px auto;
}

div.col-4 > img {
    border-radius: 50%;
    border: 2px solid #333;
    width: 15vw;
}
div.col-4 > img:hover {
    transform: scale(1.2);
    transition: 1000ms;
    cursor: pointer;
    box-shadow: 2px 2px 2px lightgray;
}

div.username {
    font-size: 200%;
    color: white;
    font-weight: bold;
    padding-top: 2vh;
}
div.opponent-username {
    font-size: 200%;
    color: white;
    font-weight: bold;
    padding-top: 2vh;
}

button.match-btn {
    display: block;
    margin: 40px auto;
    padding: 12px 24px;
    font-size: 18px;
}

div.user-select-bot {
    padding-top: 20vh;
}

div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>