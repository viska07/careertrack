<template>
  <div class="auth-page">

    <div class="auth-container">

      <!-- Introduction -->
      <section class="auth-intro">

        <div class="intro-content">

          <div class="brand-mark">
            CT
          </div>

          <p class="intro-eyebrow">
            CAREER MANAGEMENT
          </p>

          <h1>
            Build a clearer
            <span>career path.</span>
          </h1>

          <p class="intro-description">
            Create your CareerTrack account and keep
            every job application organized.
          </p>

        </div>

      </section>

      <!-- Register Form -->
      <section class="auth-form-section">

        <div class="auth-card">

          <div class="form-header">

            <p class="mobile-brand">
              CareerTrack
            </p>

            <h2>
              Create your account
            </h2>

            <p>
              Start tracking your job applications today.
            </p>

          </div>

          <form
            @submit.prevent="handleRegister"
            novalidate
          >

            <div class="form-group">

              <label for="name">
                Full Name
              </label>

              <input
                id="name"
                v-model="name"
                type="text"
                autocomplete="name"
                placeholder="Enter your full name"
                :disabled="loading"
                required
              />

            </div>

            <div class="form-group">

              <label for="email">
                Email
              </label>

              <input
                id="email"
                v-model="email"
                type="email"
                autocomplete="email"
                placeholder="Enter your email"
                :disabled="loading"
                required
              />

            </div>

            <div class="form-group">

              <label for="password">
                Password
              </label>

              <input
                id="password"
                v-model="password"
                type="password"
                autocomplete="new-password"
                placeholder="Create a password"
                :disabled="loading"
                required
              />

            </div>

            <div class="form-group">

              <label for="confirmPassword">
                Confirm Password
              </label>

              <input
                id="confirmPassword"
                v-model="confirmPassword"
                type="password"
                autocomplete="new-password"
                placeholder="Confirm your password"
                :disabled="loading"
                required
              />

            </div>

            <div
              v-if="errorMessage"
              class="error-message"
            >
              {{ errorMessage }}
            </div>

            <div
              v-if="successMessage"
              class="success-message"
            >
              {{ successMessage }}
            </div>

            <button
              type="submit"
              class="submit-button"
              :disabled="loading"
            >

              <span
                v-if="loading"
                class="spinner"
              ></span>

              <span>
                {{
                  loading
                    ? 'Creating account...'
                    : 'Create Account'
                }}
              </span>

            </button>

          </form>

          <div class="login-prompt">

            <span>
              Already have an account?
            </span>

            <router-link to="/login">
              Sign in
            </router-link>

          </div>

        </div>

      </section>

    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const router = useRouter()

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const loading = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const handleRegister = async () => {

  errorMessage.value = ''
  successMessage.value = ''

  if (!name.value.trim()) {
    errorMessage.value = 'Full name is required.'
    return
  }

  if (!email.value.trim()) {
    errorMessage.value = 'Email is required.'
    return
  }

  if (!password.value) {
    errorMessage.value = 'Password is required.'
    return
  }

  if (password.value.length < 6) {
    errorMessage.value =
      'Password must be at least 6 characters.'
    return
  }

  if (password.value !== confirmPassword.value) {
    errorMessage.value =
      'Password and confirm password do not match.'
    return
  }

  loading.value = true

  try {

    const response = await api.post(
      '/auth/register',
      {
        name: name.value.trim(),
        email: email.value.trim(),
        password: password.value
      }
    )

    successMessage.value =
      response.data.message ||
      'Registration successful.'

    setTimeout(() => {
      router.push('/login')
    }, 1000)

  } catch (error) {

    console.error(error)

    if (error.response?.status === 409) {

      errorMessage.value =
        error.response.data?.message ||
        'Email sudah terdaftar.'

    } else {

      errorMessage.value =
        error.response?.data?.message ||
        'Unable to create account. Please try again.'

    }

  } finally {

    loading.value = false

  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;

  display: flex;

  background: #f7f8fc;
}

.auth-container {
  width: 100%;
  min-height: 100vh;

  display: grid;
  grid-template-columns: 1fr 1fr;
}

/* =========================
   INTRO
========================= */

.auth-intro {
  display: flex;
  align-items: center;

  padding: 64px;

  background: #1a2e6f;
}

.intro-content {
  width: 100%;
  max-width: 520px;
  margin: 0 auto;
}

.brand-mark {
  width: 48px;
  height: 48px;

  display: flex;
  align-items: center;
  justify-content: center;

  margin-bottom: 34px;

  border-radius: 12px;

  background: rgba(255, 255, 255, 0.14);
  color: #ffffff;

  font-size: 15px;
  font-weight: 800;
}

.intro-eyebrow {
  margin: 0 0 14px;

  color: rgba(255, 255, 255, 0.7);

  font-size: 12px;
  font-weight: 700;

  letter-spacing: 0.12em;
}

.auth-intro h1 {
  max-width: 480px;

  margin: 0;

  color: #ffffff;

  font-size: clamp(36px, 4vw, 58px);
  line-height: 1.08;
  letter-spacing: -0.03em;
}

.auth-intro h1 span {
  display: block;
  color: #bfc9ef;
}

.intro-description {
  max-width: 440px;

  margin: 24px 0 0;

  color: rgba(255, 255, 255, 0.72);

  font-size: 16px;
  line-height: 1.7;
}

/* =========================
   FORM
========================= */

.auth-form-section {
  display: flex;
  align-items: center;
  justify-content: center;

  padding: 48px;
}

.auth-card {
  width: 100%;
  max-width: 430px;
}

.form-header {
  margin-bottom: 30px;
}

.mobile-brand {
  display: none;
}

.form-header h2 {
  margin: 0;

  color: #111827;

  font-size: 30px;
  line-height: 1.2;
}

.form-header p:not(.mobile-brand) {
  margin: 9px 0 0;

  color: #6b7280;

  font-size: 14px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 17px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  color: #374151;

  font-size: 13px;
  font-weight: 600;
}

.form-group input {
  width: 100%;
  height: 46px;

  box-sizing: border-box;

  padding: 0 13px;

  border: 1px solid #d9dce5;
  border-radius: 8px;

  background: #ffffff;
  color: #111827;

  font-size: 14px;

  outline: none;

  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.form-group input::placeholder {
  color: #9ca3af;
}

.form-group input:focus {
  border-color: #1a2e6f;

  box-shadow:
    0 0 0 3px rgba(26, 46, 111, 0.08);
}

.form-group input:disabled {
  background: #f9fafb;
  cursor: not-allowed;
}

.error-message,
.success-message {
  padding: 11px 13px;

  border-radius: 8px;

  font-size: 13px;
  line-height: 1.5;
}

.error-message {
  border: 1px solid #fecaca;
  background: #fff7f7;
  color: #b91c1c;
}

.success-message {
  border: 1px solid #bbf7d0;
  background: #f0fdf4;
  color: #15803d;
}

.submit-button {
  width: 100%;
  height: 46px;

  display: flex;
  align-items: center;
  justify-content: center;

  gap: 9px;

  margin-top: 3px;

  border: none;
  border-radius: 8px;

  background: #1a2e6f;
  color: #ffffff;

  font-size: 14px;
  font-weight: 600;

  cursor: pointer;

  transition:
    background 0.2s ease,
    opacity 0.2s ease;
}

.submit-button:hover {
  background: #14245a;
}

.submit-button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.spinner {
  width: 15px;
  height: 15px;

  border: 2px solid rgba(255, 255, 255, 0.35);
  border-top-color: #ffffff;

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.login-prompt {
  display: flex;
  justify-content: center;

  gap: 5px;

  margin-top: 26px;

  color: #6b7280;

  font-size: 13px;
}

.login-prompt a {
  color: #1a2e6f;

  font-weight: 600;

  text-decoration: none;
}

.login-prompt a:hover {
  text-decoration: underline;
}

/* =========================
   TABLET
========================= */

@media (max-width: 900px) {

  .auth-container {
    grid-template-columns: 0.85fr 1.15fr;
  }

  .auth-intro {
    padding: 42px;
  }

  .auth-form-section {
    padding: 36px;
  }

  .auth-intro h1 {
    font-size: 42px;
  }

}

/* =========================
   MOBILE
========================= */

@media (max-width: 700px) {

  .auth-container {
    display: block;
  }

  .auth-intro {
    display: none;
  }

  .auth-form-section {
    min-height: 100vh;

    padding: 32px 20px;
  }

  .auth-card {
    max-width: 460px;
  }

  .mobile-brand {
    display: block;

    margin: 0 0 24px;

    color: #1a2e6f;

    font-size: 20px;
    font-weight: 700;
  }

  .form-header h2 {
    font-size: 27px;
  }

}

/* =========================
   SMALL MOBILE
========================= */

@media (max-width: 400px) {

  .auth-form-section {
    padding: 28px 16px;
  }

  .form-header h2 {
    font-size: 25px;
  }

  .login-prompt {
    flex-direction: column;
    align-items: center;
  }

}
</style>