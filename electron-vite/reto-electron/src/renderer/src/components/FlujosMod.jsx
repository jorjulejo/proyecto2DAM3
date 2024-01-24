import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/FlujosMod.css'; // Asegúrate de que la ruta al archivo CSS es correcta

function FlujosMod() {
    const [flujos, setFlujos] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('URL_API_FLUJOS') // Reemplaza con la URL correcta
            .then(response => response.json())
            .then(data => setFlujos(data))
            .catch(error => console.error('Error:', error));
    }, []);

    const handleEdit = (flujo) => {
        navigate('/flujo-de-trafico/editar', { state: { flujo } });
    };

    return (
        <div className="flujos-mod">
            <h2>Modificar Flujos de Trafico</h2>
            {flujos.map((flujo) => (
                <div key={flujo.id} className="flujo-item" onClick={() => handleEdit(flujo)}>
                    <p>{`Fecha: ${flujo.FECHA}, Vehículos: ${flujo.TOTAL_VEHICULOS}`}</p>
                </div>
            ))}
        </div>
    );
}

export default FlujosMod;
