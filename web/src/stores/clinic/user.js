export function isUsable() {
    return (
        window.localStorage.zsyftxglptMh != null && 
        window.localStorage.zsyftxglptMh != undefined && 
        window.localStorage.zsyftxglptMh != 'null'
    )
}

export function get() {
    return JSON.parse(window.localStorage.zsyftxglptMh)
}

export function set(obj) {
    window.localStorage.zsyftxglptMh = JSON.stringify(obj)
    return true
}

export function clear() {
    window.localStorage.zsyftxglptMh = null
}