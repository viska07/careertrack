<template>
  <div class="detail-page">

    <Navbar />

    <main class="detail-container">

      <!-- Back -->
      <button
        class="back-button"
        @click="goBack"
      >
        ← Back to Applications
      </button>

      <!-- Loading -->
      <div
        v-if="loading"
        class="state-card"
      >
        Loading application...
      </div>

      <!-- Error -->
      <div
        v-else-if="errorMessage"
        class="state-card error-card"
      >
        <h2>
          Unable to load application
        </h2>

        <p>
          {{ errorMessage }}
        </p>

        <button
          class="primary-button"
          @click="goBack"
        >
          Back to Applications
        </button>
      </div>

      <!-- Detail -->
      <section
        v-else-if="application"
        class="detail-content"
      >

        <!-- Header -->
        <div class="detail-header">
            <div class="header-content">

                <p class="eyebrow">
                APPLICATION DETAIL
                </p>

                <h1>
                {{ application.companyName }}
                </h1>

                <p class="position">
                {{ application.position }}
                </p>

                <div
                v-if="application.location"
                class="header-location"
                >
                📍 {{ application.location }}
                </div>

            </div>

            <span
                class="status-badge"
                :class="getStatusClass(application.status)"
            >
                {{ formatStatus(application.status) }}
            </span>
            </div>

        <!-- Information -->
        <div class="detail-grid">

          <div class="detail-card">

            <h2>
              Application Information
            </h2>

            <div class="info-list">

              <div class="info-item">
                <span class="info-label">
                  Company
                </span>

                <span class="info-value">
                  {{ application.companyName }}
                </span>
              </div>

              <div class="info-item">
                <span class="info-label">
                  Position
                </span>

                <span class="info-value">
                  {{ application.position }}
                </span>
              </div>

              <div class="info-item">
                <span class="info-label">
                  Status
                </span>

                <span class="info-value">
                  {{ formatStatus(application.status) }}
                </span>
              </div>

              <div class="info-item">
                <span class="info-label">
                  Application Date
                </span>

                <span class="info-value">
                  {{
                    formatDate(application.applicationDate)
                    || 'Not specified'
                  }}
                </span>
              </div>

              <div class="info-item">
                <span class="info-label">
                  Location
                </span>

                <span class="info-value">
                  {{
                    application.location
                    || 'Not specified'
                  }}
                </span>
              </div>

            </div>

          </div>

          <!-- Job Link -->
          <div class="detail-card">

            <h2>
              Job Posting
            </h2>

            <p class="card-description">
              Open the original job posting if the URL
              is available.
            </p>

            <a
                v-if="application.jobUrl"
                :href="application.jobUrl"
                target="_blank"
                rel="noopener noreferrer"
                class="job-link"
            >
                Open Job Posting
                ↗
            </a>

            <p
                v-if="application.jobUrl"
                class="job-url"
            >
                {{ application.jobUrl }}
            </p>

            <p
              v-else
              class="no-data"
            >
              No job URL was provided.
            </p>

          </div>

        </div>

        <!-- Notes -->
        <div class="detail-card notes-card">

          <h2>
            Notes
          </h2>

          <p
            v-if="application.notes"
            class="notes"
          >
            {{ application.notes }}
          </p>

          <p
            v-else
            class="no-data"
          >
            No notes added for this application.
          </p>

        </div>

        <!-- Metadata -->
        <div class="metadata-card">

          <div>
            <span>
              Created
            </span>

            <strong>
              {{ formatDateTime(application.createdAt) }}
            </strong>
          </div>

          <div>
            <span>
              Last Updated
            </span>

            <strong>
              {{ formatDateTime(application.updatedAt) }}
            </strong>
          </div>

        </div>

        <!-- Actions -->
        <div class="detail-actions">

            <button
                class="back-action-button"
                @click="goBack"
            >
                ← Back
            </button>

            <div class="action-group">

                <button
                class="secondary-button"
                @click="editApplication"
                >
                Edit Application
                </button>

                <button
                    class="danger-button"
                    @click="openDeleteModal"
                >
                    Delete Application
                </button>

            </div>

            </div>

      </section>

    </main>

  </div>

<div
  v-if="showDeleteModal"
  class="modal-overlay"
  @click.self="closeDeleteModal"
>
  <div class="delete-modal">

    <div class="delete-icon">
      !
    </div>

    <div class="delete-content">

      <h2>
        Delete Application?
      </h2>

      <p>
        Are you sure you want to delete the application for
        <strong>
          {{ application.companyName }}
        </strong>
        ?
      </p>

      <p class="delete-warning">
        This action cannot be undone.
      </p>

    </div>

    <div class="delete-actions">

      <button
        class="cancel-delete-button"
        :disabled="deleting"
        @click="closeDeleteModal"
      >
        Cancel
      </button>

      <button
        class="confirm-delete-button"
        :disabled="deleting"
        @click="deleteApplication"
      >
        <span
          v-if="deleting"
          class="button-spinner"
        ></span>

        {{ deleting ? 'Deleting...' : 'Delete Application' }}
      </button>

    </div>

  </div>
</div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../services/api'
import Navbar from '../components/Navbar.vue'

const route = useRoute()
const router = useRouter()

const application = ref(null)

const loading = ref(true)
const errorMessage = ref('')
const showDeleteModal = ref(false)
const deleting = ref(false)
const loadApplication = async () => {

  loading.value = true
  errorMessage.value = ''

  try {

    const response = await api.get(
      `/applications/${route.params.id}`
    )

    application.value = response.data

  } catch (error) {

    console.error(error)

    if (error.response?.status === 401) {

      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('name')
      localStorage.removeItem('email')

      router.push('/login')
      return
    }

    errorMessage.value =
      error.response?.data?.message ||
      'Application not found.'

  } finally {

    loading.value = false

  }
}

const goBack = () => {
  router.push('/applications')
}

const editApplication = () => {

  router.push({
    path: '/applications',
    query: {
      edit: application.value.id
    }
  })
}

const openDeleteModal = () => {
  showDeleteModal.value = true
}

const closeDeleteModal = () => {
  if (deleting.value) {
    return
  }

  showDeleteModal.value = false
}

const deleteApplication = async () => {
  deleting.value = true
  errorMessage.value = ''

  try {
    await api.delete(
      `/applications/${application.value.id}`
    )

    router.push('/applications')
  } catch (error) {
    console.error(error)

    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('name')
      localStorage.removeItem('email')

      router.push('/login')
      return
    }

    errorMessage.value =
      error.response?.data?.message ||
      'Failed to delete application.'

    showDeleteModal.value = false
  } finally {
    deleting.value = false
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

const formatDateTime = (dateTime) => {

  if (!dateTime) {
    return 'Not available'
  }

  return new Date(dateTime).toLocaleString(
    'en-US',
    {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }
  )
}

onMounted(() => {
  loadApplication()
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;

  z-index: 1000;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 24px;

  background: rgba(17, 24, 39, 0.45);
  backdrop-filter: blur(3px);
}

.delete-modal {
  width: 100%;
  max-width: 440px;

  padding: 28px;

  background: #ffffff;

  border: 1px solid #e5e7eb;
  border-radius: 16px;

  box-shadow:
    0 20px 50px rgba(17, 24, 39, 0.16);

  animation: modalAppear 0.2s ease;
}

.delete-icon {
  width: 46px;
  height: 46px;

  margin-bottom: 18px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: #fef2f2;
  color: #dc2626;

  font-size: 20px;
  font-weight: 700;
}

.delete-content h2 {
  margin: 0;

  color: #111827;

  font-size: 20px;
  font-weight: 700;
}

.delete-content p {
  margin: 10px 0 0;

  color: #6b7280;

  font-size: 14px;
  line-height: 1.6;
}

.delete-content strong {
  color: #374151;
  font-weight: 700;
}

.delete-warning {
  color: #dc2626 !important;
  font-size: 13px !important;
}

.delete-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;

  margin-top: 26px;
}

.cancel-delete-button,
.confirm-delete-button {
  min-width: 110px;

  padding: 10px 16px;

  border-radius: 8px;

  font-size: 13px;
  font-weight: 600;

  cursor: pointer;

  transition:
    background 0.2s ease,
    border-color 0.2s ease,
    color 0.2s ease,
    opacity 0.2s ease;
}

.cancel-delete-button {
  border: 1px solid #d9dce5;

  background: #ffffff;
  color: #374151;
}

.cancel-delete-button:hover:not(:disabled) {
  background: #f9fafb;
  border-color: #c7cad3;
}

.confirm-delete-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  border: 1px solid #dc2626;

  background: #dc2626;
  color: #ffffff;
}

.confirm-delete-button:hover:not(:disabled) {
  background: #b91c1c;
  border-color: #b91c1c;
}

.cancel-delete-button:disabled,
.confirm-delete-button:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.button-spinner {
  width: 14px;
  height: 14px;

  border: 2px solid rgba(255, 255, 255, 0.45);
  border-top-color: #ffffff;

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}

@keyframes modalAppear {
  from {
    opacity: 0;
    transform: translateY(8px) scale(0.98);
  }

  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 600px) {
  .modal-overlay {
    align-items: flex-end;
    padding: 16px;
  }

  .delete-modal {
    max-width: none;
    padding: 24px;

    border-radius: 16px;
  }

  .delete-actions {
    flex-direction: column-reverse;
  }

  .cancel-delete-button,
  .confirm-delete-button {
    width: 100%;
  }
}

.detail-page {
  min-height: 100vh;
  background: #f7f8fc;
}

.detail-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 42px 32px 64px;
}

.back-button {
  padding: 0;
  border: none;
  background: transparent;
  color: #1a2e6f;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.back-button:hover {
  text-decoration: underline;
}

.detail-content {
  margin-top: 28px;
}

.detail-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 24px;

  padding: 30px;

  border: 1px solid #e9ebf2;
  border-radius: 14px;

  background: #ffffff;
}

.eyebrow {
  margin: 0 0 8px;

  color: #1a2e6f;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.detail-header h1 {
  margin: 0;

  color: #111827;
  font-size: 32px;
}

.position {
  margin: 8px 0 0;
  color: #6b7280;
  font-size: 16px;
}

.header-location {
  margin-top: 10px;
  color: #9ca3af;
  font-size: 13px;
  font-weight: 500;
}

.status-badge {
  display: inline-flex;
  padding: 8px 12px;

  border-radius: 7px;

  font-size: 12px;
  font-weight: 700;

  white-space: nowrap;
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

.detail-grid {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  gap: 16px;

  margin-top: 16px;
}

.detail-card {
  padding: 24px;

  border: 1px solid #e9ebf2;
  border-radius: 12px;

  background: #ffffff;
}

.detail-card h2 {
  margin: 0 0 20px;

  color: #111827;
  font-size: 18px;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 17px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;

  padding-bottom: 14px;

  border-bottom: 1px solid #f0f1f5;
}

.info-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.info-label {
  color: #6b7280;
  font-size: 13px;
}

.info-value {
  color: #111827;
  font-size: 13px;
  font-weight: 600;
  text-align: right;
}

.card-description {
  margin: -8px 0 20px;

  color: #6b7280;
  font-size: 13px;
  line-height: 1.6;
}

.job-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;

  padding: 10px 14px;

  border-radius: 8px;

  background: #eef1fb;
  color: #1a2e6f;

  font-size: 13px;
  font-weight: 600;

  text-decoration: none;
}

.job-link:hover {
  background: #e3e7f7;
}

.no-data {
  margin: 0;

  color: #9ca3af;
  font-size: 13px;
}

.notes-card {
  margin-top: 16px;
}

.notes {
  margin: 0;

  color: #374151;
  font-size: 14px;
  line-height: 1.7;

  white-space: pre-wrap;
}

.metadata-card {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;

  margin-top: 16px;
  padding: 20px 24px;

  border: 1px solid #e9ebf2;
  border-radius: 12px;

  background: #ffffff;
}

.metadata-card div {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.metadata-card span {
  color: #9ca3af;
  font-size: 12px;
}

.metadata-card strong {
  color: #4b5563;
  font-size: 13px;
}

.detail-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;

  gap: 12px;

  margin-top: 24px;
}

.action-group {
  display: flex;
  align-items: center;

  gap: 10px;
}

.back-action-button {
  padding: 11px 16px;

  border: 1px solid #d9dce5;
  border-radius: 8px;

  background: #ffffff;
  color: #374151;

  font-size: 13px;
  font-weight: 600;

  cursor: pointer;

  transition:
    background 0.2s ease,
    border-color 0.2s ease;
}

.back-action-button:hover {
  background: #f9fafb;
  border-color: #c7cad3;
}

.primary-button,
.secondary-button,
.danger-button {
  padding: 11px 16px;

  border-radius: 8px;

  font-size: 13px;
  font-weight: 600;

  cursor: pointer;
}

.primary-button {
  border: none;
  background: #1a2e6f;
  color: #ffffff;
}

.primary-button:hover {
  background: #14245a;
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

.state-card {
  margin-top: 28px;
  padding: 50px 24px;

  border: 1px solid #e9ebf2;
  border-radius: 12px;

  background: #ffffff;

  text-align: center;
}

.state-card h2 {
  margin: 0 0 8px;
  color: #111827;
}

.state-card p {
  margin: 0 0 20px;
  color: #6b7280;
}

.error-card {
  border-color: #fecaca;
}

.error-card h2 {
  color: #b91c1c;
}

/* =========================
   TABLET
========================= */

@media (max-width: 800px) {

  .detail-container {
    padding: 36px 24px 56px;
  }

  .detail-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .detail-grid {
    grid-template-columns: 1fr;
  }

}

/* =========================
   MOBILE
========================= */

@media (max-width: 600px) {

  .detail-container {
    padding: 28px 18px 48px;
  }

  .detail-header {
    padding: 22px;
  }

  .detail-header h1 {
    font-size: 27px;
  }

  .detail-card {
    padding: 20px;
  }

  .info-item {
    flex-direction: column;
    gap: 5px;
  }

  .info-value {
    text-align: left;
  }

  .metadata-card {
    grid-template-columns: 1fr;
    padding: 20px;
  }

  .detail-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .back-action-button {
    width: 100%;
  }

  .action-group {
    width: 100%;
    flex-direction: column;
  }

  .action-group button {
    width: 100%;
  }

}
</style>