import React, { useState } from 'react';
import '../assets/IncidentForm.css'; // Asegúrate de crear este archivo CSS para estilizar tu formulario

function IncidentForm() {
  // Estado inicial del formulario
  const initialState = {
    tipo: '',
    causa: '',
    fechaComienzo: '',
    nivelDeIncidencia: '',
    carretera: '',
    direccion: '',
    latitud: '',
    longitud: ''
  };

  // Estado del formulario
  const [incident, setIncident] = useState(initialState);

  // Manejar cambios en los inputs
  const handleChange = (e) => {
    const { name, value } = e.target;
    setIncident(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  // Manejar el envío del formulario
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('https://tu-api.com/incidencias', { // Reemplaza con la URL de tu API
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          // Agrega más cabeceras si es necesario, como tokens de autenticación
        },
        body: JSON.stringify(incident)
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData); // Procesa la respuesta de la API como necesites
        // Opcional: resetear el formulario o redireccionar al usuario
        setIncident(initialState);
      } else {
        console.error('Error en la respuesta del servidor:', response.status);
      }
    } catch (error) {
      console.error('Error al enviar el formulario:', error);
    }
  };

  // Manejar la limpieza del formulario
  const handleReset = () => {
    setIncident(initialState);
  };

  return (
    <div className="incident-form">
      <h1>Gestión de Tráfico - Incidencias</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Tipo</label>
          <input type="text" name="tipo" value={incident.tipo} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Causa</label>
          <input type="text" name="causa" value={incident.causa} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Fecha de Comienzo</label>
          <input type="date" name="fechaComienzo" value={incident.fechaComienzo} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Nivel de Incidencia</label>
          <input type="text" name="nivelDeIncidencia" value={incident.nivelDeIncidencia} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Carretera</label>
          <input type="text" name="carretera" value={incident.carretera} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Dirección</label>
          <input type="text" name="direccion" value={incident.direccion} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Latitud</label>
          <input type="text" name="latitud" value={incident.latitud} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Longitud</label>
          <input type="text" name="longitud" value={incident.longitud} onChange={handleChange} />
        </div>
        <div className="form-actions">
          <button type="button" onClick={handleReset}>Borrar</button>
          <button type="submit">Crear</button>
        </div>
      </form>
    </div>
  );
}

export default IncidentForm;
