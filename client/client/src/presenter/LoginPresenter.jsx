import React, { useState, useEffect } from 'react';
import LoginView from '../view/LoginView';
import { useNavigate } from 'react-router-dom';

const LoginPresenter = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [authenticationError, setAuthenticationError] = useState(null);
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isRegisterMode, setIsRegisterMode] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if(token) {
      setLoggedIn(true);
    }
  }, []);

  const handleLogin = async () => {
    console.log('Login:', { username, password });
  
    try {
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });
  
      const data = await response.json();
  
      if (response.ok) {
        console.log(data);
        localStorage.setItem('username', data.username);
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);
        localStorage.setItem('id', data.id);
        setLoggedIn(true);
        navigate('/courses');
      } else {
        setAuthenticationError(data.message);
      }
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

 
  const handleRegister = async () => {
    try {
      if (password !== confirmPassword) {
        setAuthenticationError('Passwords do not match');
        return;
      }
  
      const response = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password, confirmPassword }),
      });
  
  
      if (!response.ok) {
        setAuthenticationError(data.message);
      }  
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  const handleToggleMode = () => {
    setIsRegisterMode((prevMode) => !prevMode);
    setAuthenticationError(null);
  };

  return (
    <div>
      {isLoggedIn ? (
        <p>You are already logged in.</p>
      ) : (
        <LoginView
          username={username}
          password={password}
          confirmPassword={confirmPassword}
          onUsernameChange={setUsername}
          onPasswordChange={setPassword}
          onConfirmPasswordChange={setConfirmPassword}
          onAction={isRegisterMode ? handleRegister : handleLogin}
          onToggleMode={handleToggleMode}
          isRegisterMode={isRegisterMode}
          authenticationError={authenticationError}
        />
      )}
    </div>
  );
};

export default LoginPresenter;


