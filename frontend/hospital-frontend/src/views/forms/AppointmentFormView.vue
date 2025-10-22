<template>
  <div class="page-container">
    <h1>{{ isEditMode ? 'Muayene Düzenle' : 'Yeni Muayene/Randevu Ekle' }}</h1>

    <form @submit.prevent="handleSubmit" class="crud-form">

      <div class="form-group">
        <label for="doctorId">Doktor:</label>
        <select id="doctorId" v-model="appointment.doctorId" required>
          <option disabled value="">Lütfen seçiniz</option>
          <option v-for="doc in availableDoctors" :key="doc.id" :value="doc.id">
            {{ doc.fullName }} ({{ doc.specialty }})
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="patientId">Hasta:</label>
        <select id="patientId" v-model="appointment.patientId" required>
          <option disabled value="">Lütfen seçiniz</option>
          <option v-for="pat in availablePatients" :key="pat.id" :value="pat.id">
            {{ pat.fullName }} ({{ pat.nationalId }})
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="appointmentDate">Tarih ve Saat:</label>
        <input type="datetime-local" id="appointmentDate" v-model="appointment.appointmentDate" required />
      </div>

      <div class="form-group" v-if="canAdmin || canDoctor">
        <label for="diagnosis">Tanı:</label>
        <textarea id="diagnosis" v-model="appointment.diagnosis" rows="4"></textarea>
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
import { authStore } from '@/store/auth'; // Rol kontrolü için

// Form verisi
const appointment = ref({
  doctorId: '', // Select için başlangıç değeri boş olmalı
  patientId: '',
  appointmentDate: '',
  diagnosis: ''
});

// Dropdown'lar için listeler
const availableDoctors = ref([]);
const availablePatients = ref([]);

const errorMessage = ref(null);
const router = useRouter();
const route = useRoute();

const appointmentId = computed(() => route.params.id);
const isEditMode = computed(() => !!appointmentId.value);

// Rol kontrolleri
const user = computed(() => authStore.user);
const canAdmin = computed(() => user.value?.role === 'ADMIN');
const canDoctor = computed(() => user.value?.role === 'DOCTOR');

// Component yüklendiğinde hem dropdown verilerini hem de (varsa) düzenlenecek randevuyu çek
onMounted(async () => {
  // 1. Doktorları ve Hastaları çek (Dropdown için)
  try {
    const [doctorsRes, patientsRes] = await Promise.all([
      apiService.getDoctors(),
      apiService.getPatients()
    ]);
    availableDoctors.value = doctorsRes.data;
    availablePatients.value = patientsRes.data;
  } catch (error) {
    errorMessage.value = 'Doktor veya Hasta listesi yüklenemedi.';
    console.error(error);
  }

  // 2. Düzenleme modundaysak, mevcut randevu verisini çek
  if (isEditMode.value) {
    try {
      const response = await apiService.getAppointmentById(appointmentId.value);
      const data = response.data;
      // API'den gelen veriyi form modeline ata
      appointment.value.doctorId = data.doctor.id; // İç içe objeden ID'yi al
      appointment.value.patientId = data.patient.id;
      // Tarih formatı backend'den nasıl geliyorsa ona göre parse etmek gerekebilir
      appointment.value.appointmentDate = data.appointmentDate.substring(0, 16); // 'YYYY-MM-DDTHH:mm' formatı
      appointment.value.diagnosis = data.diagnosis;
    } catch (error) {
      errorMessage.value = 'Muayene bilgileri yüklenemedi.';
      console.error(error);
    }
  }
});

// Formu kaydet/güncelle
const handleSubmit = async () => {
  errorMessage.value = null;
  // API'ye gönderilecek veri (CreateAppointmentRequestDto formatında)
  const appointmentData = {
    doctorId: appointment.value.doctorId,
    patientId: appointment.value.patientId,
    appointmentDate: appointment.value.appointmentDate,
    diagnosis: appointment.value.diagnosis
  };

  try {
    if (isEditMode.value) {
      await apiService.updateAppointment(appointmentId.value, appointmentData);
    } else {
      await apiService.createAppointment(appointmentData);
    }
    router.push('/appointments'); // Listeye geri dön
  } catch (error) {
    console.error('Muayene kaydedilirken/güncellenirken hata:', error);
    errorMessage.value = 'İşlem sırasında bir hata oluştu.';
  }
};

const goBack = () => {
  router.push('/appointments');
};
</script>

<style scoped>
/* Stiller diğer formlarla benzer, select ve textarea eklendi */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input,
.form-group select,
.form-group textarea { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
