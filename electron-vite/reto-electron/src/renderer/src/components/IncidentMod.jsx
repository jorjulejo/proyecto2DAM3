import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function IncidentMod() {
    const [incidents, setIncidents] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Cargar datos de la API
        fetch('URL_API_INCIDENTS') // Reemplaza con la URL correcta
            .then(response => response.json())
            .then(data => setIncidents(data))
            .catch(error => console.error('Error:', error));
    }, []);

    const handleEdit = (incident) => {
        // Redirecciona al formulario de edici�n con los datos de la incidencia
        navigate('/incidencias/crear', { state: { incident } });
    };
    const incidentmod = {
        color :'black',
    };

    return (
        
        <div style={incidentmod}>
            <h2>Modificar Incidencias</h2>
            {incidents.map((incident, index) => (
                <div key={index} onClick={() => handleEdit(incident)}>
                    {/* Muestra los detalles de la incidencia */}
                    <p>{incident.descripcion}</p>
                </div>
            ))}
        </div>
    );
}

export default IncidentMod;
