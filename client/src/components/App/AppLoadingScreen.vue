<script setup></script>

<template>
  <div id="app-loading-screen">
    <img id="app-loading" src="../../assets/loading-pulse.svg" alt="Loading Svg" />
  </div>
</template>

<style scoped>
#app-loading-screen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(19, 41, 61, 0.5);
  z-index: 999;
}

#app-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
