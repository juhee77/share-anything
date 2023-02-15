import './App.css';
import React, { useState, useEffect } from 'react';
import axios from 'axios';

const App = () => {

  const [posts, setPosts] = useState([]);

  // useEffect( async ()  => {
  //   // axios({
  //   //   method:'GET',
  //   //   url :'https://jsonplaceholder.typicode.com/photos'
  //   // }).then(response => setPosts(response.data))

  //   // axios.get('https://jsonplaceholder.typicode.com/photos')
  //   // .then(response => setPosts(response.data)) //then 은 비동기 통신


  //   try {
  //     const response = await axios.get('https://jsonplaceholder.typicode.com/photos');
  //     setPosts(response.data);
  //   } catch (error) {
  //     console.log(error)
  //   }
  // })

  useEffect(()  => {
    // axios({
    //   method:'GET',
    //   url :'https://jsonplaceholder.typicode.com/photos'
    // }).then(response => setPosts(response.data))

    axios.get('https://jsonplaceholder.typicode.com/photos')
    .then(response => setPosts(response.data)) //then 은 비동기 통신
  })


  return (
    <ul>
      {posts.map(post => (
        <li key={post.id}>
          <div>{post.title}</div>
          <div><img src={post.thumbnailUrl}></img></div>
        </li>
      ))}
    </ul>
  )
}

export default App;
