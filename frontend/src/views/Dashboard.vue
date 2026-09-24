<template>
  <div class="dashboard-page">

    <Navbar />

    <main class="dashboard-container">

      <!-- Welcome -->
      <section class="welcome-section">
        <div>
          <p class="eyebrow">
            CAREER OVERVIEW
          </p>

          <h1>
            Welcome back, {{ name }} 👋
          </h1>

          <p class="welcome-description">
            Keep track of your job search journey in one place.
          </p>
        </div>

        <button
          class="add-button"
          @click="goToApplications"
        >
          + Add Application
        </button>
      </section>

      <!-- Statistics -->
      <section class="stats-grid">

        <div class="stat-card">
            <div class="stat-label">
            Total Applications
            </div>

            <div class="stat-value">
            {{ applications.length }}
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">
            Wishlist
            </div>

            <div class="stat-value">
            {{ getStatusCount('WISHLIST') }}
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">
            Applied
            </div>

            <div class="stat-value">
            {{ getStatusCount('APPLIED') }}
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">
            Interview
            </div>

            <div class="stat-value">
            {{ getStatusCount('INTERVIEW') }}
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">
            Offer
            </div>

            <div class="stat-value">
            {{ getStatusCount('OFFER') }}
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">
            Rejected
            </div>

            <div class="stat-value">
            {{ getStatusCount('REJECTED') }}
            </div>
        </div>

        </section>

      <!-- Applications -->
      <section class="applications-section">

        <div class="section-header">
          <div>
            <h2>
              Recent Applications
            </h2>

            <p>
              Your latest job application activity.
            </p>
          </div>

          <button
            class="view-all-button"
            @click="goToApplications"
          >
            View All →
          </button>
        </div>

        <!-- Loading -->
        <div
          v-if="loading"
          class="state-card"
        >
          Loading applications...
        </div>

        <!-- Error -->
        <div
          v-else-if="errorMessage"
          class="state-card error-card"
        >
          {{ errorMessage }}
        </div>

        <!-- Empty -->
        <div
          v-else-if="applications.length === 0"
          class="state-card"
        >
          <h3>No applications yet</h3>

          <p>
            Start tracking your job applications by adding
            your first application.
          </p>

          <button
            class="add-button"
            @click="goToApplications"
          >
            Add Application
          </button>
        </div>

        <!-- Application list -->
        <div
          v-else
          class="application-list"
        >

        <div
            v-for="application in recentApplications"
            :key="application.id"
            class="application-card"
            @click="viewApplication(application)"
            >
            <div class="application-main">

                <div class="company-info">
                <h3>
                    {{ application.companyName }}
                </h3>

                <p>
                    {{ application.position }}
                </p>

                <span
                    v-if="application.location"
                    class="location"
                >
                    📍 {{ application.location }}
                </span>
                </div>

                <div class="application-meta">

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

            <div class="application-arrow">
                →
            </div>
            </div>

        </div>

      </section>

    </main>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import Navbar from '../components/Navbar.vue'

const router = useRouter()

const name = ref(
  localStorage.getItem('name') || 'User'
)

const applications = ref([])
const loading = ref(true)
const errorMessage = ref('')

const recentApplications = computed(() => {
  return applications.value.slice(0, 5)
})

const loadApplications = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await api.get('/applications')

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

const getStatusCount = (status) => {
  return applications.value.filter(
    application => application.status === status
  ).length
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

  const options = {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  }

  return new Date(date).toLocaleDateString(
    'en-US',
    options
  )
}

const goToApplications = () => {
  router.push('/applications')
}

const viewApplication = (application) => {
  router.push(
    `/applications/${application.id}`
  )
}

onMounted(() => {
  loadApplications()
})
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: #f7f8fc;
}

.dashboard-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 48px 32px 64px;
}

.welcome-section {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 32px;
}

.eyebrow {
  margin: 0 0 8px;
  color: #1a2e6f;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.08em;
}

.welcome-section h1 {
  margin: 0;
  color: #111827;
  font-size: 32px;
  line-height: 1.2;
}

.welcome-description {
  margin: 10px 0 0;
  color: #6b7280;
  font-size: 15px;
}

.add-button {
  padding: 12px 18px;
  border: none;
  border-radius: 9px;
  background: #1a2e6f;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
}

.add-button:hover {
  background: #14245a;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 40px;
}

.stat-card {
  padding: 22px;
  background: #ffffff;
  border: 1px solid #e9ebf2;
  border-radius: 12px;
}

.stat-label {
  color: #6b7280;
  font-size: 13px;
  font-weight: 500;
}

.stat-value {
  margin-top: 8px;
  color: #111827;
  font-size: 30px;
  font-weight: 700;
}

.applications-section {
  margin-top: 12px;
}

.section-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 18px;
}

.section-header h2 {
  margin: 0;
  color: #111827;
  font-size: 21px;
}

.section-header p {
  margin: 6px 0 0;
  color: #6b7280;
  font-size: 14px;
}

.view-all-button {
  border: none;
  background: transparent;
  color: #1a2e6f;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
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
  gap: 20px;
  padding: 20px 22px;
  background: #ffffff;
  border: 1px solid #e9ebf2;
  border-radius: 12px;
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    border-color 0.2s ease;
}

.application-card:hover {
  transform: translateY(-2px);
  border-color: #d8ddef;
  box-shadow:
    0 8px 24px rgba(17, 24, 39, 0.07);
}

.application-main {
  flex: 1;
  min-width: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}

.company-info {
  min-width: 0;
}

.company-info h3 {
  margin: 0;
  color: #111827;
  font-size: 16px;
  font-weight: 700;
}

.company-info p {
  margin: 6px 0 0;
  color: #4b5563;
  font-size: 14px;
}

.location {
  display: inline-block;
  margin-top: 8px;
  color: #9ca3af;
  font-size: 12px;
}

.application-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
  flex-shrink: 0;
}

.application-arrow {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: #f3f5fb;
  color: #1a2e6f;
  font-size: 18px;
  font-weight: 600;
  flex-shrink: 0;
  transition:
    background 0.2s ease,
    transform 0.2s ease;
}

.application-card:hover .application-arrow {
  background: #eef1fb;
  transform: translateX(3px);
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

.state-card {
  padding: 36px;
  background: #ffffff;
  border: 1px solid #e9ebf2;
  border-radius: 12px;
  text-align: center;
  color: #6b7280;
}

.state-card h3 {
  margin: 0;
  color: #111827;
}

.state-card p {
  margin: 8px 0 20px;
  font-size: 14px;
}

.error-card {
  color: #b91c1c;
  background: #fff7f7;
  border-color: #fecaca;
}

@media (max-width: 800px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .welcome-section {
    align-items: flex-start;
    flex-direction: column;
  }

  .application-main {
    align-items: flex-start;
    flex-direction: column;

    width: 100%;

    gap: 14px;
  }

  .application-meta {
    width: 100%;

    align-items: flex-start;
    flex-direction: row;

    justify-content: space-between;
  }
}

@media (max-width: 520px) {
  .dashboard-container {
    padding: 32px 18px 48px;
  }

  .navbar-inner {
    padding: 0 18px;
  }

  .user-name {
    display: none;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .welcome-section h1 {
    font-size: 26px;
  }

    .application-card {
    align-items: flex-start;
    gap: 14px;

    padding: 18px;
  }

  .application-main {
    gap: 12px;
  }

  .application-meta {
    align-items: flex-start;
    flex-direction: column;

    gap: 6px;
  }

  .application-arrow {
    align-self: flex-end;
  }
}
</style>