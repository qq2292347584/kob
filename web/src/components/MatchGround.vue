<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6" style="text-align: center; margin-top: 10vh;">
                <img :src="$store.state.user.photo" alt="">
                <div class="username">{{ $store.state.user.username }}</div> 
            </div>
            <div class="col-6" style="text-align: center; margin-top: 10vh;">
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

export default {
    name: "MatchGround",
    setup() {
        let match_btn_info = ref("开始匹配");
        const store = useStore();

        const match_btn_click = ()=> {
            if(match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                }))
            } else {
                match_btn_info.value = "开始匹配";
                store.state.pk.socket.send(JSON.stringify({
                    event: "stop-matching",
                }))
            }
        }

        return {
            match_btn_info,
            match_btn_click,
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

div.col-6 > img {
    border-radius: 50%;
    border: 2px solid #333;
    width: 15vw;
}
div.col-6 > img:hover {
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
</style>