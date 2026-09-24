<template>
  <div class="auth-page">

    <div class="auth-container">

      <!-- Brand / Introduction -->
      <section class="auth-intro">

        <div class="intro-content">

          <div class="brand-mark">
            CT
          </div>

          <p class="intro-eyebrow">
            CAREER MANAGEMENT
          </p>

          <h1>
            Keep your career journey
            <span>organized.</span>
          </h1>

          <p class="intro-description">
            Track applications, manage your progress,
            and stay organized throughout your job search.
          </p>

        </div>

      </section>

      <!-- Login Form -->
      <section class="auth-form-section">

        <div class="auth-card">

          <div class="form-header">

            <p class="mobile-brand">
              CareerTrack
            </p>

            <h2>
              Welcome back
            </h2>

            <p>
              Sign in to continue to your dashboard.
            </p>

          </div>

          <form
            @submit.prevent="handleLogin"
            novalidate
          >

            <!-- Email -->
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

            <!-- Password -->
            <div class="form-group">

              <div class="password-label">

                <label for="password">
                  Password
                </label>

              </div>

              <input
                id="password"
                v-model="password"
                type="password"
                autocomplete="current-password"
                placeholder="Enter your password"
                :disabled="loading"
                required
              />

            </div>

            <!-- Error -->
            <div
              v-if="errorMessage"
              class="error-message"
            >
              {{ errorMessage }}
            </div>

            <!-- Submit -->
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
                {{ loading ? 'Signing in...' : 'Sign In' }}
              </span>

            </button>

          </form>

          <div class="register-prompt">

            <span>
              Don't have an account?
            </span>

            <router-link to="/register">
              Create an account
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

const email = ref('')
const password = ref('')

const loading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {

  errorMessage.value = ''

  if (!email.value.trim()) {
    errorMessage.value = 'Email is required.'
    return
  }

  if (!password.value) {
    errorMessage.value = 'Password is required.'
    return
  }

  loading.value = true

  try {

    const response = await api.post(
      '/auth/login',
      {
        email: email.value.trim(),
        password: password.value
      }
    )

    const data = response.data

    localStorage.setItem(
      'token',
      data.token
    )

    localStorage.setItem(
      'userId',
      String(data.userId)
    )

    localStorage.setItem(
      'name',
      data.name
    )

    localStorage.setItem(
      'email',
      data.email
    )

    router.push('/dashboard')

  } catch (error) {

    console.error(error)

    if (error.response?.status === 401) {

      errorMessage.value =
        error.response.data?.message ||
        'Email atau password salah.'

    } else {

      errorMessage.value =
        error.response?.data?.message ||
        'Unable to sign in. Please try again.'

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
   FORM SECTION
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

  gap: 19px;
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

.password-label {
  display: flex;
  justify-content: space-between;
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

.error-message {
  padding: 11px 13px;

  border: 1px solid #fecaca;
  border-radius: 8px;

  background: #fff7f7;
  color: #b91c1c;

  font-size: 13px;
  line-height: 1.5;
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

.register-prompt {
  display: flex;
  justify-content: center;

  gap: 5px;

  margin-top: 26px;

  color: #6b7280;

  font-size: 13px;
}

.register-prompt a {
  color: #1a2e6f;

  font-weight: 600;

  text-decoration: none;
}

.register-prompt a:hover {
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

  .register-prompt {
    flex-direction: column;
    align-items: center;
  }

}
</style>