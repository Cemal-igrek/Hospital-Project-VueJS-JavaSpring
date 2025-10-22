<template>
  <div class="page-container">
    <h1>Kullanıcı Yönetimi (Admin)</h1>

    <RouterLink
      to="/users/new"
      class="btn-grey add-button">
      Yeni Kullanıcı Ekle
    </RouterLink>

    <table>
      <thead>
      <tr>
        <th>Kullanıcı Adı</th>
        <th>Email</th>
        <th>Rol</th>
        <th>Aktif mi?</th>
        <th>İşlemler</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="kullanici in kullanicilar" :key="kullanici.id">
        <td>{{ kullanici.username }}</td>
        <td>{{ kullanici.email }}</td>
        <td>{{ kullanici.role }}</td>
        <td>{{ kullanici.active ? 'Evet' : 'Hayır' }}</td>
        <td>
          <RouterLink
            :to="`/users/${kullanici.id}/edit`"
            class="btn-grey btn-small">
            Düzenle
          </RouterLink>

          <button
            @click="handleDeleteUser(kullanici.id)"
            class="btn-grey btn-small btn-danger">
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

const kullanicilar = ref([]);
const loading = ref(false);
const error = ref(null);

const fetchUsers = async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await apiService.getUsers();
    kullanicilar.value = response.data;
  } catch (err) {
    error.value = 'Kullanıcı verisi alınamadı.';
    console.error(err);
  } finally {
    loading.value = false;
  }
};

onMounted(fetchUsers);

const handleDeleteUser = async (id) => {
  if (confirm('Bu kullanıcıyı silmek istediğinizden emin misiniz?')) {
    try {
      await apiService.deleteUser(id);
      await fetchUsers();
    } catch (err) {
      error.value = 'Kullanıcı silinemedi.';
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
