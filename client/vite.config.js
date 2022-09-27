import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }) => {
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) }

  return {
    plugins: [vue()],
    base: process.env.VITE_BASE_PATH + '/',
    server: {
      proxy: {
        '/api': {
          target: process.env.VITE_API_URL,
        },
      },
    },
  }
})
