import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/IncidentForm.css';

function IncidentForm() {
  const location = useLocation();
  const isEditing = location.state && location.state.incident;

  const initialState = {
    tipo: '',
    causa: '',
    fechaComienzo: '',
    nivelDeIncidencia: '',
    carretera: '',
    direccion: '',
    latitud: '',
    longitud: '',
  };

  const [incident, setIncident] = useState(initialState);

  useEffect(() => {
    if (isEditing) {
      setIncident(location.state.incident);
    }
  }, [location, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setIncident((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const method = isEditing ? 'PUT' : 'POST';
    const url = `https://tu-api.com/incidencias/${isEditing ? incident.id : ''}`;

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(incident)
      });

      if (response.ok) {
        // Procesar respuesta
        setIncident(initialState);
      } else {
        console.error('Error en la respuesta del servidor:', response.status);
      }
    } catch (error) {
      console.error('Error al enviar el formulario:', error);
    }
  };

  const handleDelete = async () => {
    if (isEditing) {
      try {
        const response = await fetch(`https://tu-api.com/incidencias/${incident.id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          }
        });

        if (response.ok) {
          // Procesar respuesta
          setIncident(initialState);
        } else {
          console.error('Error en la respuesta del servidor:', response.status);
        }
      } catch (error) {
        console.error('Error al intentar borrar:', error);
      }
    }
  };

  return (
    <div className="incident-form">
      <h1 className='incidencia-text'>Gestion de Trafico - Incidencias</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Tipo</label>
          <input
            type="text"
            name="tipo"
            value={incident.tipo}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Causa</label>
          <input
            type="text"
            name="causa"
            value={incident.causa}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Fecha de Comienzo</label>
          <input
            type="date"
            name="fechaComienzo"
            value={incident.fechaComienzo}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Nivel de Incidencia</label>
          <input
            type="text"
            name="nivelDeIncidencia"
            value={incident.nivelDeIncidencia}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Carretera</label>
          <input
            type="text"
            name="carretera"
            value={incident.carretera}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Direccion</label>
          <input
            type="text"
            name="direccion"
            value={incident.direccion}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Latitud</label>
          <input
            type="text"
            name="latitud"
            value={incident.latitud}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Longitud</label>
          <input
            type="text"
            name="longitud"
            value={incident.longitud}
            onChange={handleChange}
          />
        </div>
        <div className="form-actions">
          {isEditing && (
            <button type="button" onClick={handleDelete}>
              Borrar
            </button>
          )}
          <button type="submit">{isEditing ? 'Actualizar' : 'Crear'}</button>
        </div>
      </form>
    </div>
  );
}

export default IncidentForm;