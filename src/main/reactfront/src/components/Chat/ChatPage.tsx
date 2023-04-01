import React, { useContext, useEffect, useRef, useState } from "react";
import { Params, useNavigate, useParams } from "react-router-dom";
import * as Stomp from "@stomp/stompjs";
import AuthContext from "store/auth-context";
import "./ChatPage.css";

interface ChatMessage {
  roomId: string;
  writer: string;
  message: string;
  time: string;
}

interface ChatRoom {
  id: string;
  name: string;
}

const ChatPage: React.FC<ChatRoom> = () => {
  const client = useRef<Stomp.Client | null>(null);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const { roomId, name } = useParams<Params>();
  const [message, setMessage] = useState<string>("");
  const authCtx = useContext(AuthContext);
  const messageContainerRef = useRef<HTMLDivElement>(null);
  const [enter, setEnter] = useState<boolean>(false);
  const headers = { Authorization: "Bearer " + authCtx.token };
  const navigate = useNavigate(); // useNavigate hook 사용

  useEffect(() => {
    console.log("testing" + roomId + " " + name);
    connect();
    //return () => disconnect();
  }, []);

  const connect = async () => {
    client.current = new Stomp.Client({
      brokerURL: "ws://localhost:8080/ws",
      connectHeaders: headers,
      onConnect: async () => {
        enterRoom();
        subscribe();
        console.log("success");
      },
    });
    // 접속한다.
    client.current?.activate();
  };

  //const destination = "/user/" + authCtx.userObj.nickname + "/sub/chat/enter/" + roomId;
  const destination = "/user/" + authCtx.userObj.nickname + "/sub/chat/enter/" + roomId;
  const subscribe = async () => {
    console.log(destination);
      client.current?.subscribe(destination, (body) => { 
      const chatlist = JSON.parse(body.body);
      console.log("과거기록 들어옴"+chatlist);

      setMessages(
        chatlist.map(
          (chat: { roomId: any; writer: any; message: any; time: any }) => ({
            roomId: chat.roomId,
            writer: chat.writer,
            message: chat.message,
            time: chat.time,
          })
        )
      );
    });

    client.current?.subscribe("/sub/chat/" + roomId, (body) => {
      const parsed_body = JSON.parse(body.body);
      const { roomId, writer, message, time } = parsed_body; // 파싱된 정보를 추출
      console.log(parsed_body);
      const nameM = writer + "님: " + message;

      const newMessage: ChatMessage = {
        roomId: roomId,
        writer: writer,
        message: message,
        time: time,
      };

      console.log("subscribe " + nameM);
      setMessages((_chat_list) => [
        ..._chat_list,
        newMessage, // 파싱된 정보를 새로운 메시지 객체에 담아서 추가
      ]);
    });
  };

  const subDisconnect = () => {
    console.log("subDisconnect");
    subcribeOutRoom();
    client.current?.deactivate();
  };

  const disconnect = () => {
    console.log("subDisconnect");
    outRoom();
    client.current?.deactivate();
  };

  const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };

  const enterRoom = () => {
    if (!enter) {
      //처음 접속 한 경우에만
      client.current?.publish({
        destination: "/pub/chat/enter",
        body: JSON.stringify({
          roomId: roomId ? roomId : "ERROR",
          writer: authCtx.userObj.nickname,
          message: message,
        }),
      });
      setEnter(true);
    }
  };

  const outRoom = () => {
      setEnter(false);
  };

  const subcribeOutRoom = () => {
    if (!enter) {
      //처음 접속 한 경우에만
      client.current?.publish({
        destination: "/pub/chat/subscribe/out",
        body: JSON.stringify({
          roomId: roomId ? roomId : "ERROR",
          writer: authCtx.userObj.nickname,
          message: message,
        }),
      });
      setEnter(false);
    }
  };

  const handleSendMessage = () => {
    if (!authCtx.token) {
      console.log("token이 없습니다.");
      setMessage("token이 종료 nav로 옮겨야 함");
      return;
    }

    client.current?.publish({
      destination: "/pub/chat/send",
      body: JSON.stringify({
        roomId: roomId ? roomId : "ERROR",
        writer: authCtx.userObj.nickname,
        message: message,
      }),
    });

    setMessage("");
  };

  const handleOutChatRoom = () => {
    disconnect();
    navigate("/chat/all/rooms");
  };


  const handleOutSubscribeChatRoom = () => {
    subDisconnect();
    navigate("/chat/all/rooms");
  };

  useEffect(() => {
    if (messageContainerRef.current) {
      messageContainerRef.current.scrollTop =
        messageContainerRef.current.scrollHeight;
    }
  }, [messages]);


  return (
    <div className="chat-page-container">
      <div className="chat-header">
        <h1>{name}</h1>
      </div>
      <div className="chat-messages-container" ref={messageContainerRef}>
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`chat-bubble ${
              msg.writer === authCtx.userObj.nickname ? "mine" : "theirs"
            }`}
          >
            <div
              className={`chat-message-writer ${
                msg.writer === authCtx.userObj.nickname ? "other" : "me"
              }`}
            >
              {msg.writer}
            </div>
            <div>{msg.time}</div>

            <div className="chat-bubble-message">{msg.message}</div>
          </div>
        ))}
      </div>
      <div>
        <button onClick={handleOutChatRoom}>채팅방 나가기</button>
      </div>
      <div>
        <button onClick={handleOutSubscribeChatRoom}>구독 취소하고 나가기</button>
      </div>
      <div className="chat-input-container">
        <input
          type="text"
          placeholder="Type your message"
          value={message}
          onChange={handleMessageChange}
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );
};

export default ChatPage;
