import * as util from '@/api/util'
const PATH = `/api/auth/reward`

export function getInviteFirstTaskReward(callback){
        util.get(PATH + `/task/invite/first`, {}, callback)
}

export function getInvitationReward(code, callback){
        util.get(PATH + `/invitation`, { code }, callback)
}