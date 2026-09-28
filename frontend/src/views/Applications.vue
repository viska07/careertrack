<template>
  <div class="applications-page">

    <Navbar />

    <main class="applications-container">

      <!-- =================================
           PAGE HEADER
           ================================= -->

      <section class="page-header">

        <div class="header-copy">

          <span class="eyebrow">
            APPLICATIONS
          </span>

          <h1>
            Your job search
          </h1>

          <p>
            Track every opportunity and keep your
            application process organized.
          </p>

        </div>

        <button
          type="button"
          class="primary-button"
          @click="openCreateModal"
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
           FILTER CARD
           ================================= -->

      <section class="filter-card">

        <div class="search-wrapper">

          <span class="search-icon">
            ⌕
          </span>

          <input
            v-model="search"
            type="text"
            placeholder="Search company or position..."
            @input="handleSearch"
          />

          <button
            v-if="search"
            type="button"
            class="clear-search"
            @click="clearSearch"
          >
            ×
          </button>

        </div>


        <div class="status-filter">

          <button
            type="button"
            class="filter-button"
            :class="{
              active: !status
            }"
            @click="changeStatus('')"
          >
            All
          </button>

          <button
            type="button"
            class="filter-button"
            :class="{
              active: status === 'WISHLIST'
            }"
            @click="changeStatus('WISHLIST')"
          >
            Wishlist
          </button>

          <button
            type="button"
            class="filter-button"
            :class="{
              active: status === 'APPLIED'
            }"
            @click="changeStatus('APPLIED')"
          >
            Applied
          </button>

          <button
            type="button"
            class="filter-button"
            :class="{
              active: status === 'INTERVIEW'
            }"
            @click="changeStatus('INTERVIEW')"
          >
            Interview
          </button>

          <button
            type="button"
            class="filter-button"
            :class="{
              active: status === 'OFFER'
            }"
            @click="changeStatus('OFFER')"
          >
            Offer
          </button>

          <button
            type="button"
            class="filter-button"
            :class="{
              active: status === 'REJECTED'
            }"
            @click="changeStatus('REJECTED')"
          >
            Rejected
          </button>

        </div>

      </section>


      <!-- =================================
           RESULT SUMMARY
           ================================= -->

      <div class="result-bar">

        <div>
          <strong>
            {{ applications.length }}
          </strong>

          <span>
            {{
              applications.length === 1
                ? 'application'
                : 'applications'
            }}
          </span>
        </div>

        <div
          v-if="search || status"
          class="active-filters"
        >

          <span class="filter-label">
            Filtered by
          </span>

          <button
            v-if="search"
            type="button"
            class="filter-chip"
            @click="clearSearch"
          >
            "{{ search }}"
            <span>×</span>
          </button>

          <button
            v-if="status"
            type="button"
            class="filter-chip"
            @click="changeStatus('')"
          >
            {{ formatStatus(status) }}
            <span>×</span>
          </button>

        </div>

      </div>


      <!-- =================================
           LOADING
           ================================= -->

      <div
        v-if="loading"
        class="state-card"
      >

        <div class="loading-spinner"></div>

        <p>
          Loading applications...
        </p>

      </div>


      <!-- =================================
           EMPTY
           ================================= -->

      <div
        v-else-if="applications.length === 0"
        class="empty-card"
      >

        <div class="empty-icon">
          +
        </div>

        <h2>
          No applications found
        </h2>

        <p v-if="search || status">
          Try changing your search or filter,
          or add a new application.
        </p>

        <p v-else>
          Start tracking your job applications
          and keep everything organized.
        </p>

        <button
          type="button"
          class="primary-button"
          @click="openCreateModal"
        >
          Add application
        </button>

      </div>


      <!-- =================================
           APPLICATION LIST
           ================================= -->

      <section
        v-else
        class="application-list"
      >

        <article
          v-for="application in applications"
          :key="application.id"
          class="application-card"
          @click="viewApplication(application)"
        >

          <!-- COMPANY -->

          <div class="company-avatar">
            {{ getCompanyInitial(application.companyName) }}
          </div>


          <!-- MAIN -->

          <div class="application-main">

            <div class="application-title-row">

              <h2>
                {{ application.companyName }}
              </h2>

              <span
                class="status-badge"
                :class="getStatusClass(application.status)"
              >
                {{ formatStatus(application.status) }}
              </span>

            </div>

            <p class="position">
              {{ application.position }}
            </p>

            <div class="meta-row">

              <span v-if="application.location">
                <span class="meta-icon">⌖</span>
                {{ application.location }}
              </span>

              <span>
                <span class="meta-icon">◷</span>
                {{ formatDate(application.applicationDate) }}
              </span>

            </div>

            <p
              v-if="application.notes"
              class="notes-preview"
            >
              {{ application.notes }}
            </p>

          </div>


          <!-- ACTIONS -->

          <div
            class="card-actions"
            @click.stop
          >

            <button
              type="button"
              class="icon-button"
              title="View application"
              @click="viewApplication(application)"
            >
              →
            </button>

            <button
              type="button"
              class="icon-button edit-button"
              title="Edit application"
              @click="openEditModal(application)"
            >
              ✎
            </button>

            <button
              type="button"
              class="icon-button delete-button"
              title="Delete application"
              @click="openDeleteModal(application)"
            >
              ×
            </button>

          </div>

        </article>

      </section>

    </main>


    <!-- =================================
         CREATE / EDIT MODAL
         ================================= -->

    <div
      v-if="showFormModal"
      class="modal-overlay"
      @click.self="closeFormModal"
    >

      <div class="modal-card">

        <div class="modal-header">

          <div>
            <span class="modal-eyebrow">
              {{ editingId ? 'EDIT APPLICATION' : 'NEW APPLICATION' }}
            </span>

            <h2>
              {{
                editingId
                  ? 'Update application'
                  : 'Add application'
              }}
            </h2>

            <p>
              {{
                editingId
                  ? 'Keep your application details up to date.'
                  : 'Add a new opportunity to your career tracker.'
              }}
            </p>
          </div>

          <button
            type="button"
            class="modal-close"
            @click="closeFormModal"
          >
            ×
          </button>

        </div>


        <form
          class="application-form"
          @submit.prevent="saveApplication"
        >

          <!-- COMPANY -->

          <div class="form-group">

            <label for="companyName">
              Company name
              <span>*</span>
            </label>

            <input
              id="companyName"
              v-model="form.companyName"
              type="text"
              maxlength="150"
              placeholder="e.g. Google"
              :disabled="saving"
            />

          </div>


          <!-- POSITION -->

          <div class="form-group">

            <label for="position">
              Position
              <span>*</span>
            </label>

            <input
              id="position"
              v-model="form.position"
              type="text"
              maxlength="150"
              placeholder="e.g. Frontend Developer"
              :disabled="saving"
            />

          </div>


          <!-- STATUS -->

          <div class="form-row">

            <div class="form-group">

              <label for="status">
                Status
                <span>*</span>
              </label>

              <select
                id="status"
                v-model="form.status"
                :disabled="saving"
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


            <!-- DATE -->

            <div class="form-group">

              <label for="applicationDate">
                Application date
              </label>

              <input
                id="applicationDate"
                v-model="form.applicationDate"
                type="date"
                :disabled="saving"
              />

            </div>

          </div>


          <!-- LOCATION -->

          <div class="form-group">

            <label for="location">
              Location
            </label>

            <input
              id="location"
              v-model="form.location"
              type="text"
              maxlength="150"
              placeholder="e.g. Jakarta / Remote"
              :disabled="saving"
            />

          </div>


          <!-- JOB URL -->

          <div class="form-group">

            <label for="jobUrl">
              Job URL
            </label>

            <input
              id="jobUrl"
              v-model="form.jobUrl"
              type="url"
              maxlength="500"
              placeholder="https://..."
              :disabled="saving"
            />

          </div>


          <!-- NOTES -->

          <div class="form-group">

            <label for="notes">
              Notes
            </label>

            <textarea
              id="notes"
              v-model="form.notes"
              rows="4"
              placeholder="Add any notes about this application..."
              :disabled="saving"
            ></textarea>

          </div>


          <!-- FORM ERROR -->

          <div
            v-if="formError"
            class="form-error"
          >
            <span>!</span>
            {{ formError }}
          </div>


          <!-- ACTIONS -->

          <div class="modal-actions">

            <button
              type="button"
              class="secondary-button"
              @click="closeFormModal"
              :disabled="saving"
            >
              Cancel
            </button>

            <button
              type="submit"
              class="primary-button"
              :disabled="saving"
            >

              <span
                v-if="saving"
                class="button-spinner"
              ></span>

              {{
                saving
                  ? 'Saving...'
                  : editingId
                    ? 'Save changes'
                    : 'Add application'
              }}

            </button>

          </div>

        </form>

      </div>

    </div>


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

        <div class="delete-content">

          <span class="modal-eyebrow">
            DELETE APPLICATION
          </span>

          <h2>
            Delete this application?
          </h2>

          <p>
            You're about to remove
            <strong>
              {{ selectedApplication?.companyName }}
            </strong>
            from your applications.
            This action cannot be undone.
          </p>

        </div>


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
  onMounted,
  reactive,
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

const applications = ref([])

const loading = ref(true)

const errorMessage = ref('')


/* =================================
   FILTER
   ================================= */

const search = ref(
  route.query.search || ''
)

const status = ref(
  route.query.status || ''
)


/* =================================
   FORM MODAL
   ================================= */

const showFormModal = ref(false)

const editingId = ref(null)

const saving = ref(false)

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


/* =================================
   DELETE MODAL
   ================================= */

const showDeleteModal = ref(false)

const selectedApplication = ref(null)

const deleting = ref(false)

const deleteError = ref('')


/* =================================
   LOAD APPLICATIONS
   ================================= */

const loadApplications = async () => {

  loading.value = true

  errorMessage.value = ''

  try {

    const params = {}

    if (search.value.trim()) {
      params.search =
        search.value.trim()
    }

    if (status.value) {
      params.status =
        status.value
    }

    const response =
      await api.get(
        '/applications',
        { params }
      )

    applications.value =
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
      'Failed to load applications.'

  } finally {

    loading.value = false
  }

}


/* =================================
   SEARCH
   ================================= */

let searchTimer = null

const handleSearch = () => {

  clearTimeout(searchTimer)

  searchTimer = setTimeout(() => {

    updateQuery()

    loadApplications()

  }, 350)

}


const clearSearch = () => {

  search.value = ''

  updateQuery()

  loadApplications()

}


/* =================================
   STATUS
   ================================= */

const changeStatus = (newStatus) => {

  status.value = newStatus

  updateQuery()

  loadApplications()

}


/* =================================
   QUERY
   ================================= */

const updateQuery = () => {

  const query = {}

  if (search.value.trim()) {
    query.search =
      search.value.trim()
  }

  if (status.value) {
    query.status =
      status.value
  }

  router.replace({
    path: '/applications',
    query
  })

}


/* =================================
   FORM
   ================================= */

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


const openCreateModal = () => {

  editingId.value = null

  resetForm()

  showFormModal.value = true

}


const openEditModal = (application) => {

  editingId.value =
    application.id

  form.companyName =
    application.companyName || ''

  form.position =
    application.position || ''

  form.status =
    application.status || 'WISHLIST'

  form.applicationDate =
    application.applicationDate || ''

  form.jobUrl =
    application.jobUrl || ''

  form.location =
    application.location || ''

  form.notes =
    application.notes || ''

  formError.value = ''

  showFormModal.value = true

}


const closeFormModal = () => {

  if (saving.value) {
    return
  }

  showFormModal.value = false

  editingId.value = null

  resetForm()

}


/* =================================
   SAVE
   ================================= */

const saveApplication = async () => {

  formError.value = ''

  if (!form.companyName.trim()) {

    formError.value =
      'Company name is required.'

    return
  }

  if (!form.position.trim()) {

    formError.value =
      'Position is required.'

    return
  }

  if (!form.status) {

    formError.value =
      'Status is required.'

    return
  }

  saving.value = true

  try {

    const payload = {
      companyName:
        form.companyName.trim(),

      position:
        form.position.trim(),

      status:
        form.status,

      applicationDate:
        form.applicationDate || null,

      jobUrl:
        form.jobUrl.trim() || null,

      location:
        form.location.trim() || null,

      notes:
        form.notes.trim() || null
    }


    if (editingId.value) {

      await api.put(
        `/applications/${editingId.value}`,
        payload
      )

    } else {

      await api.post(
        '/applications',
        payload
      )

    }

    showFormModal.value = false

    editingId.value = null

    resetForm()

    await loadApplications()

  } catch (error) {

    console.error(error)

    if (
      error.response?.status === 401
    ) {

      logoutAndRedirect()

      return
    }

    formError.value =
      error.response?.data?.message ||
      'Unable to save application.'

  } finally {

    saving.value = false
  }

}


/* =================================
   DELETE
   ================================= */

const openDeleteModal = (application) => {

  selectedApplication.value =
    application

  deleteError.value = ''

  showDeleteModal.value = true

}


const closeDeleteModal = () => {

  if (deleting.value) {
    return
  }

  showDeleteModal.value = false

  selectedApplication.value = null

  deleteError.value = ''

}


const deleteApplication = async () => {

  if (!selectedApplication.value) {
    return
  }

  deleting.value = true

  deleteError.value = ''

  try {

    await api.delete(
      `/applications/${selectedApplication.value.id}`
    )

    showDeleteModal.value = false

    selectedApplication.value = null

    await loadApplications()

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
   NAVIGATION
   ================================= */

const viewApplication = (application) => {

  router.push(
    `/applications/${application.id}`
  )

}


const logoutAndRedirect = () => {

  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('name')
  localStorage.removeItem('email')

  router.push('/login')

}


/* =================================
   HELPERS
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


const formatStatus = (value) => {

  const statusMap = {
    WISHLIST: 'Wishlist',
    APPLIED: 'Applied',
    INTERVIEW: 'Interview',
    OFFER: 'Offer',
    REJECTED: 'Rejected'
  }

  return statusMap[value] || value

}


const getStatusClass = (value) => {

  return `status-${value.toLowerCase()}`

}


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
   INITIAL LOAD
   ================================= */

onMounted(async () => {

  await loadApplications()

  const editId =
    route.query.edit

  if (editId) {

    const application =
      applications.value.find(
        item =>
          String(item.id) ===
          String(editId)
      )

    if (application) {

      openEditModal(application)

      router.replace({
        path: '/applications',
        query: {
          ...route.query,
          edit: undefined
        }
      })

    }

  }

})

</script>


<style scoped>

/* =========================================
   PAGE
   ========================================= */

.applications-page {
  min-height: 100vh;

  background: var(--background);
}

.applications-container {
  width: 100%;
  max-width: 1240px;

  margin: 0 auto;

  padding: 42px 28px 70px;
}


/* =========================================
   HEADER
   ========================================= */

.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;

  gap: 30px;

  margin-bottom: 30px;
}

.header-copy {
  max-width: 650px;
}

.eyebrow,
.modal-eyebrow {
  display: block;

  margin-bottom: 9px;

  color: var(--primary);

  font-size: 9px;
  font-weight: 800;

  letter-spacing: 0.14em;
}

.header-copy h1 {
  margin: 0;

  color: var(--text-primary);

  font-size: clamp(34px, 4vw, 50px);
  line-height: 1.05;

  letter-spacing: -0.055em;
}

.header-copy p {
  margin: 14px 0 0;

  color: var(--text-secondary);

  font-size: 13px;
  line-height: 1.6;
}


/* =========================================
   BUTTON
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
    box-shadow 0.2s ease,
    opacity 0.2s ease;
}

.primary-button:hover:not(:disabled) {
  transform: translateY(-1px);

  box-shadow:
    0 11px 25px rgba(49, 85, 217, 0.22);
}

.primary-button:disabled {
  opacity: 0.6;

  cursor: not-allowed;
}

.button-plus {
  font-size: 15px;
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
   FILTER
   ========================================= */

.filter-card {
  display: flex;
  align-items: center;

  gap: 14px;

  padding: 12px;

  border: 1px solid var(--border);

  border-radius: 13px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);
}

.search-wrapper {
  position: relative;

  flex: 1;
}

.search-wrapper input {
  width: 100%;
  height: 38px;

  padding:
    0 35px
    0 34px;

  border: 1px solid var(--border-strong);

  border-radius: 8px;

  background: var(--surface-soft);

  color: var(--text-primary);

  font-size: 10px;

  outline: none;

  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.search-wrapper input::placeholder {
  color: var(--text-muted);
}

.search-wrapper input:focus {
  border-color: var(--primary);

  box-shadow:
    0 0 0 3px var(--primary-soft);
}

.search-icon {
  position: absolute;

  left: 12px;
  top: 50%;

  transform: translateY(-50%);

  color: var(--text-muted);

  font-size: 16px;

  pointer-events: none;
}

.clear-search {
  position: absolute;

  right: 10px;
  top: 50%;

  transform: translateY(-50%);

  width: 20px;
  height: 20px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: var(--border);

  color: var(--text-secondary);

  font-size: 12px;

  cursor: pointer;
}

.status-filter {
  display: flex;
  align-items: center;

  gap: 3px;

  overflow-x: auto;

  padding-bottom: 1px;
}

.filter-button {
  flex-shrink: 0;

  min-height: 32px;

  padding: 6px 10px;

  border-radius: 7px;

  background: transparent;

  color: var(--text-secondary);

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;

  transition:
    background 0.2s ease,
    color 0.2s ease;
}

.filter-button:hover {
  background: var(--primary-soft);

  color: var(--primary);
}

.filter-button.active {
  background: var(--primary);

  color: #ffffff;
}


/* =========================================
   RESULT BAR
   ========================================= */

.result-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;

  gap: 15px;

  min-height: 52px;
}

.result-bar > div:first-child {
  display: flex;
  align-items: baseline;

  gap: 4px;
}

.result-bar strong {
  color: var(--text-primary);

  font-size: 12px;
}

.result-bar > div:first-child span {
  color: var(--text-muted);

  font-size: 9px;
}

.active-filters {
  display: flex;
  align-items: center;

  gap: 6px;
}

.filter-label {
  color: var(--text-muted);

  font-size: 8px;
}

.filter-chip {
  display: inline-flex;
  align-items: center;

  gap: 6px;

  padding: 5px 8px;

  border: 1px solid #dce2ff;

  border-radius: 999px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 8px;
  font-weight: 700;

  cursor: pointer;
}

.filter-chip span {
  font-size: 11px;
}


/* =========================================
   APPLICATION LIST
   ========================================= */

.application-list {
  display: flex;
  flex-direction: column;

  gap: 9px;
}

.application-card {
  display: grid;

  grid-template-columns:
    42px
    minmax(0, 1fr)
    auto;

  align-items: center;

  gap: 14px;

  padding: 16px;

  border: 1px solid var(--border);

  border-radius: 13px;

  background: var(--surface);

  box-shadow:
    0 5px 20px rgba(23, 32, 51, 0.035);

  cursor: pointer;

  transition:
    transform 0.2s ease,
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.application-card:hover {
  transform: translateY(-1px);

  border-color: #dce2ff;

  box-shadow:
    0 9px 25px rgba(23, 32, 51, 0.07);
}

.company-avatar {
  width: 42px;
  height: 42px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 11px;

  background:
    linear-gradient(
      145deg,
      var(--primary-soft),
      var(--accent-soft)
    );

  color: var(--primary);

  font-size: 13px;
  font-weight: 800;
}

.application-main {
  min-width: 0;
}

.application-title-row {
  display: flex;
  align-items: center;

  gap: 9px;

  min-width: 0;
}

.application-title-row h2 {
  margin: 0;

  overflow: hidden;

  color: var(--text-primary);

  font-size: 12px;
  font-weight: 750;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.position {
  margin: 5px 0 0;

  color: var(--text-secondary);

  font-size: 9px;
}

.meta-row {
  display: flex;
  align-items: center;

  gap: 12px;

  margin-top: 8px;

  color: var(--text-muted);

  font-size: 8px;
}

.meta-row span {
  display: inline-flex;
  align-items: center;

  gap: 3px;
}

.meta-icon {
  color: var(--primary);

  font-size: 10px;
}

.notes-preview {
  max-width: 700px;

  margin: 8px 0 0;

  overflow: hidden;

  color: var(--text-muted);

  font-size: 8px;
  line-height: 1.5;

  text-overflow: ellipsis;
  white-space: nowrap;
}


/* =========================================
   STATUS
   ========================================= */

.status-badge {
  flex-shrink: 0;

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


/* =========================================
   ACTIONS
   ========================================= */

.card-actions {
  display: flex;
  align-items: center;

  gap: 5px;
}

.icon-button {
  width: 30px;
  height: 30px;

  display: flex;
  align-items: center;
  justify-content: center;

  border: 1px solid var(--border);

  border-radius: 7px;

  background: #ffffff;

  color: var(--text-secondary);

  font-size: 12px;

  cursor: pointer;

  transition:
    color 0.2s ease,
    border-color 0.2s ease,
    background 0.2s ease;
}

.icon-button:hover {
  color: var(--primary);

  border-color: #dce2ff;

  background: var(--primary-soft);
}

.delete-button:hover {
  color: var(--danger);

  border-color: #edcaca;

  background: var(--danger-soft);
}


/* =========================================
   STATES
   ========================================= */

.state-card,
.empty-card {
  min-height: 250px;

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

.state-card p {
  margin: 11px 0 0;

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

.empty-icon {
  width: 46px;
  height: 46px;

  display: flex;
  align-items: center;
  justify-content: center;

  margin-bottom: 14px;

  border-radius: 13px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 22px;
}

.empty-card h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 16px;

  letter-spacing: -0.02em;
}

.empty-card p {
  max-width: 390px;

  margin: 8px 0 17px;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.6;
}


/* =========================================
   MODAL
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

  overflow-y: auto;
}

.modal-card,
.delete-modal {
  width: 100%;
  max-width: 500px;

  max-height: calc(100vh - 48px);

  overflow-y: auto;

  border: 1px solid var(--border);

  border-radius: 16px;

  background: #ffffff;

  box-shadow:
    0 25px 70px rgba(23, 32, 51, 0.18);

  animation:
    modal-in 0.2s ease;
}

.modal-card {
  padding: 23px;
}

.delete-modal {
  max-width: 400px;

  padding: 26px;

  text-align: center;
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
   MODAL HEADER
   ========================================= */

.modal-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  gap: 20px;

  margin-bottom: 22px;
}

.modal-eyebrow {
  margin-bottom: 6px;

  font-size: 8px;
}

.modal-header h2,
.delete-content h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 21px;

  letter-spacing: -0.035em;
}

.modal-header p,
.delete-content p {
  margin: 7px 0 0;

  color: var(--text-secondary);

  font-size: 9px;
  line-height: 1.55;
}

.modal-close {
  width: 30px;
  height: 30px;

  flex-shrink: 0;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 8px;

  background: var(--surface-soft);

  color: var(--text-secondary);

  font-size: 17px;

  cursor: pointer;

  transition:
    background 0.2s ease,
    color 0.2s ease;
}

.modal-close:hover {
  background: var(--danger-soft);

  color: var(--danger);
}


/* =========================================
   FORM
   ========================================= */

.application-form {
  display: flex;
  flex-direction: column;

  gap: 14px;
}

.form-row {
  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 12px;
}

.form-group {
  display: flex;
  flex-direction: column;

  gap: 6px;
}

.form-group label {
  color: var(--text-primary);

  font-size: 9px;
  font-weight: 750;
}

.form-group label span {
  color: var(--danger);
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;

  border: 1px solid var(--border-strong);

  border-radius: 8px;

  background: #ffffff;

  color: var(--text-primary);

  font-size: 10px;

  outline: none;

  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.form-group input,
.form-group select {
  height: 39px;

  padding: 0 11px;
}

.form-group textarea {
  min-height: 85px;

  padding: 10px 11px;

  resize: vertical;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: var(--text-muted);
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: var(--primary);

  box-shadow:
    0 0 0 3px var(--primary-soft);
}

.form-group input:disabled,
.form-group select:disabled,
.form-group textarea:disabled {
  background: var(--surface-soft);

  cursor: not-allowed;
}


/* =========================================
   FORM ERROR
   ========================================= */

.form-error {
  display: flex;
  align-items: center;

  gap: 7px;

  padding: 9px 10px;

  border: 1px solid #f1cccc;

  border-radius: 8px;

  background: var(--danger-soft);

  color: var(--danger);

  font-size: 9px;
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


/* =========================================
   MODAL ACTIONS
   ========================================= */

.modal-actions {
  display: flex;
  justify-content: flex-end;

  gap: 8px;

  margin-top: 5px;
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
  background: #bf3f3f;

  border-color: #bf3f3f;
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


/* =========================================
   DELETE
   ========================================= */

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

.delete-content strong {
  color: var(--text-primary);
}

.delete-modal .form-error {
  margin-top: 16px;

  text-align: left;
}

.delete-modal .modal-actions {
  justify-content: center;

  margin-top: 22px;
}


/* =========================================
   ANIMATION
   ========================================= */

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}


/* =========================================
   TABLET
   ========================================= */

@media (max-width: 950px) {

  .applications-container {
    padding: 35px 20px 60px;
  }

  .filter-card {
    align-items: stretch;

    flex-direction: column;
  }

  .status-filter {
    width: 100%;
  }

}


/* =========================================
   MOBILE
   ========================================= */

@media (max-width: 700px) {

  .applications-container {
    padding: 28px 16px 50px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;

    gap: 18px;
  }

  .header-copy h1 {
    font-size: 35px;
  }

  .header-copy p {
    font-size: 11px;
  }

  .page-header .primary-button {
    width: 100%;
  }

  .filter-card {
    padding: 10px;
  }

  .status-filter {
    padding-bottom: 4px;
  }

  .result-bar {
    align-items: flex-start;

    flex-direction: column;

    justify-content: center;

    padding: 9px 0;

    gap: 7px;
  }

  .active-filters {
    flex-wrap: wrap;
  }

  .application-card {
    grid-template-columns:
      40px
      minmax(0, 1fr);

    align-items: flex-start;

    gap: 11px;

    padding: 14px;
  }

  .application-title-row {
    align-items: flex-start;

    flex-direction: column;

    gap: 6px;
  }

  .card-actions {
    grid-column: 2;

    justify-content: flex-start;

    margin-top: -2px;
  }

  .modal-overlay {
    align-items: flex-end;

    padding: 10px;
  }

  .modal-card,
  .delete-modal {
    max-height: calc(100vh - 20px);

    border-radius: 15px;
  }

  .modal-card {
    padding: 19px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

}


/* =========================================
   SMALL MOBILE
   ========================================= */

@media (max-width: 400px) {

  .applications-container {
    padding: 24px 12px 45px;
  }

  .header-copy h1 {
    font-size: 30px;
  }

  .application-card {
    padding: 12px;
  }

  .company-avatar {
    width: 38px;
    height: 38px;
  }

  .application-title-row h2 {
    font-size: 11px;
  }

  .position {
    font-size: 8px;
  }

  .meta-row {
    flex-direction: column;
    align-items: flex-start;

    gap: 4px;
  }

  .modal-actions {
    flex-direction: column-reverse;
  }

  .modal-actions button {
    width: 100%;
  }

}

</style>