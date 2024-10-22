export function convertDateRow(row, column, cellValue, index){   //定义日期格式转换函数
    let date = new Date(cellValue)
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    let dateString = `${year}-${month}-${day}`;
    return dateString  //拼接成yyyy-mm-dd形式的字符串
}
export function convertDate(cellValue){   //定义日期格式转换函数
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
    return cellValue ? cellValue : '无'
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