import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import IncidentForm from './IncidentForm';
import CameraForm from './CameraForm';
import TrafficFlow from './FlujosForm';
import '../assets/TrafficManagement.css';

function TrafficManagement() {
    return (
        <Router>
            <div className="traffic-management">
                <div className="sidebar">
                    <h1>Inicio</h1>
                    <ul>
                        <li><NavLink to="/incidencias" className={({ isActive }) => isActive ? "active" : "inactive"}>Incidencias</NavLink></li>
                        <li><NavLink to="/camaras" className={({ isActive }) => isActive ? "active" : "inactive"}>Cámaras</NavLink></li>
                        <li><NavLink to="/flujo-de-trafico" className={({ isActive }) => isActive ? "active" : "inactive"}>Flujo de Tráfico</NavLink></li>
                    </ul>
                </div>
                <div className="main-content">
                    <Routes>
                        {/* Update your routes here */}
                        <Route path="/incidencias" element={<IncidentForm />} />
                        <Route path="/camaras" element={<CameraForm />} />
                         <Route path="/flujo-de-trafico" element={<TrafficFlow />} />  
                        <Route exact path="/" element={
                            <div className="welcome">
                                <h2>Bienvenido</h2>
                            </div>
                        } />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default TrafficManagement;
