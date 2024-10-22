export function timedStartLoading(loading) { setTimeout(() => stopLoading(loading), 5000) }
export function startLoading(loading) { loading.value = true }
export function stopLoading(loading) { loading.value = false }