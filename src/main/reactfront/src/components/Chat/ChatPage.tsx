import React, { useState, useEffect, useRef, useContext } from 'react';
import { Params, useParams } from 'react-router-dom';
import * as Stomp from '@stomp/stompjs';
import AuthContext from 'store/auth-context';
import './ChatPage.css';

interface ChatMessage {
  roomId: string;
  writer: string;
  message: string;
}

interface ChatRoom {
  id: string;
  name: string;
}


const ChatPage: React.FC<ChatRoom> = () => {
  const client = useRef<Stomp.Client | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const { roomId, name } = useParams<Params>();
  const [message, setMessage] = useState<string>('');
  const authCtx = useContext(AuthContext);
  const messageContainerRef = useRef<HTMLDivElement>(null);

  const headers = { Authorization: 'Bearer ' + authCtx.token };

  useEffect(() => {
    console.log('testing' + roomId + " " + name);
    connect();
    return () => disconnect();
  }, []);

  const connect = async () => {
    client.current = new Stomp.Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: headers,
      onConnect: async () => {
        subscribe();
        console.log('success');
      }
    });
    // 접속한다.
    client.current?.activate();
  };

  const subscribe = async () => {

    client.current?.subscribe('/sub/chat/' + roomId, (body) => {

      const parsed_body = JSON.parse(body.body);
      const { roomId, writer, message } = parsed_body; // 파싱된 정보를 추출
      const nameM = writer + "님: " + message;

      const newMessage: ChatMessage = {
        roomId: roomId,
        writer: writer,
        message: message
      };

      console.log('subscribe ' + nameM);
      setMessages((_chat_list) => [
        ..._chat_list, newMessage// 파싱된 정보를 새로운 메시지 객체에 담아서 추가
      ]);

    });
  };

  const disconnect = () => {
    console.log('disconnect');
    client.current?.deactivate();
  };


  const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };


  const handleSendMessage = () => {
    if (!authCtx.token) {
      console.log('token이 없습니다.');
      return;
    }

    client.current?.publish({
      destination: "/pub/chat",
      body: JSON.stringify({
        roomId: roomId ? roomId : "ERROR",
        writer: authCtx.userObj.nickname,
        message: message
      }),
    });
    setMessage('');
  };

  useEffect(() => {
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop = messageContainerRef.current.scrollHeight;
    }
  }, [messages]);

  // return (
  //   <div className="chat-page-container">
  //     <div className="chat-header">
  //       <h1>{name}</h1>
  //     </div>
  //     <div className="chat-messages-container" ref={messageContainerRef}>
  //       {messages.map((msg, index) => (
  //         <div key={index} className={`chat-message ${msg.writer ===  authCtx.userObj.nickname? 'right' : 'left'}`}>
  //           <div className={`chat-message-writer ${msg.writer ===  authCtx.userObj.nickname? 'other' : 'me'}`}>{msg.writer}</div>
  //           <div className="message">{msg.message}</div>
  //         </div>
  //       ))}
  //     </div>
  //     <div className="chat-input-container">
  //       <input type="text" placeholder="Type your message" value={message} onChange={handleMessageChange} />
  //       <button onClick={handleSendMessage}>Send</button>
  //     </div>
  //   </div>
  // );
  return (
    <div className="chat-page-container">
      <div className="chat-header">
        <h1>{name}</h1>
      </div>
      <div className="chat-messages-container" ref={messageContainerRef}>
        {messages.map((msg, index) => (
          <div key={index} className={`chat-bubble ${msg.writer === authCtx.userObj.nickname ? 'mine' : 'theirs'}`}>
            <div className={`chat-message-writer ${msg.writer === authCtx.userObj.nickname ? 'other' : 'me'}`}>{msg.writer}</div>
            <div className="chat-bubble-message">{msg.message}</div>
          </div>
        ))}
      </div>
      <div className="chat-input-container">
        <input type="text" placeholder="Type your message" value={message} onChange={handleMessageChange} />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );

};

export default ChatPage;


