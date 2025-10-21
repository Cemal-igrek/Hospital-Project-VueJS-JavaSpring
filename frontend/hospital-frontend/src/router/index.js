import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '@/store/auth' // Global store'u import et

// View'ları import et
import LoginView from '../views/LoginView.vue'
import PatientListView from '../views/PatientListView.vue'
import DoctorListView from '../views/DoctorListView.vue'
import AppointmentListView from '../views/AppointmentListView.vue'
import PrescriptionListView from '../views/PrescriptionListView.vue'
import UserListView from '../views/UserListView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: { requiresGuest: true } // Giriş yapan bu sayfayı göremez
  },
  {
    path: '/patients',
    name: 'patients',
    component: PatientListView,
    meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } // Yetkili rolleri ekle
  },
  {
    path: '/doctors',
    name: 'doctors',
    component: DoctorListView,
    meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] }
  },
  {
    path: '/appointments',
    name: 'appointments',
    component: AppointmentListView,
    meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] }
  },
  {
    path: '/prescriptions',
    name: 'prescriptions',
    component: PrescriptionListView,
    meta: { roles: ['ADMIN', 'DOCTOR'] } // Sekreter göremez
  },
  {
    path: '/users',
    name: 'users',
    component: UserListView,
    meta: { roles: ['ADMIN'] } // Sadece Admin görebilir
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// *** DÜZELTİLMİŞ ROUTER KORUMASI (NAVIGATION GUARD) ***
router.beforeEach((to, from, next) => {
  const user = authStore.user;

  // Gidilmek istenen sayfanın meta bilgilerini direkt "to" objesinden al
  const requiresRoles = to.meta.roles;
  const requiresGuest = to.meta.requiresGuest;

  // 1. Korumalı bir sayfa (roles[] var) VE kullanıcı giriş yapmamışsa:
  if (requiresRoles && !user) {
    // Kullanıcıyı /login sayfasına yönlendir
    return next({ name: 'login' });
  }

  // 2. "Misafir" sayfası (login gibi) VE kullanıcı giriş yapmışsa:
  if (requiresGuest && user) {
    // Kullanıcıyı ana sayfaya (patients) yönlendir
    return next({ name: 'patients' });
  }

  // 3. Korumalı bir sayfa, kullanıcı var AMA rolü yetersizse:
  if (requiresRoles && user && !requiresRoles.includes(user.role)) {
    // Kullanıcıyı yetkisinin olduğu bir sayfaya (patients) yönlendir
    // (veya bir 'Yetkiniz Yok' (403) sayfası oluşturulabilir)
    return next({ name: 'patients' });
  }

  // Diğer tüm durumlarda (yetkisi varsa veya sayfa korumalı değilse)
  // gitmek istediği yere izin ver:
  next();
});

export default router
