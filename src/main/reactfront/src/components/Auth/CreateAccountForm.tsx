import React, { useContext, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import './CreateAccountForm.module.css';

const CreateAccountForm = () => {

    let navigate = useNavigate();
    const authCtx = useContext(AuthContext);
    const emailInputRef = useRef<HTMLInputElement>(null);
    const passwordInputRef = useRef<HTMLInputElement>(null);
    const nicknameInputRef = useRef<HTMLInputElement>(null);
    const loginIdInputRef = useRef<HTMLInputElement>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const [previewUrl, setPreviewUrl] = useState<string>();

    const submitHandler = async (event: React.FormEvent) => {
        event.preventDefault();

        const enteredLoginId = loginIdInputRef.current!.value;
        const enteredEmail = emailInputRef.current!.value;
        const enteredPassword = passwordInputRef.current!.value;
        const enteredNickname = nicknameInputRef.current!.value;
        const enteredProfileImg = fileInputRef.current!.files![0];
        
        authCtx.signup(enteredEmail, enteredPassword, enteredNickname, enteredLoginId, enteredProfileImg); // 수정된 부분


        if (authCtx.isSuccess) {
            navigate("/", { replace: true });
        }
    }

    const imageChangeHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
        event.preventDefault();
      
        const files = event.target.files;
        if (files && files.length === 1) {
          const reader = new FileReader();
          reader.onloadend = () => {
            setPreviewUrl(reader.result as string);
          };
          reader.readAsDataURL(files[0]);
        }
      };

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
            <div className="form-group">
              <label htmlFor="profileImg" className="col-sm-2 control-label">Profile Image</label>
              <input type='file' id='profileImg' className="form-control-file" required ref={fileInputRef} onChange={imageChangeHandler}/>
              {fileInputRef && <img src={previewUrl} alt="Profile Preview"/>}
            </div>
            <div>
              <button type='submit' className="btn btn-primary btn"> Submit</button>
            </div>
          </form>
        </section>
      );
      
};
export default CreateAccountForm;