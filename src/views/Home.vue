<template>
    <span>Hello, <strong>{{ currentUser.email }}</strong>, you are <strong>{{ currentUser.role }}</strong></span>
</template>

<script>
    import {store} from "../simple_store";

    export default {
        computed: {
            currentUser() {
                return store.currentUser;
            }
        },
    }
</script>
