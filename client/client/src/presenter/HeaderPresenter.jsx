import React, { useEffect, useState } from 'react';
import HeaderView from '../view/HeaderView.jsx';
import { useLocation, useNavigate } from 'react-router-dom';

const HeaderPresenter = () => {
  const location = useLocation();
 

  const [isLoggedIn, setLoggedIn] = useState(false);
  const [username, setUsername] = useState('');
  const [token, setToken] = useState('');
  const [role, setRole] = useState('');
  const [id, setId] = useState('');
  const navigate = useNavigate();
  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    const storedUsername = localStorage.getItem('username');
    const storedRole = localStorage.getItem('role');
    const storedId = localStorage.getItem('id');

    const isLoggedIn = storedToken !== null;

    if (isLoggedIn) {
      setToken(storedToken || '');
      setUsername(storedUsername || '');
      setRole(storedRole || '');
      setId(storedId || '');
      setLoggedIn(true);
    }
  }, [location.pathname, localStorage.getItem('token')]);

  const handleLogout = () => {
   
    localStorage.removeItem('username');
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('id');

 
    setToken('');
    setUsername('');
    setRole('');
    setId('');
    setLoggedIn(false);

    navigate('/courses');
  };

  return (
    <HeaderView
      location={location}
      isLoggedIn={isLoggedIn}
      username={username}
      token={token}
      role={role}
      id={id}
      onLogout={handleLogout}
    />
  );
};

export default HeaderPresenter;









