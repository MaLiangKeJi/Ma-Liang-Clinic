import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
// qs.parse() 是将 URL 解析成对象的形式
// qs.stringify() 是将对象 序列化成 URL 的形式，以 & 进行拼接(a=1&b=2)

// JSON.parse() 将 JSON 字符串转换为 JavaScript 对象
// JSON.stringify() 将 JavaScript 对象转换为 JSON 字符串

const tagService = {
    addTag(params){
        return axios.put(`/api/clinic/tag`, params);
    },
    selectTag(params, callback){
        return axios.get(`/api/clinic/tag`, { params }).then(({status, data}) => {
            try {
                if(status === 200 && data.code === 200) {
                    callback(data.data)
                }
            } catch (error) {
                console.error(error)
            }
        })
    },
}

export default tagService;
