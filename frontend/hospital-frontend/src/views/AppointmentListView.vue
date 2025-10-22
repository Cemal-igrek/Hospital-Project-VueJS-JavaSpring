<template>
  <div class="page-container">
    <h1>Muayene Yönetimi</h1>

    <RouterLink
      to="/appointments/new"
      class="btn-grey add-button"
      v-if="canAdmin || canDoctor || canSecretary">
      Yeni Muayene/Randevu Ekle
    </RouterLink>

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
        <td>{{ formatDateTime(muayene.appointmentDate) }}</td>
        <td>{{ muayene.doctor?.fullName }}</td> <td>{{ muayene.patient?.fullName }}</td>
        <td>{{ muayene.diagnosis }}</td>
        <td>
          <RouterLink
            :to="`/appointments/${muayene.id}/edit`"
            class="btn-grey btn-small"
            v-if="canAdmin || canDoctor || canSecretary">
            Düzenle
          </RouterLink>

          <button
            @click="handleDeleteAppointment(muayene.id)"
            class="btn-grey btn-small btn-danger"
            v-if="canAdmin">
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

const muayeneler = ref([]);
const loading = ref(false);
const error = ref(null);

// Rol kontrolü
const user = computed(() => authStore.user);
const canAdmin = computed(() => user.value?.role === 'ADMIN');
const canDoctor = computed(() => user.value?.role === 'DOCTOR');
const canSecretary = computed(() => user.value?.role === 'SECRETARY');

// Veri çekme
const fetchAppointments = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await apiService.getAppointments();
    muayeneler.value = response.data;
  } catch (err) {
    error.value = 'Muayene verisi alınamadı.';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchAppointments);

// Silme
const handleDeleteAppointment = async (id) => {
  if (confirm('Bu muayeneyi silmek istediğinizden emin misiniz?')) {
    try {
      await apiService.deleteAppointment(id);
      fetchAppointments(); // Listeyi yenile
    } catch (err) {
      error.value = 'Muayene silinemedi.';
      console.error(err);
    }
  }
};

// Basit tarih formatlama yardımcısı
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return '';
  try {
    const date = new Date(dateTimeString);
    return date.toLocaleString('tr-TR'); // Lokal tarih/saat formatı
  } catch (e) {
    return dateTimeString; // Hata olursa orijinali döndür
  }
};
</script>

<style scoped>
/* Stiller diğer listeleme sayfalarıyla aynı */
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
