import * as util from '@/api/util'

const PATH = `/store-manage`

const productService = {
    search(params, callback){
        util.get(PATH + '/product/page', params, callback)
    },
    create(params, callback){
        util.put(PATH+'/product', params, callback)
    },
    update(params, callback){
        util.update(PATH+'/product', params, callback)
    },
    delete(params, callback){
        util.del(PATH+'/product', params, callback)
    },
}

export default productService;