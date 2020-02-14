<template>
    <b-row class="align-items-center h-100">
        <b-col sm="8" offset-sm="2" md="6" offset-md="3" lg="6" offset-lg="3" xl="4" offset-xl="4">
            <b-card>
                <b-card-body>
                    <b-alert :show="errorShown">{{ error }}</b-alert>

                    <b-form-group label="Email" label-for="email-field">
                        <b-form-input @input="hideErrorWhenTyping"
                                      id="email-field"
                                      required
                                      type="email"
                                      v-model="email"/>
                    </b-form-group>

                    <b-form-group label="Password" label-for="password-field">
                        <b-form-input @input="hideErrorWhenTyping"
                                      id="password-field"
                                      required
                                      type="password"
                                      @keyup.enter="login"
                                      v-model="password"/>
                    </b-form-group>

                    <b-button variant="primary" @click="login">Login</b-button>
                </b-card-body>
            </b-card>
        </b-col>
    </b-row>
</template>

<script>
    export default {
        data: () => ({
            email: '',
            password: '',
            error: false,
        }),
        computed: {
            errorShown() {
                return this.error !== false;
            }
        },
        methods: {
            hideErrorWhenTyping() {
                if (this.errorShown) {
                    this.error = false;
                }
            },
            login() {
                this.$AuthPlugin.login(this.email, this.password)
                    .then(() => {
                        this.$router.push('/home');
                    })
                    .catch(err => {
                        this.error = err.response?.data?.message || 'Unknown error';
                    });
            }
        }
    }
</script>
