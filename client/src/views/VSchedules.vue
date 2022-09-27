<script setup>
import moment from 'moment'
import { computed, onBeforeMount, ref } from 'vue'
import { useEvents } from '../stores/events.js'
import { useEventCategories } from '../stores/eventCategories.js'

import ScheduleFormInput from '../components/VSchedules/ScheduleFormInput.vue'
import DeleteSchedule from '../components/VSchedules/DeleteSchedule.vue'
import EventCategory from '../components/VSchedules/EventCategory.vue'
import PageWrapper from '../components/PageWrapper.vue'

const eventStore = useEvents()
const categoryStore = useEventCategories()

const isLoading = ref(false)
const searchEvent = ref('')
const categoryFilter = ref('All')
const dateFilter = ref('')
const upcomingPastFilter = ref('All')
const filterDropdown = ref(false)

const eventModelTemplate = {
  category: null,
  bookingName: '',
  bookingEmail: '',
  eventStartTime: '',
  eventNotes: '',
}

const eventModel = ref({ ...eventModelTemplate })
const eventModelError = ref({})
const eventDetail = ref({})
const editMode = ref(false)

const addEvent = async () => {
  try {
    isLoading.value = true
    const data = await eventStore.addNewEvent({
      ...eventModel.value,
      categoryId: eventModel.value.category.categoryId,
      eventStartTime: moment(eventModel.value.eventStartTime).toISOString(),
    })
    scheduleFormInputModal.value.close()
    eventDetail.value = { ...data }
  } catch (err) {
    const fieldErrors = err.fieldErrors
    for (let key in fieldErrors) {
      fieldErrors[key] = fieldErrors[key].join(', ')
    }
    eventModelError.value = fieldErrors
  } finally {
    isLoading.value = false
  }
}

const editEvent = async () => {
  try {
    isLoading.value = true
    await eventStore.updateEvent(eventModel.value.bookingId, {
      eventStartTime: moment(eventModel.value.eventStartTime).toISOString(),
      eventNotes: eventModel.value.eventNotes,
    })

    eventDetail.value = { ...eventModel.value }
    scheduleFormInputModal.value.close()
  } catch (err) {
    const fieldErrors = err.fieldErrors
    for (let key in fieldErrors) {
      fieldErrors[key] = fieldErrors[key].join(', ')
    }
    eventModelError.value = fieldErrors
  } finally {
    isLoading.value = false
  }
}

const deleteEvent = async () => {
  try {
    isLoading.value = true
    await eventStore.deleteEventById(eventDetail.value.bookingId)
  } catch (err) {
    alert(err.message)
  } finally {
    isLoading.value = false
    resetDetail()
    confirmDeleteModal.value.close()
  }
}

const resetEventForm = () => {
  eventModel.value = { ...eventModelTemplate }
  eventModelError.value = {}
}

const viewDetail = async (bookingId) => {
  eventDetail.value = await eventStore.getEventDetailById(bookingId)
}

const editDetail = () => {
  editMode.value = true
  eventModel.value = {
    ...eventDetail.value,
    eventStartTime: moment(eventDetail.value.eventStartTime).format('YYYY-MM-DDTHH:mm'),
  }
  eventModel.value.category.eventDuration = eventModel.value.eventDuration
  scheduleFormInputModal.value.show()
}

const resetDetail = () => (eventDetail.value = {})

const categoryFilterChange = async () => {
  if (dateFilter.value !== '' || upcomingPastFilter.value !== 'All') return

  let filterOption = {}
  if (categoryFilter.value !== 'All') {
    filterOption = { sort: 'category', categoryId: categoryFilter.value }
  }

  try {
    isLoading.value = true
    await eventStore.fetchEvents(filterOption)
  } finally {
    isLoading.value = false
  }
}

const dateFilterChange = async () => {
  if (upcomingPastFilter.value !== 'All') return
  let filterOption = {}

  if (dateFilter.value !== '') {
    filterOption = { sort: 'date', eventDate: dateFilter.value }
  }
  try {
    isLoading.value = true
    await eventStore.fetchEvents(filterOption)
  } finally {
    isLoading.value = false
  }
}

const upcomingPastFilterChange = async () => {
  let filterOption = {}

  if (upcomingPastFilter.value !== 'All') {
    filterOption = { sort: upcomingPastFilter.value }
  }

  try {
    isLoading.value = true
    await eventStore.fetchEvents(filterOption)
  } finally {
    isLoading.value = false
  }
}

const filteredEvents = computed(() => {
  let filtered = eventStore.events

  if (categoryFilter.value !== 'All') {
    filtered = filtered.filter((el) => el.category.categoryId === categoryFilter.value)
  }

  if (dateFilter.value !== '') {
    filtered = filtered.filter((el) => {
      return moment(el.eventStartTime).format('YYYY-MM-DD') == moment(dateFilter.value).format('YYYY-MM-DD')
    })
  }

  return filtered.filter((el) => el.bookingName.toLowerCase().includes(searchEvent.value.toLowerCase())) || []
})

const confirmDeleteModal = ref({
  state: false,
  show: () => (confirmDeleteModal.value.state = true),
  close: () => (confirmDeleteModal.value.state = false),
  onConfirm: deleteEvent,
  onCancel: () => {
    confirmDeleteModal.value.close()
    isLoading.value = false
  },
})

const scheduleFormInputModal = ref({
  state: false,
  show: () => (scheduleFormInputModal.value.state = true),
  close: () => {
    resetEventForm()
    eventModelError.value = {}
    editMode.value = false
    scheduleFormInputModal.value.state = false
  },
})

onBeforeMount(async () => {
  isLoading.value = true
  await eventStore.fetchEvents()
  await categoryStore.fetchEventCategories()
  isLoading.value = false
})
</script>

<template>
  <PageWrapper>
    <DeleteSchedule :modal-state="confirmDeleteModal" :event-detail="eventDetail" :event-store="eventStore" />
    <ScheduleFormInput
      :event-model="eventModel"
      :event-model-error="eventModelError"
      :is-edit-mode="editMode"
      :modal-state="scheduleFormInputModal"
      @add-event="addEvent"
      @edit-event="editEvent"
      @reset-form="resetEventForm"
    />
    <app-loading-screen v-if="isLoading" />
    <div id="schedules">
      <div class="schedules-list-container">
        <div class="schedules-list-header">
          <h4 id="schedules-total">{{ (!isLoading && filteredEvents.length) || 0 }} event(s).</h4>
          <div>
            <input v-model.trim="searchEvent" class="event-search-bar" type="text" placeholder="Search" />
            <font-awesome-icon id="schedules-filter" icon="filter" @click="filterDropdown = !filterDropdown" />
          </div>
        </div>

        <div
          v-if="filterDropdown"
          style="background-color: rgba(245, 245, 245, 0.9); padding: 10px 15px; display: flex; gap: 22px"
        >
          <div>
            <label>By Date times : </label>
            <input v-model="dateFilter" type="date" @change="dateFilterChange" />
          </div>
          <div>
            <label>By Category : </label>
            <select @change="categoryFilterChange" v-model="categoryFilter">
              <option value="All">All</option>
              <option
                v-for="(categories, index) in categoryStore.eventCategories"
                :key="categories.categoryId"
                :value="categories.categoryId"
              >
                {{ categories.categoryName }}
              </option>
            </select>
          </div>
          <div>
            <label>By Event Status : </label>
            <select @change="upcomingPastFilterChange" v-model="upcomingPastFilter">
              <option value="All">All</option>
              <option value="upcoming">Upcoming</option>
              <option value="past">Past</option>
            </select>
          </div>
        </div>

        <div class="schedules-list" v-if="!isLoading && filteredEvents.length > 0">
          <div
            :class="`schedule-card ${eventDetail && eventDetail.bookingId === event.bookingId && 'card-active'}`"
            v-for="event in filteredEvents"
            :key="event.bookingId"
            @click="viewDetail(event.bookingId)"
          >
            <h3 class="title">{{ $truncate(event.bookingName, 50) }}</h3>
            <div class="date">
              <font-awesome-icon icon="calendar-days" />
              {{ moment(event.eventStartTime).format('LL') }} •
              {{ $getFormattedEventPeriod(event.eventStartTime, event.eventDuration) }}
            </div>
            <div class="duration">
              <font-awesome-icon icon="clock" />
              {{ event.eventDuration }} Minute(s)
            </div>
            <div style="display: flex; gap: 8px">
              <EventCategory :category="event.category" />
            </div>
          </div>
        </div>
        <div class="schedules-list" v-else>
          <div class="no-schedules">
            <font-awesome-icon icon="note-sticky" />
            <h3>No scheduled events.</h3>
          </div>
        </div>

        <font-awesome-icon id="schedules-add" icon="add" @click="scheduleFormInputModal.show" />
      </div>
      <div
        :class="`schedule-detail-container ${Object.keys(eventDetail).length > 0 && 'slide-in'}`"
        v-if="Object.keys(eventDetail).length > 0"
      >
        <div class="schedule-detail">
          <h1 style="font-size: 18pt">
            {{ eventDetail.bookingName }}
          </h1>
          <cite>({{ eventDetail.bookingEmail }})</cite>
          <EventCategory :category="eventDetail.category" />
          <p class="detail-datetime">
            <font-awesome-icon icon="calendar-days" />
            {{ moment(eventDetail.eventStartTime).format('LL') }} •
            {{ $getFormattedEventPeriod(eventDetail.eventStartTime, eventDetail.eventDuration) }}
          </p>
          <cite v-show="eventDetail.eventNotes">‟ {{ eventDetail.eventNotes }} ”</cite>
          <div class="app-input-group">
            <app-button button-type="warning" @click="editDetail">Edit Details</app-button>
            <app-button button-type="outline-danger" @click="confirmDeleteModal.show">Cancel Event</app-button>
          </div>
        </div>
        <font-awesome-icon id="schedule-detail-exit" icon="xmark" @click="resetDetail" />
      </div>
    </div>
  </PageWrapper>
</template>

<style scoped>
#schedules {
  display: flex;
  height: 100%;
  overflow: hidden;
}

#schedules-filter {
  cursor: pointer;
}

#schedules-add {
  position: absolute;
  bottom: 32px;
  right: 32px;
  background-color: #1b98e0;
  color: white;
  width: fit-content;
  height: 20px;
  padding: 1rem;
  border-radius: 100%;
  transition: all 0.3s ease-out;
}

#schedules-add:hover {
  cursor: pointer;
  transform: scale(1.05);
  background-color: #42a6df;
}

#schedules-total {
  margin: 12px 0;
}

#schedule-detail-exit {
  position: absolute;
  top: 18px;
  right: 24px;
  font-size: 20pt;
  color: rgba(10, 10, 10, 0.8);
  transition: color 0.3s ease-out;
}

#schedule-detail-exit:hover {
  cursor: pointer;
  color: darkred;
}

#schedule-detail-exit:active {
  color: red;
}

.schedules-list {
  position: relative;
  overflow-y: scroll;
  width: 100%;
  height: 100%;
  padding: 8px 18px;
  background-color: rgba(245, 245, 245, 0.55);
}

.schedules-list .no-schedules {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  opacity: 0.2;
  text-align: center;
}

.schedules-list .no-schedules svg {
  margin: 25px 0;
  font-size: 62pt;
}

.schedule-list svg {
  font-size: 20pt;
  vertical-align: middle;
  margin-right: 10px;
}

.schedules-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: whitesmoke;
  border-bottom: 1px solid rgba(20, 20, 20, 0.1);
  padding: 0 1em;
}

.schedules-list-header .event-search-bar {
  border-radius: 15px;
  padding: 0 10px;
  margin: 0 10px;
}

.schedules-list-container {
  position: relative;
  flex: 6;
  display: flex;
  flex-direction: column;
}

.schedule-detail {
  padding: 28px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  text-align: center;
}

.schedule-detail .event-category {
  margin: 18px auto;
  font-size: 9pt;
}

.schedule-detail .detail-datetime {
  margin: 10px 0px;
  color: rgba(10, 10, 10, 0.8);
}

.schedule-detail-container {
  position: relative;
  flex: 5;
  width: 100%;
  background: rgba(250, 250, 250, 0.7);
  border-radius: 5px;
  margin: 0 1em;
}

.schedule-card {
  background-color: whitesmoke;
  margin: 10px 0;
  padding: 15px 20px;
  border-radius: 5px;
  border: 1px solid rgba(0, 100, 148, 0.2);
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
  transition: all 0.3s ease-out;
}

.schedule-card .date {
  color: rgba(25, 25, 25, 0.8);
}
.duration {
  color: steelblue;
  font-size: 10pt;
}

.schedule-card:hover,
.schedule-card.card-active {
  cursor: pointer;
  transform: scale(1.015);
  background-color: rgb(0, 100, 148);
}

.schedule-card:hover > .title,
.schedule-card:hover > .date,
.schedule-card:hover > .duration,
.schedule-card.card-active > .title,
.schedule-card.card-active > .date,
.schedule-card.card-active > .duration {
  color: white !important;
}

.schedule-card .title {
  font-size: 16pt;
}

.schedule-card .category {
  background: green;
  width: fit-content;
  padding: 2px 8px;
  border-radius: 20px;
  font-size: 9pt;
  margin: 10px 0;
  color: white;
}

.schedule-card .date svg,
.schedule-card .duration svg {
  margin-right: 8px;
}

.slide-in {
  -webkit-animation: slide-in-right 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: slide-in-right 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}

@-webkit-keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(2vw);
    transform: translateX(2vw);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
@keyframes slide-in-right {
  0% {
    -webkit-transform: translateX(2vw);
    transform: translateX(2vw);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateX(0);
    transform: translateX(0);
    opacity: 1;
  }
}
</style>
