<script setup>
import { onBeforeMount, ref } from 'vue'
import moment from 'moment'
import { useUsers } from '../stores/users.js'
import AppDropdown from '../components/App/Dropdown/AppDropdown.vue'
import AppDropdownItem from '../components/App/Dropdown/AppDropdownItem.vue'
import PageWrapper from '../components/PageWrapper.vue'

const isLoading = ref(true)
const userStore = useUsers()

const confirmDeleteModal = ref(false)
const setConfirmDeleteModal = (state) => (confirmDeleteModal.value = state)

const viewDetailModal = ref(false)
const setViewDetailModal = (state) => (viewDetailModal.value = state)

const selectedUser = ref({})

const deleteUser = (user) => {
  selectedUser.value = user
  setConfirmDeleteModal(true)
}

const confirmDeleteUser = async () => {
  setConfirmDeleteModal(false)

  isLoading.value = true
  try {
    await userStore.deleteUserById(selectedUser.value.id)
  } catch (err) {
    console.log(err)
  } finally {
    isLoading.value = false
  }
}

const viewUserDetail = (user) => {
  selectedUser.value = user
  setViewDetailModal(true)
}

onBeforeMount(async () => {
  await userStore.fetchUsers()
  isLoading.value = false
})
</script>

<template>
  <PageWrapper>
    <app-modal :show="viewDetailModal" modal-type="info" @ok="setViewDetailModal(false)">
      <h1>User Detail</h1>
      <div style="display: grid; grid-template-columns: repeat(2, 1fr)">
        <h3>Name :</h3>
        <p>{{ selectedUser.name }}</p>
        <h3>Email :</h3>
        <p>{{ selectedUser.email }}</p>
        <h3>Role :</h3>
        <p>{{ selectedUser.role.label }}</p>
        <h3>Created On :</h3>
        <p>{{ moment(selectedUser.createdOn).format('lll') }}</p>
        <h3>Updated On :</h3>
        <p>{{ moment(selectedUser.updatedOn).format('lll') }}</p>
      </div>
    </app-modal>
    <app-modal
      :show="confirmDeleteModal"
      modal-type="confirm"
      @confirm="confirmDeleteUser"
      @cancel="setConfirmDeleteModal(false)"
    >
      <h3 class="delete-modal-title">Are you sure to Delete?</h3>
      <h2>Delete User {{ selectedUser.name }}</h2>
    </app-modal>
    <app-loading-screen v-if="isLoading" />
    <div v-else id="users">
      <div class="user-list-header">
        <h4>
          {{ userStore.users.length || 0 }}
          User(s).
        </h4>
      </div>
      <div class="user-list" v-if="userStore.users.length > 0">
        <div class="user-card" v-for="user in userStore.users" :key="user.userId">
          <div class="user-card-content">
            <div>
              <div class="user-card-detail">
                <div class="role-badge">{{ user.role.label.toUpperCase() }}</div>
                <b class="card-title" @click="viewUserDetail(user)">{{ user.name }}</b>
              </div>
              <div class="user-card-detail" style="margin-top: 12px">
                <font-awesome-icon icon="envelope" />
                {{ user.email }}
              </div>
            </div>
            <AppDropdown>
              <AppDropdownItem style="color: darkorange" @click.prevent="$router.push(`/users/${user.id}/edit`)">
                <font-awesome-icon icon="pen" />
                <span>Edit</span>
              </AppDropdownItem>
              <AppDropdownItem style="color: red; font-weight: bold" @click.prevent="deleteUser(user)">
                <font-awesome-icon icon="trash" />
                <span>Delete</span>
              </AppDropdownItem>
            </AppDropdown>
          </div>
        </div>
      </div>
      <div style="position: relative; width: 100%; height: 100%" v-else>
        <div class="no-user">No user(s).</div>
      </div>
    </div>
  </PageWrapper>
</template>

<style scoped>
#users {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: rgba(245, 245, 245, 0.55);
}

.user-list-header {
  background-color: whitesmoke;
  border-bottom: 1px solid rgba(20, 20, 20, 0.1);
  padding: 12px;
}

.user-list {
  position: relative;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  overflow-y: scroll;
  padding: 8px 18px;
}

.user-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 30px 38px;
  border-radius: 10px;
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
  transition: all 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}

.user-card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.user-card-detail {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 8px;
}

.delete-modal-title {
  font-weight: 400;
  opacity: 0.5;
  padding-bottom: 12px;
}

.role-badge {
  background-color: black;
  border-radius: 20px;
  padding: 2px 10px;
  height: fit-content;
  font-size: 0.8em;
  color: white;
}
.card-title {
  text-decoration: underline;
  transition: all 0.3s cubic-bezier(0.165, 0.84, 0.44, 1);
}
.card-title:hover {
  color: #42a6df;
  cursor: pointer;
}

.no-user {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0.5;
}

@media screen and (max-width: 1097px) {
  .user-list {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>
