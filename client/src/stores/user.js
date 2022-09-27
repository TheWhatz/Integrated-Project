import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { computed, ref } from 'vue'
import { deleteToken, setToken } from '../utils/token'

export const useUser = defineStore('user', () => {
  const user = ref({})
  const token = ref(localStorage.getItem('token'))
  const isAuthenticated = ref(false)

  const loadUser = async () => {
    const existToken = getToken()
    if (!isAuthenticated.value && existToken) {
      setToken(existToken)
    }

    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/auth`)
      user.value = res.data
      isAuthenticated.value = true
    } catch (err) {
      throw err
    }
  }

  const refreshToken = async () => {
    const refreshToken = getRefreshToken()
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/auth/refreshToken`, {
        headers: {
          Authorization: `Bearer ${refreshToken}`,
        },
      })
      setToken(res.data.accessToken)
      return res
    } catch (err) {
      return err.response
    }
  }

  const loginUser = async (loginData) => {
    try {
      const res = await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/auth`, loginData)
      const data = res.data
      token.value = data.accessToken
      isAuthenticated.value = true
      setToken(token.value, data.refreshToken)
      await loadUser()
    } catch (err) {
      throw err.response.data
    }
  }

  const registerUser = async (registerData) => {
    try {
      await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/users`, registerData)
    } catch (err) {
      throw err.response.data
    }
  }

  const logout = () => {
    user.value = {}
    token.value = null
    isAuthenticated.value = false
    deleteToken()
  }

  const getToken = () => localStorage.getItem('token')
  const getRefreshToken = () => localStorage.getItem('refreshToken')

  return {
    user,
    loginUser,
    registerUser,
    loadUser,
    refreshToken,
    logout,
    getToken,
    getRefreshToken,
    isAuthenticated,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUser, import.meta.hot))
}
