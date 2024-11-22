
import React from 'react';

const LoginView = ({
  username,
  password,
  confirmPassword,
  onUsernameChange,
  onPasswordChange,
  onConfirmPasswordChange,
  onAction, 
  onToggleMode,
  isRegisterMode,
}) => {
  return (
    <div>
      <h2>{isRegisterMode ? 'Register' : 'Login'}</h2>
      <form>
        <label>
          Username:
          <input type="text" value={username} onChange={(e) => onUsernameChange(e.target.value)} />
        </label>
        <br />
        <label>
          Password:
          <input type="password" value={password} onChange={(e) => onPasswordChange(e.target.value)} />
        </label>
        {isRegisterMode && (
          <>
            <br />
            <label>
              Confirm Password:
              <input type="password" value={confirmPassword} onChange={(e) => onConfirmPasswordChange(e.target.value)} />
            </label>
          </>
        )}
        <br />
        <button type="button" onClick={onAction}> {}
          {isRegisterMode ? 'Register' : 'Login'}
        </button>
        <br />
        <button type="button" onClick={onToggleMode}>
          {isRegisterMode ? 'Switch to Login' : 'Switch to Register'}
        </button>
      </form>
    </div>
  );
};

export default LoginView;
