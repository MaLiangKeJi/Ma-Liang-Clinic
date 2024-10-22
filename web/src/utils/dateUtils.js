/**获取均为两位数的时分秒字符串 */
export function getTimeStr(date) {
    if(!(date instanceof Date)){
        throw new Error('请输入Date 类型参数');
    }
    const now = date;
    // 获取小时、分钟和秒
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    // 拼接小时、分钟和秒
    const resultStr = `${hours}${minutes}${seconds}`;

    return resultStr;
}

/**
 * @param {均为两位数的时分秒} timeStr 示例: 120000
 */
export function getDate(date, timeStr) {
    let hour = timeStr.substring(0, 2);
    let minute = timeStr.substring(2, 4);
    let second = timeStr.substring(4, 6);
    date.setHours(hour);
    date.setMinutes(minute);
    date.setSeconds(second);
    return date;
}

//'yyyy-MM-dd HH:mm:ss'->2023-02-18 21:49:05
//'yyyy年MM月dd日 HH:mm:ss'->2023年02月18日 21:49:05
//'yyyy-MM-dd hh:mm:ss A'->2023-02-18 21:49:05 下午
export function formatDateTime(date, format) {
    const o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'h+': date.getHours() % 12 === 0 ? 12 : date.getHours() % 12, // 小时
        'H+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        S: date.getMilliseconds(), // 毫秒
        a: date.getHours() < 12 ? '上午' : '下午', // 上午/下午
        A: date.getHours() < 12 ? 'AM' : 'PM', // AM/PM
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (let k in o) {
        if (new RegExp('(' + k + ')').test(format)) {
            format = format.replace(
                RegExp.$1,
                RegExp.$1.length === 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
            );
        }
    }
    return format;
}