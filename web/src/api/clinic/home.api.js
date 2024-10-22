import * as util from '@/api/util';

const PATH = `/api/clinic`;

/**查询营业实例 */
export function searchBusiness(callback) {
    util.get(PATH + '/business', {}, callback)
};

/**
 * 开始/停止营业
 * @param {工作标识符} workFlag 0:营业;1:不营业;
 */
export function reverseBusiness(workFlag, callback) {
    util.get(PATH + '/reverse/business', workFlag, callback)
};