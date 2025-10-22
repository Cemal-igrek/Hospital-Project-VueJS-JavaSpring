<template>
  <div class="page-container">
    <h1>Reçete Yönetimi</h1>

    <RouterLink
      to="/prescriptions/new"
      class="btn-grey add-button"
      v-if="canDoctor">
      Yeni Reçete Ekle
    </RouterLink>

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
          <RouterLink
            :to="`/prescriptions/${recete.id}/edit`"
            class="btn-grey btn-small"
            v-if="canAdmin || canDoctor">
            Düzenle
          </RouterLink>

          <button
            @click="handleDeletePrescription(recete.id)"
            class="btn-grey btn-small btn-danger"
            v-if="canAdmin || canDoctor">
            Sil
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="loading">Yükleniyor...</p>
    <p v-if="error" class="error-message">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { RouterLink } from 'vue-router';
import apiService from '@/services/apiService';
import { authStore } from '@/store/auth';

const receteler = ref([]);
const loading = ref(false);
const error = ref(null);

const user = computed(() => authStore.user);
const canAdmin = computed(() => user.value?.role === 'ADMIN');
const canDoctor = computed(() => user.value?.role === 'DOCTOR');

const fetchPrescriptions = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await apiService.getPrescriptions();
    receteler.value = response.data;
  } catch (err) {
    error.value = 'Reçete verisi alınamadı.';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchPrescriptions);

const handleDeletePrescription = async (id) => {
  if (confirm('Bu reçeteyi silmek istediğinizden emin misiniz?')) {
    try {
      await apiService.deletePrescription(id);
      fetchPrescriptions();
    } catch (err) {
      error.value = 'Reçete silinemedi.';
      console.error(err);
    }
  }
};
</script>

<style scoped>
.add-button { margin-bottom: 15px; display: inline-block; }
.page-container { padding: 20px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
th { background-color: #f4f4f4; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 8px 12px; cursor: pointer; border-radius: 4px; margin-right: 5px; text-decoration: none; color: inherit; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-small { padding: 4px 8px; font-size: 0.9em; }
.btn-danger { border-color: #d9534f; color: #d9534f; }
.btn-danger:hover { background-color: #d9534f; color: white; }
.error-message { color: red; }
</style>
