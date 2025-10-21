<template>
  <div class="page-container">
    <h1>Muayene Yönetimi</h1>
    <button class="btn-grey">Yeni Muayene Ekle</button>

    <table>
      <thead>
      <tr>
        <th>Tarih</th>
        <th>Doktor</th>
        <th>Hasta</th>
        <th>Tanı</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="muayene in muayeneler" :key="muayene.id">
        <td>{{ muayene.appointmentDate }}</td>
        <td>{{ muayene.doctor.fullName }}</td>
        <td>{{ muayene.patient.fullName }}</td>
        <td>{{ muayene.diagnosis }}</td>
        <td>
          <button class="btn-grey btn-small">Detay</button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script setup>
// Aynı mantık, 'apiService.getAppointments()' çağrılıyor.
import { ref, onMounted } from 'vue';
import apiService from '@/services/apiService';

const muayeneler = ref([]);
const error = ref(null);

onMounted(async () => {
  try {
    const response = await apiService.getAppointments();
    muayeneler.value = response.data;
  } catch (err) {
    console.error('Muayeneleri çekerken hata oluştu:', err);
    error.value = 'Muayene verisi alınamadı. (CORS veya Auth hatası olabilir)';
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
