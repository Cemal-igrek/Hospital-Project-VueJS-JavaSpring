<template>
  <div class="page-container">
    <h1>Kullanıcı Yönetimi (Admin)</h1>
    <button class="btn-grey">Yeni Kullanıcı Ekle</button>

    <table>
      <thead>
      <tr>
        <th>Kullanıcı Adı</th>
        <th>Email</th>
        <th>Rol</th>
        <th>Aktif mi?</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="kullanici in kullanicilar" :key="kullanici.id">
        <td>{{ kullanici.username }}</td>
        <td>{{ kullanici.email }}</td>
        <td>{{ kullanici.role }}</td>
        <td>{{ kullanici.active ? 'Evet' : 'Hayır' }}</td>
        <td>
          <button class="btn-grey btn-small">Rol Değiştir</button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script setup>
// Aynı mantık, 'apiService.getUsers()' çağrılıyor.
import { ref, onMounted } from 'vue';
import apiService from '@/services/apiService';

const kullanicilar = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await apiService.getUsers();
    kullanicilar.value = response.data;
  } catch (err) {
    console.error('Kullanıcıları çekerken hata oluştu:', err);
    error.value = 'Kullanıcı verisi alınamadı. (Bu sayfayı sadece Admin görebilir)';
  }
});
</script>

<style scoped>
/* Stiller diğer sayfalarla aynı... */
.page-container { padding: 20px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
th { background-color: #f4f4f4; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 8px 12px; cursor: pointer; border-radius: 4px; margin-right: 5px; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-small { padding: 4px 8px; font-size: 0.9em; }
.error-message { color: red; }
</style>
