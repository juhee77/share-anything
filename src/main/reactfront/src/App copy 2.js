import './App.css';
import React, { useState } from 'react';

const App = () => {

  const [text, setText] = useState("");
  const [ edit, setEdit] = useState(false);

  let content = <div>
    {text}<button onClick={() => setEdit(true)}>수정</button>
    </div>

  if(edit){
    content =  <div>
    <input type="text"
      value={text}
      onChange={(e) => {
        console.log(e.target.value);
        setText(e.target.value);
      }}
    />
    <button onClick={() => setEdit(false)}> 수정 </button>
  </div>
  }

  return (<div>
      {content} 
  </div>
  );
}

// 게시글의 내용을 수정
export default App;
