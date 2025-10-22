<template>
  <div class="page-container">
    <h1>{{ isEditMode ? 'Kullanıcı Düzenle' : 'Yeni Kullanıcı Ekle' }}</h1>

    <form @submit.prevent="handleSubmit" class="crud-form">

      <div class="form-group">
        <label for="username">Kullanıcı Adı:</label>
        <input type="text" id="username" v-model="user.username" required :disabled="isEditMode" />
      </div>

      <div class="form-group" v-if="!isEditMode">
        <label for="password">Şifre:</label>
        <input type="password" id="password" v-model="user.password" :required="!isEditMode" />
      </div>

      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="user.email" />
      </div>

      <div class="form-group">
        <label for="role">Rol:</label>
        <select id="role" v-model="user.role" required :disabled="isEditMode">
          <option disabled value="">Lütfen seçiniz</option>
          <option value="ADMIN">ADMIN</option>
          <option value="DOCTOR">DOKTOR</option>
          <option value="SECRETARY">SEKRETER</option>
        </select>
      </div>

      <div v-if="user.role === 'DOCTOR' && !isEditMode" class="doctor-fields">
        <div class="form-group">
          <label for="doctorFullName">Doktor Adı Soyadı:</label>
          <input type="text" id="doctorFullName" v-model="doctorInfo.fullName" :required="user.role === 'DOCTOR' && !isEditMode"/>
        </div>

        <div class="form-group">
          <label for="doctorSpecialty">Uzmanlık Alanı:</label>
          <input type="text" id="doctorSpecialty" v-model="doctorInfo.specialty" />
        </div>

        <div class="form-group">
          <label for="doctorPhone">Telefon:</label>
          <input type="tel" id="doctorPhone" v-model="doctorInfo.phone" />
        </div>

      </div>
      <div class="form-group" v-if="isEditMode">
        <label for="active">Hesap Aktif mi?</label>
        <select id="active" v-model="user.active">
          <option :value="true">Evet</option>
          <option :value="false">Hayır (Devre Dışı)</option>
        </select>
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
import { ref, onMounted, computed, reactive } from 'vue'; // reactive eklendi
import { useRouter, useRoute } from 'vue-router';
import apiService from '@/services/apiService';

// User form verisi
const user = ref({
  username: '',
  password: '',
  email: '',
  role: '',
  active: true
});

// YENİ: Doktor bilgileri için ayrı bir reaktif nesne
const doctorInfo = reactive({
  fullName: '',
  specialty: '',
  phone: ''
});

const errorMessage = ref(null);
const router = useRouter();
const route = useRoute();

const userId = computed(() => route.params.id);
const isEditMode = computed(() => !!userId.value);

onMounted(async () => {
  if (isEditMode.value) {
    try {
      const response = await apiService.getUserById(userId.value);
      const data = response.data;
      user.value.username = data.username;
      user.value.email = data.email;
      user.value.role = data.role;
      user.value.active = data.active;
    } catch (error) {
      errorMessage.value = 'Kullanıcı bilgileri yüklenemedi.';
      console.error(error);
    }
  }
});

// Formu kaydet/güncelle
const handleSubmit = async () => {
  errorMessage.value = null;

  try {
    if (isEditMode.value) {
      const updateData = {
        email: user.value.email,
        role: user.value.role,
        active: user.value.active
      };
      await apiService.updateUser(userId.value, updateData);
    } else {
      if (!user.value.password) {
        errorMessage.value = "Şifre alanı boş bırakılamaz.";
        return;
      }

      const createData = {
        username: user.value.username,
        password: user.value.password,
        email: user.value.email,
        role: user.value.role,
        doctorFullName: user.value.role === 'DOCTOR' ? doctorInfo.fullName : null,
        doctorSpecialty: user.value.role === 'DOCTOR' ? doctorInfo.specialty : null,
        doctorPhone: user.value.role === 'DOCTOR' ? doctorInfo.phone : null,
      };

      if (user.value.role === 'DOCTOR' && !createData.doctorFullName) {
        errorMessage.value = "Rol DOKTOR seçildiğinde Doktor Adı Soyadı zorunludur.";
        return;
      }

      await apiService.createUser(createData);
    }
    router.push('/users'); // Listeye geri dön
  } catch (error) {
    console.error('Kullanıcı kaydedilirken/güncellenirken hata:', error);
    errorMessage.value = error.response?.data?.message || 'İşlem sırasında bir hata oluştu.'; // Backend'den gelen hatayı göster
  }
};

const goBack = () => {
  router.push('/users');
};
</script>

<style scoped>
/* Stiller aynı kalabilir, istersen doctor-fields için ek stil ekleyebilirsin */
.doctor-fields {
  margin-top: 20px;
  margin-bottom: 20px;
  padding-top: 15px;
  border-top: 1px dashed #ccc;
  border-bottom: 1px dashed #ccc;
  padding-bottom: 5px;
}
.doctor-fields h4 {
  margin-top: 0;
  color: #555;
}
/* ... (Diğer stiller aynı) ... */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input,
.form-group select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
.form-group input:disabled { background-color: #eee; cursor: not-allowed; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
