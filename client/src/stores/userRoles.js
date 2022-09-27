import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useUserRoles = defineStore('userRoles', () => {
  const userRoles = ref([])

  const fetchUserRoles = async () => {
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/roles`)
      userRoles.value = res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const toOptions = () => {
    const options = []
    userRoles.value.forEach((role) => {
      options.push({
        label: role.label,
        value: role.name,
      })
    })
    return options
  }

  return {
    userRoles,
    fetchUserRoles,
    toOptions,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserRoles, import.meta.hot))
}
