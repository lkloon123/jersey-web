<template>
    <div>
        <h3 class="mb-4">Update User</h3>

        <b-row class="form-group">
            <label for="email-field" class="col-2 col-form-label">Email</label>
            <b-col cols="10">
                <b-form-input id="email-field"
                              type="email"
                              v-model="email"
                              :readonly="!canUpdateEmail"/>
            </b-col>
        </b-row>

        <b-row class="form-group">
            <label for="phone-field" class="col-2 col-form-label">Phone</label>
            <b-col cols="10">
                <b-form-input id="phone-field"
                              v-model="phone"/>
            </b-col>
        </b-row>

        <b-button variant="primary" @click="submit">Submit</b-button>
    </div>
</template>

<script>
    import axios from 'axios';
    import {store} from "../../simple_store";
    import qs from "qs";

    export default {
        data: () => ({
            email: '',
            phone: '',
        }),
        computed: {
            canUpdateEmail() {
                return store.currentUser?.role === 'admin';
            }
        },
        methods: {
            submit() {
                const params = {
                    phone: this.phone,
                };

                if (this.canUpdateEmail) {
                    params.email = this.email;
                }

                axios.patch(`/user/${this.$route.params.id}`, qs.stringify(params))
                    .then(() => {
                        this.$router.push('/home/user');
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        },
        created() {
            axios.get(`/user/${this.$route.params.id}`)
                .then(res => {
                    this.email = res.data.email;
                    this.phone = res.data.phone;
                })
                .catch(err => {
                    alert(err.response.data.message);
                    this.$router.push('/home/user');
                });
        }
    }
</script>

<style scoped>

</style>
