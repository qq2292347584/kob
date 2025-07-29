<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'">
    </PlayGround>
    <MatchGround v-if="$store.state.pk.status === 'matching'">
    </MatchGround>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
import MatchGround from '@/components/MatchGround.vue'

export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        //当组件进行挂载时建立连接
        onMounted(() => {
            store.commit("updateOpponent", {
                opponent_username: "我的对手",
                opponent_photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = (message) => {
                console.log(message.data);
                const data = JSON.parse(message.data);
                console.log(data);
                if(data.event === "start-matching") { //匹配成功
                    store.commit("updateOpponent", {
                        opponent_username: data.opponent_username,
                        opponent_photo: data.opponent_photo,
                    })
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 3000)
                    store.commit("updateGameMap", data.gamemap);

                }
            }

            socket.onclose = () => {
                console.log("disconnected!");
            }
        });

        //当组件关闭时断开连接
        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");

        })

    }
}
</script>

<style scoped>

</style>
