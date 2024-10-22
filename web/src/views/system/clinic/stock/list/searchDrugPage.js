import { ref } from 'vue'

export const visible = ref(false)
export function show() { visible.value = true }
export function hidden(callback) {
    visible.value = false
    if(callback) callback(visible.value)
}