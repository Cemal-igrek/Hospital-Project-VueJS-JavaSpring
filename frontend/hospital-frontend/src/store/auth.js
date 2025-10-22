import { reactive } from 'vue';

export const authStore = reactive({

  user: JSON.parse(sessionStorage.getItem('user')) || null,


  setUser(userData) {
    this.user = userData;
    sessionStorage.setItem('user', JSON.stringify(userData));
  },


  logout() {
    this.user = null;
    sessionStorage.removeItem('user');
  }
});
