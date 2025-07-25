<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <router-link class="navbar-brand" :to="{name: 'pk_index_view'}">
                King Of Bots
            </router-link>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav">
                    <li class="nav-item">
                    <router-link :class=" $route.name === 'pk_index_view' ? 'nav-link active' : 'nav-link'" aria-current="page" :to="{name: 'pk_index_view'}">对战</router-link>
                    </li>
                    <li class="nav-item">
                    <router-link :class=" $route.name === 'record_index_view' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index_view'}">对局列表</router-link>
                    </li>
                    <li class="nav-item">
                    <router-link :class=" $route.name === 'ranklist_index_view' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index_view'}">排行榜</router-link>
                    </li>
                </ul>
            </div>  
            <ul class="navbar-nav" v-if="$store.state.user.is_login">
                <li class="nav-item dropdown">
                    <router-link class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    {{ $store.state.user.username }}
                    </router-link>
                    <ul class="dropdown-menu">
                        <li><router-link class="dropdown-item" :to="{name: 'user_bot_index_view'}">我的Bot</router-link></li>
                        <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                <li class="nav-item">
                    <router-link :to="{name: 'user_account_login'}" class="nav-link" role="button">
                        登录
                    </router-link>
                </li>
                <li class="nav-item">
                    <router-link :to="{name: 'user_account_register'}" class="nav-link" role="button">
                        注册
                    </router-link>
                </li>

            </ul>
        </div>
    </nav>
</template>

<script>
import { useStore } from 'vuex';
// import { ref } from 'vue';
// import router from '@/router/index';

export default{
    name:"NavBar",
    setup() {
        const store = useStore();

        const logout = ()=> {
            store.dispatch("logout");
        }
        return {
            logout,
        }
    }
}

</script>


<style scoped>

</style>