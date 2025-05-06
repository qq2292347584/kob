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
            <ul class="navbar-nav">
                <li class="nav-item dropdown">
                    <router-link class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    kaihuang
                    </router-link>
                    <ul class="dropdown-menu">
                        <li><router-link class="dropdown-item" :to="{name: 'user_bot_index_view'}">我的Bot</router-link></li>
                        <li><router-link class="dropdown-item" href="#">退出</router-link></li>
                    </ul>
                </li>
            </ul>

        </div>
    </nav>
</template>

<script>
export default{
    name:"NavBar",
}
</script>


<style scoped>

</style>