<template>
  <div class="page-container">
    <h1>Reçete Yönetimi</h1>
    <button class="btn-grey">Yeni Reçete Ekle (DOKTOR)</button>

    <table>
      <thead>
      <tr>
        <th>İlaç Adı</th>
        <th>Doz</th>
        <th>Açıklama</th>
        <th>Ait Olduğu Muayene ID</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="recete in receteler" :key="recete.id">
        <td>{{ recete.medicationName }}</td>
        <td>{{ recete.dose }}</td>
        <td>{{ recete.instructions }}</td>
        <td>{{ recete.appointmentId }}</td>
        <td>
          <button class="btn-grey btn-small">Düzenle</button>
          <button class="btn-grey btn-small btn-danger">Sil</button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script setup>
// Aynı mantık, 'apiService.getPrescriptions()' çağrılıyor.
import { ref, onMounted } from 'vue';
import apiService from '@/services/apiService';

const receteler = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await apiService.getPrescriptions();
    receteler.value = response.data;
  } catch (err) {
    console.error('Reçeteleri çekerken hata oluştu:', err);
    error.value = 'Reçete verisi alınamadı. (CORS, Auth hatası veya bu rolün yetkisi olmayabilir)';
  }
});
</script>

<style scoped>
/* Stiller diğer listeleme sayfalarıyla aynı */
.page-container { padding: 20px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
th { background-color: #f4f4f4; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 8px 12px; cursor: pointer; border-radius: 4px; margin-right: 5px; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-small { padding: 4px 8px; font-size: 0.9em; }
.btn-danger { border-color: #d9534f; color: #d9534f; }
.btn-danger:hover { background-color: #d9534f; color: white; }
.error-message { color: red; }
</style>
