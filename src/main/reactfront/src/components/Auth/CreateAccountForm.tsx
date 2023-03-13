import React, {useContext, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from '../../store/auth-context';
//import classes from './CreateAccountForm.module.css';

const CreateAccountForm = () => {

    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const emailInputRef = useRef<HTMLInputElement>(null);
    const passwordInputRef = useRef<HTMLInputElement>(null);
    const nicknameInputRef = useRef<HTMLInputElement>(null);
    const nameInputRef = useRef<HTMLInputElement>(null);

    const submitHandler = (event: React.FormEvent) => {
        event.preventDefault();

        const entereName = nameInputRef.current!.value;
        const enteredEmail = emailInputRef.current!.value;
        const enteredPassword = passwordInputRef.current!.value;
        const enteredNickname = nicknameInputRef.current!.value;

        authCtx.signup(enteredEmail, enteredPassword, enteredNickname, entereName);

        if (authCtx.isSuccess) {
            return navigate("/", {replace: true});

        }

    }

    return (
        <section>
            <h1>Create Account</h1>
            <form onSubmit={submitHandler}>
                <div>
                    <label htmlFor='email'>Your email</label>
                    <input type='email' id='email' required ref={emailInputRef}/>
                </div>
                <div>
                    <label htmlFor='name'>Your name</label>
                    <input type='name' id='name' required ref={nameInputRef}/>
                </div>
                <div>
                    <label htmlFor="password">Your password</label>
                    <input type='password' id='password' required ref={passwordInputRef}/>
                </div>
                <div>
                    <label htmlFor="nickname">NickName</label>
                    <input type='nickname' id='nickname' required ref={nicknameInputRef}/>
                </div>
                <div>
                    <button type='submit'>Submit</button>
                </div>
            </form>
        </section>
    );
};

export default CreateAccountForm;