import React, { useState, useEffect } from 'react';
import { Stomp } from '@stomp/stompjs';

interface Props {
  chatRoomId: string;
}

const ChatRoom: React.FC<Props> = ({ chatRoomId }) => {
  const [messages, setMessages] = useState<string[]>([]);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      return;
    }

    const stompClient = Stomp.client('ws://localhost:8080/ws');
    stompClient.connect({ Authorization: `Bearer ${token}` }, () => {
      console.log(`Connected to chat room ${chatRoomId}`);
      stompClient.subscribe(`/pub/chat/room${chatRoomId}`, (message) => {
        setMessages((prevMessages) => [...prevMessages, message.body]);
      });
    });

    return () => {
      stompClient.disconnect();
      console.log(`Disconnected from chat room ${chatRoomId}`);
    };
  }, [chatRoomId]);

  return (
    <div>
      <h1>Chat Room {chatRoomId}</h1>
      <ul>
        {messages.map((message, index) => (
          <li key={index}>{message}</li>
        ))}
      </ul>
    </div>
  );
};

export default ChatRoom;
