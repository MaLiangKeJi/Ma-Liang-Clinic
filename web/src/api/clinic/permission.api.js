import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例

const permissionService = {
    getResourcePageRouter(params, callback){
        return axios.get(`/api/auth/permission/resource/page`, { params }).then(({status, data}) => {
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

export default permissionService;