
import React, { useContext, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';
//import classes from './ChangePassword.module.css';

const ChangePassword = () => {

  let navigate = useNavigate();

  const authCtx = useContext(AuthContext);
  const exPasswordInputRef = useRef<HTMLInputElement>(null);
  const newPasswordInputRef = useRef<HTMLInputElement>(null);
  const newPasswordAgainInputRef = useRef<HTMLInputElement>(null);

  const submitHandler = (event: React.FormEvent) => {
    event.preventDefault();
    const enteredExPassword = exPasswordInputRef.current!.value;
    const enteredNewPassword = newPasswordInputRef.current!.value;
    const enteredNewPasswordAgain = newPasswordAgainInputRef.current!.value;
    if (enteredNewPassword !== enteredNewPasswordAgain) {
      alert("Password Write Correct!");
      return;
    }
    console.log('change pw start!');
    authCtx.changePassword(enteredExPassword, enteredNewPassword);
    console.log(authCtx.isSuccess);
    if (authCtx.isSuccess) {
      alert("다시 로그인 하세요.")
      authCtx.logout();
      navigate("/", { replace: true });
    }
  }

  return (
    <form onSubmit={submitHandler}>
      <div className="form-group" >
        <label htmlFor='ex-password'>Old Password</label>
        <input className="form-control"
          type='password'
          id='ex-password'
          minLength={8}
          ref={exPasswordInputRef}
        />
        <label htmlFor='new-password' >New Password</label>
        <input className="form-control"
          type='password'
          id='new-password'
          minLength={8}
          ref={newPasswordInputRef}
        />
        <label htmlFor='new-password' >New Password Again</label>
        <input className="form-control"
          type='password'
          id='new-password'
          minLength={8}
          ref={newPasswordAgainInputRef}
        />
      </div>
      <div>
        <button type='submit' className="btn btn-primary btn">Change Password</button>
      </div>
    </form>
  );
}

export { ChangePassword };