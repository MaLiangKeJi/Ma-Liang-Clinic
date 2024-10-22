/** 
 * api 接口统一出口管理
 */

import wareService from '@/api/warehouse.api.js'; // 内容
import streamService from '@/api/stream.api.js'; // 审批
import productService from '@/api/product.api.js'; // 内容

export default {
    wareService,
    productService,
    streamService,
}