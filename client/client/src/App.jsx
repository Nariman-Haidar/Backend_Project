import './App.css'
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HeaderView from './view/HeaderView';
import LoginPresenter from './presenter/LoginPresenter';
import CoursePresenter from './presenter/CoursePresenter';
import UserPresenter from './presenter/UserPresenter';
import UserQueueItemPresenter from './presenter/UserQueueItemPresenter';
import AdminQueueItemPresenter from './presenter/AdminQueueItemPresenter';
import HeaderPresenter from './presenter/HeaderPresenter';
import AccessPresenter from './presenter/AccessPresenter';

function App() {
  return (
    <Router>
      <Routes>
        <Route
          path="*"
          element={
            <div>
              <HeaderPresenter  />
              <Routes>
                <Route path="users" element={<UserPresenter />} />
                <Route path="/user-access/:userId"  element={<AccessPresenter />} />
                <Route path="/courses" element={<CoursePresenter />} />
                <Route path="/user-courses/:courseId"  element={<UserQueueItemPresenter />} />
                <Route path="/admin-courses/:courseId" element={<AdminQueueItemPresenter />} />
                <Route path="/login" element={<LoginPresenter />} />
              </Routes>
            </div>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;



 