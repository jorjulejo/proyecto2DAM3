import create from 'zustand';

const useStore = create((set) => ({
  token: '', // Tu estado global, en este caso el token
  error: '', // Otras propiedades globales si es necesario

  setToken: (newToken) => set({ token: newToken }),
  setError: (newError) => set({ error: newError }),
}));

export default useStore;
