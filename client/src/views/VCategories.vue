<script setup>
import { onBeforeMount, ref } from 'vue'
import { useEventCategories } from '../stores/eventCategories'
import PageWrapper from '../components/PageWrapper.vue'

const categoryStore = useEventCategories()
const isLoading = ref(false)

onBeforeMount(async () => {
  if (categoryStore.eventCategories.length <= 0) {
    isLoading.value = true
  }
  await categoryStore.fetchEventCategories()
  isLoading.value = false
})
</script>

<template>
  <PageWrapper>
    <app-loading-screen v-if="isLoading" />
    <div v-else id="categories">
      <div class="category-list-header">
        <h4>
          {{ categoryStore.eventCategories.length || 0 }}
          Record(s).
        </h4>
      </div>
      <div class="category-list">
        <div class="category-card" v-for="category in categoryStore.eventCategories" :key="category.categoryId">
          <div class="category-card-content">
            <h2>{{ category.categoryName }}</h2>
            <div class="duration">
              <font-awesome-icon icon="clock" />
              {{ category.eventDuration }}
              minute(s)
            </div>
            <p class="description" v-if="category.eventCategoryDesc">
              {{ category.eventCategoryDesc }}
            </p>
            <p v-else class="description empty">No description</p>
          </div>
          <div class="button-group">
            <router-link :to="`/categories/${category.categoryId}`">
              <app-button button-type="warning">
                <font-awesome-icon icon="pen-to-square" />
              </app-button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </PageWrapper>
</template>

<style scoped>
#categories {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.category-list-header {
  background-color: whitesmoke;
  border-bottom: 1px solid rgba(20, 20, 20, 0.1);
  padding: 12px;
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, 1fr);
  gap: 12px;
  overflow-y: scroll;
  height: 100%;
  padding: 8px 18px;
  background-color: rgba(245, 245, 245, 0.55);
}

.category-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 30px 38px;
  border-radius: 10px;
}

.duration {
  color: steelblue;
  width: fit-content;
  font-size: 10pt;
}

.description {
  margin-top: 15px;
}

.description.empty {
  opacity: 0.4;
  font-style: italic;
}
</style>
