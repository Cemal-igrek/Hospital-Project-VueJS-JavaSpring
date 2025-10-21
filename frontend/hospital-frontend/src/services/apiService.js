import axios from 'axios';

const apiClient = axios.create({
  baseURL:'http://localhost:8080/api'
});
apiClient.defaults.withCredentials = true;
export default {
  // --- Auth Services ---
  login(credentials) {
    // credentials = { username: '...', password: '...' }
    return apiClient.post('/auth/login', credentials);
  },
  // TODO: getCurrentUser() { return apiClient.get('/auth/me'); }
  // TODO: changePassword(data) { return apiClient.put('/auth/change-password', data); }

  // --- Patient Services ---
  getPatients() {
    return apiClient.get('/patients');
  },
  getPatientById(id) {
    return apiClient.get(`/patients/${id}`);
  },
  createPatient(patientData) {
    return apiClient.post('/patients', patientData);
  },
  updatePatient(id, patientData) {
    return apiClient.put(`/patients/${id}`, patientData);
  },
  deletePatient(id) {
    return apiClient.delete(`/patients/${id}`);
  },

  // --- Doctor Services ---
  getDoctors() {
    return apiClient.get('/doctors');
  },
  getDoctorById(id) {
    return apiClient.get(`/doctors/${id}`);
  },
  createDoctor(doctorData) {
    return apiClient.post('/doctors', doctorData);
  },
  updateDoctor(id, doctorData) {
    return apiClient.put(`/doctors/${id}`, doctorData);
  },
  deleteDoctor(id) {
    return apiClient.delete(`/doctors/${id}`);
  },

  // --- User Services (for Admin) ---
  getUsers() {
    return apiClient.get('/users');
  },
  getUserById(id) {
    return apiClient.get(`/users/${id}`);
  },
  createUser(userData) {
    return apiClient.post('/users', userData);
  },
  updateUser(id, userData) {
    return apiClient.put(`/users/${id}`, userData);
  },
  deleteUser(id) {
    return apiClient.delete(`/users/${id}`);
  },

  // --- Appointment Services ---
  getAppointments() {
    return apiClient.get('/appointments');
  },
  getAppointmentById(id) {
    return apiClient.get(`/appointments/${id}`);
  },
  createAppointment(appointmentData) {
    return apiClient.post('/appointments', appointmentData);
  },
  updateAppointment(id, appointmentData) {
    return apiClient.put(`/appointments/${id}`, appointmentData);
  },
  deleteAppointment(id) {
    return apiClient.delete(`/appointments/${id}`);
  },

  // --- Prescription Services ---
  getPrescriptionById(id) {
    return apiClient.get(`/prescriptions/${id}`);
  },
  getPrescriptionsByAppointment(appointmentId) {
    return apiClient.get(`/prescriptions/by-appointment/${appointmentId}`);
  },
  createPrescription(prescriptionData) {
    return apiClient.post('/prescriptions', prescriptionData);
  },
  updatePrescription(id, prescriptionData) {
    return apiClient.put(`/prescriptions/${id}`, prescriptionData);
  },
  deletePrescription(id) {
    return apiClient.delete(`/prescriptions/${id}`);
  }
};
