
import React from 'react';

const UserAccess = (props) => {
  return (
    <>
      <ul>
        {props.courses.map((course) => (
          <li key={course.id}>
            {course.title}
            <button onClick={() => props.onDeleteClick(course)}>
             Delete
            </button>
          </li>
        ))}
      </ul>
    </>
  );
};
const AdminAccess = (props) => {
    return (
      <>
        <ul>
          {props.courses.map((course) => (
            <li key={course.id}>
              {course.title}
              <button onClick={() => props.onAddClick(course)}>
               Add
              </button>
            </li>
          ))}
        </ul>
      </>
    );
  };
  
   
export  {UserAccess, AdminAccess};

 