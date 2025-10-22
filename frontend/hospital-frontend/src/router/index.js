import { createRouter, createWebHistory } from 'vue-router'
import { authStore } from '@/store/auth'
import LoginView from "@/views/LoginView.vue";
import PatientListView from "@/views/PatientListView.vue";
import PatientFormView from "@/views/forms/PatientFormView.vue";
import DoctorListView from "@/views/DoctorListView.vue";
import DoctorFormView from "@/views/forms/DoctorFormView.vue";
import AppointmentListView from "@/views/AppointmentListView.vue";
import AppointmentFormView from "@/views/forms/AppointmentFormView.vue";
import PrescriptionListView from "@/views/PrescriptionListView.vue";
import PrescriptionFormView from "@/views/forms/PrescriptionFormView.vue";
import UserFormView from "@/views/forms/UserFormView.vue";
import UserListView from "@/views/UserListView.vue";


const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'login', component: LoginView, meta: { requiresGuest: true } },

  // --- Hasta Rotaları ---
  { path: '/patients', name: 'patients', component: PatientListView, meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } },
  { path: '/patients/new', name: 'patient-new', component: PatientFormView, meta: { roles: ['ADMIN', 'SECRETARY'] } },
  { path: '/patients/:id/edit', name: 'patient-edit', component: PatientFormView, meta: { roles: ['ADMIN', 'SECRETARY'] } },

  // --- Doktor Rotaları ---
  { path: '/doctors', name: 'doctors', component: DoctorListView, meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } },
  { path: '/doctors/new', name: 'doctor-new', component: DoctorFormView, meta: { roles: ['ADMIN'] } },
  { path: '/doctors/:id/edit', name: 'doctor-edit', component: DoctorFormView, meta: { roles: ['ADMIN'] } },

  // --- Muayene Rotaları ---
  { path: '/appointments', name: 'appointments', component: AppointmentListView, meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } },
  { path: '/appointments/new', name: 'appointment-new', component: AppointmentFormView, meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } },
  { path: '/appointments/:id/edit', name: 'appointment-edit', component: AppointmentFormView, meta: { roles: ['ADMIN', 'DOCTOR', 'SECRETARY'] } },

  // --- Reçete Rotaları ---
  { path: '/prescriptions', name: 'prescriptions', component: PrescriptionListView, meta: { roles: ['ADMIN', 'DOCTOR'] } },
  { path: '/prescriptions/new', name: 'prescription-new', component: PrescriptionFormView, meta: { roles: ['DOCTOR'] } },
  { path: '/prescriptions/:id/edit', name: 'prescription-edit', component: PrescriptionFormView, meta: { roles: ['ADMIN', 'DOCTOR'] } },

  {
    path: '/users',
    name: 'users',
    component: UserListView,
    meta: { roles: ['ADMIN'] }
  },
  { // YENİ: Kullanıcı Ekleme Formu
    path: '/users/new',
    name: 'user-new',
    component: UserFormView,
    meta: { roles: ['ADMIN'] } // Sadece Admin
  },
  { // YENİ: Kullanıcı Düzenleme Formu
    path: '/users/:id/edit',
    name: 'user-edit',
    component: UserFormView,
    meta: { roles: ['ADMIN'] } // Sadece Admin
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

// Navigation Guard (Aynı)
router.beforeEach((to, from, next) => {
  const user = authStore.user;
  const requiresRoles = to.meta.roles;
  const requiresGuest = to.meta.requiresGuest;

  if (requiresRoles && !user) return next({ name: 'login' });
  if (requiresGuest && user) return next({ name: 'patients' }); // veya dashboard
  if (requiresRoles && user && !requiresRoles.includes(user.role)) return next({ name: 'patients' }); // Yetkisizse anasayfaya

  next(); // Diğer durumlarda izin ver
});

export default router
