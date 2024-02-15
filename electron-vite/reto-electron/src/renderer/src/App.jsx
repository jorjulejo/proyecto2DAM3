import React, { useEffect } from 'react';
import useStore from './Store/useStore'; // Importa el store
import TrafficManagement from './components/TrafficManagement';

function App() {
  const { token, error, setToken, setError } = useStore(); // Obtiene el estado y las funciones

  useEffect(() => {
      iniciarSesionAutomatico();
  }, []);

  const iniciarSesionAutomatico = async () => {
    try {
      const credenciales = {
        email: 'ikbdt@plaiaundi.net',
        contrasena: '20022002',
      };

      const response = await fetch('http://127.0.0.1:8080/usuarios/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(credenciales),
      });

      if (response.ok) {
        const data = await response.text();
        console.log('Token:', token),
        setToken(data); // Actualiza el token en el store
        setError('');
      } else {
        console.log('Error en la respuesta del servidor:', response.status);
        setError('Las credenciales son incorrectas.');
      }
    } catch (error) {
      setError('Error al iniciar sesión.' + error);
    }
  };

  return (
    <div>
      <TrafficManagement />
    </div>
  );
}

export default App;
