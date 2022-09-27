<script setup>
const props = defineProps({
  modelValue: {},
  modelModifiers: { default: () => ({}) },
  required: {
    type: Boolean,
    default: false,
  },
  labelText: {
    type: String,
  },
  options: {
    type: Array,
  },
  errorMessage: {
    type: String,
  },
})
const emit = defineEmits(['update:modelValue'])

const emitValue = (evt) => {
  let value = evt.target.value
  if (props.modelModifiers.trim) {
    value = value.trim()
  }
  emit('update:modelValue', value)
}
</script>

<template>
  <div class="input-group">
    <label v-show="labelText" :for="$attrs.id" :class="required && 'required'">{{ labelText }}</label>
    <select
      v-if="options"
      v-bind="$attrs"
      :class="errorMessage && 'error'"
      :value="modelValue"
      @input="emitValue"
      :required="required"
    >
      <option v-for="(option, index) in options" :key="index" :value="option.value">
        {{ option.label }}
      </option>
    </select>
    <input
      v-else
      v-bind="$attrs"
      :class="errorMessage && 'error'"
      :value="modelValue"
      @input="emitValue"
      :required="required"
    />
    <div v-show="errorMessage" class="error-message">{{ errorMessage }}</div>
  </div>
</template>

<style scoped>
.input-group input,
.input-group select {
  width: 100%;
}
.input-group input.error {
  border-color: red;
}
.input-group label {
  font-size: 0.8em;
}
.error-message {
  color: red;
  font-size: 0.7em;
}
</style>
