import axios from 'axios'

export const setToken = (token, refreshToken) => {
  if (!token) return
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
  localStorage.setItem('token', token)

  if (refreshToken) localStorage.setItem('refreshToken', refreshToken)
}

export const deleteToken = () => {
  delete axios.defaults.headers.common['Authorization']
  localStorage.removeItem('token')
  localStorage.removeItem('refreshToken')
}
