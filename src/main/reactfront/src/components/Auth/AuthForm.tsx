import React, { useState, useRef, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';
//import classes from './AuthForm.module.css';

const AuthForm = () => {

  const nicknameInputRef = useRef<HTMLInputElement>(null);
  const passwordInputRef = useRef<HTMLInputElement>(null);

  let navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const authCtx = useContext(AuthContext);

  const submitHandler = async (event: React.FormEvent) => {
    event.preventDefault();
    
    const enteredNickname = nicknameInputRef.current!.value;
    const enteredPassword = passwordInputRef.current!.value;

    setIsLoading(true);
    authCtx.login(enteredNickname, enteredPassword);
    setIsLoading(false);

    if (authCtx.isSuccess) {
      console.log("nowHERE")
      navigate("/", { replace: true });
    }
    
}

    return (
      <section>
        <h1>Login</h1>
        <form onSubmit={submitHandler}>
          <div >
            <label htmlFor='nickname'>Your Name</label>
            <input type='nickname' id='nickname' required ref={nicknameInputRef}/>
          </div>
          <div >
            <label htmlFor="password">Your password</label>
            <input type='password' id='password' required ref={passwordInputRef}/>
          </div>
          <div >
            <button type='submit'>Login</button>
            {isLoading && <p>Loading</p>}
            <p>Create Account</p>
          </div>
        </form>
      </section>
    );
  }

export default AuthForm;