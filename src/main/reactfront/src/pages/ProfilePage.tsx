import { Fragment } from "react";
import { ChangeUsername } from "../components/Profile/ChangeUsername";
import { ChangePassword } from "../components/Profile/ChangePassword";
import React from "react";

const ProfilePage = () => {
  return (
    <Fragment>
      <ChangePassword />
      <ChangeUsername />
    </Fragment>
  );
};

export default ProfilePage;