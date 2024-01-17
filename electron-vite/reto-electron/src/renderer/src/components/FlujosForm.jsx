import React, { useState } from 'react';
import '../assets/FlujosForm.css'; // Asegúrate de crear este archivo CSS para estilizar tu formulario

function FlujosForm() {
  // Estado inicial del formulario
  const initialState = {
    FECHA: '',
    RANGO_TIEMPO: '',
    MEDIA_VELOCIDAD: '',
    TOTAL_VEHICULOS: '',
    LATITUD: '',
    LONGITUD: '',
    USUARIO: ''
  };

  // Estado del formulario
  const [flujos, setFlujos] = useState(initialState);

  // Manejar cambios en los inputs
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFlujos(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Manejar el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('https://tu-api.com/flujos', { // Reemplaza con la URL de tu API
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Agrega más cabeceras si es necesario, como tokens de autenticación
        },
        body: JSON.stringify(flujos)
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData); // Procesa la respuesta de la API como necesites
        // Opcional: resetear el formulario o redireccionar al usuario
        setFlujos(initialState);
      } else {
        console.error('Error en la respuesta del servidor:', response.status);
      }
    } catch (error) {
      console.error('Error al enviar el formulario:', error);
    }
  };

  // Manejar la limpieza del formulario
  const handleReset = () => {
    setFlujos(initialState);
  };

  return (
    <div className="flujos-form">
      <h1>Gestión de Tráfico -Flujos</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Fecha</label>
          <input type="date" name="FECHA" value={flujos.FECHA} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Rango de Tiempo</label>
          <input type="number" name="RANGO_TIEMPO" value={flujos.RANGO_TIEMPO} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Media de Velocidad</label>
          <input type="number" name="MEDIA_VELOCIDAD" value={flujos.MEDIA_VELOCIDAD} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Total de Vehículos</label>
          <input type="number" name="TOTAL_VEHICULOS" value={flujos.TOTAL_VEHICULOS} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Latitud</label>
          <input type="text" name="LATITUD" value={flujos.LATITUD} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Longitud</label>
          <input type="text" name="LONGITUD" value={flujos.LONGITUD} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Usuario</label>
          <input type="text" name="USUARIO" value={flujos.USUARIO} onChange={handleChange} />
        </div>
        <div className="form-actions">
          <button type="button" onClick={handleReset}>Borrar</button>
          <button type="submit">Crear</button>
        </div>
      </form>
    </div>
  );
}

export default FlujosForm;
