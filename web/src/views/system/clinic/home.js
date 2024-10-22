import routerInstance from '@/router/index'

export function convertDate(row, column, cellValue, index){   //定义日期格式转换函数
    let date = new Date(cellValue)
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    let dateString = `${year}-${month}-${day}`;
    return dateString  //拼接成yyyy-mm-dd形式的字符串
}

export function convertName(row, column, cellValue, index) {
    return cellValue ? cellValue : '无'
}
export function convertSex(row, column, cellValue, index) {
    return cellValue == 0 ? '女' : '男'
}
export function convertAge(row, column, cellValue, index) {
    return cellValue ? cellValue + '岁' : '无'
}
export function convertAddr(row, column, cellValue, index) {
    return cellValue ? cellValue : '无'
}
export function convertIsFirst(row, column, cellValue, index) {
    return cellValue == 0 ? '初诊' : '复诊'
}
export function convertDiagnosis(row, column, cellValue, index) {
    return cellValue ? cellValue : '无'
}
export function convertPhone(row, column, cellValue, index) {
    return cellValue ? cellValue : '无'
}
export function filterSexHandler(value, row) {
    return value == row.sex
}
export function formatDateToYMD(date) {
    let year = date.getFullYear();
    let month = (1 + date.getMonth()).toString().padStart(2, '0');
    let day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
}
export function getStartOfDay(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
}
export function getEndOfDay(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate(), 23, 59, 59, 999);
}

export function toLogoutPage() {
    routerInstance.push({ path: '/clinic/login' })
}

export function toMePage() {
    routerInstance.push({ path: '/me' })
}

/**带是否营业的我的页面 */
export function toMePageByWork(isWork) {
    routerInstance.push({ path: '/me/'+isWork });
}

function openNewTab(path) {
    let routeData = routerInstance.resolve({
        path,
    });
    window.open(routeData.href, "_blank");
    submitLoading.close()
}
export function toProgress() {
    routerInstance.push({ path: `/clinic/doctor/patient/add` })
}
export function toPayList() {
    openNewTab(`/clinic/pay/list`)
}
export function toAddRetail() {
    openNewTab(`/clinic/retail/add`)
}
export function toStockList() {
    openNewTab(`/clinic/stock/list`)
}
export function toAddDisinfection() {
    openNewTab(`/clinic/disinfection/add`)
}
export function toAddSterilize() {
    openNewTab(`/clinic/sterilize/add`)
}
export function toAddProof() {
    openNewTab(`/clinic/proof/add`)
}

export function toRenew() {
    routerInstance.push({
        path: '/clinic/member'
    })
}

export function formatTimeToHMS(date) {
    let hours = date.getHours().toString().padStart(2, '0');
    let minutes = date.getMinutes().toString().padStart(2, '0');
    let seconds = date.getSeconds().toString().padStart(2, '0');
    return `${hours}:${minutes}:${seconds}`;
}