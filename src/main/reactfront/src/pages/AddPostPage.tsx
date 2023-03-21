import React, { useState } from "react";
import PostForm from "components/Post/PostForm";
import axios from "axios";
import * as authAction from '../store/auth-action'

const AddPostPage: React.FC = () => {
    return (
      <div>
        <h1>Add Post</h1>
        <PostForm/>
      </div>
    );
  };
  
  export default AddPostPage;