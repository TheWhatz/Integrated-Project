import axios from 'axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { ref } from 'vue'

export const useEvents = defineStore('events', () => {
  const events = ref([])

  const addNewEvent = async (newEvent) => {
    try {
      const res = await axios.post(`${import.meta.env.VITE_BASE_PATH}/api/events`, newEvent)
      await fetchEvents()
      return res.data
    } catch (err) {
      throw err.response.data
    }
  }

  const updateEvent = async (bookingId, editedEvent) => {
    try {
      await axios.patch(`${import.meta.env.VITE_BASE_PATH}/api/events/${bookingId}`, editedEvent)
      await fetchEvents()
    } catch (err) {
      throw err.response.data
    }
  }

  const deleteEventById = async (bookingId) => {
    try {
      await axios.delete(`${import.meta.env.VITE_BASE_PATH}/api/events/${bookingId}`)
    } catch (err) {
      throw err.response.data
    } finally {
      await fetchEvents()
    }
  }

  const getEventDetailById = (bookingId) => {
    return events.value.find((event) => event.bookingId === bookingId)
  }

  const getEventsSameCategory = (searchEvent) => {
    return events.value.filter(
      (event) =>
        searchEvent.bookingId !== event.bookingId && event.category.categoryId === searchEvent.category.categoryId
    )
  }

  const fetchEvents = async (filterOption) => {
    let requestParams = ''
    if (filterOption !== undefined && typeof filterOption === 'object') {
      requestParams = new URLSearchParams(filterOption)
    }
    try {
      const res = await axios(`${import.meta.env.VITE_BASE_PATH}/api/events?${requestParams}`)
      events.value = res.data
    } catch (err) {
      throw err.response.data
    }
  }

  return {
    events,
    fetchEvents,
    getEventDetailById,
    getEventsSameCategory,
    addNewEvent,
    updateEvent,
    deleteEventById,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useEvents, import.meta.hot))
}
