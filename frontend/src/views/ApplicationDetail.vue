<template>
  <div class="detail-page">

    <Navbar />

    <main class="detail-container">

      <!-- =================================
           BACK
           ================================= -->

      <button
        type="button"
        class="back-button"
        @click="goBack"
      >
        <span>←</span>
        Back to applications
      </button>


      <!-- =================================
           LOADING
           ================================= -->

      <div
        v-if="loading"
        class="state-card"
      >
        <div class="loading-spinner"></div>

        <p>
          Loading application...
        </p>
      </div>


      <!-- =================================
           ERROR
           ================================= -->

      <div
        v-else-if="errorMessage"
        class="error-state"
      >

        <div class="error-icon">
          !
        </div>

        <h2>
          Unable to load application
        </h2>

        <p>
          {{ errorMessage }}
        </p>

        <button
          type="button"
          class="primary-button"
          @click="goBack"
        >
          Back to applications
        </button>

      </div>


      <!-- =================================
           DETAIL
           ================================= -->

      <template v-else-if="application">

        <!-- HERO -->

        <section class="detail-hero">

          <div class="hero-left">

            <div class="company-avatar large">
              {{ companyInitial }}
            </div>

            <div class="hero-copy">

              <span class="eyebrow">
                APPLICATION DETAIL
              </span>

              <h1>
                {{ application.companyName }}
              </h1>

              <p class="hero-position">
                {{ application.position }}
              </p>

              <div class="hero-meta">

                <span
                  v-if="application.location"
                >
                  <span class="meta-icon">
                    ⌖
                  </span>

                  {{ application.location }}
                </span>

                <span
                  v-if="application.applicationDate"
                >
                  <span class="meta-icon">
                    ◷
                  </span>

                  {{ formatDate(
                    application.applicationDate
                  ) }}
                </span>

              </div>

            </div>

          </div>


          <div class="hero-right">

            <span
              class="status-badge"
              :class="statusClass"
            >
              {{ formatStatus(
                application.status
              ) }}
            </span>

          </div>

        </section>


        <!-- CONTENT -->

        <section class="content-grid">

          <!-- MAIN -->

          <div class="main-column">

            <!-- APPLICATION INFO -->

            <article class="detail-card">

              <div class="card-heading">

                <div>
                  <span class="card-eyebrow">
                    APPLICATION
                  </span>

                  <h2>
                    Application information
                  </h2>
                </div>

              </div>


              <div class="info-grid">

                <div class="info-item">

                  <span class="info-label">
                    Company
                  </span>

                  <strong>
                    {{ application.companyName }}
                  </strong>

                </div>


                <div class="info-item">

                  <span class="info-label">
                    Position
                  </span>

                  <strong>
                    {{ application.position }}
                  </strong>

                </div>


                <div class="info-item">

                  <span class="info-label">
                    Status
                  </span>

                  <span
                    class="status-badge"
                    :class="statusClass"
                  >
                    {{ formatStatus(
                      application.status
                    ) }}
                  </span>

                </div>


                <div class="info-item">

                  <span class="info-label">
                    Application date
                  </span>

                  <strong>
                    {{
                      application.applicationDate
                        ? formatDate(
                            application.applicationDate
                          )
                        : 'Not specified'
                    }}
                  </strong>

                </div>


                <div class="info-item">

                  <span class="info-label">
                    Location
                  </span>

                  <strong>
                    {{
                      application.location ||
                      'Not specified'
                    }}
                  </strong>

                </div>

              </div>

            </article>


            <!-- NOTES -->

            <article class="detail-card">

              <div class="card-heading">

                <div>
                  <span class="card-eyebrow">
                    NOTES
                  </span>

                  <h2>
                    Your notes
                  </h2>
                </div>

              </div>


              <div
                v-if="application.notes"
                class="notes-content"
              >
                {{ application.notes }}
              </div>

              <div
                v-else
                class="empty-notes"
              >
                <span>
                  No notes have been added to
                  this application yet.
                </span>
              </div>

            </article>


            <!-- JOB POSTING -->

            <article class="detail-card">

              <div class="card-heading">

                <div>
                  <span class="card-eyebrow">
                    JOB POSTING
                  </span>

                  <h2>
                    Job information
                  </h2>
                </div>

              </div>


              <div
                v-if="application.jobUrl"
                class="job-link-card"
              >

                <div class="job-link-icon">
                  ↗
                </div>

                <div class="job-link-content">

                  <span>
                    Original job posting
                  </span>

                  <p>
                    Open the job posting in a
                    new browser tab.
                  </p>

                </div>

                <a
                  :href="application.jobUrl"
                  target="_blank"
                  rel="noopener noreferrer"
                  class="open-link"
                  @click.stop
                >
                  Open
                  <span>↗</span>
                </a>

              </div>


              <div
                v-else
                class="empty-notes"
              >
                <span>
                  No job posting URL has been
                  added.
                </span>
              </div>

            </article>

          </div>


          <!-- SIDEBAR -->

          <aside class="side-column">

            <!-- ACTIONS -->

            <article class="action-card">

              <span class="card-eyebrow">
                ACTIONS
              </span>

              <h2>
                Manage application
              </h2>

              <p>
                Update the information or remove
                this application from your tracker.
              </p>


              <button
                type="button"
                class="primary-button full-button"
                @click="editApplication"
              >
                <span>✎</span>
                Edit application
              </button>


              <button
                type="button"
                class="danger-outline-button"
                @click="openDeleteModal"
              >
                <span>×</span>
                Delete application
              </button>

            </article>


            <!-- ACTIVITY -->

            <article class="activity-card">

              <span class="card-eyebrow">
                ACTIVITY
              </span>

              <h2>
                Timeline
              </h2>


              <div class="timeline">

                <div class="timeline-item">

                  <span class="timeline-dot"></span>

                  <div>

                    <strong>
                      Application tracked
                    </strong>

                    <p>
                      {{
                        formatDateTime(
                          application.createdAt
                        )
                      }}
                    </p>

                  </div>

                </div>


                <div
                  v-if="
                    application.updatedAt &&
                    application.updatedAt !==
                    application.createdAt
                  "
                  class="timeline-item"
                >

                  <span class="timeline-dot current"></span>

                  <div>

                    <strong>
                      Application updated
                    </strong>

                    <p>
                      {{
                        formatDateTime(
                          application.updatedAt
                        )
                      }}
                    </p>

                  </div>

                </div>

              </div>

            </article>

          </aside>

        </section>

      </template>

    </main>


    <!-- =================================
         DELETE MODAL
         ================================= -->

    <div
      v-if="showDeleteModal"
      class="modal-overlay"
      @click.self="closeDeleteModal"
    >

      <div class="delete-modal">

        <div class="delete-icon">
          !
        </div>

        <span class="modal-eyebrow">
          DELETE APPLICATION
        </span>

        <h2>
          Delete this application?
        </h2>

        <p>
          You're about to remove
          <strong>
            {{ application?.companyName }}
          </strong>
          from your applications.
          This action cannot be undone.
        </p>


        <div
          v-if="deleteError"
          class="form-error"
        >
          <span>!</span>
          {{ deleteError }}
        </div>


        <div class="modal-actions">

          <button
            type="button"
            class="secondary-button"
            @click="closeDeleteModal"
            :disabled="deleting"
          >
            Cancel
          </button>

          <button
            type="button"
            class="danger-button"
            @click="deleteApplication"
            :disabled="deleting"
          >

            <span
              v-if="deleting"
              class="button-spinner"
            ></span>

            {{
              deleting
                ? 'Deleting...'
                : 'Delete application'
            }}

          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>
import {
  computed,
  onMounted,
  ref
} from 'vue'

import {
  useRoute,
  useRouter
} from 'vue-router'

import api from '../services/api'

import Navbar from '../components/Navbar.vue'


const router = useRouter()
const route = useRoute()


/* =================================
   DATA
   ================================= */

const application = ref(null)

const loading = ref(true)

const errorMessage = ref('')


/* =================================
   DELETE
   ================================= */

const showDeleteModal = ref(false)

const deleting = ref(false)

const deleteError = ref('')


/* =================================
   COMPUTED
   ================================= */

const companyInitial = computed(() => {

  if (!application.value?.companyName) {
    return '?'
  }

  return application.value.companyName
    .trim()
    .charAt(0)
    .toUpperCase()

})


const statusClass = computed(() => {

  if (!application.value?.status) {
    return ''
  }

  return `status-${application.value.status.toLowerCase()}`

})


/* =================================
   LOAD
   ================================= */

const loadApplication = async () => {

  loading.value = true

  errorMessage.value = ''

  try {

    const response =
      await api.get(
        `/applications/${route.params.id}`
      )

    application.value =
      response.data

  } catch (error) {

    console.error(error)

    if (
      error.response?.status === 401
    ) {

      logoutAndRedirect()

      return
    }

    errorMessage.value =
      error.response?.data?.message ||
      'Application could not be found.'

  } finally {

    loading.value = false
  }

}


/* =================================
   NAVIGATION
   ================================= */

const goBack = () => {

  router.push('/applications')

}


const editApplication = () => {

  router.push({
    path: '/applications',
    query: {
      edit: String(
        application.value.id
      )
    }
  })

}


/* =================================
   DELETE MODAL
   ================================= */

const openDeleteModal = () => {

  deleteError.value = ''

  showDeleteModal.value = true

}


const closeDeleteModal = () => {

  if (deleting.value) {
    return
  }

  showDeleteModal.value = false

  deleteError.value = ''

}


/* =================================
   DELETE
   ================================= */

const deleteApplication = async () => {

  if (!application.value) {
    return
  }

  deleting.value = true

  deleteError.value = ''

  try {

    await api.delete(
      `/applications/${application.value.id}`
    )

    router.push('/applications')

  } catch (error) {

    console.error(error)

    if (
      error.response?.status === 401
    ) {

      logoutAndRedirect()

      return
    }

    deleteError.value =
      error.response?.data?.message ||
      'Unable to delete application.'

  } finally {

    deleting.value = false
  }

}


/* =================================
   STATUS
   ================================= */

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


/* =================================
   DATE
   ================================= */

const formatDate = (date) => {

  if (!date) {
    return 'Not specified'
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


const formatDateTime = (date) => {

  if (!date) {
    return 'Not available'
  }

  return new Date(date).toLocaleString(
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


/* =================================
   LOGOUT
   ================================= */

const logoutAndRedirect = () => {

  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('name')
  localStorage.removeItem('email')

  router.push('/login')

}


/* =================================
   INITIAL LOAD
   ================================= */

onMounted(() => {

  loadApplication()

})

</script>


<style scoped>

/* =========================================
   PAGE
   ========================================= */

.detail-page {
  min-height: 100vh;

  background: var(--background);
}

.detail-container {
  width: 100%;
  max-width: 1240px;

  margin: 0 auto;

  padding: 30px 28px 70px;
}


/* =========================================
   BACK
   ========================================= */

.back-button {
  display: inline-flex;
  align-items: center;

  gap: 7px;

  margin-bottom: 22px;

  padding: 5px 0;

  background: transparent;

  color: var(--text-secondary);

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;

  transition:
    color 0.2s ease;
}

.back-button:hover {
  color: var(--primary);
}

.back-button span {
  font-size: 13px;
}


/* =========================================
   HERO
   ========================================= */

.detail-hero {
  display: flex;
  align-items: center;
  justify-content: space-between;

  gap: 25px;

  margin-bottom: 20px;

  padding: 24px;

  border: 1px solid var(--border);

  border-radius: 16px;

  background:
    linear-gradient(
      135deg,
      #ffffff,
      #fafbff
    );

  box-shadow:
    0 6px 24px rgba(23, 32, 51, 0.035);
}

.hero-left {
  display: flex;
  align-items: center;

  gap: 17px;

  min-width: 0;
}

.company-avatar {
  width: 52px;
  height: 52px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  border-radius: 14px;

  background:
    linear-gradient(
      145deg,
      var(--primary-soft),
      var(--accent-soft)
    );

  color: var(--primary);

  font-size: 17px;
  font-weight: 800;
}

.company-avatar.large {
  width: 58px;
  height: 58px;

  border-radius: 16px;

  font-size: 19px;
}

.hero-copy {
  min-width: 0;
}

.eyebrow,
.card-eyebrow,
.modal-eyebrow {
  display: block;

  margin-bottom: 6px;

  color: var(--primary);

  font-size: 8px;
  font-weight: 800;

  letter-spacing: 0.14em;
}

.hero-copy h1 {
  margin: 0;

  overflow: hidden;

  color: var(--text-primary);

  font-size: 28px;
  line-height: 1.1;

  letter-spacing: -0.045em;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.hero-position {
  margin: 5px 0 0;

  color: var(--text-secondary);

  font-size: 11px;
}

.hero-meta {
  display: flex;
  align-items: center;

  flex-wrap: wrap;

  gap: 12px;

  margin-top: 9px;

  color: var(--text-muted);

  font-size: 8px;
}

.hero-meta span {
  display: inline-flex;
  align-items: center;

  gap: 4px;
}

.meta-icon {
  color: var(--primary);

  font-size: 10px;
}


/* =========================================
   STATUS
   ========================================= */

.status-badge {
  display: inline-flex;
  align-items: center;

  width: fit-content;

  padding: 6px 9px;

  border-radius: 999px;

  font-size: 8px;
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


/* =========================================
   CONTENT GRID
   ========================================= */

.content-grid {
  display: grid;

  grid-template-columns:
    minmax(0, 1.6fr)
    minmax(260px, 0.7fr);

  align-items: start;

  gap: 14px;
}

.main-column,
.side-column {
  display: flex;
  flex-direction: column;

  gap: 14px;
}


/* =========================================
   CARDS
   ========================================= */

.detail-card,
.action-card,
.activity-card {
  padding: 21px;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);
}

.card-heading {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  margin-bottom: 18px;
}

.card-eyebrow {
  margin-bottom: 5px;

  font-size: 7px;
}

.card-heading h2,
.action-card h2,
.activity-card h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 16px;

  letter-spacing: -0.03em;
}


/* =========================================
   INFO
   ========================================= */

.info-grid {
  display: grid;

  grid-template-columns:
    repeat(2, 1fr);

  gap: 17px;

  padding-top: 3px;
}

.info-item {
  display: flex;
  flex-direction: column;

  gap: 5px;
}

.info-label {
  color: var(--text-muted);

  font-size: 8px;
}

.info-item strong {
  color: var(--text-primary);

  font-size: 10px;
  font-weight: 700;
}


/* =========================================
   NOTES
   ========================================= */

.notes-content {
  padding: 13px;

  border-radius: 9px;

  background: var(--surface-soft);

  color: var(--text-secondary);

  font-size: 10px;
  line-height: 1.7;

  white-space: pre-wrap;
}

.empty-notes {
  padding: 14px;

  border: 1px dashed var(--border-strong);

  border-radius: 9px;

  background: var(--surface-soft);

  color: var(--text-muted);

  font-size: 9px;
  line-height: 1.5;
}


/* =========================================
   JOB LINK
   ========================================= */

.job-link-card {
  display: flex;
  align-items: center;

  gap: 11px;

  padding: 12px;

  border: 1px solid var(--border);

  border-radius: 9px;

  background: var(--surface-soft);
}

.job-link-icon {
  width: 33px;
  height: 33px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  border-radius: 8px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 12px;
  font-weight: 700;
}

.job-link-content {
  flex: 1;

  min-width: 0;
}

.job-link-content span {
  color: var(--text-primary);

  font-size: 9px;
  font-weight: 750;
}

.job-link-content p {
  margin: 3px 0 0;

  color: var(--text-muted);

  font-size: 8px;
}

.open-link {
  display: inline-flex;
  align-items: center;

  gap: 4px;

  padding: 6px 9px;

  border-radius: 7px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 8px;
  font-weight: 750;

  text-decoration: none;

  transition:
    background 0.2s ease;
}

.open-link:hover {
  background: #dfe5ff;
}


/* =========================================
   ACTION CARD
   ========================================= */

.action-card h2,
.activity-card h2 {
  font-size: 15px;
}

.action-card p {
  margin: 7px 0 18px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.6;
}

.full-button {
  width: 100%;
}

.danger-outline-button {
  width: 100%;

  min-height: 38px;

  display: flex;
  align-items: center;
  justify-content: center;

  gap: 7px;

  margin-top: 8px;

  border: 1px solid #edcaca;

  border-radius: 8px;

  background: #ffffff;

  color: var(--danger);

  font-size: 9px;
  font-weight: 750;

  cursor: pointer;

  transition:
    background 0.2s ease,
    border-color 0.2s ease;
}

.danger-outline-button:hover {
  border-color: var(--danger);

  background: var(--danger-soft);
}


/* =========================================
   PRIMARY BUTTON
   ========================================= */

.primary-button {
  min-height: 38px;

  display: inline-flex;
  align-items: center;
  justify-content: center;

  gap: 7px;

  padding: 8px 13px;

  border-radius: 8px;

  background:
    linear-gradient(
      135deg,
      var(--primary),
      var(--accent)
    );

  color: #ffffff;

  font-size: 9px;
  font-weight: 750;

  cursor: pointer;

  box-shadow:
    0 7px 17px rgba(49, 85, 217, 0.14);

  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.primary-button:hover {
  transform: translateY(-1px);

  box-shadow:
    0 10px 22px rgba(49, 85, 217, 0.2);
}


/* =========================================
   ACTIVITY
   ========================================= */

.timeline {
  position: relative;

  display: flex;
  flex-direction: column;

  gap: 18px;

  margin-top: 19px;
}

.timeline::before {
  content: "";

  position: absolute;

  left: 4px;
  top: 6px;
  bottom: 6px;

  width: 1px;

  background: var(--border);
}

.timeline-item {
  position: relative;

  display: flex;

  gap: 10px;
}

.timeline-dot {
  position: relative;
  z-index: 2;

  width: 9px;
  height: 9px;

  flex-shrink: 0;

  margin-top: 2px;

  border: 2px solid var(--primary);

  border-radius: 50%;

  background: #ffffff;
}

.timeline-dot.current {
  border-color: var(--accent);

  background: var(--accent);
}

.timeline-item strong {
  display: block;

  color: var(--text-primary);

  font-size: 9px;
}

.timeline-item p {
  margin: 3px 0 0;

  color: var(--text-muted);

  font-size: 8px;
}


/* =========================================
   STATE
   ========================================= */

.state-card {
  min-height: 300px;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);
}

.state-card p {
  margin: 10px 0 0;

  color: var(--text-muted);

  font-size: 9px;
}

.loading-spinner {
  width: 26px;
  height: 26px;

  border: 2px solid var(--primary-soft);

  border-top-color: var(--primary);

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}


/* =========================================
   ERROR STATE
   ========================================= */

.error-state {
  min-height: 300px;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  padding: 30px;

  border: 1px solid var(--border);

  border-radius: 14px;

  background: var(--surface);

  text-align: center;
}

.error-state .error-icon {
  width: 42px;
  height: 42px;

  display: flex;
  align-items: center;
  justify-content: center;

  margin-bottom: 14px;

  border-radius: 12px;

  background: var(--danger-soft);

  color: var(--danger);

  font-size: 16px;
  font-weight: 800;
}

.error-state h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 16px;
}

.error-state p {
  max-width: 400px;

  margin: 8px 0 17px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.5;
}


/* =========================================
   DELETE MODAL
   ========================================= */

.modal-overlay {
  position: fixed;

  inset: 0;

  z-index: 500;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 24px;

  background: rgba(17, 24, 39, 0.48);

  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
}

.delete-modal {
  width: 100%;
  max-width: 400px;

  padding: 27px;

  border: 1px solid var(--border);

  border-radius: 16px;

  background: #ffffff;

  box-shadow:
    0 25px 70px rgba(23, 32, 51, 0.18);

  text-align: center;

  animation:
    modal-in 0.2s ease;
}

.delete-icon {
  width: 48px;
  height: 48px;

  display: flex;
  align-items: center;
  justify-content: center;

  margin: 0 auto 15px;

  border-radius: 14px;

  background: var(--danger-soft);

  color: var(--danger);

  font-size: 17px;
  font-weight: 800;
}

.delete-modal .modal-eyebrow {
  margin-bottom: 6px;
}

.delete-modal h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 20px;

  letter-spacing: -0.035em;
}

.delete-modal > p {
  margin: 8px auto 0;

  max-width: 310px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.6;
}

.delete-modal strong {
  color: var(--text-primary);
}

.form-error {
  display: flex;
  align-items: center;

  gap: 7px;

  margin-top: 15px;

  padding: 9px 10px;

  border: 1px solid #f1cccc;

  border-radius: 8px;

  background: var(--danger-soft);

  color: var(--danger);

  font-size: 9px;

  text-align: left;
}

.form-error span {
  width: 16px;
  height: 16px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  border-radius: 50%;

  background: var(--danger);

  color: #ffffff;

  font-size: 8px;
  font-weight: 800;
}

.modal-actions {
  display: flex;
  justify-content: center;

  gap: 8px;

  margin-top: 22px;
}

.secondary-button,
.danger-button {
  min-height: 38px;

  padding: 8px 13px;

  border-radius: 8px;

  font-size: 9px;
  font-weight: 750;

  cursor: pointer;

  transition:
    background 0.2s ease,
    border-color 0.2s ease,
    color 0.2s ease,
    opacity 0.2s ease;
}

.secondary-button {
  border: 1px solid var(--border-strong);

  background: #ffffff;

  color: var(--text-secondary);
}

.secondary-button:hover:not(:disabled) {
  border-color: var(--primary);

  background: var(--primary-soft);

  color: var(--primary);
}

.danger-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  gap: 7px;

  border: 1px solid var(--danger);

  background: var(--danger);

  color: #ffffff;
}

.danger-button:hover:not(:disabled) {
  border-color: #bf3f3f;

  background: #bf3f3f;
}

.secondary-button:disabled,
.danger-button:disabled {
  opacity: 0.55;

  cursor: not-allowed;
}

.button-spinner {
  width: 12px;
  height: 12px;

  border: 2px solid rgba(255, 255, 255, 0.4);

  border-top-color: #ffffff;

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes modal-in {
  from {
    opacity: 0;
    transform: translateY(8px) scale(0.98);
  }

  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}


/* =========================================
   TABLET
   ========================================= */

@media (max-width: 950px) {

  .detail-container {
    padding: 30px 20px 60px;
  }

  .content-grid {
    grid-template-columns: 1fr;
  }

  .side-column {
    display: grid;

    grid-template-columns: 1fr 1fr;

    align-items: start;
  }

}


/* =========================================
   MOBILE
   ========================================= */

@media (max-width: 700px) {

  .detail-container {
    padding: 25px 16px 50px;
  }

  .detail-hero {
    align-items: flex-start;

    flex-direction: column;

    padding: 19px;
  }

  .hero-left {
    width: 100%;
  }

  .hero-copy h1 {
    max-width: 100%;

    font-size: 24px;

    white-space: normal;
  }

  .hero-right {
    width: 100%;
  }

  .hero-right .status-badge {
    width: fit-content;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .side-column {
    grid-template-columns: 1fr;
  }

  .job-link-card {
    align-items: flex-start;
  }

  .open-link {
    flex-shrink: 0;
  }

  .modal-overlay {
    align-items: flex-end;

    padding: 10px;
  }

  .delete-modal {
    max-height: calc(100vh - 20px);

    border-radius: 15px;
  }

}


/* =========================================
   SMALL MOBILE
   ========================================= */

@media (max-width: 400px) {

  .detail-container {
    padding: 22px 12px 45px;
  }

  .detail-hero {
    padding: 16px;
  }

  .company-avatar.large {
    width: 50px;
    height: 50px;

    border-radius: 13px;

    font-size: 17px;
  }

  .hero-copy h1 {
    font-size: 22px;
  }

  .hero-position {
    font-size: 10px;
  }

  .detail-card,
  .action-card,
  .activity-card {
    padding: 17px;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    width: 100%;
  }

}

</style>