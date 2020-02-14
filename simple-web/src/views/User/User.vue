<template>
    <b-row class="align-items-center h-100">
        <b-col>
            <b-button to="/home/user/add" class="mb-3" v-if="canAddUser">Add User</b-button>
            <vue-good-table
                    :columns="columns"
                    :rows="data">
                <template slot="table-row" slot-scope="props">
                    <div v-if="props.column.field === 'action'" class="text-center">
                        <b-button :to="`/home/user/update/${props.row.id}`" class="mr-2">
                            <b-icon-pencil/>
                        </b-button>
                        <b-button variant="danger" v-if="canDeleteUser" @click="deleteUser(props.row.id)">
                            <b-icon-trash/>
                        </b-button>
                    </div>
                    <template v-else>
                        {{props.formattedRow[props.column.field]}}
                    </template>
                </template>
            </vue-good-table>
        </b-col>
    </b-row>
</template>

<script>
    import {VueGoodTable} from 'vue-good-table';
    import axios from 'axios';
    import {BIconPencil, BIconTrash} from 'bootstrap-vue';
    import {store} from "../../simple_store";

    export default {
        data: () => ({
            columns: [
                {
                    label: 'Id',
                    field: 'id',
                    type: 'number'
                },
                {
                    label: 'Email',
                    field: 'email',
                },
                {
                    label: 'Phone',
                    field: 'phone',
                },
                {
                    label: 'Department',
                    field: 'department'
                },
                {
                    label: 'Role',
                    field: 'role'
                },
                {
                    label: 'Action',
                    field: 'action'
                }
            ],
            data: [],
        }),
        computed: {
            canAddUser() {
                return store.currentUser?.role === 'admin';
            },
            canDeleteUser() {
                return store.currentUser?.role === 'admin';
            },
        },
        methods: {
            deleteUser(id) {
                axios.delete(`/user/${id}`)
                    .then(() => {
                        this.loadUser();
                    })
                    .catch(err => {
                        alert(err.response.data.message);
                    });
            },
            loadUser() {
                axios.get('/user')
                    .then(res => {
                        this.data = res.data;
                    });
            }
        },
        components: {
            VueGoodTable, BIconPencil, BIconTrash
        },
        created() {
            this.loadUser();
        }
    }
</script>

<style scoped>
    @import "~vue-good-table/dist/vue-good-table.css";
</style>
