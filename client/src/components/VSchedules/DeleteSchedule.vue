<script setup>
import moment from 'moment'
defineProps({
  modalState: {
    type: Object,
  },
  eventDetail: {
    type: Object,
  },
  eventStore: {
    type: Object,
  },
})
</script>

<template>
  <app-modal
    :show="modalState.state"
    modal-type="confirm"
    @confirm="modalState.onConfirm"
    @cancel="modalState.onCancel"
    class="delete-modal"
  >
    <h3 class="title">Do you want to cancel this event?</h3>
    <h2 class="booking-name">
      #{{ eventDetail.bookingId }} {{ eventDetail.bookingName }}
    </h2>
    <p class="event-start-time">
      {{ moment(eventDetail.eventStartTime).format('LL') }} •
      {{
        $getFormattedEventPeriod(eventDetail.eventStartTime, eventDetail.eventDuration)
      }}
    </p>
  </app-modal>
</template>

<style scoped>
.delete-modal {
  text-align: center;
}
.title {
  font-weight: 400;
  /* border-bottom: 1px solid rgba(10, 10, 10, 0.5); */
  opacity: 0.5;
  padding-bottom: 12px;
}
.booking-name,
.event-start-time {
  margin: 12px 0;
}
</style>
