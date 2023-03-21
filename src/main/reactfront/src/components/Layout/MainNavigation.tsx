import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

//import classes from './MainNavigation.module.css';
import AuthContext from '../../store/auth-context';
import React from 'react';


const MainNavigation = () =>{

  const authCtx = useContext(AuthContext);
  const [nickname, setNickname] = useState('');
  let isLogin = authCtx.isLoggedIn;
  let isGet = authCtx.isGetSuccess;

  const callback = (str:string) => {
    setNickname(str);
  }

  useEffect(() => {
    if (isLogin) {
      console.log('MainNavigation start -> '+authCtx.userObj.email);
      authCtx.getUser();
    } 
  }, [isLogin]);

  useEffect(() => {
    if (isGet) {
      console.log('MainNavigation get start -> '+authCtx.userObj.nickname);
      callback(authCtx.userObj.nickname);
    }
  }, [isGet]);


  const toggleLogoutHandler = () => {
    authCtx.logout();
  }

  
  return(
    <header>
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <Link className="navbar-brand" to="/">
        Home
      </Link>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav">
          {!isLogin && (
            <>
              <li className="nav-item">
                <Link className="nav-link" to="/login">
                  Login
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/signup">
                  Sign-Up
                </Link>
              </li>
            </>
          )}
          {isLogin && (
            <>
              <li className="nav-item">
                <Link className="nav-link" to="/profile">
                  {nickname}
                </Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/post/add">
                  게시물 작성
                </Link>
              </li>
              <li className="nav-item">
                <button
                  className="btn btn-primary"
                  onClick={toggleLogoutHandler}
                >
                  Logout
                </button>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
    </header>
  );
};

export default MainNavigation;