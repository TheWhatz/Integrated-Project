<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import router from '../router'
import { useEventCategories } from '../stores/eventCategories'
import PageWrapper from '../components/PageWrapper.vue'

const categoryStore = useEventCategories()
const route = useRoute()

const isLoading = ref(false)
const category = ref({})
const eventModelError = ref({})

const editCategory = async () => {
  try {
    isLoading.value = true
    await categoryStore.updateEventCategory(category.value)
    router.replace('/categories')
  } catch (err) {
    const fieldErrors = err.fieldErrors
    for (let key in fieldErrors) {
      fieldErrors[key] = fieldErrors[key].join(', ')
    }
    eventModelError.value = fieldErrors
  } finally {
    isLoading.value = false
  }
}

const formConfig = ref({
  isLoading: true,
  maxLength: {
    categoryName: 100,
    eventDuration: 480,
    eventCategoryDesc: 500,
  },
  min: {
    eventDuration: 1,
  },
})

const visibleValidationErrorMsg = computed(() => {
  return {
    visibility: eventModelError.value != null ? 'visible' : 'hidden',
  }
})

onMounted(async () => {
  try {
    isLoading.value = true
    const data = await categoryStore.fetchEventCategoryById(route.params.categoryId)
    category.value = data
  } catch (err) {
    if (err.status === 404) {
      router.replace({ path: '/not-found' })
    }
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <PageWrapper>
    <app-loading-screen v-if="isLoading" />
    <div v-else id="edit-category">
      <div class="edit-category-box">
        <h2>Edit category</h2>
        <form @submit="editCategory">
          <div style="display: flex; gap: 12px">
            <div class="app-input-group" style="flex: 5">
              <label class="required">Category name</label>
              <input
                v-model.trim="category.categoryName"
                type="text"
                required
                :class="eventModelError.categoryName && 'invalid'"
                :maxlength="formConfig.maxLength.categoryName"
              />
              <div class="input-validation-box">
                <div class="input-invalid-msg" :style="visibleValidationErrorMsg">
                  {{ eventModelError.categoryName }}
                </div>
                <div class="input-counter">
                  {{ (category.categoryName && category.categoryName.length) || 0 }}/{{
                    formConfig.maxLength.categoryName
                  }}
                </div>
              </div>
            </div>
            <div class="app-input-group" style="flex: 2">
              <label class="required">Duration</label>
              <input
                style="text-align: center"
                v-model.number="category.eventDuration"
                type="number"
                :class="eventModelError.eventDuration && 'invalid'"
                :min="formConfig.min.eventDuration"
                :max="formConfig.maxLength.eventDuration"
                required
              />
              <div class="input-validation-box">
                <div class="input-invalid-msg" :style="visibleValidationErrorMsg">
                  {{ eventModelError.eventDuration }}
                </div>
              </div>
              <div class="input-counter">
                ( {{ formConfig.min.eventDuration }} - {{ formConfig.maxLength.eventDuration }} Minutes )
              </div>
            </div>
          </div>
          <div class="app-input-group">
            <label>Description</label>
            <textarea
              style="width: 100%; height: 100px"
              v-model.trim="category.eventCategoryDesc"
              :class="eventModelError.eventCategoryDesc && 'invalid'"
              :maxlength="formConfig.maxLength.eventCategoryDesc"
            ></textarea>
            <div class="input-validation-box">
              <div class="input-invalid-msg" :style="visibleValidationErrorMsg">
                {{ eventModelError.eventCategoryDesc }}
              </div>
              <div class="input-counter">
                {{ (category.eventCategoryDesc && category.eventCategoryDesc.length) || 0 }}/{{
                  formConfig.maxLength.eventCategoryDesc
                }}
              </div>
            </div>
          </div>
          <div class="app-button-group">
            <app-button button-type="warning" type="submit">Save</app-button>
            <app-button button-type="danger" type="reset" @click="router.replace('/categories')">Cancel</app-button>
          </div>
        </form>
      </div>
    </div>
  </PageWrapper>
</template>

<style scoped>
#edit-category {
  height: 100%;
  max-width: 985px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit-category-box {
  margin: 0 auto;
  padding: 28px 32px;
  width: 60%;
  border-radius: 12px;
  background-color: white;
  box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
  -webkit-box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
  -moz-box-shadow: 0px 0px 8px -5px rgba(0, 0, 0, 0.5);
}

.input-counter {
  display: flex;
  font-size: 10pt;
  justify-content: space-between;
  color: rgba(10, 10, 10, 0.5);
}

.input-validation-box {
  display: flex;
  font-size: 10pt;
  justify-content: space-between;
}

.input-validation-box .input-invalid-msg {
  color: rgb(220, 30, 30);
}

.input-validation-box .input-counter {
  color: rgba(10, 10, 10, 0.5);
}

input {
  width: 100%;
}

label {
  display: block;
}
</style>
