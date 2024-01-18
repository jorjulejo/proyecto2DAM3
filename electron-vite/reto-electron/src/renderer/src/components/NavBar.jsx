import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function NavBar() {
  const location = useLocation();

  const base = location.pathname.split('/').filter(p => p.length > 0)[0];
  const createPath = `/${base}/crear`;
  const modifyPath = `/${base}/modificar`;

  const showCreateLink = !location.pathname.includes('/crear');
  const showModifyLink = !location.pathname.includes('/modificar');


  const navStyle = {
    backgroundColor: '#e8e8e8',
    display: 'flex', // Disposición horizontal
  } ;

  const linkStyle = {
    color: 'black', // Texto blanco
    margin: '10px', // Espaciado entre los enlaces
    textDecoration: 'none', // Sin subrayado en los enlaces
    fontSize: '16px', // Tamaño de fuente
  };

  return (
    <nav style={navStyle}>
      <div>
        {showCreateLink && <Link style={linkStyle} to={createPath}>Crear</Link>}
        {showModifyLink && <Link style={linkStyle} to={modifyPath}>Modificar</Link>}
      </div>
    </nav>
  );
}

export default NavBar;
