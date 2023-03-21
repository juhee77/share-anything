import {GET, POST} from "./fetch-auth-action";

const createTokenHeader = (token:string) => {
  return {
    headers: {
      'Authorization': 'Bearer ' + token
    }
  }
}

const calculateRemainingTime = (expirationTime:number) => {
  const currentTime = new Date().getTime();
  const adjExpirationTime = new Date(expirationTime).getTime();
  return adjExpirationTime - currentTime;
};

export const loginTokenHandler = (accessToken:string, refreshToken:string, expirationTime:number) => {
  localStorage.setItem('token', accessToken);
  localStorage.setItem('refreshToken',refreshToken)
  localStorage.setItem('expirationTime', String(expirationTime));

  return calculateRemainingTime(expirationTime);
}

export const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem('token');
  const refreshToken = localStorage.getItem('refreshToken');
  const storedExpirationDate = localStorage.getItem('expirationTime') || '0';

  const remaingTime = calculateRemainingTime(+ storedExpirationDate);

  if(remaingTime <= 1000) {
    //TODO : reissued Token 
    localStorage.removeItem('token');
    localStorage.removeItem('expirationTime');
    return null
  }

  return {
    token: storedToken,
    refreshToken : refreshToken,
    duration: remaingTime
  }
}

export const signupActionHandler = (email: string, password: string, nickname: string, loginId:string) => {
  const URL = '/auth/signup'
  const signupObject = { email, password, nickname ,loginId};

  return POST(URL, signupObject, {});
};

export const loginActionHandler = (loginId:string, password: string) => {
  const URL = '/auth/login';
  const loginObject = { loginId, password };
  return POST(URL, loginObject, {});
};

export const logoutActionHandler = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('expirationTime');
};

export const getUserActionHandler = (token:string) => {
  const URL = '/me-profile';
  return GET(URL, createTokenHeader(token));
}

export const changeNicknameActionHandler = ( nickname:string, token: string) => {
  const URL = '/member/modify/nickname';
  const changeNicknameObj = { nickname : nickname };
  return POST(URL, changeNicknameObj, createTokenHeader(token));
}

export const changePasswordActionHandler = (
  exPassword: string,
  newPassword: string,
  token: string
) => {
  const URL = '/member/modify/password';
  const changePasswordObj = { exPassword, newPassword };
  return POST(URL, changePasswordObj, createTokenHeader(token));
}

export const addPostActionHandler = (
  title : string,
  text : string,
  open : boolean,
  token :string,
) => {
  const URL = '/post/add';
  const postObj = {title, text, open};
  return POST(URL, postObj, createTokenHeader(token));
}