import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../assets/FlujosForm.css';

function FlujosForm() {
  const location = useLocation();
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [flujos, setFlujos] = useState({
    FECHA: '',
    RANGO_TIEMPO: '',
    MEDIA_VELOCIDAD: '',
    TOTAL_VEHICULOS: '',
    LATITUD: '',
    LONGITUD: '',
    USUARIO: ''
  });

  useEffect(() => {
    if (location.state?.flujos) {
      setFlujos(location.state.flujos);
      setIsEditing(true);
    }
  }, [location.state?.flujos]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFlujos(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Aqu� puedes agregar validaciones antes de enviar
    const method = isEditing ? 'PUT' : 'POST';
    const url = `https://tu-api.com/flujos/${isEditing ? flujos.id : ''}`;

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(flujos)
      });

      if (response.ok) {
        const responseData = await response.json();
        console.log(responseData);
        setFlujos(initialState);
        navigate('/flujos-de-trafico');
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
        const response = await fetch(`https://tu-api.com/flujos/${flujos.id}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          }
        });

        if (response.ok) {
          setFlujos(initialState);
          navigate('/flujos-de-trafico');
        } else {
          console.error('Error en la respuesta del servidor:', response.status);
        }
      } catch (error) {
        console.error('Error al intentar borrar:', error);
      }
    }
  };

  const handleReset = () => {
    setFlujos(initialState);
  };

  return (
    <div className="flujos-form">
      <h1 className='flujo-text'>Gestion de Trafico - Flujos</h1>
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