<template>
  <div class="dashboard-page">

    <Navbar />

    <main class="dashboard-container">

      <!-- =================================
           HERO
           ================================= -->

      <section class="hero-section">

        <div class="hero-copy">

          <span class="eyebrow">
            CAREER OVERVIEW
          </span>

          <h1>
            Welcome back,
            <span>{{ name }}</span>
          </h1>

          <p>
            Keep your job search organized,
            focused, and moving forward.
          </p>

        </div>

        <button
          type="button"
          class="primary-button"
          @click="goToApplications"
        >
          <span class="button-plus">
            +
          </span>

          Add application
        </button>

      </section>


      <!-- =================================
           ERROR
           ================================= -->

      <div
        v-if="errorMessage"
        class="error-banner"
      >
        <span class="error-icon">
          !
        </span>

        <span>
          {{ errorMessage }}
        </span>
      </div>


      <!-- =================================
           MAIN STATS
           ================================= -->

      <section class="stats-grid">

        <!-- TOTAL -->

        <article class="stat-card stat-card-main">

          <div class="stat-header">

            <span class="stat-label">
              Total applications
            </span>

            <span class="stat-icon">
              ↗
            </span>

          </div>

          <div class="stat-number">
            {{ applications.length }}
          </div>

          <p class="stat-description">
            Everything you're tracking
          </p>

          <div class="stat-accent"></div>

        </article>


        <!-- ACTIVE -->

        <article class="stat-card">

          <div class="stat-header">

            <span class="stat-label">
              Active
            </span>

            <span class="stat-icon">
              ◌
            </span>

          </div>

          <div class="stat-number">
            {{ activeCount }}
          </div>

          <p class="stat-description">
            Applications still in progress
          </p>

        </article>


        <!-- INTERVIEW -->

        <article class="stat-card">

          <div class="stat-header">

            <span class="stat-label">
              Interviews
            </span>

            <span class="stat-icon">
              ✦
            </span>

          </div>

          <div class="stat-number">
            {{ getStatusCount('INTERVIEW') }}
          </div>

          <p class="stat-description">
            Conversations in progress
          </p>

        </article>

      </section>


      <!-- =================================
           OVERVIEW + PROGRESS
           ================================= -->

      <section class="overview-grid">

        <!-- APPLICATION OVERVIEW -->

        <article class="overview-card">

          <div class="section-heading">

            <div>
              <span class="section-eyebrow">
                APPLICATION OVERVIEW
              </span>

              <h2>
                Your pipeline
              </h2>
            </div>

            <button
              type="button"
              class="text-button"
              @click="goToApplications"
            >
              View all
              <span>→</span>
            </button>

          </div>


          <div class="pipeline-list">

            <!-- WISHLIST -->

            <div class="pipeline-row">

              <div class="pipeline-info">

                <span class="pipeline-dot wishlist"></span>

                <span>
                  Wishlist
                </span>

              </div>

              <strong>
                {{ getStatusCount('WISHLIST') }}
              </strong>

            </div>


            <!-- APPLIED -->

            <div class="pipeline-row">

              <div class="pipeline-info">

                <span class="pipeline-dot applied"></span>

                <span>
                  Applied
                </span>

              </div>

              <strong>
                {{ getStatusCount('APPLIED') }}
              </strong>

            </div>


            <!-- INTERVIEW -->

            <div class="pipeline-row">

              <div class="pipeline-info">

                <span class="pipeline-dot interview"></span>

                <span>
                  Interview
                </span>

              </div>

              <strong>
                {{ getStatusCount('INTERVIEW') }}
              </strong>

            </div>


            <!-- OFFER -->

            <div class="pipeline-row">

              <div class="pipeline-info">

                <span class="pipeline-dot offer"></span>

                <span>
                  Offer
                </span>

              </div>

              <strong>
                {{ getStatusCount('OFFER') }}
              </strong>

            </div>


            <!-- REJECTED -->

            <div class="pipeline-row">

              <div class="pipeline-info">

                <span class="pipeline-dot rejected"></span>

                <span>
                  Rejected
                </span>

              </div>

              <strong>
                {{ getStatusCount('REJECTED') }}
              </strong>

            </div>

          </div>

        </article>


        <!-- CAREER PROGRESS -->

        <article class="progress-card">

          <div class="section-heading">

            <div>
              <span class="section-eyebrow">
                CAREER PROGRESS
              </span>

              <h2>
                Keep moving
              </h2>
            </div>

            <span class="progress-percent">
              {{ progressPercentage }}%
            </span>

          </div>


          <div class="progress-circle-wrapper">

            <div
              class="progress-circle"
              :style="{
                '--progress':
                  progressPercentage * 3.6 + 'deg'
              }"
            >

              <div class="progress-circle-inner">

                <strong>
                  {{ progressPercentage }}%
                </strong>

                <span>
                  progress
                </span>

              </div>

            </div>

          </div>


          <p class="progress-message">
            {{
              progressPercentage > 70
                ? 'You are making strong progress. Keep the momentum going.'
                : progressPercentage > 40
                  ? 'You are building momentum. Keep tracking your opportunities.'
                  : 'Start adding opportunities and keep your career journey moving.'
            }}
          </p>


          <button
            type="button"
            class="secondary-button"
            @click="goToApplications"
          >
            Manage applications
            <span>→</span>
          </button>

        </article>

      </section>


      <!-- =================================
           RECENT APPLICATIONS
           ================================= -->

      <section class="recent-section">

        <div class="section-heading recent-heading">

          <div>

            <span class="section-eyebrow">
              RECENT ACTIVITY
            </span>

            <h2>
              Recent applications
            </h2>

          </div>

          <button
            type="button"
            class="text-button"
            @click="goToApplications"
          >
            See all
            <span>→</span>
          </button>

        </div>


        <!-- LOADING -->

        <div
          v-if="loading"
          class="state-card"
        >
          <div class="loading-spinner"></div>

          <p>
            Loading your applications...
          </p>
        </div>


        <!-- EMPTY -->

        <div
          v-else-if="recentApplications.length === 0"
          class="empty-card"
        >

          <div class="empty-icon">
            +
          </div>

          <h3>
            No applications yet
          </h3>

          <p>
            Start tracking your job applications
            and keep everything organized in one place.
          </p>

          <button
            type="button"
            class="primary-button"
            @click="goToApplications"
          >
            Add your first application
          </button>

        </div>


        <!-- APPLICATION LIST -->

        <div
          v-else
          class="application-list"
        >

          <article
            v-for="application in recentApplications"
            :key="application.id"
            class="application-row"
            @click="viewApplication(application)"
          >

            <div class="company-avatar">
              {{ getCompanyInitial(application.companyName) }}
            </div>


            <div class="application-main">

              <h3>
                {{ application.companyName }}
              </h3>

              <p>
                {{ application.position }}
              </p>

            </div>


            <div class="application-location">

              <span v-if="application.location">
                {{ application.location }}
              </span>

              <span v-else>
                Location not specified
              </span>

            </div>


            <div class="application-date">
              {{ formatDate(application.applicationDate) }}
            </div>


            <div
              class="status-badge"
              :class="getStatusClass(application.status)"
            >
              {{ formatStatus(application.status) }}
            </div>


            <span class="row-arrow">
              →
            </span>

          </article>

        </div>

      </section>

    </main>

  </div>
</template>


<script setup>
import {
  computed,
  onMounted,
  ref
} from 'vue'

import { useRouter } from 'vue-router'

import api from '../services/api'

import Navbar from '../components/Navbar.vue'


const router = useRouter()


/* =================================
   USER
   ================================= */

const name = ref(
  localStorage.getItem('name') || 'User'
)


/* =================================
   APPLICATION DATA
   ================================= */

const applications = ref([])

const loading = ref(true)

const errorMessage = ref('')


/* =================================
   COMPUTED
   ================================= */

const recentApplications = computed(() => {
  return applications.value.slice(0, 5)
})


const activeCount = computed(() => {

  return applications.value.filter(
    application =>
      application.status === 'WISHLIST' ||
      application.status === 'APPLIED' ||
      application.status === 'INTERVIEW'
  ).length

})


const progressPercentage = computed(() => {

  const total =
    applications.value.length

  if (total === 0) {
    return 0
  }

  const completed =
    applications.value.filter(
      application =>
        application.status === 'OFFER'
    ).length

  return Math.min(
    100,
    Math.round(
      (completed / total) * 100
    )
  )

})


/* =================================
   LOAD APPLICATIONS
   ================================= */

const loadApplications = async () => {

  loading.value = true

  errorMessage.value = ''

  try {

    const response =
      await api.get('/applications')

    applications.value =
      response.data

  } catch (error) {

    console.error(error)

    if (
      error.response?.status === 401
    ) {

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


/* =================================
   STATUS
   ================================= */

const getStatusCount = (status) => {

  return applications.value.filter(
    application =>
      application.status === status
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


/* =================================
   COMPANY
   ================================= */

const getCompanyInitial = (companyName) => {

  if (!companyName) {
    return '?'
  }

  return companyName
    .trim()
    .charAt(0)
    .toUpperCase()

}


/* =================================
   DATE
   ================================= */

const formatDate = (date) => {

  if (!date) {
    return 'No date'
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


/* =================================
   NAVIGATION
   ================================= */

const goToApplications = () => {

  router.push('/applications')

}


const viewApplication = (application) => {

  router.push(
    `/applications/${application.id}`
  )

}


/* =================================
   INITIAL LOAD
   ================================= */

onMounted(() => {

  loadApplications()

})
</script>


<style scoped>

/* =========================================
   PAGE
   ========================================= */

.dashboard-page {
  min-height: 100vh;

  background: var(--background);
}

.dashboard-container {
  width: 100%;
  max-width: 1240px;

  margin: 0 auto;

  padding: 42px 28px 70px;
}


/* =========================================
   HERO
   ========================================= */

.hero-section {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;

  gap: 30px;

  margin-bottom: 30px;
}

.hero-copy {
  max-width: 650px;
}

.eyebrow,
.section-eyebrow {
  display: block;

  margin-bottom: 9px;

  color: var(--primary);

  font-size: 9px;
  font-weight: 800;

  letter-spacing: 0.14em;
}

.hero-copy h1 {
  margin: 0;

  color: var(--text-primary);

  font-size: clamp(34px, 4vw, 52px);
  line-height: 1.05;

  letter-spacing: -0.055em;
}

.hero-copy h1 span {
  color: var(--primary);
}

.hero-copy p {
  margin: 14px 0 0;

  color: var(--text-secondary);

  font-size: 13px;
  line-height: 1.6;
}


/* =========================================
   BUTTONS
   ========================================= */

.primary-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  gap: 7px;

  min-height: 40px;

  padding: 9px 15px;

  border-radius: 9px;

  background:
    linear-gradient(
      135deg,
      var(--primary),
      var(--accent)
    );

  color: #ffffff;

  font-size: 10px;
  font-weight: 750;

  cursor: pointer;

  box-shadow:
    0 8px 20px rgba(49, 85, 217, 0.16);

  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.primary-button:hover {
  transform: translateY(-1px);

  box-shadow:
    0 11px 25px rgba(49, 85, 217, 0.22);
}

.button-plus {
  font-size: 15px;
  line-height: 1;
}

.secondary-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  gap: 7px;

  width: 100%;

  min-height: 38px;

  padding: 8px 13px;

  border: 1px solid var(--border-strong);

  border-radius: 8px;

  background: #ffffff;

  color: var(--text-primary);

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;

  transition:
    border-color 0.2s ease,
    background 0.2s ease,
    color 0.2s ease;
}

.secondary-button:hover {
  border-color: var(--primary);

  background: var(--primary-soft);

  color: var(--primary);
}

.text-button {
  display: inline-flex;
  align-items: center;

  gap: 6px;

  padding: 4px 0;

  background: transparent;

  color: var(--primary);

  font-size: 9px;
  font-weight: 750;

  cursor: pointer;
}

.text-button:hover {
  text-decoration: underline;
}


/* =========================================
   ERROR
   ========================================= */

.error-banner {
  display: flex;
  align-items: center;

  gap: 9px;

  margin-bottom: 20px;

  padding: 11px 13px;

  border: 1px solid #f1cccc;

  border-radius: 9px;

  background: var(--danger-soft);

  color: var(--danger);

  font-size: 10px;
}

.error-icon {
  width: 18px;
  height: 18px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  border-radius: 50%;

  background: var(--danger);

  color: #ffffff;

  font-size: 9px;
  font-weight: 800;
}


/* =========================================
   STATS
   ========================================= */

.stats-grid {
  display: grid;

  grid-template-columns:
    repeat(3, 1fr);

  gap: 14px;

  margin-bottom: 14px;
}

.stat-card {
  position: relative;

  min-height: 155px;

  padding: 20px;

  overflow: hidden;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);
}

.stat-card-main {
  background:
    linear-gradient(
      145deg,
      #ffffff,
      var(--primary-soft)
    );

  border-color: #e0e5ff;
}

.stat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-label {
  color: var(--text-secondary);

  font-size: 10px;
  font-weight: 700;
}

.stat-icon {
  color: var(--primary);

  font-size: 14px;
  font-weight: 700;
}

.stat-number {
  margin-top: 21px;

  color: var(--text-primary);

  font-size: 34px;
  font-weight: 750;

  line-height: 1;

  letter-spacing: -0.05em;
}

.stat-description {
  margin: 8px 0 0;

  color: var(--text-muted);

  font-size: 9px;
}

.stat-accent {
  position: absolute;

  right: -25px;
  bottom: -35px;

  width: 100px;
  height: 100px;

  border-radius: 50%;

  background:
    rgba(124, 92, 252, 0.08);
}


/* =========================================
   OVERVIEW
   ========================================= */

.overview-grid {
  display: grid;

  grid-template-columns:
    1.4fr 0.8fr;

  gap: 14px;

  margin-bottom: 42px;
}

.overview-card,
.progress-card {
  min-height: 300px;

  padding: 22px;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);
}


/* =========================================
   SECTION HEADING
   ========================================= */

.section-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  gap: 20px;

  margin-bottom: 22px;
}

.section-heading h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 19px;
  font-weight: 750;

  letter-spacing: -0.035em;
}

.section-eyebrow {
  margin-bottom: 5px;

  font-size: 8px;
}


/* =========================================
   PIPELINE
   ========================================= */

.pipeline-list {
  display: flex;
  flex-direction: column;

  gap: 5px;
}

.pipeline-row {
  display: flex;
  align-items: center;
  justify-content: space-between;

  min-height: 39px;

  padding: 0 9px;

  border-radius: 7px;

  transition:
    background 0.2s ease;
}

.pipeline-row:hover {
  background: var(--surface-soft);
}

.pipeline-info {
  display: flex;
  align-items: center;

  gap: 9px;

  color: var(--text-secondary);

  font-size: 10px;
  font-weight: 600;
}

.pipeline-row strong {
  color: var(--text-primary);

  font-size: 11px;
}

.pipeline-dot {
  width: 7px;
  height: 7px;

  flex-shrink: 0;

  border-radius: 50%;
}

.pipeline-dot.wishlist {
  background: #8b7cf6;
}

.pipeline-dot.applied {
  background: #4f75df;
}

.pipeline-dot.interview {
  background: #d99332;
}

.pipeline-dot.offer {
  background: #25a67d;
}

.pipeline-dot.rejected {
  background: #d65a5a;
}


/* =========================================
   PROGRESS
   ========================================= */

.progress-card {
  display: flex;
  flex-direction: column;
}

.progress-percent {
  color: var(--primary);

  font-size: 14px;
  font-weight: 800;
}

.progress-circle-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;

  margin: 0 auto 17px;
}

.progress-circle {
  width: 140px;
  height: 140px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background:
    conic-gradient(
      var(--primary) 0deg,
      var(--accent) var(--progress),
      #edf0f7 var(--progress),
      #edf0f7 360deg
    );
}

.progress-circle-inner {
  width: 112px;
  height: 112px;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: #ffffff;
}

.progress-circle-inner strong {
  color: var(--text-primary);

  font-size: 25px;
  line-height: 1;

  letter-spacing: -0.04em;
}

.progress-circle-inner span {
  margin-top: 5px;

  color: var(--text-muted);

  font-size: 8px;
}

.progress-message {
  margin: 0 auto 16px;

  max-width: 260px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.55;

  text-align: center;
}


/* =========================================
   RECENT
   ========================================= */

.recent-section {
  width: 100%;
}

.recent-heading {
  margin-bottom: 14px;
}

.application-list {
  overflow: hidden;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);
}

.application-row {
  display: grid;

  grid-template-columns:
    40px
    minmax(150px, 1.5fr)
    minmax(100px, 0.8fr)
    90px
    auto
    20px;

  align-items: center;

  gap: 13px;

  min-height: 72px;

  padding: 11px 18px;

  border-bottom: 1px solid var(--border);

  cursor: pointer;

  transition:
    background 0.2s ease;
}

.application-row:last-child {
  border-bottom: 0;
}

.application-row:hover {
  background: var(--surface-soft);
}

.company-avatar {
  width: 38px;
  height: 38px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 10px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 12px;
  font-weight: 800;
}

.application-main {
  min-width: 0;
}

.application-main h3 {
  margin: 0;

  overflow: hidden;

  color: var(--text-primary);

  font-size: 10px;
  font-weight: 750;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.application-main p {
  margin: 4px 0 0;

  overflow: hidden;

  color: var(--text-muted);

  font-size: 8px;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.application-location,
.application-date {
  overflow: hidden;

  color: var(--text-secondary);

  font-size: 8px;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.status-badge {
  width: fit-content;

  padding: 5px 8px;

  border-radius: 999px;

  font-size: 7px;
  font-weight: 750;
}

.status-wishlist {
  background: var(--accent-soft);
  color: var(--accent);
}

.status-applied {
  background: var(--primary-soft);
  color: var(--primary);
}

.status-interview {
  background: var(--warning-soft);
  color: var(--warning);
}

.status-offer {
  background: var(--success-soft);
  color: var(--success);
}

.status-rejected {
  background: var(--danger-soft);
  color: var(--danger);
}

.row-arrow {
  color: var(--text-muted);

  font-size: 13px;

  transition:
    color 0.2s ease,
    transform 0.2s ease;
}

.application-row:hover .row-arrow {
  color: var(--primary);

  transform: translateX(2px);
}


/* =========================================
   STATES
   ========================================= */

.state-card,
.empty-card {
  min-height: 190px;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  padding: 25px;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  text-align: center;
}

.state-card p {
  margin: 10px 0 0;

  color: var(--text-muted);

  font-size: 9px;
}

.loading-spinner {
  width: 25px;
  height: 25px;

  border: 2px solid var(--primary-soft);

  border-top-color: var(--primary);

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}

.empty-icon {
  width: 40px;
  height: 40px;

  display: flex;
  align-items: center;
  justify-content: center;

  margin-bottom: 12px;

  border-radius: 12px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 20px;
}

.empty-card h3 {
  margin: 0;

  color: var(--text-primary);

  font-size: 13px;
}

.empty-card p {
  max-width: 350px;

  margin: 7px 0 15px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.55;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}


/* =========================================
   TABLET
   ========================================= */

@media (max-width: 950px) {

  .dashboard-container {
    padding: 35px 20px 60px;
  }

  .overview-grid {
    grid-template-columns: 1fr;
  }

  .application-row {
    grid-template-columns:
      40px
      minmax(150px, 1fr)
      auto
      20px;
  }

  .application-location {
    display: none;
  }

  .application-date {
    display: none;
  }

}


/* =========================================
   MOBILE
   ========================================= */

@media (max-width: 700px) {

  .dashboard-container {
    padding: 28px 16px 50px;
  }

  .hero-section {
    flex-direction: column;
    align-items: flex-start;

    gap: 18px;
  }

  .hero-copy h1 {
    font-size: 35px;
  }

  .hero-copy p {
    font-size: 11px;
  }

  .primary-button {
    width: 100%;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .stat-card {
    min-height: 130px;
  }

  .overview-card,
  .progress-card {
    padding: 18px;
  }

  .application-row {
    grid-template-columns:
      38px
      1fr
      auto;

    gap: 10px;

    padding: 11px 13px;
  }

  .status-badge {
    grid-column: 2;
    grid-row: 2;

    margin-top: -4px;
  }

  .row-arrow {
    grid-column: 3;
    grid-row: 1 / span 2;
  }

}


/* =========================================
   SMALL MOBILE
   ========================================= */

@media (max-width: 400px) {

  .dashboard-container {
    padding: 24px 12px 45px;
  }

  .hero-copy h1 {
    font-size: 30px;
  }

  .stat-number {
    font-size: 30px;
  }

  .section-heading h2 {
    font-size: 17px;
  }

  .text-button {
    font-size: 8px;
  }

  .application-main h3 {
    font-size: 9px;
  }

}

</style>