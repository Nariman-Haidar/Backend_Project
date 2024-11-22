import React from 'react';

const UserView = ({ users, onButtonClick }) => {
  return (
    <>
      <ul>
        {users.map((user) => (
          <li key={user.id}>
            <button onClick={() => onButtonClick(user.id)}>
            {user.username}
            </button>
          </li>
        ))}
      </ul>
    </>
  );
};

export default UserView;



