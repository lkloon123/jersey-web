<template>
    <div>
        <h3 class="mb-4">Add User</h3>
        <b-row class="form-group">
            <label for="email-field" class="col-2 col-form-label">Email</label>
            <b-col cols="10">
                <b-form-input id="email-field"
                              type="email"
                              v-model="email"/>
            </b-col>
        </b-row>

        <b-row class="form-group">
            <label for="phone-field" class="col-2 col-form-label">Phone</label>
            <b-col cols="10">
                <b-form-input id="phone-field"
                              v-model="phone"/>
            </b-col>
        </b-row>

        <b-row class="form-group">
            <label for="password-field" class="col-2 col-form-label">Password</label>
            <b-col cols="10">
                <b-form-input id="password-field"
                              type="password"
                              required
                              v-model="password"/>
            </b-col>
        </b-row>

        <b-row class="form-group">
            <label for="department-field" class="col-2 col-form-label">Department</label>
            <b-col cols="10">
                <b-form-input id="department-field"
                              required
                              v-model="department"/>
            </b-col>
        </b-row>

        <b-row class="form-group">
            <label class="col-2 col-form-label">Role</label>
            <b-col cols="10">
                <b-form-select v-model="role" :options="roleOptions"></b-form-select>
            </b-col>
        </b-row>

        <b-button variant="primary" @click="submit">Submit</b-button>
    </div>
</template>

<script>
    import axios from 'axios';
    import qs from "qs";

    export default {
        data: () => ({
            email: '',
            phone: '',
            password: '',
            department: '',
            role: 'user',
            roleOptions: [
                {value: 'user', text: 'User'},
                {value: 'manager', text: 'Manager'},
                {value: 'admin', text: 'Admin'},
            ]
        }),
        methods: {
            submit() {
                const params = {
                    email: this.email,
                    phone: this.phone,
                    password: this.password,
                    department: this.department,
                    role: this.role
                };

                axios.post('/user', qs.stringify(params))
                    .then(() => {
                        this.$router.push('/home/user');
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        }
    }
</script>

<style scoped>

</style>
