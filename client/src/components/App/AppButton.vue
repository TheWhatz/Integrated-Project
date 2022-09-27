<script setup>
defineProps({
  buttonType: {
    type: String,
    default: 'primary',
  },
})
</script>

<template>
  <button :class="`btn ${buttonType}`">
    <slot></slot>
  </button>
</template>

<style scoped>
.btn {
  outline: none;
  border: none;
  border-radius: 5px;
  padding: 10px;
  margin: 5px 8px;
  transition: all 0.3s ease-out;
}
.btn:hover {
  cursor: pointer;
}

.btn:disabled {
  pointer-events: none;
  cursor: default;
  opacity: 0.6;
}

.btn.primary {
  border: 1px solid rgba(200, 200, 200, 0.5);
  background-color: #006494;
  color: white;
}
.btn.primary:hover {
  background-color: #1786ba;
}
.btn.success {
  background-color: green;
  color: white;
  border: 1px solid darkgreen;
}
.btn.success:hover {
  background-color: rgb(0, 155, 0);
}
.btn.warning {
  background-color: orange;
  border: 1px solid rgb(211, 116, 0);
  color: black;
}
.btn.warning:hover {
  background-color: gold;
  border: 1px solid orange;
}
.btn.danger {
  background-color: rgb(200, 0, 0);
  color: white;
}
.btn.danger:hover {
  background-color: rgb(230, 0, 0);
  color: white;
}
.btn.outline-danger {
  background-color: white;
  color: rgb(200, 0, 0);
  border: 1px solid rgb(200, 0, 0);
}
.btn.outline-danger:hover {
  background-color: rgb(200, 0, 0);
  color: white;
}
</style>
