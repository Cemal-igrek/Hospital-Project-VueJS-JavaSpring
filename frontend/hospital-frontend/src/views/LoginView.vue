<template>
  <div class="page-container">
    <h1>Giriş Yap</h1>
    <p>PDF'e göre login sayfası [cite: 123]</p>

    <form @submit.prevent="handleLogin" class="login-form">
      <div class="form-group">
        <label for="username">Kullanıcı Adı:</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="password">Şifre:</label>
        <input type="password" id="password" v-model="password" required />
      </div>

      <button type="submit" class="btn-grey">Giriş Yap</button>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import apiService from '@/services/apiService';
import { authStore } from '@/store/auth'; // 1. Global store'u import et

// Form inputlarına bağlanacak reaktif değişkenler
const username = ref('');
const password = ref('');
const errorMessage = ref(null);

const router = useRouter(); // Yönlendirici

// Form submit edildiğinde çalışacak metot
const handleLogin = async () => {
  try {
    const credentials = {
      username: username.value,
      password: password.value
    };

    // API'ye giriş isteği at (Backend httpOnly cookie'yi set edecek)
    const response = await apiService.login(credentials);

    // 2. Başarılı girişte dönen UserDto'yu global store'a kaydet
    authStore.setUser(response.data);

    errorMessage.value = null;

    // 3. Kullanıcıyı anasayfaya (veya ilk sayfasına) yönlendir
    router.push('/patients'); // (veya /dashboard gibi bir anasayfa)

  } catch (error) {
    console.error('Giriş hatası:', error);
    authStore.logout(); // Hata olursa store'u temizle
    errorMessage.value = 'Kullanıcı adı veya şifre hatalı.';
  }
};
</script>

<style scoped>
/* Bu sayfaya özel stiller */
.login-form {
  max-width: 400px;
  margin-top: 20px;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box; /* Padding'in genişliği etkilememesi için */
}
.btn-grey {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 4px;
}
.btn-grey:hover {
  background-color: #e0e0e0;
}
.error-message {
  color: red;
  margin-top: 15px;
}
</style>
