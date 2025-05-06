import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView.vue'
import RankListView from '../views/ranklist/RankListView.vue'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import NotFound from '../views/error/NotFound.vue'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/",
  },
  {
    path: "/pk/",
    name: "pk_index_view",
    component: PkIndexView,
  },
  {
    path: "/ranklist/",
    name: "ranklist_index_view",
    component: RankListView,
  },
  {
    path: "/record/",
    name: "record_index_view",
    component: RecordIndexView,
  },
  {
    path: "/user/bot/",
    name: "user_bot_index_view",
    component: UserBotIndexView
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  },
  {
    path: "/:catchAll(.*)",
    redirect: "/404/",
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
