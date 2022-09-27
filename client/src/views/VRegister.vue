<script setup>
import { ref, onBeforeMount } from 'vue'
import { useUser } from '../stores/user'
import AppInput from '../components/App/AppInput.vue'
import { useUserRoles } from '../stores/userRoles'
import { useRouter } from 'vue-router'

const router = useRouter()

const userStore = useUser()
const roleStore = useUserRoles()

const roleOptions = ref([])
const isLoading = ref(false)
const formRegister = ref({
  email: '',
  password: '',
  confirmPassword: '',
  name: '',
  role: '',
})

const errorMessage = ref({
  email: '',
  password: '',
  confirmPassword: '',
  name: '',
  role: '',
})

const registerUser = async () => {
  const passwordMatched = validatePasswordMatch()

  if (!passwordMatched) return
  isLoading.value = true
  try {
    await userStore.registerUser({
      email: formRegister.value.email,
      name: formRegister.value.name,
      role: formRegister.value.role,
      password: formRegister.value.password,
    })
    alert('Your account has been created!')
    router.push('/login')
  } catch (err) {
    errorMessage.value = {}
    const fieldErrors = err.fieldErrors
    for (let key in fieldErrors) {
      errorMessage.value[key] = fieldErrors[key].join(', ')
    }
  } finally {
    isLoading.value = false
  }
}

const validatePasswordMatch = () => {
  errorMessage.value.password = ''
  errorMessage.value.confirmPassword = ''

  if (!formRegister.value.password || !formRegister.value.confirmPassword) {
    return false
  }

  if (formRegister.value.password.length < 8 || formRegister.value.password.length > 14) {
    errorMessage.value.password = 'Password size must be between 8 and 14.'
    return false
  }

  if (formRegister.value.password !== formRegister.value.confirmPassword) {
    errorMessage.value.confirmPassword = 'Password mismatch.'
    return false
  } else {
    errorMessage.value.confirmPassword = ''
    return true
  }
}

onBeforeMount(async () => {
  await roleStore.fetchUserRoles()
  roleOptions.value = roleStore.toOptions()
  formRegister.value.role = roleOptions.value[0].value
})
</script>

<template>
  <app-loading-screen v-show="isLoading" />
  <div class="container">
    <div class="wrapper">
      <div class="title">Welcome to OASIP-SSI2</div>
      <div class="desc">Please, fill form to create account.</div>
      <form @submit.prevent="registerUser">
        <AppInput
          type="text"
          v-model.trim="formRegister.name"
          label-text="Name"
          :maxlength="100"
          :error-message="errorMessage.name"
          :required="true"
        />
        <AppInput
          type="email"
          pattern="^[^(\.)][a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}"
          v-model.trim="formRegister.email"
          label-text="Email"
          :maxlength="50"
          :error-message="errorMessage.email"
          :required="true"
        />
        <AppInput
          type="password"
          v-model="formRegister.password"
          label-text="Password"
          @focusout="validatePasswordMatch"
          :minlength="8"
          :error-message="errorMessage.password"
          :required="true"
        />
        <AppInput
          type="password"
          v-model="formRegister.confirmPassword"
          label-text="Confirm Password"
          @focusout="validatePasswordMatch"
          :minlength="8"
          :error-message="errorMessage.confirmPassword"
          :required="true"
        />
        <AppInput
          v-model.trim="formRegister.role"
          label-text="Role"
          :options="roleOptions"
          :error-message="errorMessage.role"
          :required="true"
        />
        <app-button class="btn-submit" type="submit">Create New Account</app-button>
      </form>
      <router-link class="register-href" to="/login">I already have an account</router-link>
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

.title {
  font-weight: 300;
}

.desc {
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
