import React, { useState, useRef, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import './AuthForm.module.css';

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
      <section className='loginForm'>
        <h1>Login</h1>
        <form onSubmit={submitHandler} className="form-horizontal">
          <div >
            <label htmlFor='nickname'  className="col-sm-2 control-label" >Your Name</label>
            <input type='nickname' id='nickname' className="form-control" required ref={nicknameInputRef}/>
          </div>
          <div >
            <label htmlFor="password" className="col-sm-2 control-label">Your password</label>
            <input type='password' id='password' className="form-control" required ref={passwordInputRef}/>
          </div>
          <div >
            <button type='submit' className="btn btn-primary btn">Login</button>
            {isLoading && <p>Loading</p>}
          </div>
        </form>
      </section>
    );
  }

export default AuthForm;