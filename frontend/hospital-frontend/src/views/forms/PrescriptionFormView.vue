<template>
  <div class="page-container">
    <h1>{{ isEditMode ? 'Reçete Düzenle' : 'Yeni Reçete Ekle' }}</h1>

    <form @submit.prevent="handleSubmit" class="crud-form">

      <div class="form-group">
        <label for="appointmentId">Ait Olduğu Muayene:</label>
        <select id="appointmentId" v-model="prescription.appointmentId" required :disabled="isEditMode">
          <option disabled value="">Lütfen seçiniz</option>
          <option v-for="app in availableAppointments" :key="app.id" :value="app.id">
            ID: {{ app.id }} - {{ app.patient?.fullName }} (Dr. {{ app.doctor?.fullName }})
          </option>
        </select>
        <small v-if="isEditMode">Mevcut reçetenin muayenesi değiştirilemez.</small>
      </div>

      <div class="form-group">
        <label for="medicationName">İlaç Adı:</label>
        <input type="text" id="medicationName" v-model="prescription.medicationName" required />
      </div>

      <div class="form-group">
        <label for="dose">Doz:</label>
        <input type="text" id="dose" v-model="prescription.dose" />
      </div>

      <div class="form-group">
        <label for="instructions">Açıklama / Kullanım Talimatı:</label>
        <textarea id="instructions" v-model="prescription.instructions" rows="3"></textarea>
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
const prescription = ref({
  appointmentId: '',
  medicationName: '',
  dose: '',
  instructions: ''
});

// Muayene dropdown'u için liste
const availableAppointments = ref([]);

const errorMessage = ref(null);
const router = useRouter();
const route = useRoute();

const prescriptionId = computed(() => route.params.id);
const isEditMode = computed(() => !!prescriptionId.value);

// Component yüklendiğinde
onMounted(async () => {
  // 1. Muayeneleri çek (Dropdown için)
  try {
    const response = await apiService.getAppointments();
    availableAppointments.value = response.data;
  } catch (error) {
    errorMessage.value = 'Muayene listesi yüklenemedi.';
    console.error(error);
  }

  // 2. Düzenleme modundaysak, mevcut reçete verisini çek
  if (isEditMode.value) {
    try {
      const response = await apiService.getPrescriptionById(prescriptionId.value);
      const data = response.data;
      // API'den gelen veriyi form modeline ata
      prescription.value.appointmentId = data.appointmentId;
      prescription.value.medicationName = data.medicationName;
      prescription.value.dose = data.dose;
      prescription.value.instructions = data.instructions;
    } catch (error) {
      errorMessage.value = 'Reçete bilgileri yüklenemedi.';
      console.error(error);
    }
  }
});

// Formu kaydet/güncelle
const handleSubmit = async () => {
  errorMessage.value = null;
  // API'ye gönderilecek veri (CreatePrescriptionRequestDto formatında)
  const prescriptionData = {
    appointmentId: prescription.value.appointmentId,
    medicationName: prescription.value.medicationName,
    dose: prescription.value.dose,
    instructions: prescription.value.instructions
  };

  try {
    if (isEditMode.value) {
      await apiService.updatePrescription(prescriptionId.value, prescriptionData);
    } else {
      await apiService.createPrescription(prescriptionData);
    }
    router.push('/prescriptions'); // Listeye geri dön
  } catch (error) {
    console.error('Reçete kaydedilirken/güncellenirken hata:', error);
    errorMessage.value = 'İşlem sırasında bir hata oluştu.';
  }
};

const goBack = () => {
  router.push('/prescriptions');
};
</script>

<style scoped>
/* Stiller diğer formlarla benzer */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input,
.form-group select,
.form-group textarea { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
.form-group small { font-size: 0.8em; color: #666; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
