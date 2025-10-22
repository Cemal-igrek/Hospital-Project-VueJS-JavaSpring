<template>
  <div class="page-container">
    <h1>Giriş Yap</h1>

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
import { authStore } from '@/store/auth';


const username = ref('');
const password = ref('');
const errorMessage = ref(null);

const router = useRouter();

const handleLogin = async () => {
  try {
    const credentials = {
      username: username.value,
      password: password.value
    };

    const response = await apiService.login(credentials);

    authStore.setUser(response.data);

    errorMessage.value = null;


    router.push('/patients');

  } catch (error) {
    console.error('Giriş hatası:', error);
    authStore.logout();
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
