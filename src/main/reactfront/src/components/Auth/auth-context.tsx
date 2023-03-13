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

export const loginTokenHandler = (token:string, expirationTime:number) => {
  localStorage.setItem('token', token);
  localStorage.setItem('expirationTime', String(expirationTime));

  return calculateRemainingTime(expirationTime);
}

export const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem('token');
  const storedExpirationDate = localStorage.getItem('expirationTime') || '0';

  const remaingTime = calculateRemainingTime(+ storedExpirationDate);

  if(remaingTime <= 1000) {
    localStorage.removeItem('token');
    localStorage.removeItem('expirationTime');
    return null
  }

  return {
    token: storedToken,
    duration: remaingTime
  }
}

export const signupActionHandler = (email: string, password: string, nickname: string, name:string) => {
  const URL = '/auth/signup'
  const signupObject = { email, password, nickname,name };

  return POST(URL, signupObject, {});
};

export const loginActionHandler = (email:string, password: string) => {
  const URL = '/auth/login';
  const loginObject = { email, password };
  return POST(URL, loginObject, {});
};

export const logoutActionHandler = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('expirationTime');
};

export const getUserActionHandler = (token:string) => {
  const URL = '/member/me';
  return GET(URL, createTokenHeader(token));
}

export const changeNicknameActionHandler = ( nickname:string, token: string) => {
  const URL = '/member/nickname';
  const changeNicknameObj = { nickname };
  return POST(URL, changeNicknameObj, createTokenHeader(token));
}

export const changePasswordActionHandler = (
  exPassword: string,
  newPassword: string,
  token: string
) => {
  const URL = '/member/password';
  const changePasswordObj = { exPassword, newPassword }
  return POST(URL, changePasswordObj, createTokenHeader(token));
}