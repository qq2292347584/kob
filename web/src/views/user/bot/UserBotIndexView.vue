<template>
    <div class="container" style = "margin-top: 20px;">
        <div class="row">
            <div class="col-3">
                <div class="card">
                    <div class="card-body">
                        <img :src="$store.state.user.photo" alt="用户头像" style="width: 100%;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card">
                    <div class="card-header">
                        <span style = "font-weight: bolder; font-size:130%;">我的Bot</span>
                        <button type="button" class="btn btn-success" style = "float: right;" data-bs-toggle="modal" data-bs-target="#add-bot-btn">创建Bot</button>

                        
                        <div class="modal fade" id="add-bot-btn" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="botname" class="form-label">Bot名称</label>
                                        <input v-model="addbot.title" type="text" class="form-control" id="botname" placeholder="请填写bot名称">
                                    </div>
                                    <div class="mb-3">
                                        <label for="bot-description" class="form-label">Bot描述</label>
                                        <textarea v-model="addbot.description" class="form-control" id="bot-description" rows="3" placeholder="请填写Bot描述"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="bot-content" class="form-label">Bot代码</label>
                                        <VAceEditor
                                            v-model:value="addbot.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <div class="error-message" style="color: red;">{{ addbot.error_message }}</div>
                                    <button @click="add_bot()" type="button" class="btn btn-primary">创建</button>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body" style="text-align: center;">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Bot名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right:20px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-btn-' + bot.id"> 修改</button>
                                        <button @click="delete_bot(bot.id)" type="button" class="btn btn-danger">删除</button>

                                        <div style="text-align: left;" class="modal fade" :id="'update-bot-btn-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                    <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="botname" class="form-label">Bot名称</label>
                                                            <input v-model="bot.title" type="text" class="form-control" id="botname" placeholder="请填写bot名称">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="bot-description" class="form-label">Bot描述</label>
                                                            <textarea v-model="bot.description" class="form-control" id="bot-description" rows="3" placeholder="请填写Bot描述"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="bot-content" class="form-label">Bot代码</label>
                                                            <VAceEditor
                                                                v-model:value="bot.content"
                                                                @init="editorInit"
                                                                lang="c_cpp"
                                                                theme="textmate"
                                                                style="height: 300px" />
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message" style="color: red;">{{ bot.error_message }}</div>
                                                        <button @click="update_bot(bot)" type="button" class="btn btn-primary">保持修改</button>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                    </div>
                                                    </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>    
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
export default {
    components: {
        VAceEditor,
    },
    setup() {
        ace.config.set(
            "basePath", 
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const editorInit = () => {
            return {
                lineNumbers: true, // 显示行号
                mode: 'javascript', // 设置代码语言
                theme: 'default', // 设置主题
                // 其他 CodeMirror 配置
            };
        };
        const store = useStore();
        let bots = ref([]);

        let addbot = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        })
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
        refresh_bots();
        const add_bot = () => {
            addbot.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/add/",
                type: "post",
                data: {
                    title: addbot.title,
                    description: addbot.description,
                    content: addbot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success"){
                        addbot.title = "";
                        addbot.description = "";
                        addbot.content = "";
                        Modal.getInstance("#add-bot-btn").hide();
                        refresh_bots();
                    } else addbot.error_message = resp.error_message;
                },
                error(resp) {
                    addbot.error_message = resp.error_message;
                }
            })
        }
        
        const update_bot = (bot) => {
            addbot.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if(resp.error_message === "success"){
                        Modal.getInstance("#update-bot-btn-" + bot.id).hide();
                    } else bot.error_message = resp.error_message;
                },
                error(resp) {
                    bot.error_message = resp.error_message;
                }
            })
        }
        const delete_bot = (bot_id) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot_id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp){
                    if(resp.error_message === "success") {
                        refresh_bots();
                    }
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }
        
        
        return {
            refresh_bots,
            add_bot,
            delete_bot,
            update_bot,
            editorInit,
            bots,
            addbot,
        }
    }
}
</script>

<style scoped>

</style>