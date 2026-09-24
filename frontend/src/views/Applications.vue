<template>
  <div class="applications-page">

    <Navbar />

    <main class="applications-container">

      <!-- Page Header -->
      <section class="page-header">

        <div>
          <p class="eyebrow">
            JOB TRACKER
          </p>

          <h1>
            My Applications
          </h1>

          <p class="page-description">
            Manage and track all of your job applications.
          </p>
        </div>

        <button
          class="primary-button"
          @click="openCreateForm"
        >
          + Add Application
        </button>

      </section>

      <!-- Search & Filter -->
      <section class="filter-section">

        <div class="search-wrapper">

          <input
            v-model="searchInput"
            type="text"
            placeholder="Search company or position..."
            @keyup.enter="applyFilters"
          />

          <button
            class="search-button"
            @click="applyFilters"
          >
            Search
          </button>

        </div>

        <select
          v-model="selectedStatus"
          @change="applyFilters"
        >
          <option value="">
            All Status
          </option>

          <option value="WISHLIST">
            Wishlist
          </option>

          <option value="APPLIED">
            Applied
          </option>

          <option value="INTERVIEW">
            Interview
          </option>

          <option value="OFFER">
            Offer
          </option>

          <option value="REJECTED">
            Rejected
          </option>
        </select>

      </section>

      <div class="result-info">
  <span>
    {{ applications.length }}
    {{ applications.length === 1 ? 'application' : 'applications' }}
  </span>

  <button
        v-if="searchInput || selectedStatus"
        class="clear-filter-button"
        @click="clearFilters"
    >
        Clear filters
    </button>
    </div>

      <!-- Error -->
      <div
        v-if="errorMessage"
        class="alert error-alert"
      >
        {{ errorMessage }}
      </div>

      <!-- Loading -->
      <div
        v-if="loading"
        class="state-card"
      >
        Loading applications...
      </div>

      <!-- Empty -->
      <div
        v-else-if="applications.length === 0"
        class="state-card"
      >
        <div class="empty-icon">
          📋
        </div>

        <h2>
          No applications found
        </h2>

        <p>
          Try changing your search or filter,
          or add a new application.
        </p>

        <button
          class="primary-button"
          @click="openCreateForm"
        >
          + Add Application
        </button>
      </div>

      <!-- Application List -->
      <section
        v-else
        class="application-list"
      >

        <article
          v-for="application in applications"
          :key="application.id"
          class="application-card"
        >

          <div class="application-content">

            <div class="company-section">

              <h2>
                {{ application.companyName }}
              </h2>

              <p class="position">
                {{ application.position }}
              </p>

              <p
                v-if="application.location"
                class="location"
              >
                📍 {{ application.location }}
              </p>

            </div>

            <div class="application-info">

              <span
                class="status-badge"
                :class="getStatusClass(application.status)"
              >
                {{ formatStatus(application.status) }}
              </span>

              <span
                v-if="application.applicationDate"
                class="application-date"
              >
                {{ formatDate(application.applicationDate) }}
              </span>

            </div>

          </div>

          <div class="application-actions">

            <button
                class="view-button"
                @click="viewApplication(application)"
            >
                View
            </button>

            <button
                class="secondary-button"
                @click="openEditForm(application)"
            >
                Edit
            </button>

            <button
                class="danger-button"
                @click="deleteApplication(application)"
            >
                Delete
            </button>

            </div>

        </article>

      </section>

    </main>

    <!-- Application Modal -->
    <div
      v-if="showForm"
      class="modal-overlay"
      @click.self="closeForm"
    >

      <div class="modal">

        <div class="modal-header">

          <div>
            <h2>
              {{
                editingApplication
                  ? 'Edit Application'
                  : 'Add Application'
              }}
            </h2>

            <p>
              {{
                editingApplication
                  ? 'Update your application information.'
                  : 'Add a new job application to your tracker.'
              }}
            </p>
          </div>

          <button
            class="close-button"
            @click="closeForm"
          >
            ×
          </button>

        </div>

        <form
          class="application-form"
          @submit.prevent="submitForm"
        >

          <div class="form-group">

            <label for="companyName">
              Company Name
            </label>

            <input
              id="companyName"
              v-model="form.companyName"
              type="text"
              placeholder="e.g. Microsoft"
              required
            />

          </div>

          <div class="form-group">

            <label for="position">
              Position
            </label>

            <input
              id="position"
              v-model="form.position"
              type="text"
              placeholder="e.g. Frontend Developer"
              required
            />

          </div>

          <div class="form-row">

            <div class="form-group">

              <label for="status">
                Status
              </label>

              <select
                id="status"
                v-model="form.status"
                required
              >
                <option value="WISHLIST">
                  Wishlist
                </option>

                <option value="APPLIED">
                  Applied
                </option>

                <option value="INTERVIEW">
                  Interview
                </option>

                <option value="OFFER">
                  Offer
                </option>

                <option value="REJECTED">
                  Rejected
                </option>
              </select>

            </div>

            <div class="form-group">

              <label for="applicationDate">
                Application Date
              </label>

              <input
                id="applicationDate"
                v-model="form.applicationDate"
                type="date"
              />

            </div>

          </div>

          <div class="form-group">

            <label for="location">
              Location
            </label>

            <input
              id="location"
              v-model="form.location"
              type="text"
              placeholder="e.g. Jakarta"
            />

          </div>

          <div class="form-group">

            <label for="jobUrl">
              Job URL
            </label>

            <input
              id="jobUrl"
              v-model="form.jobUrl"
              type="url"
              placeholder="https://example.com/job"
            />

          </div>

          <div class="form-group">

            <label for="notes">
              Notes
            </label>

            <textarea
              id="notes"
              v-model="form.notes"
              rows="4"
              placeholder="Add notes about this application..."
            ></textarea>

          </div>

          <div
            v-if="formError"
            class="alert error-alert"
          >
            {{ formError }}
          </div>

          <div class="modal-actions">

            <button
              type="button"
              class="secondary-button"
              @click="closeForm"
            >
              Cancel
            </button>

            <button
              type="submit"
              class="primary-button"
              :disabled="formLoading"
            >
              {{
                formLoading
                  ? 'Saving...'
                  : editingApplication
                    ? 'Save Changes'
                    : 'Add Application'
              }}
            </button>

          </div>

        </form>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'
import Navbar from '../components/Navbar.vue'

const router = useRouter()
const route = useRoute()
const applications = ref([])

const loading = ref(false)
const errorMessage = ref('')

const searchInput = ref('')
const selectedStatus = ref('')

const showForm = ref(false)
const editingApplication = ref(null)
const formLoading = ref(false)
const formError = ref('')

const form = reactive({
  companyName: '',
  position: '',
  status: 'WISHLIST',
  applicationDate: '',
  jobUrl: '',
  location: '',
  notes: ''
})

const loadApplications = async () => {

  loading.value = true
  errorMessage.value = ''

  try {

    const params = {}

    if (searchInput.value.trim()) {
      params.search = searchInput.value.trim()
    }

    if (selectedStatus.value) {
      params.status = selectedStatus.value
    }

    const response = await api.get(
      '/applications',
      { params }
    )

    applications.value = response.data

  } catch (error) {

    console.error(error)

    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      return
    }

    errorMessage.value =
      error.response?.data?.message ||
      'Failed to load applications.'

  } finally {

    loading.value = false

  }
}

const applyFilters = () => {
  loadApplications()
}

const clearFilters = async () => {
  searchInput.value = ''
  selectedStatus.value = ''

  await loadApplications()
}

const resetForm = () => {

  form.companyName = ''
  form.position = ''
  form.status = 'WISHLIST'
  form.applicationDate = ''
  form.jobUrl = ''
  form.location = ''
  form.notes = ''

  formError.value = ''
}

const viewApplication = (application) => {

  router.push(
    `/applications/${application.id}`
  )
}

const openCreateForm = () => {

  editingApplication.value = null

  resetForm()

  showForm.value = true
}

const openEditForm = (application) => {

  editingApplication.value = application

  form.companyName = application.companyName || ''
  form.position = application.position || ''
  form.status = application.status || 'WISHLIST'
  form.applicationDate =
    application.applicationDate || ''
  form.jobUrl = application.jobUrl || ''
  form.location = application.location || ''
  form.notes = application.notes || ''

  formError.value = ''

  showForm.value = true
}

const closeForm = () => {

  if (formLoading.value) {
    return
  }

  showForm.value = false
  editingApplication.value = null
  resetForm()
}

const submitForm = async () => {

  formError.value = ''
  formLoading.value = true

  try {

    const payload = {
      companyName: form.companyName,
      position: form.position,
      status: form.status,
      applicationDate:
        form.applicationDate || null,
      jobUrl: form.jobUrl || null,
      location: form.location || null,
      notes: form.notes || null
    }

    if (editingApplication.value) {

      await api.put(
        `/applications/${editingApplication.value.id}`,
        payload
      )

    } else {

      await api.post(
        '/applications',
        payload
      )

    }

    closeForm()

    await loadApplications()

  } catch (error) {

    console.error(error)

    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      return
    }

    formError.value =
      error.response?.data?.message ||
      'Failed to save application.'

  } finally {

    formLoading.value = false

  }
}

const deleteApplication = async (application) => {

  const confirmed = window.confirm(
    `Delete application for ${application.companyName}?`
  )

  if (!confirmed) {
    return
  }

  try {

    await api.delete(
      `/applications/${application.id}`
    )

    applications.value =
      applications.value.filter(
        item => item.id !== application.id
      )

  } catch (error) {

    console.error(error)

    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
      return
    }

    errorMessage.value =
      error.response?.data?.message ||
      'Failed to delete application.'

  }
}

const formatStatus = (status) => {

  const statusMap = {
    WISHLIST: 'Wishlist',
    APPLIED: 'Applied',
    INTERVIEW: 'Interview',
    OFFER: 'Offer',
    REJECTED: 'Rejected'
  }

  return statusMap[status] || status
}

const getStatusClass = (status) => {
  return `status-${status.toLowerCase()}`
}

const formatDate = (date) => {

  if (!date) {
    return ''
  }

  return new Date(date).toLocaleDateString(
    'en-US',
    {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    }
  )
}

onMounted(async () => {

  await loadApplications()

  const editId = route.query.edit

  if (editId) {

    const application = applications.value.find(
      item => String(item.id) === String(editId)
    )

    if (application) {
      openEditForm(application)
    }

  }
})

</script>

<style scoped>
.applications-page {
  min-height: 100vh;
  background: #f7f8fc;
}

.applications-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 32px 64px;
}

.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 30px;
}

.eyebrow {
  margin: 0 0 8px;
  color: #1a2e6f;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.page-header h1 {
  margin: 0;
  color: #111827;
  font-size: 32px;
}

.page-description {
  margin: 8px 0 0;
  color: #6b7280;
  font-size: 15px;
}

.primary-button {
  padding: 11px 17px;
  border: none;
  border-radius: 9px;
  background: #1a2e6f;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.primary-button:hover {
  background: #14245a;
}

.primary-button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.search-wrapper {
  flex: 1;
  display: flex;
  gap: 8px;
}

.search-wrapper input,
.filter-section select {
  height: 42px;
  border: 1px solid #d9dce5;
  border-radius: 8px;
  background: #ffffff;
  color: #111827;
  font-size: 14px;
  outline: none;
}

.search-wrapper input {
  width: 100%;
  padding: 0 13px;
}

.filter-section select {
  min-width: 160px;
  padding: 0 12px;
}

.search-wrapper input:focus,
.filter-section select:focus,
.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  border-color: #1a2e6f;
}

.search-button {
  height: 42px;
  padding: 0 17px;
  border: none;
  border-radius: 8px;
  background: #eef1fb;
  color: #1a2e6f;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.search-button:hover {
  background: #e3e7f7;
}

.alert {
  margin-bottom: 20px;
  padding: 12px 14px;
  border-radius: 8px;
  font-size: 13px;
}

.error-alert {
  border: 1px solid #fecaca;
  background: #fff7f7;
  color: #b91c1c;
}

.state-card {
  padding: 50px 24px;
  border: 1px solid #e9ebf2;
  border-radius: 12px;
  background: #ffffff;
  text-align: center;
}

.empty-icon {
  margin-bottom: 10px;
  font-size: 32px;
}

.state-card h2 {
  margin: 0;
  color: #111827;
  font-size: 20px;
}

.state-card p {
  max-width: 450px;
  margin: 8px auto 22px;
  color: #6b7280;
  font-size: 14px;
}

.application-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.application-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;

  padding: 20px 22px;

  border: 1px solid #e9ebf2;
  border-radius: 12px;
  background: #ffffff;
}

.application-content {
  flex: 1;
  min-width: 0;

  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.company-section {
  min-width: 0;
}

.company-section h2 {
  margin: 0;
  color: #111827;
  font-size: 17px;
}

.position {
  margin: 6px 0 0;
  color: #4b5563;
  font-size: 14px;
}

.location {
  margin: 8px 0 0;
  color: #9ca3af;
  font-size: 12px;
}

.application-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  flex-shrink: 0;
}

.status-badge {
  display: inline-flex;
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 700;
}

.status-wishlist {
  background: #f3f4f6;
  color: #4b5563;
}

.status-applied {
  background: #dbeafe;
  color: #1d4ed8;
}

.status-interview {
  background: #fef3c7;
  color: #b45309;
}

.status-offer {
  background: #dcfce7;
  color: #15803d;
}

.status-rejected {
  background: #fee2e2;
  color: #b91c1c;
}

.application-date {
  color: #9ca3af;
  font-size: 12px;
}

.application-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.view-button,
.secondary-button,
.danger-button {
  padding: 8px 13px;
  border-radius: 7px;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
}

.view-button {
  padding: 8px 13px;
  border: none;
  border-radius: 7px;
  background: #eef1fb;
  color: #1a2e6f;
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
}

.view-button:hover {
  background: #e3e7f7;
}

.secondary-button {
  border: 1px solid #d9dce5;
  background: #ffffff;
  color: #374151;
}

.secondary-button:hover {
  background: #f9fafb;
}

.danger-button {
  border: 1px solid #fecaca;
  background: #fff7f7;
  color: #b91c1c;
}

.danger-button:hover {
  background: #fee2e2;
}

/* Modal */

.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 1000;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 24px;

  background: rgba(17, 24, 39, 0.45);
}

.modal {
  width: 100%;
  max-width: 620px;
  max-height: calc(100vh - 48px);

  overflow-y: auto;

  padding: 28px;

  border-radius: 14px;
  background: #ffffff;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.18);
}

.modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 24px;
}

.modal-header h2 {
  margin: 0;
  color: #111827;
  font-size: 21px;
}

.modal-header p {
  margin: 6px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.close-button {
  width: 34px;
  height: 34px;

  border: none;
  border-radius: 8px;

  background: #f3f4f6;
  color: #4b5563;

  font-size: 22px;
  line-height: 1;
  cursor: pointer;
}

.application-form {
  display: flex;
  flex-direction: column;
  gap: 17px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.form-group label {
  color: #374151;
  font-size: 13px;
  font-weight: 600;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;

  padding: 11px 12px;

  border: 1px solid #d9dce5;
  border-radius: 8px;

  background: #ffffff;
  color: #111827;

  font-size: 14px;
  outline: none;
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 5px;
}

.result-info {
  display: flex;
  align-items: center;
  justify-content: space-between;

  margin-top: -10px;
  margin-bottom: 20px;

  color: #6b7280;
  font-size: 13px;
}

.clear-filter-button {
  padding: 7px 12px;

  border: 1px solid #d9dce5;
  border-radius: 7px;

  background: #ffffff;
  color: #374151;

  font-size: 12px;
  font-weight: 600;

  cursor: pointer;

  transition:
    background 0.2s ease,
    border-color 0.2s ease,
    color 0.2s ease;
}

.clear-filter-button:hover {
  background: #f3f5fb;
  border-color: #1a2e6f;
  color: #1a2e6f;
}

@media (max-width: 600px) {
  .result-info {
    align-items: flex-start;
    flex-direction: column;
    gap: 10px;
  }

  .clear-filter-button {
    width: 100%;
  }
}

/* Responsive */

@media (max-width: 900px) {

  .applications-container {
    padding: 40px 24px 56px;
  }

  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .application-content {
    align-items: flex-start;
    flex-direction: column;
    gap: 14px;
  }

  .application-info {
    align-items: flex-start;
    flex-direction: row;
  }

}

@media (max-width: 650px) {

  .applications-container {
    padding: 32px 18px 48px;
  }

  .page-header h1 {
    font-size: 27px;
  }

  .filter-section {
    align-items: stretch;
    flex-direction: column;
  }

  .search-wrapper {
    width: 100%;
  }

  .filter-section select {
    width: 100%;
  }

  .application-card {
    align-items: stretch;
    flex-direction: column;
  }

  .application-content {
    width: 100%;
  }

  .application-info {
    width: 100%;
    justify-content: space-between;
  }

  .application-actions {
    width: 100%;
  }

  .application-actions button {
    flex: 1;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 17px;
  }

  .modal-overlay {
    align-items: flex-start;
    padding: 16px;
  }

  .modal {
    max-height: calc(100vh - 32px);
    padding: 22px;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    width: 100%;
  }

}

@media (max-width: 420px) {

  .search-wrapper {
    flex-direction: column;
  }

  .search-button {
    width: 100%;
  }

  .application-info {
    align-items: flex-start;
    flex-direction: column;
  }

  .modal {
    padding: 18px;
  }

}
</style>