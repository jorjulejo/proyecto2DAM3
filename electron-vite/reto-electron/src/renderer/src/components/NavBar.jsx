import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function NavBar() {
  const location = useLocation();

  // Simplifica la obtenci�n del path base
  const base = location.pathname.split('/')[1];
  const createPath = `/${base}/crear`;
  const modifyPath = `/${base}/modificar`;

  // Usar una funci�n para determinar la visibilidad del enlace
  const shouldShowLink = (path) => !location.pathname.includes(path);

  // Estilos mejorados para la barra de navegaci�n y los enlaces
  const navStyle = {
    backgroundColor: '#f8f9fa', // Color de fondo m�s claro
    padding: '10px 20px', // Padding para una mejor presentaci�n
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)', // Sombra para resaltar la barra de navegaci�n
    marginTop: 0, // Eliminar margen superior
    display: 'flex', // Usar flexbox para la disposici�n de los elementos
    alignItems: 'flex-start' // Alinear elementos al inicio del contenedor
  };
  
  

  const linkStyle = {
    color: '#007bff', // Color de enlace m�s atractivo
    margin: '0 10px', // Espaciado horizontal entre enlaces
    textDecoration: 'none', // Sin subrayado en los enlaces
    fontSize: '16px', // Tama�o de fuente mantenido
  };

  return (
    <nav style={navStyle}>
      <div>
        {shouldShowLink('/crear') && <Link style={linkStyle} to={createPath}>Crear</Link>}
        {shouldShowLink('/modificar') && <Link style={linkStyle} to={modifyPath}>Modificar</Link>}
      </div>
    </nav>
  );
}

export default NavBar;
