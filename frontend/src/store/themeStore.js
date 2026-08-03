import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useThemeStore = create(
  persist(
    (set, get) => ({
      theme: 'light', // 'light' | 'dark'
      toggleTheme: () => {
        const nextTheme = get().theme === 'light' ? 'dark' : 'light';
        set({ theme: nextTheme });
        if (typeof document !== 'undefined') {
          document.documentElement.dataset.theme = nextTheme;
          document.documentElement.classList.toggle('dark', nextTheme === 'dark');
        }
      },
      setTheme: (theme) => {
        set({ theme });
        if (typeof document !== 'undefined') {
          document.documentElement.dataset.theme = theme;
          document.documentElement.classList.toggle('dark', theme === 'dark');
        }
      },
      initTheme: () => {
        const currentTheme = get().theme || 'light';
        if (typeof document !== 'undefined') {
          document.documentElement.dataset.theme = currentTheme;
          document.documentElement.classList.toggle('dark', currentTheme === 'dark');
        }
      }
    }),
    {
      name: 'clinic-theme',
    }
  )
);

export default useThemeStore;
