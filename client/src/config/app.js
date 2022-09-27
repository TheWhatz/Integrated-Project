import moment from 'moment'

export const configureVueApp = (app) => {
  app.config.globalProperties.$getFormattedEventPeriod = (time, duration) => {
    const startTime = moment(time)
    const finishTime = startTime.clone().add(duration, 'minutes')
    return `${startTime.format('LT')} - ${finishTime.format('LT')}`
  }

  app.config.globalProperties.$truncate = (str, maxLength) => {
    return str.length > maxLength ? `${str.substr(0, maxLength - 1)}\u2026` : str
  }

  app.config.globalProperties.$capitalize = (str) => {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase()
  }
}
