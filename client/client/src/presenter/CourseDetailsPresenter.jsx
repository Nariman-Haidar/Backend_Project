import React from 'react';
import CourseDetailsView from '../view/CourseDetailsView.jsx';
import CreateUseEffect from './CreateUseEffect';

const CourseDetailsPresenter = ({ courseId }) => {
  const endpoint = `courses/courses/${courseId}`;
  const courseDetails = CreateUseEffect({ endpoint: endpoint });

  return <CourseDetailsView courseDetails={courseDetails} />;
};

export default CourseDetailsPresenter;
