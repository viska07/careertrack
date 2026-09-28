<template>
  <nav class="navbar">
    <div class="navbar-inner">

      <!-- BRAND -->
      <router-link
        to="/dashboard"
        class="brand"
      >
        <span class="brand-mark">
          C
        </span>

        <span class="brand-name">
          Career<span>Track</span>
        </span>
      </router-link>


      <!-- NAVIGATION -->
      <div class="nav-center">

        <router-link
          to="/dashboard"
          class="nav-link"
          active-class="active"
        >
          <span class="nav-icon">
            ▦
          </span>

          <span>
            Dashboard
          </span>
        </router-link>

        <router-link
          to="/applications"
          class="nav-link"
          active-class="active"
        >
          <span class="nav-icon">
            ≡
          </span>

          <span>
            Applications
          </span>
        </router-link>

      </div>


      <!-- USER AREA -->
      <div class="nav-right">

        <div class="user-profile">

          <div class="user-avatar">
            {{ userInitial }}
          </div>

          <div class="user-info">

            <span class="user-name">
              {{ name }}
            </span>

            <span class="user-label">
              Career seeker
            </span>

          </div>

        </div>

        <button
          type="button"
          class="logout-button"
          @click="logout"
        >
          <span>
            Logout
          </span>

          <span class="logout-arrow">
            →
          </span>
        </button>

      </div>

    </div>
  </nav>
</template>


<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const name = ref(
  localStorage.getItem('name') || 'User'
)

const userInitial = computed(() => {
  return (
    name.value
      .trim()
      .charAt(0)
      .toUpperCase() || 'U'
  )
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  localStorage.removeItem('name')
  localStorage.removeItem('email')

  router.push('/login')
}
</script>


<style scoped>

/* =========================================
   NAVBAR
   ========================================= */

.navbar {
  position: sticky;
  top: 0;
  z-index: 100;

  width: 100%;

  border-bottom: 1px solid var(--border);

  background: rgba(255, 255, 255, 0.92);

  backdrop-filter: blur(14px);
  -webkit-backdrop-filter: blur(14px);
}


/* =========================================
   NAVBAR INNER
   ========================================= */

.navbar-inner {
  width: 100%;
  max-width: 1240px;
  min-height: 70px;

  margin: 0 auto;
  padding: 0 28px;

  display: grid;
  grid-template-columns: 1fr auto 1fr;

  align-items: center;

  gap: 24px;
}


/* =========================================
   BRAND
   ========================================= */

.brand {
  justify-self: start;

  display: inline-flex;
  align-items: center;

  gap: 9px;

  width: fit-content;

  color: var(--text-primary);

  text-decoration: none;
}

.brand-mark {
  width: 34px;
  height: 34px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 9px;

  background:
    linear-gradient(
      135deg,
      var(--primary),
      var(--accent)
    );

  color: #ffffff;

  font-size: 15px;
  font-weight: 800;

  box-shadow:
    0 6px 15px rgba(49, 85, 217, 0.18);
}

.brand-name {
  color: var(--text-primary);

  font-size: 17px;
  font-weight: 800;

  letter-spacing: -0.035em;
}

.brand-name span {
  color: var(--primary);
}


/* =========================================
   NAVIGATION CENTER
   ========================================= */

.nav-center {
  display: flex;
  align-items: center;

  gap: 4px;

  padding: 4px;

  border: 1px solid var(--border);

  border-radius: 10px;

  background: var(--surface-soft);
}

.nav-link {
  position: relative;

  display: flex;
  align-items: center;

  gap: 7px;

  min-height: 34px;

  padding: 7px 12px;

  border-radius: 7px;

  color: var(--text-secondary);

  font-size: 10px;
  font-weight: 700;

  text-decoration: none;

  transition:
    color 0.2s ease,
    background 0.2s ease,
    box-shadow 0.2s ease;
}

.nav-link:hover {
  color: var(--primary);

  background: var(--primary-soft);
}

.nav-link.active {
  color: var(--primary);

  background: #ffffff;

  box-shadow:
    0 2px 7px rgba(23, 32, 51, 0.06);
}

.nav-icon {
  width: 14px;

  display: flex;
  align-items: center;
  justify-content: center;

  color: currentColor;

  font-size: 13px;
  font-weight: 700;
}


/* =========================================
   USER AREA
   ========================================= */

.nav-right {
  justify-self: end;

  display: flex;
  align-items: center;

  gap: 13px;
}

.user-profile {
  display: flex;
  align-items: center;

  gap: 9px;
}

.user-avatar {
  width: 34px;
  height: 34px;

  display: flex;
  align-items: center;
  justify-content: center;

  flex-shrink: 0;

  border-radius: 9px;

  background: var(--primary-soft);

  color: var(--primary);

  font-size: 11px;
  font-weight: 800;
}

.user-info {
  display: flex;
  flex-direction: column;

  gap: 2px;
}

.user-name {
  max-width: 120px;

  overflow: hidden;

  color: var(--text-primary);

  font-size: 10px;
  font-weight: 750;

  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-label {
  color: var(--text-muted);

  font-size: 8px;
}


/* =========================================
   LOGOUT
   ========================================= */

.logout-button {
  display: flex;
  align-items: center;

  gap: 5px;

  min-height: 34px;

  padding: 7px 11px;

  border: 1px solid var(--border-strong);

  border-radius: 8px;

  background: #ffffff;

  color: var(--text-secondary);

  font-size: 9px;
  font-weight: 700;

  cursor: pointer;

  transition:
    color 0.2s ease,
    border-color 0.2s ease,
    background 0.2s ease,
    transform 0.2s ease;
}

.logout-button:hover {
  color: var(--danger);

  border-color: #edcaca;

  background: var(--danger-soft);

  transform: translateY(-1px);
}

.logout-arrow {
  font-size: 12px;
}


/* =========================================
   TABLET
   ========================================= */

@media (max-width: 900px) {

  .navbar-inner {
    grid-template-columns: auto 1fr auto;

    padding: 0 20px;

    gap: 14px;
  }

  .nav-center {
    justify-self: center;
  }

  .user-info {
    display: none;
  }

  .nav-right {
    gap: 8px;
  }

}


/* =========================================
   MOBILE
   ========================================= */

@media (max-width: 650px) {

  .navbar-inner {
    min-height: auto;

    grid-template-columns: 1fr auto;

    padding: 12px 16px;

    gap: 10px;
  }

  .brand-name {
    font-size: 16px;
  }

  .brand-mark {
    width: 32px;
    height: 32px;

    border-radius: 8px;
  }

  .nav-right {
    justify-self: end;
  }

  .nav-center {
    grid-column: 1 / -1;
    grid-row: 2;

    width: 100%;

    justify-content: stretch;
  }

  .nav-link {
    flex: 1;

    justify-content: center;

    padding: 7px 8px;
  }

  .user-avatar {
    width: 32px;
    height: 32px;
  }

  .logout-button {
    min-height: 32px;

    padding: 7px 9px;
  }

  .logout-arrow {
    display: none;
  }

}


/* =========================================
   SMALL MOBILE
   ========================================= */

@media (max-width: 400px) {

  .navbar-inner {
    padding: 10px 12px;
  }

  .brand-name {
    font-size: 15px;
  }

  .nav-link {
    font-size: 9px;
  }

  .nav-icon {
    font-size: 12px;
  }

  .logout-button span:first-child {
    font-size: 8px;
  }

}

</style>