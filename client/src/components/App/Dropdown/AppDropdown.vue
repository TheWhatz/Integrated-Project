<script setup>
import { ref } from 'vue'
const showDropdown = ref(false)
const dropdownContent = ref()
const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
  setTimeout(() => {
    if (showDropdown.value) {
      dropdownContent.value.focus()
    }
  }, 50)
}
</script>

<template>
  <div class="dropdown-container">
    <font-awesome-icon class="dropdown-toggle" icon="ellipsis" @click="toggleDropdown" />
    <transition name="dropdown">
      <div v-if="showDropdown" ref="dropdownContent" class="dropdown" :tabindex="0" @focusout="showDropdown = false">
        <slot />
      </div>
    </transition>
  </div>
</template>

<style scoped>
.dropdown-container {
  position: relative;
}

.dropdown {
  position: absolute;
  right: 0;
  background-color: whitesmoke;
  padding: 10px 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.9);
}

.dropdown-toggle {
  transition: all 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.dropdown-toggle:hover {
  cursor: pointer;
  transform: scale(1.1);
}

.dropdown:focus {
  outline: none;
}

.dropdown-enter-active {
  animation: dropdown-in 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275) both;
}

.dropdown-leave-active {
  animation: dropdown-out 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55) both;
}

@keyframes dropdown-in {
  0% {
    transform: translateZ(-35px) translateY(-20px) translateX(25px);
    opacity: 0;
    pointer-events: none;
  }
  100% {
    transform: translateZ(0) translateY(0) translateX(0);
    opacity: 1;
  }
}
@keyframes dropdown-out {
  0% {
    transform: translateZ(0) translateY(0) translateX(0);
    opacity: 1;
    pointer-events: none;
  }
  100% {
    transform: translateZ(-35px) translateY(-20px) translateX(25px);
    opacity: 0;
  }
}
</style>
