<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUser } from '../../stores/user'

const router = useRouter()
const userStore = useUser()
const user = ref(userStore.user)
const navOpened = ref(false)
const navsRef = ref()

const isMobile = computed(() => {
  return window.matchMedia('(max-width: 788px)')
})

const toggleMenu = (evt) => {
  navOpened.value = !navOpened.value
}

const logout = () => {
  router.push('/login')
  userStore.logout()
}
</script>

<template>
  <nav id="app-sidebar">
    <img id="app-sidebar-logo" src="../../assets/logo.png" alt="Nav Logo" />
    <div id="app-nav">
      <div>OASIP - SSI2</div>
      <button id="btn-menu" :class="{ toggled: navOpened }" @click="toggleMenu">
        <div></div>
        <div></div>
        <div></div>
      </button>
    </div>
    <!-- <div style="text-align: center; color: white; font-size: 0.8em">Welcome, {{ user.name }}</div> -->
    <ul ref="navsRef" id="navs" :class="isMobile && { opened: navOpened }">
      <li>
        <router-link :to="{ path: '/' }">
          <font-awesome-icon icon="calendar-days" />
          <span>Schedules</span>
        </router-link>
      </li>
      <li>
        <router-link :to="{ path: '/categories' }" :class="$route.path.includes('/categories') && 'router-link-active'">
          <font-awesome-icon icon="list" />
          <span>Categories</span>
        </router-link>
      </li>
      <li>
        <router-link :to="{ path: '/users' }" :class="$route.path.includes('/users') && 'router-link-active'">
          <font-awesome-icon icon="users" />
          <span>Users</span>
        </router-link>
      </li>
      <li>
        <a class="logout" @click="logout">
          <span style="color: red">Log out</span>
        </a>
      </li>
    </ul>
  </nav>
</template>

<style scoped>
#app-sidebar {
  width: 100%;
  background-color: #13293d;
}

#app-sidebar-logo {
  display: block;
  margin: 0 auto;
  width: 180px;
  padding: 20px;
}

#app-nav {
  display: none;
  color: white;
}

@media screen and (max-width: 788px) {
  #app-sidebar {
    flex: 0 !important;
  }

  #app-sidebar-logo {
    display: none;
  }

  #app-nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
  }

  #navs {
    visibility: hidden;
    opacity: 0;
    position: absolute;
    top: 0;
    left: 0;
    background-color: inherit;
    width: 40%;
    height: 100%;
    z-index: 999;
    transform: translateX(-100%);
    transition: all 0.4s cubic-bezier(0.645, 0.045, 0.355, 1);
  }

  #navs.opened {
    visibility: visible;
    opacity: 1;
    transform: translateX(0);
  }
}

#navs {
  margin: 0 auto;
  list-style: none;
}

/*select router-link*/
#navs li a {
  display: block;
  padding: 8px;
  text-decoration: none;
  color: white;
  transition: all 0.3s ease-out;
  opacity: 0.5;
}

#navs li a:hover {
  opacity: 0.8;
}

#navs li a.router-link-active,
#navs li a.router-link-exact-active {
  opacity: 1;
  background-color: #006494;
}

#navs li a svg {
  width: 20px;
}

#navs li a span {
  padding: 0 12px;
}

#btn-menu {
  outline: none;
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  gap: 5px;
}

#btn-menu:hover {
  cursor: pointer;
}

#btn-menu div {
  width: 25px;
  height: 3px;
  background-color: white;
  transition: all 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
  pointer-events: none;
  user-select: none;
}

#btn-menu.toggled div:nth-child(1) {
  transform: translate(0, 8px) rotate(45deg);
}

#btn-menu.toggled div:nth-child(2) {
  opacity: 0;
}

#btn-menu.toggled div:nth-child(3) {
  transform: translate(0, -8px) rotate(-45deg);
}

.logout {
  color: red;
  font-weight: bold;
  transition: 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.logout:hover {
  cursor: pointer;
  opacity: 1;
}
</style>
