export function isUsable() {
    return (
        window.localStorage.tabs != null && 
        window.localStorage.tabs != undefined && 
        window.localStorage.tabs != 'null'
    )
}

export function get() {
    return JSON.parse(window.localStorage.tabs)
}
export function getActiveIndex() {
    return window.localStorage.activeIndex
}
export function setActiveIndex(activeIndex) {
    window.localStorage.activeIndex = activeIndex.value
}

export function set(pages, activeIndex) {
    window.localStorage.tabs = JSON.stringify(pages.value)
    window.localStorage.activeIndex = activeIndex.value
    return true
}

export function clear() {
    window.localStorage.tabs = null
}