<template>
  <div class="page-container">
    <h1>Hasta Yönetimi</h1>

    <button class="btn-grey">Yeni Hasta Ekle</button>

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
          <button class="btn-grey btn-small">Düzenle</button>
          <button class="btn-grey btn-small btn-danger">Sil</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
// Burası Vue 3'ün modern JavaScript (<script>) bloğudur

// 1. Gerekli Vue fonksiyonlarını ve API servisimizi import ediyoruz
import { ref, onMounted } from 'vue';
import apiService from '@/services/apiService';

// 2. 'hastalar' adında reaktif bir değişken oluşturuyoruz.
// 'ref', içindeki veri değiştiğinde Vue'nin haberdar olmasını ve
// <template> bölümünü otomatik güncellemesini sağlar.
const hastalar = ref([]);

// 3. 'onMounted', Vue component'i (sayfası) ekrana yüklendiği an
// bir kez çalışan bir fonksiyondur (Angular'daki ngOnInit gibi).
onMounted(async () => {
  try {
    // API servisimizden hastaları çekiyoruz
    const response = await apiService.getPatients();

    // Gelen veriyi 'hastalar' reaktif değişkenimize atıyoruz.
    // 'ref' ile oluşturulan değişkene erişmek için '.value' kullanılır.
    hastalar.value = response.data;
  } catch (error) {
    console.error('Hastaları çekerken hata oluştu:', error);
    // TODO: Kullanıcıya bir hata mesajı göster
  }
});
</script>

<style scoped>
/* 'scoped' kelimesi, bu CSS'in SADECE bu component (.vue dosyası)
 içinde geçerli olmasını sağlar, başka sayfaları etkilemez.
*/
.page-container {
  padding: 20px;
  background-color: #fff; /* PDF'in istediği beyaz zemin [cite: 7] */
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f4f4f4;
}

/* PDF'in istediği gri butonlar [cite: 7] */
.btn-grey {
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 4px;
  margin-right: 5px;
}

.btn-grey:hover {
  background-color: #e0e0e0;
}

.btn-small {
  padding: 4px 8px;
  font-size: 0.9em;
}

.btn-danger {
  border-color: #d9534f;
  color: #d9534f;
}

.btn-danger:hover {
  background-color: #d9534f;
  color: white;
}
</style>
