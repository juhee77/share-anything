import React, {useContext, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import './CreateAccountForm.module.css';

const CreateAccountForm = () => {

    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const emailInputRef = useRef<HTMLInputElement>(null);
    const passwordInputRef = useRef<HTMLInputElement>(null);
    const nicknameInputRef = useRef<HTMLInputElement>(null);
    const loginIdInputRef = useRef<HTMLInputElement>(null);

    const submitHandler = (event: React.FormEvent) => {
        event.preventDefault();

        const entereLoginId = loginIdInputRef.current!.value;
        const enteredEmail = emailInputRef.current!.value;
        const enteredPassword = passwordInputRef.current!.value;
        const enteredNickname = nicknameInputRef.current!.value;

        authCtx.signup(enteredEmail, enteredPassword, enteredNickname, entereLoginId);

        if (authCtx.isSuccess) {
            return navigate("/", {replace: true});

        }

    }

    return (
        <section>
            <h1>Create Account</h1>
            <form onSubmit={submitHandler} className="form-horizontal">
                <div className="form-group">
                    <label htmlFor='email' className="col-sm-2 control-label">Your email</label>
                    <input type='email' id='email'  className="form-control" required ref={emailInputRef}/>
                </div>
                <div className="form-group">
                    <label htmlFor='loginId' className="col-sm-2 control-label" >Your loginId</label>
                    <input type='loginId' id='loginId' className="form-control"  required ref={loginIdInputRef}/>
                </div>
                <div className="form-group">
                    <label htmlFor="password" className="col-sm-2 control-label">Your password</label>
                    <input type='password' id='password' className="form-control"  required ref={passwordInputRef}/>
                </div>
                <div className="form-group">
                    <label htmlFor="nickname" className="col-sm-2 control-label" >NickName</label>
                    <input type='nickname' id='nickname' className="form-control"   required ref={nicknameInputRef}/>
                </div>
                <div>
                    <button type='submit' className="btn btn-primary btn"> Submit</button>
                </div>
            </form>
        </section>
    );
};

export default CreateAccountForm;