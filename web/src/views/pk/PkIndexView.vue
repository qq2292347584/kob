<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'">
    </PlayGround>
    <MatchGround v-if="$store.state.pk.status === 'matching'">
    </MatchGround>
    <ResultBoard v-if="$store.state.pk.loser != 'none'">
    </ResultBoard>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue'
import { onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
import MatchGround from '@/components/MatchGround.vue'
import ResultBoard from '@/components/ResultBoard.vue'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
        store.commit("updateLoser", "none");

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
                const data = JSON.parse(message.data);
                console.log(data);
                if(data.event === "start-matching") { //匹配成功
                    store.commit("updateOpponent", {
                        opponent_username: data.opponent_username,
                        opponent_photo: data.opponent_photo,
                    })
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 1000)
                    store.commit("updateGame", {
                        map: data.game.map,
                        a_id: data.game.a_id,
                        a_sx: data.game.a_sx,
                        a_sy: data.game.a_sy,
                        b_id: data.game.b_id,
                        b_sx: data.game.b_sx,
                        b_sy: data.game.b_sy,
                    });
                } else if(data.event === "move") {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if(data.event === "result") {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if(data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                    store.commit("updateLoser", data.loser);
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
