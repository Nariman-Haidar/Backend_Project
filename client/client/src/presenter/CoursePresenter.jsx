import React, { useEffect, useState } from 'react';
import CourseView from '../view/CourseView.jsx';
import CreateUseEffect from './CreateUseEffect';
import { useNavigate } from 'react-router-dom';

const CoursePresenter = () => {

  const endpoint = 'course/courses';
  const data = CreateUseEffect({ endpoint: endpoint });
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [role, setRole] = useState('');
  const [id, setId] = useState('');
  const [userCourses, setUserCourses] = useState([]);
  const [adminCourses, setAdminCourses] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const storedRole = localStorage.getItem('role');
    const token = localStorage.getItem('token');
    const storedId = localStorage.getItem('id');
  
    const userIsLoggedIn = token !== null;
  
    if (userIsLoggedIn) {
      setLoggedIn(userIsLoggedIn);
      setRole(storedRole || '');
      setToken(token || '');
      setId(storedId || '');
     
    }
  
    let userEventSource;
    let adminEventSource;
  
    if (id && token) {
      userEventSource = new EventSource(`http://localhost:8080/api/access/courses/${id}?token=${token}`);
      userEventSource.onmessage = (event) => {
         const newData = JSON.parse(event.data);
         if(!userCourses.includes(newData)){
          setUserCourses((prevData) => [...prevData, newData]);
         }
      };
    }
  
    if (role === 'ADMIN' && (id && token)) {
      adminEventSource = new EventSource(`http://localhost:8080/api/administrator/courses/${id}?token=${token}`);
      adminEventSource.onmessage = (event) => {
        const newData = JSON.parse(event.data);
        if (!adminCourses.includes(newData)) {
        setAdminCourses((prevData) => [...prevData, newData]);
      }
      };
    }
  
    return () => {
      if (userEventSource) {
        userEventSource.close();
      }
      if (adminEventSource) {
        adminEventSource.close();
      }
    };
  
  }, [endpoint, data]);
  



  const handleCourseClick = async (course) => {
    if (!isLoggedIn) {
      alert('You need to be logged in to access this course')
    } 
    if (course.status === 0 && isLoggedIn) {
      alert('This course is currently closed')

    } else if (course.status === 1 && isLoggedIn) {
        const isAdminCourse = adminCourses.includes(course.id);
        const isCourseAvailable = userCourses.includes(course.id);
        if (isCourseAvailable||isAdminCourse) {
        
          navigate(role === 'ADMIN' ? `/admin-courses/${course.id}` : `/user-courses/${course.id}`);

        } else {
          alert(`You do not have permission to access this course ${course.title}`)
        }
    }
  };


  const handleToggleStatus = async (course) => {
    if (role === 'ADMIN') {
      
      const isCourseAvailable = adminCourses.includes(course.id);
      
      if (isCourseAvailable) {
      
        const updatedCourse = {
          ...course,
          status: course.status == 1 ? 0 : 1,
        };
        
        await fetch(`http://localhost:8080/api/course/update?token=${token}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(updatedCourse),
        });
       
 

      } else {
        alert("You do not have admin's permission to toggle the course status");
      }
    } else {
      alert('You do not have permission to toggle the course status');
    }
  };
  


  return <CourseView 
  courses={data}
  onCourseClick={handleCourseClick}
  onToggleStatus={handleToggleStatus}
  isAdmin={role === 'ADMIN'}
  />;
};

export default CoursePresenter;




