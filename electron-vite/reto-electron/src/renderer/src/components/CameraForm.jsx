// CameraForm.jsx
import React, { useState } from 'react';
import '../assets/CameraForm.css'; // Make sure this CSS file is created and contains the appropriate styles

function CameraForm() {
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
        // Aquí manejarías el envío de los datos, por ejemplo, a un servidor o a una API
        console.log(camera);
        // Implement the logic to send the data to your API
    };

    const handleReset = () => {
        setCamera(initialState);
    };

    return (
        <div className="camera-form">
            <h1>Gestión de Tráfico - Cámaras</h1>
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
                            📷 {/* Replace with an actual icon */}
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
                    <button type="button" onClick={handleReset}>Borrar</button>
                    <button type="submit">Crear</button>
                </div>
            </form>
        </div>
    );
}

export default CameraForm;
