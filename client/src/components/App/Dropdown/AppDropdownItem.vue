<script setup></script>

<template>
  <div class="dropdown-item">
    <slot />
  </div>
</template>

<style scoped>
.dropdown-item {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
  font-size: 0.75em;
  transition: all 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.dropdown-item:hover {
  cursor: pointer;
}
</style>
