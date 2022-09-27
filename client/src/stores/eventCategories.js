import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useEventCategories = defineStore('eventCategories', () => {
  const eventCategories = ref([])

  const fetchEventCategories = async () => {
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/categories`)
      eventCategories.value = res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const fetchEventCategoryById = async (categoryId) => {
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/categories/${categoryId}`)
      return res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const updateEventCategory = async (category) => {
    try {
      const res = await axios.put(`${import.meta.env.VITE_BASE_PATH}/api/categories/${category.categoryId}`, category)
      return res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const getCategoryByCategoryId = (categoryId) => {
    return eventCategories.value.find((category) => category.categoryId === categoryId)
  }

  return {
    eventCategories,
    fetchEventCategories,
    fetchEventCategoryById,
    updateEventCategory,
    getCategoryByCategoryId,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useEventCategories, import.meta.hot))
}
