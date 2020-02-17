<template>
    <b-row class="align-items-center h-100">
        <b-col>
            <b-card no-body>
                <b-card-header header-tag="nav">
                    <b-nav card-header pills>
                        <b-nav-item to="/home" exact exact-active-class="active">Home</b-nav-item>
                        <b-nav-item to="/home/user" active-class="active">
                            User
                        </b-nav-item>
                        <b-nav-item @click="logout">Logout</b-nav-item>
                    </b-nav>
                </b-card-header>

                <b-card-body>
                    <router-view></router-view>
                </b-card-body>
            </b-card>
        </b-col>
    </b-row>
</template>

<script>
    import {mutations} from "../simple_store";

    export default {
        methods: {
            logout() {
                mutations.clearCurrentUser();
                this.$AuthPlugin.logout();
                this.$router.replace('/login');
            }
        }
    }
</script>

<style scoped>

</style>
