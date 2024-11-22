import React, { useState, useEffect } from 'react';
import UserView from '../view/UserView.jsx';
import CreateUseEffect from './CreateUseEffect.jsx';
import { useNavigate } from 'react-router-dom';

const UserPresenter = () => {
  const navigate = useNavigate();
  const endpoint = 'users/users';
  const data = CreateUseEffect({ endpoint: endpoint });

  const handleButtonClick = (userId) => {
    navigate(  `/user-access/${userId}`);
   
    console.log(`Button clicked for user with ID ${userId}`);
  };

  return <UserView users={data} onButtonClick={handleButtonClick} />;
};

export default UserPresenter;

