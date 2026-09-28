<template>
  <div class="auth-page">

    <!-- ===============================
         VISUAL PANEL
         =============================== -->

    <section class="auth-visual">

      <div class="visual-content">

        <router-link
          to="/login"
          class="brand"
        >
          <span class="brand-mark">
            C
          </span>

          <span class="brand-name">
            Career<span>Track</span>
          </span>
        </router-link>

        <div class="visual-copy">

          <span class="visual-eyebrow">
            BUILD YOUR NEXT MOVE
          </span>

          <h1>
            Your next
            <span>opportunity</span>
            starts here.
          </h1>

          <p>
            Create your CareerTrack account and
            bring your job search, applications,
            and career progress together.
          </p>

        </div>

        <div class="visual-decoration">

          <div class="progress-card">

            <div class="progress-top">

              <span>
                Career progress
              </span>

              <strong>
                72%
              </strong>

            </div>

            <div class="progress-track">
              <div class="progress-value"></div>
            </div>

            <div class="progress-labels">
              <span>
                Applications
              </span>

              <span>
                Keep moving
              </span>
            </div>

          </div>

          <div class="floating-pill pill-one">
            <span class="pill-dot"></span>
            Stay organized
          </div>

          <div class="floating-pill pill-two">
            <span class="pill-check">
              ✓
            </span>
            Track every opportunity
          </div>

          <div class="orb orb-one"></div>
          <div class="orb orb-two"></div>

        </div>

        <div class="visual-footer">
          One place for every step of your career journey.
        </div>

      </div>

    </section>

    <!-- ===============================
         REGISTER PANEL
         =============================== -->

    <main class="auth-content">

      <div class="auth-card">

        <div class="mobile-brand">

          <router-link
            to="/login"
            class="brand"
          >
            <span class="brand-mark">
              C
            </span>

            <span class="brand-name">
              Career<span>Track</span>
            </span>
          </router-link>

        </div>

        <div class="auth-header">

          <span class="auth-eyebrow">
            GET STARTED
          </span>

          <h2>
            Create your account
          </h2>

          <p>
            Start organizing your career journey today.
          </p>

        </div>

        <!-- ===========================
             ALERTS
             =========================== -->

        <div
          v-if="errorMessage"
          class="form-alert error-alert"
        >
          <span class="alert-icon">
            !
          </span>

          <span>
            {{ errorMessage }}
          </span>
        </div>

        <div
          v-if="successMessage"
          class="form-alert success-alert"
        >
          <span class="alert-icon success-icon">
            ✓
          </span>

          <span>
            {{ successMessage }}
          </span>
        </div>

        <!-- ===========================
             FORM
             =========================== -->

        <form
          class="auth-form"
          @submit.prevent="handleRegister"
        >

          <!-- FULL NAME -->

          <div class="form-group">

            <label for="name">
              Full name
            </label>

            <div class="input-wrapper">

              <span class="input-icon">
                •
              </span>

              <input
                id="name"
                v-model="name"
                type="text"
                autocomplete="name"
                placeholder="Your full name"
                maxlength="100"
                :disabled="loading"
              />

            </div>

          </div>

          <!-- EMAIL -->

          <div class="form-group">

            <label for="email">
              Email
            </label>

            <div class="input-wrapper">

              <span class="input-icon">
                @
              </span>

              <input
                id="email"
                v-model="email"
                type="email"
                autocomplete="email"
                placeholder="you@example.com"
                maxlength="150"
                :disabled="loading"
              />

            </div>

          </div>

          <!-- PASSWORD -->

          <div class="form-group">

            <label for="password">
              Password
            </label>

            <div class="input-wrapper">

              <span class="input-icon">
                •
              </span>

              <input
                id="password"
                v-model="password"
                type="password"
                autocomplete="new-password"
                placeholder="At least 6 characters"
                :disabled="loading"
              />

            </div>

          </div>

          <!-- CONFIRM PASSWORD -->

          <div class="form-group">

            <label for="confirmPassword">
              Confirm password
            </label>

            <div class="input-wrapper">

              <span class="input-icon">
                •
              </span>

              <input
                id="confirmPassword"
                v-model="confirmPassword"
                type="password"
                autocomplete="new-password"
                placeholder="Repeat your password"
                :disabled="loading"
              />

            </div>

          </div>

          <!-- PASSWORD NOTE -->

          <div class="password-note">

            <span class="note-dot"></span>

            Use at least 6 characters for your password.

          </div>

          <!-- SUBMIT -->

          <button
            type="submit"
            class="submit-button"
            :disabled="loading"
          >

            <span
              v-if="loading"
              class="button-spinner"
            ></span>

            <span>
              {{
                loading
                  ? 'Creating account...'
                  : 'Create account'
              }}
            </span>

            <span
              v-if="!loading"
              class="button-arrow"
            >
              →
            </span>

          </button>

        </form>

        <!-- ===========================
             LOGIN
             =========================== -->

        <div class="auth-footer">

          <span>
            Already have an account?
          </span>

          <router-link
            to="/login"
          >
            Sign in
          </router-link>

        </div>

      </div>

    </main>

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

    errorMessage.value =
      'Full name is required.'

    return
  }

  if (!email.value.trim()) {

    errorMessage.value =
      'Email is required.'

    return
  }

  if (!password.value) {

    errorMessage.value =
      'Password is required.'

    return
  }

  if (password.value.length < 6) {

    errorMessage.value =
      'Password must be at least 6 characters.'

    return
  }

  if (
    password.value !==
    confirmPassword.value
  ) {

    errorMessage.value =
      'Password and confirm password do not match.'

    return
  }

  loading.value = true

  try {

    const response =
      await api.post(
        '/auth/register',
        {
          name:
            name.value.trim(),

          email:
            email.value.trim(),

          password:
            password.value
        }
      )

    successMessage.value =
      response.data.message ||
      'Registration successful.'

    setTimeout(() => {

      router.push('/login')

    }, 1200)

  } catch (error) {

    console.error(error)

    if (
      error.response?.status === 409
    ) {

      errorMessage.value =
        error.response.data?.message ||
        'Email is already registered.'

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

  display: grid;
  grid-template-columns: 1.05fr 0.95fr;

  background: var(--background);
}

/* ===============================
   VISUAL PANEL
   =============================== */

.auth-visual {
  position: relative;

  min-height: 100vh;

  overflow: hidden;

  background:
    radial-gradient(
      circle at 85% 15%,
      rgba(124, 92, 252, 0.35),
      transparent 25%
    ),
    radial-gradient(
      circle at 15% 85%,
      rgba(49, 85, 217, 0.3),
      transparent 30%
    ),
    linear-gradient(
      145deg,
      #18275f,
      #3155d9 62%,
      #6952dc
    );

  color: #ffffff;
}

.visual-content {
  position: relative;

  width: 100%;
  max-width: 650px;
  min-height: 100vh;

  margin: 0 auto;

  padding: 42px 56px;

  display: flex;
  flex-direction: column;
}

.brand {
  display: inline-flex;
  align-items: center;

  gap: 10px;

  width: fit-content;

  color: #ffffff;

  text-decoration: none;
}

.brand-mark {
  width: 36px;
  height: 36px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 10px;

  background: rgba(255, 255, 255, 0.14);

  border: 1px solid rgba(255, 255, 255, 0.2);

  color: #ffffff;

  font-size: 16px;
  font-weight: 800;

  box-shadow:
    0 8px 22px rgba(0, 0, 0, 0.1);
}

.brand-name {
  font-size: 18px;
  font-weight: 800;

  letter-spacing: -0.03em;
}

.brand-name span {
  color: #c9c1ff;
}

/* ===============================
   VISUAL COPY
   =============================== */

.visual-copy {
  position: relative;
  z-index: 2;

  max-width: 510px;

  margin-top: auto;
  margin-bottom: auto;
}

.visual-eyebrow {
  display: block;

  margin-bottom: 15px;

  color: rgba(255, 255, 255, 0.62);

  font-size: 10px;
  font-weight: 800;

  letter-spacing: 0.15em;
}

.visual-copy h1 {
  margin: 0;

  color: #ffffff;

  font-size: clamp(42px, 4.5vw, 62px);
  line-height: 1.03;

  letter-spacing: -0.055em;
}

.visual-copy h1 span {
  color: #c9c1ff;
}

.visual-copy p {
  max-width: 470px;

  margin: 22px 0 0;

  color: rgba(255, 255, 255, 0.68);

  font-size: 14px;
  line-height: 1.75;
}

/* ===============================
   DECORATION
   =============================== */

.visual-decoration {
  position: absolute;
  inset: 0;

  pointer-events: none;
}

.orb {
  position: absolute;

  border-radius: 50%;

  border: 1px solid rgba(255, 255, 255, 0.09);
}

.orb-one {
  width: 320px;
  height: 320px;

  right: -120px;
  top: 18%;
}

.orb-two {
  width: 210px;
  height: 210px;

  left: -110px;
  bottom: 13%;
}

.progress-card {
  position: absolute;

  z-index: 3;

  right: 8%;
  top: 25%;

  width: 245px;

  padding: 16px;

  border: 1px solid rgba(255, 255, 255, 0.14);

  border-radius: 14px;

  background: rgba(255, 255, 255, 0.1);

  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);

  box-shadow:
    0 20px 45px rgba(10, 19, 56, 0.16);

  transform: rotate(3deg);
}

.progress-top,
.progress-labels {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.progress-top {
  margin-bottom: 12px;
}

.progress-top span {
  color: rgba(255, 255, 255, 0.68);

  font-size: 9px;
}

.progress-top strong {
  color: #ffffff;

  font-size: 11px;
}

.progress-track {
  width: 100%;
  height: 6px;

  overflow: hidden;

  border-radius: 999px;

  background: rgba(255, 255, 255, 0.12);
}

.progress-value {
  width: 72%;
  height: 100%;

  border-radius: inherit;

  background:
    linear-gradient(
      90deg,
      #ffffff,
      #c9c1ff
    );
}

.progress-labels {
  margin-top: 9px;
}

.progress-labels span {
  color: rgba(255, 255, 255, 0.4);

  font-size: 7px;
}

.floating-pill {
  position: absolute;

  z-index: 3;

  display: flex;
  align-items: center;

  gap: 7px;

  padding: 9px 12px;

  border: 1px solid rgba(255, 255, 255, 0.13);

  border-radius: 999px;

  background: rgba(255, 255, 255, 0.09);

  color: rgba(255, 255, 255, 0.78);

  font-size: 8px;

  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

.pill-one {
  right: 17%;
  bottom: 30%;

  transform: rotate(-3deg);
}

.pill-two {
  right: 5%;
  bottom: 20%;

  transform: rotate(2deg);
}

.pill-dot {
  width: 6px;
  height: 6px;

  border-radius: 50%;

  background: #c9c1ff;

  box-shadow:
    0 0 0 3px rgba(201, 193, 255, 0.12);
}

.pill-check {
  color: #c9c1ff;

  font-size: 10px;
  font-weight: 800;
}

.visual-footer {
  position: relative;
  z-index: 2;

  color: rgba(255, 255, 255, 0.42);

  font-size: 9px;
}

/* ===============================
   AUTH CONTENT
   =============================== */

.auth-content {
  min-height: 100vh;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 40px;
}

.auth-card {
  width: 100%;
  max-width: 390px;
}

.mobile-brand {
  display: none;
}

/* ===============================
   HEADER
   =============================== */

.auth-header {
  margin-bottom: 25px;
}

.auth-eyebrow {
  display: block;

  margin-bottom: 9px;

  color: var(--primary);

  font-size: 9px;
  font-weight: 800;

  letter-spacing: 0.14em;
}

.auth-header h2 {
  margin: 0;

  color: var(--text-primary);

  font-size: 28px;
  line-height: 1.15;

  letter-spacing: -0.04em;
}

.auth-header p {
  margin: 9px 0 0;

  color: var(--text-secondary);

  font-size: 12px;
}

/* ===============================
   ALERTS
   =============================== */

.form-alert {
  display: flex;
  align-items: center;

  gap: 9px;

  margin-bottom: 15px;

  padding: 10px 11px;

  border-radius: 9px;

  font-size: 10px;
  line-height: 1.5;
}

.error-alert {
  border: 1px solid #f1cccc;

  background: var(--danger-soft);

  color: var(--danger);
}

.success-alert {
  border: 1px solid #c8eadf;

  background: var(--success-soft);

  color: var(--success);
}

.alert-icon {
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

.success-icon {
  background: var(--success);
}

/* ===============================
   FORM
   =============================== */

.auth-form {
  display: flex;
  flex-direction: column;

  gap: 13px;
}

.form-group {
  display: flex;
  flex-direction: column;

  gap: 6px;
}

.form-group label {
  color: var(--text-primary);

  font-size: 10px;
  font-weight: 700;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;

  top: 50%;
  left: 12px;

  transform: translateY(-50%);

  color: var(--text-muted);

  font-size: 13px;
  font-weight: 700;

  pointer-events: none;
}

.input-wrapper input {
  width: 100%;
  height: 42px;

  padding: 0 13px 0 34px;

  border: 1px solid var(--border-strong);
  border-radius: 9px;

  outline: none;

  background: #ffffff;

  color: var(--text-primary);

  font-size: 11px;

  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.input-wrapper input::placeholder {
  color: var(--text-muted);
}

.input-wrapper input:focus {
  border-color: var(--primary);

  box-shadow:
    0 0 0 3px var(--primary-soft);
}

.input-wrapper input:disabled {
  background: #f7f8fb;

  cursor: not-allowed;
}

/* ===============================
   PASSWORD NOTE
   =============================== */

.password-note {
  display: flex;
  align-items: center;

  gap: 6px;

  margin-top: -2px;

  color: var(--text-muted);

  font-size: 8px;
}

.note-dot {
  width: 5px;
  height: 5px;

  border-radius: 50%;

  background: var(--accent);
}

/* ===============================
   SUBMIT
   =============================== */

.submit-button {
  width: 100%;
  height: 44px;

  display: flex;
  align-items: center;
  justify-content: center;

  gap: 8px;

  margin-top: 4px;

  border: 0;
  border-radius: 9px;

  background:
    linear-gradient(
      135deg,
      var(--primary),
      var(--accent)
    );

  color: #ffffff;

  font-size: 11px;
  font-weight: 750;

  cursor: pointer;

  box-shadow:
    0 8px 20px rgba(49, 85, 217, 0.16);

  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease,
    opacity 0.2s ease;
}

.submit-button:hover:not(:disabled) {
  transform: translateY(-1px);

  box-shadow:
    0 11px 24px rgba(49, 85, 217, 0.22);
}

.submit-button:disabled {
  opacity: 0.65;

  cursor: not-allowed;
}

.button-arrow {
  font-size: 14px;
}

.button-spinner {
  width: 13px;
  height: 13px;

  border: 2px solid rgba(255, 255, 255, 0.4);

  border-top-color: #ffffff;

  border-radius: 50%;

  animation: spin 0.7s linear infinite;
}

/* ===============================
   FOOTER
   =============================== */

.auth-footer {
  display: flex;
  align-items: center;
  justify-content: center;

  gap: 5px;

  margin-top: 22px;

  color: var(--text-muted);

  font-size: 10px;
}

.auth-footer a {
  color: var(--primary);

  font-weight: 750;

  text-decoration: none;
}

.auth-footer a:hover {
  text-decoration: underline;
}

/* ===============================
   ANIMATION
   =============================== */

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* ===============================
   RESPONSIVE
   =============================== */

@media (max-width: 900px) {
  .auth-page {
    grid-template-columns: 1fr;
  }

  .auth-visual {
    display: none;
  }

  .auth-content {
    min-height: 100vh;

    padding: 35px 24px;
  }

  .auth-card {
    max-width: 410px;
  }

  .mobile-brand {
    display: flex;

    justify-content: center;

    margin-bottom: 45px;
  }

  .mobile-brand .brand {
    color: var(--text-primary);
  }

  .mobile-brand .brand-mark {
    background:
      linear-gradient(
        135deg,
        var(--primary),
        var(--accent)
      );

    border: 0;
  }

  .mobile-brand .brand-name {
    color: var(--text-primary);
  }

  .mobile-brand .brand-name span {
    color: var(--primary);
  }
}

@media (max-width: 600px) {
  .auth-content {
    align-items: flex-start;

    padding: 28px 18px;
  }

  .mobile-brand {
    margin-bottom: 38px;
  }

  .auth-header h2 {
    font-size: 25px;
  }

  .auth-header p {
    font-size: 11px;
  }
}

@media (max-width: 400px) {
  .auth-content {
    padding: 24px 14px;
  }

  .mobile-brand {
    margin-bottom: 32px;
  }

  .auth-header {
    margin-bottom: 21px;
  }

  .auth-header h2 {
    font-size: 23px;
  }

  .input-wrapper input,
  .submit-button {
    height: 41px;
  }

  .auth-footer {
    flex-direction: column;

    gap: 3px;

    margin-top: 20px;
  }
}
</style>