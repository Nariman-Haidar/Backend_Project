import React from 'react';

const CourseDetailsView = ({ courseDetails }) => {
  if (!courseDetails) {
    return <p>Loading...</p>;
  }

  return (
    <>
      <h2>Course Details</h2>
      <p>Title: {courseDetails.title}</p>
      <p>Status: {courseDetails.status}</p>
      {}
    </>
  );
};

export default CourseDetailsView;
