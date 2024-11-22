import React, { useEffect, useState } from 'react';
import CreateUseEffect from './CreateUseEffect';

import { useParams } from 'react-router-dom';
import {AdminAccess, UserAccess} from '../view/AccessView.jsx';

const AccessPresenter = () => {
  const { userId } = useParams();
  const endpointAccess = `access/by-user/${userId}`;
  const endpointAdmin = `administrator/by-user/${localStorage.getItem('id')}`;

  const userAccess = CreateUseEffect({ endpoint: endpointAccess });
  const dataAdmin = CreateUseEffect({ endpoint: endpointAdmin  });
 
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [role, setRole] = useState(localStorage.getItem('role'));

  const handleDeleteCourse = async (course) => {
    if (role === 'ADMIN') {
      const isCourseAvailable = dataAdmin.find((item) => item.id === course.id);
      const isCourseAlredyExist = userAccess.find((item) => item.id === course.id);
      if (isCourseAvailable && isCourseAlredyExist) {
             
        await fetch(`http://localhost:8080/api/access/delete/${userId}?token=${token}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(course),
        });
       
 

      } else {
        alert("You do not have admin's permission for this course");
      }
    } else {
      alert('You are not admin in this course');
    }
  };
  

  const handleAddCourse =  async (course) => {
    if (role === 'ADMIN') {
        const isCourseAvailable = dataAdmin.find((item) => item.id === course.id);
        const isCourseAlredyExist = userAccess.find((item) => item.id === course.id);
      
        if (isCourseAvailable && !isCourseAlredyExist) {
        await  fetch(`http://localhost:8080/api/access/add/${userId}?token=${token}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(course),
        });
       

      } 
      else if (isCourseAvailable && isCourseAlredyExist) {
        alert(`The user has already been added to access ${course.title}`);
      }
      
      else {
        alert("You do not have admin's permission for this course");
      }
    } else {
      alert('You are not admin in this course');
    }
  };
 

  return (  
    <>
   <a>The courses that user can access</a>
    <UserAccess courses={userAccess} onDeleteClick={handleDeleteCourse}/> 
    <a>The courses that admin can access and add to a user</a>
    <AdminAccess  courses={dataAdmin} onAddClick={handleAddCourse}/>
    </>
  )
  

};

export default AccessPresenter;