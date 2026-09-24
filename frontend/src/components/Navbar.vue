<template>
  <nav class="navbar">

    <div class="navbar-inner">

      <router-link
        to="/dashboard"
        class="brand"
      >
        CareerTrack
      </router-link>

      <div class="nav-center">

        <router-link
          to="/dashboard"
          class="nav-link"
          active-class="active"
        >
          Dashboard
        </router-link>

        <router-link
          to="/applications"
          class="nav-link"
          active-class="active"
        >
          Applications
        </router-link>

      </div>

      <div class="nav-right">

        <span class="user-name">
          {{ name }}
        </span>

        <button
          class="logout-button"
          @click="logout"
        >
          Logout
        </button>

      </div>

    </div>

  </nav>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const name = ref(
  localStorage.getItem('name') || 'User'
)

const logout = () => {

  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('name')
  localStorage.removeItem('email')

  router.push('/login')
}
</script>

<style scoped>
.navbar {
  width: 100%;
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
}

.navbar-inner {
  width: 100%;
  max-width: 1200px;
  height: 72px;

  margin: 0 auto;
  padding: 0 32px;

  display: grid;
  grid-template-columns: 1fr auto 1fr;
  align-items: center;

  box-sizing: border-box;
}

.brand {
  justify-self: start;

  color: #1a2e6f;
  font-size: 24px;
  font-weight: 700;
  text-decoration: none;
}

.nav-center {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-link {
  padding: 8px 13px;

  border-radius: 7px;

  color: #6b7280;

  font-size: 14px;
  font-weight: 600;

  text-decoration: none;

  transition:
    background 0.2s ease,
    color 0.2s ease;
}

.nav-link:hover {
  color: #1a2e6f;
  background: #f3f5fb;
}

.nav-link.active {
  color: #1a2e6f;
  background: #eef1fb;
}

.nav-right {
  justify-self: end;

  display: flex;
  align-items: center;
  gap: 16px;
}

.user-name {
  color: #374151;
  font-size: 14px;
  font-weight: 600;
}

.logout-button {
  padding: 9px 16px;

  border: 1px solid #d1d5db;
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

.logout-button:hover {
  background: #f9fafb;
  border-color: #9ca3af;
}

/* =========================
   TABLET
========================= */

@media (max-width: 800px) {

  .navbar-inner {
    grid-template-columns: auto 1fr auto;
    padding: 0 24px;
  }

  .nav-center {
    justify-content: center;
  }

  .nav-link {
    padding: 7px 10px;
    font-size: 13px;
  }

}

/* =========================
   MOBILE
========================= */

@media (max-width: 600px) {

  .navbar-inner {
    height: auto;
    min-height: 68px;

    grid-template-columns: 1fr auto;

    padding: 12px 18px;
    gap: 12px;
  }

  .brand {
    font-size: 21px;
  }

  .nav-center {
    grid-column: 1 / -1;
    grid-row: 2;

    width: 100%;

    justify-content: center;

    padding-bottom: 4px;
  }

  .nav-link {
    flex: 1;
    text-align: center;
  }

  .nav-right {
    gap: 8px;
  }

  .user-name {
    display: none;
  }

  .logout-button {
    padding: 8px 12px;
  }

}
</style>