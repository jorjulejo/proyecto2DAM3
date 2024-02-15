import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/FlujosMod.css'; // Aseg�rate de que la ruta al archivo CSS es correcta
import useToken from '../Store/useStore'; // Importa el store


function FlujosMod() {
    const [flujos, setFlujos] = useState([]);
    const navigate = useNavigate();
    const { token } = useToken();

    useEffect(() => {
        const fetchFlujos = async () => {
            try {
                const headers = {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                };

                const response = await fetch('http://127.0.0.1:8080/api/flujos/seleccionar', { headers });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                setFlujos(data); // Asumiendo que la API devuelve un array de incidentes
            } catch (error) {
                console.error('Error fetching incidents:', error);
            }
        };

        fetchFlujos();
    }, [token]); // Si esperas que otros valores cambien y causen una actualización, agrégalos aquí


    const handleEdit = (flujo) => {
        navigate('/flujo-de-trafico/crear', { state: { flujo } });
    };
    return (
        <div className="flujos-mod">
            <h2>Modificar Flujos</h2>
            {flujos.length > 0 ? flujos.map((flujo) => (
                <div key={flujo.id.string} className="flujo-item" onClick={() => handleEdit(flujo)}>
                    <p>{flujo.fecha.string}</p>
                    <p>{flujo.media_velocidad.string}</p>
                    <p>{flujo.total_vehiculos.string}</p>
                </div>
            )) : <p>No hay flujos para mostrar.</p>}
        </div>
    );
}

export default FlujosMod;
