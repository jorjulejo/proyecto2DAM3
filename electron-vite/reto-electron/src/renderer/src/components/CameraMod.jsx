import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function CameraMod() {
    const [cameras, setCameras] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Cargar datos de la API
        fetch('URL_API_CAMARAS') // Reemplaza con la URL correcta
            .then(response => response.json())
            .then(data => setCameras(data))
            .catch(error => console.error('Error:', error));
    }, []);

    const handleEdit = (camera) => {
        // Redirecciona al formulario de edici�n con los datos de la c�mara
        navigate('/camaras/editar', { state: { camera } });
    };

    const cameramod = {
        color :'black',
    };

    return (
        <div style={cameramod}>
            <h2>Modificar Camaras</h2>
            {cameras.map((camera, index) => (
                <div key={index} onClick={() => handleEdit(camera)}>
                    {/* Muestra los detalles de la c�mara */}
                    <p>{camera.nombre}</p>
                </div>
            ))}
        </div>
    );
}

export default CameraMod;
