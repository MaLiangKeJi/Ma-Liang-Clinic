import * as util from '@/api/util';

const PATH = `/api/clinic/feedback`;

export function add(param, callback) {
    util.put(PATH, param, callback,true)
};