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
        <input type="password" id="password" v-model="user.password" required />
      </div>

      <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="user.email" />
      </div>

      <div class="form-group">
        <label for="role">Rol:</label>
        <select id="role" v-model="user.role" required>
          <option disabled value="">Lütfen seçiniz</option>
          <option value="ADMIN">ADMIN</option>
          <option value="DOCTOR">DOKTOR</option>
          <option value="SECRETARY">SEKRETER</option>
        </select>
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
import { ref, onMounted, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import apiService from '@/services/apiService';

// Form verisi
const user = ref({
  username: '',
  password: '', // Sadece ekleme için
  email: '',
  role: '', // Select için başlangıç
  active: true // Düzenleme için
});

const errorMessage = ref(null);
const router = useRouter();
const route = useRoute();

const userId = computed(() => route.params.id);
const isEditMode = computed(() => !!userId.value);

// Component yüklendiğinde
onMounted(async () => {
  // Düzenleme modundaysak, mevcut kullanıcı verisini çek
  if (isEditMode.value) {
    try {
      const response = await apiService.getUserById(userId.value);
      const data = response.data;
      // API'den gelen veriyi form modeline ata
      user.value.username = data.username;
      user.value.email = data.email;
      user.value.role = data.role;
      user.value.active = data.active;
      // Şifre alanı düzenlemede gösterilmez/doldurulmaz
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
      // Düzenleme: UpdateUserRequestDto formatında veri gönder
      const updateData = {
        email: user.value.email,
        role: user.value.role,
        active: user.value.active
      };
      await apiService.updateUser(userId.value, updateData);
    } else {
      // Ekleme: CreateUserRequestDto formatında veri gönder
      if (!user.value.password) {
        errorMessage.value = "Şifre alanı boş bırakılamaz.";
        return;
      }
      const createData = {
        username: user.value.username,
        password: user.value.password,
        email: user.value.email,
        role: user.value.role
      };
      await apiService.createUser(createData);
    }
    router.push('/users'); // Listeye geri dön
  } catch (error) {
    console.error('Kullanıcı kaydedilirken/güncellenirken hata:', error);
    // Backend'den gelen spesifik hata mesajını göstermek daha iyi olur
    errorMessage.value = 'İşlem sırasında bir hata oluştu. (Kullanıcı adı/email zaten var mı?)';
  }
};

const goBack = () => {
  router.push('/users');
};
</script>

<style scoped>
/* Stiller diğer formlarla aynı */
.page-container { padding: 20px; }
.crud-form { max-width: 600px; margin-top: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input,
.form-group select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
/* Disabled input için stil */
.form-group input:disabled { background-color: #eee; cursor: not-allowed; }
.btn-grey { background-color: #f0f0f0; border: 1px solid #ccc; padding: 10px 15px; cursor: pointer; border-radius: 4px; margin-right: 10px; font-weight: bold; }
.btn-grey:hover { background-color: #e0e0e0; }
.btn-secondary { background-color: #ccc; border-color: #bbb; }
.btn-secondary:hover { background-color: #bbb; }
.error-message { color: red; margin-top: 15px; }
</style>
