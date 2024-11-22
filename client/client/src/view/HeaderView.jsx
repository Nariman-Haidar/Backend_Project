
import React from 'react';
import { NavLink } from 'react-router-dom';
import './HeaderView.css';

const HeaderView = ({ isLoggedIn, username, role, onLogout }) => {
  return (
    <header>
      <nav>
        <ul className="header-list">
          {role === 'ADMIN' && (
            <li>
              <NavLink to="/users" className="active">
                Users
              </NavLink>
            </li>
          )}
          <li>
            <NavLink to="/courses" className="active">
              Courses
            </NavLink>
          </li>
          {isLoggedIn ? (
            <>
              <li>
                <span className="user-info">
                  Welcome: {username} ({role})
                </span>
              </li>
              <li>
                <NavLink onClick={() => onLogout()} className="active">
                  Logout
                </NavLink>
              </li>
            </>
          ) : (
            <>
              <li>
                <NavLink to="/login" className="active">
                  Login
                </NavLink>
              </li>
              {}
            </>
          )}
        </ul>
      </nav>
    </header>
  );
};

export default HeaderView;



