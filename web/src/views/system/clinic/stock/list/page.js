import { ref } from 'vue'

export const current = ref(1)
export const size = ref(10)
export const total = ref(0)
export function getPage() { return { current: current.value, size: size.value, total: total.value } }
export function setPage(pageCurrent, pageTotal) {
    current.value = pageCurrent
    total.value = pageTotal
}