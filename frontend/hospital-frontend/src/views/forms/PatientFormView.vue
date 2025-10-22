<template>
  <div class="page-container">
    <h1>{{ isEditMode ? 'Hasta Düzenle' : 'Yeni Hasta Ekle' }}</h1>

    <form @submit.prevent="handleSubmit" class="crud-form">

      <div class="form-group">
        <label for="fullName">Adı Soyadı:</label>
        <input type="text" id="fullName" v-model="patient.fullName" required />
      </div>

      <div class="form-group">
        <label for="nationalId">T.C. Kimlik No:</label>
        <input type="text" id="nationalId" v-model="patient.nationalId" required />
      </div>

      <div class="form-group">
        <label for="dateOfBirth">Doğum Tarihi:</label>
        <input type="date" id="dateOfBirth" v-model="patient.dateOfBirth" />
      </div>

      <div class="form-group">
        <label for="phone">Telefon:</label>
        <input type="tel" id="phone" v-model="patient.phone" />
      </div>

      <button type="submit" class="btn-grey">
        {{ isEditMode ? 'Güncelle' : 'Kaydet' }}
      </button>

      <button type="button" @click="goBack" class="btn-grey btn-secondary">
        İptal
      </button>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router'; // Yönlendirme ve mevcut route bilgisi için
import apiService from '@/services/apiService';

// Form verilerini tutacak reaktif nesne
const patient = ref({
  fullName: '',
  nationalId: '',
  dateOfBirth: null,
  phone: ''
});

const errorMessage = ref(null);
const router = useRouter(); // Yönlendirme fonksiyonları
const route = useRoute(); // Mevcut route (URL) bilgileri (/patients/:id/edit gibi)

// URL'de 'id' parametresi varsa düzenleme modundayız demektir
const patientId = computed(() => route.params.id);
const isEditMode = computed(() => !!patientId.value); // ID varsa true, yoksa false

// Component yüklendiğinde çalışır
onMounted(async () => {
  // Eğer düzenleme modundaysak (URL'de ID varsa)
  if (isEditMode.value) {
    try {
      // API'den o ID'li hastanın bilgilerini çek
      const response = await apiService.getPatientById(patientId.value);
      // Gelen veriyi formdaki 'patient' nesnesine ata
      patient.value = response.data;
    } catch (error) {
      console.error('Hasta bilgisi çekilirken hata:', error);
      errorMessage.value = 'Hasta bilgileri yüklenemedi.';
    }
  }
});

// Form submit edildiğinde çalışacak metot
const handleSubmit = async () => {
  errorMessage.value = null; // Önceki hataları temizle
  try {
    if (isEditMode.value) {
      // Düzenleme modundaysak: Update API'sini çağır
      await apiService.updatePatient(patientId.value, patient.value);
    } else {
      // Ekleme modundaysak: Create API'sini çağır
      await apiService.createPatient(patient.value);
    }
    // Başarılı olursa hasta listesi sayfasına geri dön
    router.push('/patients');
  } catch (error) {
    console.error('Hasta kaydedilirken/güncellenirken hata:', error);
    errorMessage.value = 'İşlem sırasında bir hata oluştu.';
  }
};

// İptal butonuna basıldığında listeye geri dön
const goBack = () => {
  router.push('/patients');
};
</script>

<style scoped>
/* Diğer view'lardaki stillere ek olarak form stilleri */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
