<template>
    <div class="result-board">
        <div class="result-board-content" v-if="$store.state.pk.loser === 'all'">
            Draw!
        </div>
        <div class="result-board-content" v-else-if="$store.state.pk.loser === 'A' && $store.state.user.id == $store.state.pk.a_id">
            Loser!
        </div>
        <div class="result-board-content" v-else-if="$store.state.pk.loser === 'B' && $store.state.user.id == $store.state.pk.b_id">
            Loser!
        </div>
        <div class="result-board-content" v-else>
            Win!
        </div>
        <div class="result-board-btn">
            <button @click="restart()" type="button" class="btn btn-warning btn-lg">再来!</button>
        </div>
        
    </div>

</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOpponent", {
                opponent_username: "我的对手",
                opponent_photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
        }
        
        return {
            restart,
        }

    }
}
</script>

<style scoped>
div.result-board {
    width: 30vw;
    height: 30vh;
    position: absolute;
    background-color: rgba(50, 50, 50, 0.5);
    top: 30vh;
    left: 35vw;
    border-radius: 10px;
}
div.result-board:hover {
    transition: 1000ms;
    transform: scale(1.2);
    box-shadow: 5px 5px 10px gray;
    cursor: pointer;
}

div.result-board-content {
    font-size: 10vh;
    color: white;
    font-weight: 600;
    font-style: italic;
    text-align: center;
    margin-top: 5vh;
}

div.result-board-btn {
    text-align: center;
    margin-top: 3vh;
    
}
</style>
