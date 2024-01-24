// CameraForm.jsx
import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import '../assets/CameraForm.css';

function CameraForm() {
    const location = useLocation();
    const isEditing = location.state && location.state.camera;

    const initialState = {
        nombre: '',
        urlImagen: '',
        archivoImagen: null,
        latitud: '',
        longitud: '',
        carretera: '',
        kilometro: '',
    };

    const [camera, setCamera] = useState(initialState);

    useEffect(() => {
        if (isEditing) {
            setCamera(location.state.camera);
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
            setCamera(prevState => ({
                ...prevState,
                archivoImagen: file,
                urlImagen: file.name
            }));
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        const method = isEditing ? 'PUT' : 'POST';
        const url = `https://tu-api.com/camaras/${isEditing ? camera.id : ''}`;

        // ...manejo de la petici�n a la API
    };

    const handleDelete = async () => {
        if (isEditing) {
            // ...manejo de la petici�n DELETE a la API
        }
    };

    const handleReset = () => {
        setCamera(initialState);
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
                    <label htmlFor="urlImagen">Imagen</label>
                    <div className="input-wrapper">
                        <input
                            id="urlImagen"
                            type="text"
                            name="urlImagen"
                            value={camera.urlImagen}
                            onChange={handleChange}

                        />
                        <label htmlFor="archivoImagen" className="file-upload-icon">
                            📷 
                        </label>
                    </div>
                    <input
                        id="archivoImagen"
                        type="file"
                        name="archivoImagen"
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
                        type="number"
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