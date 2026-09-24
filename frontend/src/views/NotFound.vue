<template>
  <div class="not-found-page">

    <div class="not-found-card">

      <p class="error-code">
        404
      </p>

      <h1>
        Page Not Found
      </h1>

      <p class="description">
        The page you are looking for does not exist
        or may have been moved.
      </p>

      <button
        class="back-button"
        @click="goToDashboard"
      >
        Back to Dashboard
      </button>

    </div>

  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToDashboard = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;

  display: flex;
  align-items: center;
  justify-content: center;

  padding: 32px;

  background: #f7f8fc;
}

.not-found-card {
  width: 100%;
  max-width: 520px;

  padding: 48px 40px;

  text-align: center;

  background: #ffffff;

  border: 1px solid #e9ebf2;
  border-radius: 16px;

  box-shadow:
    0 12px 32px rgba(17, 24, 39, 0.06);
}

.error-code {
  margin: 0;

  color: #1a2e6f;

  font-size: 64px;
  font-weight: 800;
  line-height: 1;
}

.not-found-card h1 {
  margin: 20px 0 0;

  color: #111827;

  font-size: 28px;
  font-weight: 700;
}

.description {
  max-width: 400px;

  margin: 12px auto 0;

  color: #6b7280;

  font-size: 14px;
  line-height: 1.6;
}

.back-button {
  margin-top: 28px;

  padding: 11px 18px;

  border: none;
  border-radius: 8px;

  background: #1a2e6f;
  color: #ffffff;

  font-size: 13px;
  font-weight: 600;

  cursor: pointer;

  transition:
    background 0.2s ease,
    transform 0.2s ease;
}

.back-button:hover {
  background: #14245a;
  transform: translateY(-1px);
}

@media (max-width: 600px) {
  .not-found-page {
    padding: 20px;
  }

  .not-found-card {
    padding: 36px 24px;
  }

  .error-code {
    font-size: 52px;
  }

  .not-found-card h1 {
    font-size: 24px;
  }

  .description {
    font-size: 13px;
  }

  .back-button {
    width: 100%;
  }
}
</style>