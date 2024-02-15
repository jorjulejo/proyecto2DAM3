import React from 'react';
import { BrowserRouter as Router, Routes, Route, NavLink, useParams } from 'react-router-dom';
import IncidentForm from './IncidentForm';
import CameraForm from './CameraForm';
import IncidentMod from './IncidentMod';
import CameraMod from './CameraMod';
import FlujosMod from './FlujosMod';
import '../assets/TrafficManagement.css'; // Asegurate de que este CSS sea responsivo y consistente
import NavBar from './NavBar';
import FlujosForm from './FlujosForm';

// Considera usar React.lazy para la carga perezosa de componentes
function DynamicForm() {
    const { section, action, id } = useParams();
    const forms = {
        crear: {
            incidencias: <IncidentForm />,
            camaras: <CameraForm />,
            'flujo-de-trafico': <FlujosForm />
        },
        modificar: {
            incidencias: <IncidentMod id={id} />,
            camaras: <CameraMod id={id} />,
            'flujo-de-trafico': <FlujosMod id={id} />
        }
    };

    return forms[action][section] || <div>Error: Formulario no encontrado</div>;
}

function TrafficManagement() {
    return (
       
            <Router>
                <div className="traffic-management">
                    <div className="sidebar">

                        <ul>
                            <li><NavLink to="/incidencias/modificar" className={({ isActive }) => isActive ? "active" : "inactive"}>Incidencias</NavLink></li>
                            <li><NavLink to="/camaras/modificar" className={({ isActive }) => isActive ? "active" : "inactive"}>Camaras</NavLink></li>
                            <li><NavLink to="/flujo-de-trafico/modificar" className={({ isActive }) => isActive ? "active" : "inactive"}>Flujo de Trafico</NavLink></li>
                        </ul>

                    </div>

                    <div className="main-content">
                        <NavBar />
                        <Routes>
                            <Route path="/incidencias" element={<IncidentForm />} />
                            <Route path="/camaras" element={<CameraForm />} />
                            <Route path="/flujo-de-trafico" element={<FlujosForm />} />
                            <Route path="/:section/:action" element={<DynamicForm />} />
                            <Route path="/:section/:action/:id" element={<DynamicForm />} />
                      
                        </Routes>
                    </div>
                </div>
            </Router>
      
    );
}

export default TrafficManagement;
