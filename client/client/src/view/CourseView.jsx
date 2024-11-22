import React from 'react';
import './CourseView.css';
const CourseView = ({ courses, onCourseClick, onToggleStatus, isAdmin }) => {
  return (
    <>
      <ul>
        {courses.map((course) => (
          <li key={course.id}>
            <span onClick={() => onCourseClick(course)}>
              {course.status === 0 ? (
                <span>{course.title} - Closed</span>
              ) : (
                <span>{course.title} - Open</span>
              )}
            </span>
            {isAdmin && (
              <button onClick={() => onToggleStatus(course)}>
                {course.status == 1 ? (
                  <span>Close</span>
                ) : (
                  <span>Open</span>
                )}
              </button>
            )}
          </li>
        ))}
      </ul>
    </>
  );
};

export default CourseView;


