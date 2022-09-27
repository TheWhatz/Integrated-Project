import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useUsers = defineStore('users', () => {
  const users = ref([])

  const editUser = async (userId, editedData) => {
    try {
      await axios.patch(`${import.meta.env.VITE_BASE_PATH}/api/users/${userId}`, editedData)
    } catch (err) {
      throw err.response.data
    }
  }

  const fetchUsers = async () => {
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/users`)
      users.value = res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const fetchUserByUserId = async (userId) => {
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/users/${userId}`)
      return res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const deleteUserById = async (userId) => {
    try {
      await axios.delete(`${import.meta.env.VITE_BASE_PATH}/api/users/${userId}`)
      await fetchUsers()
    } catch (err) {
      throw err.response.data
    }
  }

  return {
    users,
    fetchUsers,
    fetchUserByUserId,
    deleteUserById,
    editUser,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUsers, import.meta.hot))
}
