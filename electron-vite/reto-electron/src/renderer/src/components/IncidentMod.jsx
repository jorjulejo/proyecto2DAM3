import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/IncidentMod.css'; // Aseg�rate de que la ruta al archivo CSS es correcta
import useToken from '../Store/useStore'; // Importa el store


function IncidentMod() {
    const [incidents, setIncidents] = useState([]);
    const navigate = useNavigate();
    const { token } = useToken();



    useEffect(() => {
        const fetchIncidents = async () => {
            try {
                const headers = {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                };

                const response = await fetch('http://127.0.0.1:8080/api/incidencias/seleccionar', { headers });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                setIncidents(data); // Asumiendo que la API devuelve un array de incidentes
            } catch (error) {
                console.error('Error fetching incidents:', error);
            }
        };

        fetchIncidents();
    }, [token]); // Si esperas que otros valores cambien y causen una actualización, agrégalos aquí

    const handleEdit = (incident) => {
        navigate('/incidencias/crear', { state: { incident } });
    };

    return (
        <div className="incident-mod">
            <h2>Modificar Incidencias</h2>
            {incidents.length > 0 ? incidents.map((incident) => (
                <div key={incident.id.string} className="incident-item" onClick={() => handleEdit(incident)}>
                    <p>{incident.tipo.string}</p>
                    <p>{incident.causa.string}</p>
                    <p>{incident.latitud.string}</p>
                    <p>{incident.longitud.string}</p>
                </div>
            )) : <p>No hay incidencias para mostrar.</p>}
        </div>
    );
}

export default IncidentMod;
