<template>
  <div class="page-container">
    <h1>{{ isEditMode ? 'Doktor Düzenle' : 'Yeni Doktor Ekle' }}</h1>

    <form @submit.prevent="handleSubmit" class="crud-form">

      <div class="form-group">
        <label for="fullName">Adı Soyadı:</label>
        <input type="text" id="fullName" v-model="doctor.fullName" required />
      </div>

      <div class="form-group">
        <label for="specialty">Uzmanlık Alanı:</label>
        <input type="text" id="specialty" v-model="doctor.specialty" />
      </div>

      <div class="form-group">
        <label for="phone">Telefon:</label>
        <input type="tel" id="phone" v-model="doctor.phone" />
      </div>

      <div class="form-group">
        <label for="userId">İlişkili Kullanıcı ID:</label>
        <input type="number" id="userId" v-model.number="doctor.userId" required />
        <small>Bu doktor profiline bağlanacak User ID'si.</small>
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
import { useRouter, useRoute } from 'vue-router';
import apiService from '@/services/apiService';

// Form verisi
const doctor = ref({
  fullName: '',
  specialty: '',
  phone: '',
  userId: null
});

const errorMessage = ref(null);
const router = useRouter();
const route = useRoute();

const doctorId = computed(() => route.params.id);
const isEditMode = computed(() => !!doctorId.value);

// Düzenleme modundaysa veriyi çek
onMounted(async () => {
  if (isEditMode.value) {
    try {
      const response = await apiService.getDoctorById(doctorId.value);
      doctor.value = response.data;
    } catch (error) {
      console.error('Doktor bilgisi çekilirken hata:', error);
      errorMessage.value = 'Doktor bilgileri yüklenemedi.';
    }
  }
});

// Formu kaydet/güncelle
const handleSubmit = async () => {
  errorMessage.value = null;
  // userId'nin null olmadığından emin olalım
  if (!doctor.value.userId) {
    errorMessage.value = "Lütfen ilişkili bir Kullanıcı ID'si girin.";
    return;
  }

  // API'ye gönderilecek veri (CreateDoctorRequestDto formatında)
  const doctorData = {
    fullName: doctor.value.fullName,
    specialty: doctor.value.specialty,
    phone: doctor.value.phone,
    userId: doctor.value.userId
  };

  try {
    if (isEditMode.value) {
      await apiService.updateDoctor(doctorId.value, doctorData);
    } else {
      await apiService.createDoctor(doctorData);
    }
    router.push('/doctors'); // Listeye geri dön
  } catch (error) {
    console.error('Doktor kaydedilirken/güncellenirken hata:', error);
    errorMessage.value = 'İşlem sırasında bir hata oluştu. (Kullanıcı ID geçerli mi?)';
  }
};

const goBack = () => {
  router.push('/doctors');
};
</script>

<style scoped>
/* Stiller PatientFormView ile aynı */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
.form-group small { font-size: 0.8em; color: #666; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
