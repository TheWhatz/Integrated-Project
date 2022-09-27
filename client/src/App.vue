<script setup></script>

<template>
  <router-view />
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=IBM+Plex+Sans+Thai:wght@100;200;300;400;500;600;700&display=swap');
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'IBM Plex Sans Thai', sans-serif;
}

input,
textarea,
select {
  border: 1px solid rgba(10, 10, 10, 0.2);
  padding: 0 5px;
  transition: all 0.3s ease-out;
}
textarea {
  width: 100%;
  resize: none;
  padding: 5px 8px;
}
input,
select {
  height: 30px;
}
input:focus,
textarea:focus,
select:focus {
  outline: none;
  border: 1px solid #247ba0;
}
input.invalid,
textarea.invalid,
select.invalid {
  border: 1px solid rgba(220, 30, 30, 0.5) !important;
}
label {
  width: fit-content;
}
label.required:after {
  content: '*';
  padding: 0 5px;
  color: red;
}
#app {
  position: relative;
  height: 100vh;
  background: rgb(213, 213, 213);
  background: linear-gradient(90deg, rgba(213, 213, 213, 1) 0%, rgba(231, 231, 231, 1) 100%);
}

.app-wrapper {
  display: flex;
  height: 100%;
}

@media screen and (max-width: 788px) {
  .app-wrapper {
    flex-direction: column;
  }
}

.app-container {
  width: 100%;
  overflow-y: auto;
}

.app-content {
  height: 100%;
  padding: 24px;
}

.app-input-group {
  margin: 15px auto;
  width: 100%;
}

.app-button-group {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  width: 100%;
}

.app-button-group > button {
  width: 50%;
}

/* width */
::-webkit-scrollbar {
  width: 10px;
}

/* Track */
::-webkit-scrollbar-track {
  background: #f1f1f1;
}

/* Handle */
::-webkit-scrollbar-thumb {
  background: #13293d;
  border-radius: 12px;
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
