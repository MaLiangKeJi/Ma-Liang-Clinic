export function isUsable() {
    return (
        window.localStorage.patient != null && 
        window.localStorage.patient != undefined && 
        window.localStorage.patient != 'null'
    )
}

export function get() {
    return JSON.parse(window.localStorage.patient)
}

export function set(obj) {
    window.localStorage.patient = JSON.stringify(obj)
    return true
}

export function clear() {
    window.localStorage.patient = null
}