import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/IncidentMod.css'; // Asegúrate de que la ruta al archivo CSS es correcta

function IncidentMod() {
    const [incidents, setIncidents] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('URL_API_INCIDENTS') // Reemplaza con la URL correcta
            .then(response => response.json())
            .then(data => setIncidents(data))
            .catch(error => console.error('Error:', error));
    }, []);

    const handleEdit = (incident) => {
        navigate('/incidencias/crear', { state: { incident } });
    };

    return (
        <div className="incident-mod">
            <h2>Modificar Incidencias</h2>
            {incidents.map((incident) => (
                <div key={incident.id} className="incident-item" onClick={() => handleEdit(incident)}>
                    <p>{incident.descripcion}</p>
                </div>
            ))}
        </div>
    );
}

export default IncidentMod;
