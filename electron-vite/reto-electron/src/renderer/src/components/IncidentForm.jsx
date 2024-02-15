import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/IncidentForm.css';
import useToken from '../Store/useStore'; // Importa el store

function IncidentForm() {
  const location = useLocation();
  const isEditing = location.state && location.state.incident;
  const { token } = useToken();

  function transformIncidentData(incidentData) {
    const transformedData = {};
    for (const key in incidentData) {
      if (
        incidentData[key] &&
        typeof incidentData[key] === 'object' &&
        'string' in incidentData[key]
      ) {
        transformedData[key] = incidentData[key].string;
      } else if (
        incidentData[key] &&
        typeof incidentData[key] === 'object' &&
        !('string' in incidentData[key])
      ) {
        transformedData[key] = ''; // Si es un objeto sin propiedad 'string', establece el campo como una cadena vacía
      } else {
        transformedData[key] = incidentData[key];
      }
    }
    return transformedData;
  }



  const initialState = {
    tipo: '',
    causa: '',
    comienzo: '',
    nivel_incidencia: '',
    carretera: '',
    direccion: '',
    latitud: '',
    longitud: '',
    usuario: 'ikbdt@plaiaundi.net'
  };
  function formatDate(dateString) {
    const parts = dateString.match(/(\d{2})-(\w{3})-(\d{2})/);
    if (!parts) return '';

    const months = {
      'JAN': '01', 'FEB': '02', 'MAR': '03', 'APR': '04', 'MAY': '05', 'JUN': '06',
      'JUL': '07', 'AUG': '08', 'SEP': '09', 'OCT': '10', 'NOV': '11', 'DEC': '12'
    };
    const year = '20' + parts[3];
    const month = months[parts[2].toUpperCase()];
    const day = parts[1];

    return `${year}-${month}-${day}`;
  }

  const [incident, setIncident] = useState(initialState);

  useEffect(() => {
    if (isEditing && location.state.incident) {
      // Primero transforma los datos para obtener solo los valores 'string'
      const transformedIncident = transformIncidentData(location.state.incident);

      // Luego, formatea la fecha de comienzo si es necesario
      if (transformedIncident.comienzo) {
        transformedIncident.comienzo = formatDate(transformedIncident.comienzo);
      }

      // Establece el estado con los datos transformados y formateados
      setIncident(transformedIncident);
    }
  }, [location, isEditing]);



  const handleChange = (e) => {
    const { name, value } = e.target;
    setIncident((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };


  const rutaActualizar = 'actualizar'; // Reemplaza con tu ruta real para actualizar
  const rutaInsertar = 'insertar';
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Comprobación para asegurarse de que todos los campos están completos
    const camposRequeridos = ['tipo', 'causa', 'comienzo', 'nivel_incidencia', 'carretera', 'direccion', 'latitud', 'longitud'];
    const camposIncompletos = camposRequeridos.some(campo => !incident[campo]);

    if (camposIncompletos) {
      alert('Por favor, completa todos los campos antes de enviar.');
      return; // Detener la función si hay campos incompletos
    }

    const method = isEditing ? 'PUT' : 'POST';
    const url = `http://127.0.0.1:8080/api/incidencias/${isEditing ? rutaActualizar : rutaInsertar}`;

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` // Asegúrate de que el token es correcto
        },
        body: JSON.stringify(incident)
      });

      if (response.ok) {
        // Procesar respuesta
        if (!isEditing)
          setIncident(initialState);
      } else {
        console.error('Error en la respuesta del servidor:', response.status);
      }
    } catch (error) {
      console.error('Error al enviar el formulario:', error);
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
            name="tipo" // El nombre del campo debe ser "tipo"
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
            name="comienzo"
            value={incident.comienzo}
            onChange={handleChange}
          />
        </div>
        <div className="form-group">
          <label>Nivel de Incidencia</label>
          <input
            type="text"
            name="nivel_incidencia"
            value={incident.nivel_incidencia}
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