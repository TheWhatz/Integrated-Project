<script setup>
import AppSidebar from './App/AppSidebar.vue'
</script>

<template>
  <div class="app-wrapper">
    <AppSidebar style="flex: 1" />
    <div style="flex: 6" class="app-container">
      <div class="app-content">
        <slot />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
