<script setup></script>

<template>
  <div id="app-layout">

    <div class="sidebar" v-if="user">
      <h2>Hastane YS</h2>
      <nav>
        <RouterLink
          to="/patients"
          v-if="canSecretary || canDoctor || canAdmin">
          Hasta Yönetimi
        </RouterLink>

        <RouterLink
          to="/doctors"
          v-if="canSecretary || canDoctor || canAdmin">
          Doktor Listesi
        </RouterLink>

        <RouterLink
          to="/appointments"
          v-if="canSecretary || canDoctor || canAdmin">
          Muayene / Randevu
        </RouterLink>

        <RouterLink
          to="/prescriptions"
          v-if="canDoctor || canAdmin">
          Reçete Yönetimi
        </RouterLink>

        <RouterLink
          to="/users"
          v-if="canAdmin">
          Kullanıcı Yönetimi
        </RouterLink>
      </nav>

      <div class="user-footer">
        <span>Hoşgeldin, {{ user.username }} ({{ user.role }})</span>
        <button @click="handleLogout" class="btn-logout">Çıkış Yap</button>
      </div>
    </div>

    <div class="main-content">
      <RouterView />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { RouterLink, RouterView, useRouter } from 'vue-router';
import { authStore } from '@/store/auth';

const router = useRouter();

// Store'daki 'user' verisine reaktif bir şekilde bağlan
const user = computed(() => authStore.user);

// Rolleri daha kolay kontrol etmek için computed (hesaplanmış) değişkenler
const canAdmin = computed(() => user.value?.role === 'ADMIN');
const canDoctor = computed(() => user.value?.role === 'DOCTOR');
const canSecretary = computed(() => user.value?.role === 'SECRETARY');

// Çıkış yap metodu
const handleLogout = () => {
  authStore.logout();
  // TODO: Backend'de bir /api/auth/logout endpoint'i varsa
  // onu da çağırıp cookie'yi temizlemek gerekir.

  // Kullanıcıyı login sayfasına yönlendir
  router.push('/login');
};
</script>

<style scoped>
#app-layout {
  display: flex;
  height: 100vh;
  background-color: #fff;
}

.sidebar {
  width: 240px;
  background-color: #f9f9f9;
  border-right: 1px solid #e0e0e0;
  padding: 20px;
  display: flex;
  flex-direction: column;
  transition: width 0.3s;
}

.sidebar h2 {
  margin-top: 0;
  text-align: center;
  color: #333;
}

.sidebar nav {
  display: flex;
  flex-direction: column;
  margin-top: 20px;
  flex-grow: 1; /* Navigasyonun kalan boşluğu doldurmasını sağlar */
}

.sidebar nav a {
  padding: 12px 15px;
  text-decoration: none;
  color: #444;
  border-radius: 4px;
  margin-bottom: 8px;
  font-weight: 500;
}

.sidebar nav a:hover {
  background-color: #e0e0e0;
}

.sidebar nav a.router-link-exact-active {
  background-color: #e0e0e0;
  color: #000;
  font-weight: 600;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #fff; /* PDF'in istediği beyaz zemin */
}

.user-footer {
  margin-top: auto; /* En alta iter */
  padding-top: 15px;
  border-top: 1px solid #ddd;
  text-align: center;
}
.user-footer span {
  display: block;
  font-size: 0.9em;
  color: #666;
  margin-bottom: 10px;
}

.btn-logout {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 8px 12px;
  width: 100%;
  border-radius: 4px;
  cursor: pointer;
}
.btn-logout:hover {
  background-color: #d32f2f;
}
</style>

<style scoped></style>
