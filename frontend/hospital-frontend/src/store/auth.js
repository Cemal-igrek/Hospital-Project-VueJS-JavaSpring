import { reactive } from 'vue';

// Burası, tüm Vue component'lerinin erişebileceği global bir "depo"
// 'reactive' kullanıyoruz ki, 'user' değiştiği an tüm component'ler
// bunu anında fark etsin.

export const authStore = reactive({
  // Başlangıçta kullanıcı yok.
  // sessionStorage'da (tarayıcı sekmesi kapanana kadar)
  // kayıtlı bir kullanıcı var mı diye kontrol ediyoruz.
  user: JSON.parse(sessionStorage.getItem('user')) || null,

  /**
   * Kullanıcıyı state'e ve sessionStorage'a kaydeder.
   * @param {object} userData Backend'den dönen UserDto
   */
  setUser(userData) {
    this.user = userData;
    sessionStorage.setItem('user', JSON.stringify(userData));
  },

  /**
   * Kullanıcıyı sistemden ve sessionStorage'dan siler.
   */
  logout() {
    this.user = null;
    sessionStorage.removeItem('user');
  }
});
