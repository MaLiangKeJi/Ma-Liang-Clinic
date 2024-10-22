import { reactive } from 'vue';

/**邀请 */
export const invite = reactive({
    setInvite(inviteCode) {
        window.localStorage.setItem('invite', JSON.stringify(inviteCode));
    },
    getInvite() {
        let inviteStr = window.localStorage.getItem('invite');
        if (inviteStr != null && inviteStr != '')
            return JSON.parse(inviteStr);
    },
    delInvite() {
        window.localStorage.removeItem('invite');
    },
})