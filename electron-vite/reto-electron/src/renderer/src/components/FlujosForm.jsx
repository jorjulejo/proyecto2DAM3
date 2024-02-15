import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/FlujosForm.css';
import useToken from '../Store/useStore'; // Importa el store


function FlujosForm() {
  const location = useLocation();
  const isEditing = location.state && location.state.flujo;
  const { token } = useToken();

  function transformFlujoData(flujoData) {
    const transformedData = {};
    for (const key in flujoData) {
      if (
        flujoData[key] &&
        typeof flujoData[key] === 'object' &&
        'string' in flujoData[key]
      ) {
        transformedData[key] = flujoData[key].string;
      } else if (
        flujoData[key] &&
        typeof flujoData[key] === 'object' &&
        !('string' in flujoData[key])
      ) {
        transformedData[key] = ''; // Si es un objeto sin propiedad 'string', establece el campo como una cadena vacía
      } else {
        transformedData[key] = flujoData[key];
      }
    }
    return transformedData;
  }

  const initialState = {
    fecha: '',
    rango_tiempo: '',
    media_velocidad: '',
    total_vehiculos: '',
    latitud: '',
    longitud: '',
    usuario: 'ikbdt@plaiaundi.com'
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

  const [flujos, setFlujos] = useState(initialState);


  useEffect(() => {
    if (isEditing && location.state.flujo) {
      // Primero transforma los datos para obtener solo los valores 'string'
      const transformedFlujo = transformFlujoData(location.state.flujo);

      // Luego, formatea la fecha de comienzo si es necesario
      if (transformedFlujo.comienzo) {
        transformedFlujo.comienzo = formatDate(transformedFlujo.comienzo);
      }

      // Establece el estado con los datos transformados y formateados
      setFlujos(transformedFlujo);
    }
  }, [location, isEditing]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFlujos((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const rutaActualizar = 'actualizar'; // Reemplaza con tu ruta real para actualizar
  const rutaInsertar = 'insertar';
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Comprobación para asegurarse de que todos los campos están completos
    const camposRequeridos = ['fecha', 'rango_tiempo', 'media_velocidad', 'total_vehiculos', 'latitud', 'longitud'];
    const camposIncompletos = camposRequeridos.some(campo => !flujos[campo]);

    if (camposIncompletos) {
      alert('Por favor, completa todos los campos antes de enviar.');
      return; // Detiene la función si hay campos incompletos
    }

    const method = isEditing ? 'PUT' : 'POST';
    const url = `http://127.0.0.1:8080/api/flujos/${isEditing ? rutaActualizar : rutaInsertar}`;

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` // Asegúrate de que el token es correcto
        },
        body: JSON.stringify(flujos)
      });

      if (response.ok) {
        // Procesar respuesta
        if (!isEditing)
          setFlujos(initialState);
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
        const deleteData = { id: flujos.id }; // Crear el objeto JSON con el campo "id"

        const response = await fetch(`http://127.0.0.1:8080/api/flujos/borrar`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(deleteData) // Convertir el objeto JSON a una cadena JSON
        });

        if (response.ok) {
          // Procesar respuesta
          setFlujos(initialState);
        } else {
          console.error('Error en la respuesta del servidor:', response.status);
        }
      } catch (error) {
        console.error('Error al intentar borrar:', error);
      }
    }
  };

  return (
    <div className="flujos-form">
      <h1 className='flujo-text'>Gestion de Trafico - Flujos</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Fecha</label>
          <input type="date" name="fecha" value={flujos.fecha} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Rango de Tiempo</label>
          <input type="number" name="rango_tiempo" value={flujos.rango_tiempo} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Media de Velocidad</label>
          <input type="number" name="media_velocidad" value={flujos.media_velocidad} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Total de Vehículos</label>
          <input type="number" name="total_vehiculos" value={flujos.total_vehiculos} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Latitud</label>
          <input type="text" name="latitud" value={flujos.latitud} onChange={handleChange} />
        </div>
        <div className="form-group">
          <label>Longitud</label>
          <input type="text" name="longitud" value={flujos.longitud} onChange={handleChange} />
        </div>
        <div className="form-actions">
          {isEditing && (
            <button type="button" className="delete-button" onClick={handleDelete}>
              Borrar
            </button>
          )}
          <button type="submit" className="submit-button">
            {isEditing ? 'Actualizar' : 'Crear'}
          </button>
        </div>
      </form>
    </div>
  );
}

export default FlujosForm;