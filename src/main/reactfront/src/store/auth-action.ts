import { userInfo } from "os";
import { GET, PATCH, POST } from "./fetch-auth-action";

const createTokenHeader = (token: string) => {
  return {
    headers: {
      Authorization: "Bearer " + token,
    },
  };
};

const calculateRemainingTime = (expirationTime: number) => {
  const currentTime = new Date().getTime();
  const adjExpirationTime = new Date(expirationTime).getTime();
  return adjExpirationTime - currentTime;
};

export const loginTokenHandler = (
  accessToken: string,
  refreshToken: string,
  expirationTime: number
) => {
  localStorage.setItem("token", accessToken);
  localStorage.setItem("refreshToken", refreshToken);
  localStorage.setItem("expirationTime", String(expirationTime));

  return calculateRemainingTime(expirationTime);
};

export const retrieveStoredToken = () => {
  const storedToken = localStorage.getItem("token");
  const refreshToken = localStorage.getItem("refreshToken");
  const storedExpirationDate = localStorage.getItem("expirationTime") || "0";

  const remaingTime = calculateRemainingTime(+storedExpirationDate);

  if (remaingTime <= 1000) {
    //TODO : reissued Token
    localStorage.removeItem("token");
    localStorage.removeItem("expirationTime");
    return null;
  }

  return {
    token: storedToken,
    refreshToken: refreshToken,
    duration: remaingTime,
  };
};

export const signupActionHandler = (
  email: string,
  password: string,
  nickname: string,
  loginId: string,
  profileImg: File
) => {
  const URL = "/auth/signup";
  const formData = new FormData();
  formData.append("email", email);
  formData.append("loginId", loginId);
  formData.append("password", password);
  formData.append("nickname", nickname);
  formData.append("profileImg", profileImg);

  return POST(URL, formData, {});
};

export const loginActionHandler = (loginId: string, password: string) => {
  const URL = "/auth/login";
  const loginObject = { loginId, password };
  return POST(URL, loginObject, {});
};

export const logoutActionHandler = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("expirationTime");
};

export const getUserActionHandler = (token: string) => {
  const URL = "/my/profile";
  return GET(URL, createTokenHeader(token));
};

export const changeNicknameActionHandler = (
  nickname: string,
  token: string,
  loginId: string
) => {
  const URL = "/member/" + loginId + "/nickname";
  const changeNicknameObj = { nickname: nickname };
  return PATCH(URL, changeNicknameObj, createTokenHeader(token));
};

export const changePasswordActionHandler = (
  exPassword: string,
  newPassword: string,
  token: string,
  loginId: string
) => {
  const URL = "/member/" + loginId + "/password";
  const changePasswordObj = { exPassword, newPassword };
  return PATCH(URL, changePasswordObj, createTokenHeader(token));
};

export const addPostActionHandler = (
  title: string,
  text: string,
  open: boolean,
  board: String,
  token: string
) => {
  const URL = "/post";
  const postObj = { title, text, board, open };
  return POST(URL, postObj, createTokenHeader(token));
};
