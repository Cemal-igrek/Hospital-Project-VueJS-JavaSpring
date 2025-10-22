<template>
  <div class="login-page">
    <div class="login-card">
      <h1 class="title">🔐 Giriş Yap</h1>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label for="username">Kullanıcı Adı</label>
          <input
            type="text"
            id="username"
            v-model="username"
            placeholder="Kullanıcı adınızı girin"
            required
          />
        </div>

        <div class="form-group">
          <label for="password">Şifre</label>
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="Şifrenizi girin"
            required
          />
        </div>

        <button type="submit" class="btn-login">Giriş Yap</button>

        <p v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </p>
      </form>
    </div>
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
/* === Genel Sayfa Yapısı === */
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  font-family: "Inter", "Segoe UI", Roboto, sans-serif;
}

/* === Login Kartı === */
.login-card {
  background-color: #ffffff;
  padding: 40px 35px;
  border-radius: 16px;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 400px;
  text-align: center;
  animation: fadeIn 0.6s ease-in-out;
}

.title {
  margin-bottom: 25px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 1.8rem;
}

/* === Form === */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.form-group {
  text-align: left;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #34495e;
  font-weight: 500;
  font-size: 0.95rem;
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #dcdde1;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #4ca1af;
  box-shadow: 0 0 4px rgba(76, 161, 175, 0.5);
}

/* === Buton === */
.btn-login {
  background-color: #4ca1af;
  color: #fff;
  border: none;
  padding: 12px 0;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.25s ease, transform 0.1s ease;
}

.btn-login:hover {
  background-color: #3b8c99;
  transform: translateY(-1px);
}

.error-message {
  color: #e74c3c;
  font-size: 0.9rem;
  margin-top: 10px;
}

/* === Animasyon === */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
