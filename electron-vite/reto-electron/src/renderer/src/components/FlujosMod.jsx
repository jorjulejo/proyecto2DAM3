import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function FlujosMod() {
    const [flujos, setFlujos] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Cargar datos de la API
        fetch('URL_API_FLUJOS') // Reemplaza con la URL correcta
            .then(response => response.json())
            .then(data => setFlujos(data))
            .catch(error => console.error('Error:', error));
    }, []);

    const handleEdit = (flujo) => {
        // Redirecciona al formulario de edición con los datos del flujo
        navigate('/flujo-de-trafico/editar', { state: { flujo } });
    };

    const flujosmod = {
        color :'black',
    };

    return (
        <div  style={flujosmod}>
            <h2>Modificar Flujos </h2>
            {flujos.map((flujo, index) => (
                <div key={index} onClick={() => handleEdit(flujo)}>
                    {/* Muestra los detalles del flujo de tráfico */}
                    <p>{`Fecha: ${flujo.FECHA}, Vehículos: ${flujo.TOTAL_VEHICULOS}`}</p>
                </div>
            ))}
        </div>
    );
}

export default FlujosMod;
