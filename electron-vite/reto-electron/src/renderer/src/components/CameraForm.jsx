// CameraForm.jsx
import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/CameraForm.css';
import useToken from '../Store/useStore'; // Importa el store

function CameraForm() {
    const location = useLocation();
    const isEditing = location.state && location.state.camera;
    const { token } = useToken();

    function transformCameraData(cameraData) {
        const transformedData = {};
        for (const key in cameraData) {
            if (
                cameraData[key] &&
                typeof cameraData[key] === 'object' &&
                'string' in cameraData[key]
            ) {
                transformedData[key] = cameraData[key].string;
            } else if (
                cameraData[key] &&
                typeof cameraData[key] === 'object' &&
                !('string' in cameraData[key])
            ) {
                transformedData[key] = ''; // Si es un objeto sin propiedad 'string', establece el campo como una cadena vacía
            } else {
                transformedData[key] = cameraData[key];
            }
        }
        return transformedData;
    }

    const initialState = {
        nombre: '',
        url: '', // Cambiado de urlImagen
        imagen: null, // Cambiado de archivoImagen
        latitud: '',
        longitud: '',
        carretera: '',
        kilometro: '',
        usuario: 'ikbdt@plaiaundi.net' // Agregar si es necesario
    };
    const [camera, setCamera] = useState(initialState);

    useEffect(() => {
        if (isEditing && location.state.camera) {
            const transformedCamera = transformCameraData(location.state.camera);
            // Establece el estado con los datos transformados y formateados
            setCamera(transformedCamera);
        }
    }, [location, isEditing]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCamera(prevState => ({
            ...prevState,
            [name]: value
        }));
    };


    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            // Convertir el archivo a Base64 para el campo imagen
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = () => {
                setCamera(prevState => ({
                    ...prevState,
                    imagen: reader.result,
                    url: file.name
                }));
            };
        }
    };


    const rutaActualizar = 'actualizar'; // Reemplaza con tu ruta real para actualizar
    const rutaInsertar = 'insertar';
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validación para asegurarse de que todos los campos relevantes están completos
        const camposRequeridos = ['nombre', 'url', 'latitud', 'longitud', 'carretera', 'kilometro'];
        // Incluye 'imagen' en camposRequeridos si es obligatorio cargar una imagen al crear o actualizar una cámara
        const camposIncompletos = camposRequeridos.some(campo => !camera[campo]);

        if (camposIncompletos) {
            alert('Por favor, completa todos los campos antes de enviar.');
            return; // Detiene la función si hay campos incompletos
        }

        const method = isEditing ? 'PUT' : 'POST';
        const url = `http://127.0.0.1:8080/api/camaras/${isEditing ? rutaActualizar : rutaInsertar}`;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // Asegúrate de que el token es correcto
                },
                body: JSON.stringify(camera)
            });

            if (response.ok) {
                // Procesar respuesta
                if (!isEditing)
                    setCamera(initialState);
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
                const deleteData = { id: camera.id }; // Crear el objeto JSON con el campo "id"

                const response = await fetch(`http://127.0.0.1:8080/api/camaras/borrar`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    body: JSON.stringify(deleteData) // Convertir el objeto JSON a una cadena JSON
                });

                if (response.ok) {
                    // Procesar respuesta
                    setCamera(initialState);
                } else {
                    console.error('Error en la respuesta del servidor:', response.status);
                }
            } catch (error) {
                console.error('Error al intentar borrar:', error);
            }
        }
    };

    return (
        <div className="camera-form">
            <h1 className='camera-text'>Gestion de Trafico - Camaras</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Nombre</label>
                    <input
                        type="text"
                        name="nombre"
                        value={camera.nombre}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group file-input-group">
                    <label htmlFor="url">Imagen</label>
                    <div className="input-wrapper">
                        <input
                            id="url"
                            type="text"
                            name="url"
                            value={camera.url}
                            onChange={handleChange}

                        />
                        <label htmlFor="imagen" className="file-upload-icon">
                            📷
                        </label>
                    </div>
                    <input
                        id="imagen"
                        type="file"
                        name="imagen"
                        accept="image/*"
                        onChange={handleFileChange}
                        style={{ display: 'none' }}
                    />
                </div>
                <div className="form-group">
                    <label>Latitud</label>
                    <input
                        type="text"
                        name="latitud"
                        value={camera.latitud}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Longitud</label>
                    <input
                        type="text"
                        name="longitud"
                        value={camera.longitud}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Carretera</label>
                    <input
                        type="text"
                        name="carretera"
                        value={camera.carretera}
                        onChange={handleChange}
                    />
                </div>
                <div className="form-group">
                    <label>Kilómetro</label>
                    <input
                        type="text"
                        name="kilometro"
                        value={camera.kilometro}
                        onChange={handleChange}
                    />
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
export default CameraForm;