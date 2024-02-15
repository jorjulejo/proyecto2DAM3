import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../assets/CameraMod.css'; // Asegúrate de que la ruta al archivo CSS es correcta
import useToken from '../Store/useStore'; // Importa el store

function CameraMod() {
    const [cameras, setCameras] = useState([]);
    const navigate = useNavigate();
    const { token } = useToken();

    useEffect(() => {
        const fetchCamaras = async () => {
            try {
                const headers = {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                };

                const response = await fetch('http://127.0.0.1:8080/api/camaras/seleccionar', { headers });
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const data = await response.json();
                console.log(data);
                setCameras(data); // Asumiendo que la API devuelve un array de cámaras
            } catch (error) {
                console.error('Error fetching cameras:', error);
            }
        };
    
        fetchCamaras();
    }, [token]);

    const handleEdit = (camera) => {
        navigate('/camaras/crear', { state: { camera } });
    };

    return (
        <div className="camera-mod">
            <h2>Modificar Camaras</h2>
            {cameras.length > 0 ? cameras.map((camera) => (
                <div key={camera.id.string} className="camera-item" onClick={() => handleEdit(camera)}>
                    <p>{camera.nombre.string}</p>
                    <p>{camera.longitud.string}</p>
                    <p>{camera.carretera.string}</p>
                    <p>{camera.kilometro.string}</p>
                </div>
            )) : <p>No hay cámaras para mostrar.</p>}
        </div>
    );
}

export default CameraMod;
