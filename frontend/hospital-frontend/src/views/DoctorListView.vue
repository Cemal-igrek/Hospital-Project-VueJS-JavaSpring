<template>
  <div class="page-container">
    <h1>Doktor Yönetimi</h1>
    <button class="btn-grey">Yeni Doktor Ekle</button>

    <table>
      <thead>
      <tr>
        <th>Adı Soyadı</th>
        <th>Uzmanlık Alanı</th>
        <th>Telefon</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="doktor in doktorlar" :key="doktor.id">
        <td>{{ doktor.fullName }}</td>
        <td>{{ doktor.specialty }}</td>
        <td>{{ doktor.phone }}</td>
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
// Bu yapı PatientListView ile birebir aynı, sadece
// apiService.getDoctors() çağrılıyor.

import { ref, onMounted } from 'vue';
import apiService from '@/services/apiService';

const doktorlar = ref([]);
const error = ref(null);

// Component yüklendiğinde (onMounted) veriyi çek
onMounted(async () => {
  try {
    const response = await apiService.getDoctors();
    doktorlar.value = response.data;
  } catch (err) {
    console.error('Doktorları çekerken hata oluştu:', err);
    error.value = 'Doktor verisi alınamadı. (CORS veya Auth hatası olabilir)';
  }
});
</script>

<style scoped>
/* Stil kodları PatientListView.vue ile aynı.
  Normalde bu stiller (buton, tablo) 'src/assets/main.css' gibi
  global bir dosyaya taşınır ki tekrar yazmak gerekmesin.
  Ama basitlik için şimdilik kopyala-yapıştır yapabiliriz.
*/
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
