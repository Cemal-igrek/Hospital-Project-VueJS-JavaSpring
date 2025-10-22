<template>
  <div class="page-container">
    <h1>Hasta Yönetimi</h1>

    <RouterLink
      to="/patients/new"
      class="btn-grey add-button"
      v-if="canAdmin || canSecretary">
      Yeni Hasta Ekle
    </RouterLink>

    <table>
      <thead>
      <tr>
        <th>Adı Soyadı</th>
        <th>T.C. Kimlik No</th>
        <th>Telefon</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="hasta in hastalar" :key="hasta.id">
        <td>{{ hasta.fullName }}</td>
        <td>{{ hasta.nationalId }}</td>
        <td>{{ hasta.phone }}</td>
        <td>
          <RouterLink
            :to="`/patients/${hasta.id}/edit`"
            class="btn-grey btn-small"
            v-if="canAdmin || canSecretary">
            Düzenle
          </RouterLink>

          <button
            @click="handleDeletePatient(hasta.id)"
            class="btn-grey btn-small btn-danger"
            v-if="canAdmin">
            Sil
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="loading">Yükleniyor...</p>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { RouterLink } from 'vue-router'; // RouterLink'i import et
import apiService from '@/services/apiService';
import { authStore } from '@/store/auth'; // Rol kontrolü için store

const hastalar = ref([]);
const loading = ref(false);
const errorMessage = ref(null);

// Rol kontrolü için computed değişkenler
const user = computed(() => authStore.user);
const canAdmin = computed(() => user.value?.role === 'ADMIN');
const canSecretary = computed(() => user.value?.role === 'SECRETARY');

// Veriyi çekme fonksiyonu (tekrar kullanılabilir hale getirdik)
const fetchPatients = async () => {
  loading.value = true;
  errorMessage.value = null;
  try {
    const response = await apiService.getPatients();
    hastalar.value = response.data;
  } catch (error) {
    console.error('Hastaları çekerken hata:', error);
    errorMessage.value = 'Hasta verileri alınamadı.';
  } finally {
    loading.value = false;
  }
};

// Component yüklendiğinde hastaları çek
onMounted(fetchPatients);

// SİL BUTONU METODU
const handleDeletePatient = async (id) => {
  // Kullanıcıdan onay al
  if (confirm('Bu hastayı silmek istediğinizden emin misiniz?')) {
    try {
      // API'yi çağır
      await apiService.deletePatient(id);
      // Başarılı olursa:
      // 1. Yöntem: Listeyi yeniden çek (en kolayı)
      fetchPatients();
      // 2. Yöntem: Silinen hastayı 'hastalar' array'inden manuel çıkar (daha performanslı)
      // hastalar.value = hastalar.value.filter(h => h.id !== id);
    } catch (error) {
      console.error('Hasta silinirken hata:', error);
      errorMessage.value = 'Hasta silinemedi.';
    }
  }
};
</script>

<style scoped>
/* Stiller öncekiyle aynı, sadece add-button için margin ekleyebiliriz */
.add-button {
  margin-bottom: 15px;
  display: inline-block; /* Buton gibi davranması için */
}
/* ... (Diğer stil kodları aynı) ... */
.page-container { padding: 20px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
th { background-color: #f4f4f4; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 8px 12px; cursor: pointer; border-radius: 4px; margin-right: 5px; text-decoration: none; /* RouterLink için */ color: inherit; /* RouterLink için */ }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-small { padding: 4px 8px; font-size: 0.9em; }
.btn-danger { border-color: #d9534f; color: #d9534f; }
.btn-danger:hover { background-color: #d9534f; color: white; }
.error-message { color: red; }
</style>
