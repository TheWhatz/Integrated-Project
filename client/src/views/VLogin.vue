<script setup>
import { ref } from 'vue'
import { useUser } from '../stores/user'
import AppInput from '../components/App/AppInput.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUser()

const formLogin = ref({
  email: '',
  password: '',
})

const errorMessage = ref({
  email: '',
  password: '',
})

const loginUser = async () => {
  const passwordValid = validatePassword()

  if (!passwordValid) return

  try {
    await userStore.loginUser(formLogin.value)
    router.push('/')
  } catch (err) {
    errorMessage.value = {}
    if (err.status === 404) {
      errorMessage.value.email = err.message
    } else if (err.status === 401) {
      errorMessage.value.password = err.message
    }
  }
}

const validatePassword = () => {
  errorMessage.value.password = ''

  if (formLogin.value.password.length < 8 || formLogin.value.password.length > 14) {
    errorMessage.value.password = 'Password size must be between 8 and 14.'
    return false
  }

  return true
}
</script>

<template>
  <div class="container">
    <div class="wrapper">
      <div class="welcome-title">Welcome to OASIP-SSI2</div>
      <div class="welcome-desc">Please, fill form to login.</div>
      <form @submit.prevent="loginUser">
        <AppInput
          type="email"
          pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
          v-model.trim="formLogin.email"
          label-text="Email"
          :max-length="50"
          :error-message="errorMessage.email"
          :required="true"
        />
        <AppInput
          type="password"
          v-model="formLogin.password"
          label-text="Password"
          :minlength="8"
          :error-message="errorMessage.password"
          :required="true"
        />
        <app-button class="btn-submit" type="submit">Login</app-button>
      </form>
      <router-link class="register-href" to="/register">I don't have an account</router-link>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 735px;
  width: 100%;
  margin: 0 auto;
  height: 100%;
}

.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  min-height: 100%;
  margin: 0 auto;
}

.welcome-title {
  font-weight: 300;
}

.welcome-desc {
  font-weight: 300;
  font-size: 0.8em;
  margin: 5px auto;
  opacity: 0.7;
}

.register-href {
  font-size: 0.7em;
  margin-top: 10px;
}

input {
  width: 100%;
}

form label {
  font-size: 0.8em;
}

form .btn-submit {
  width: 100%;
  margin-top: 2em;
  margin-left: auto;
  margin-right: auto;
}
</style>
