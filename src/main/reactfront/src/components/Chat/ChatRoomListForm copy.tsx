
import * as StompJs from '@stomp/stompjs';
import { FormEvent, useContext, useEffect, useRef, useState } from 'react';
import AuthContext from '../../store/auth-context';

interface ChatRoom {
  id: string;
  name: string;
}

type Props = {
  chatRooms: ChatRoom[];
};

const ChatRoomListForm: React.FC<Props> = ({ chatRooms }) => {
  const authCtx = useContext(AuthContext);
  const token = authCtx.token;
  const [loading, setLoading] = useState<boolean>(false);
  const [currentChatRoom, setCurrentChatRoom] = useState<ChatRoom | null>(null);
  const [message, setMessage] = useState<string>('');
  const [messages, setMessages] = useState<string[]>([]);
  const headers = { Authorization: 'Bearer ' + token };
  const client = useRef<StompJs.Client | null>(null);

  const connect = async () => {
    client.current = new StompJs.Client({
      brokerURL: 'ws://localhost:8080/ws',
      connectHeaders: headers,
      onConnect: async () => {
        subscribe();
        console.log('success');
      }
    });
    // 접속한다.
    client.current.activate();
  };

  //소켓 연결
  const handleChatRoomClick = (chatRoom: ChatRoom) => {
    console.log("connect chat room");
    if (!token) {
      console.log('token이 없습니다.');
      return;
    }
    setLoading(true);
    disconnect();
    setMessages([]);
    setCurrentChatRoom(chatRoom);
    setLoading(false);


    connect();
    console.log(chatRoom.name + "을 클릭함");
  };


  const publish = (message: any) => {
    if (!client.current?.connected) return;

    console.log(message);

    client.current.publish({
      destination: "/pub/chat",
      body: JSON.stringify({
        roomId: currentChatRoom?.id,
        writer: authCtx.userObj.nickname,
        message: message
      }),
    });
    setMessage('');
  };


  const handleMessageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
  };

  const subscribe = async () => {
    while (loading) {
      await new Promise(resolve => setTimeout(resolve, 100));
    }
    console.log(currentChatRoom ? "SETTING" : "NULL ERROR");
    client.current?.subscribe('/sub/chat/' + currentChatRoom?.id, (body) => {

      const parsed_body = JSON.parse(body.body);
      const { roomId, writer, message } = parsed_body; // 파싱된 정보를 추출
      const nameM = writer + "님: " + message;

      console.log('subscribe ' + nameM);
      setMessages((_chat_list) => [
        ..._chat_list, nameM // 파싱된 정보를 새로운 메시지 객체에 담아서 추가
      ]);

    });
  };

  const disconnect = () => {
    console.log('disconnect');
    client.current?.deactivate();
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>, chat: string) => {
    event.preventDefault();
    publish(message);
  };

  useEffect(() => {
    console.log('useEffect');
    connect();
    return () => disconnect();
  }, []);

  return (
    <div>
      <h1>Chat Room List</h1>
      <ul>
        {chatRooms.map((chatRoom) => (
          <li key={chatRoom.id} onClick={() => handleChatRoomClick(chatRoom)}>
            {chatRoom.name}
          </li>
        ))}
      </ul>
      {currentChatRoom && (
        <div>
          <h2>Chatting in {currentChatRoom.name}</h2>
          <div>
            <ul>
              {messages.map((message, index) => (
                <li key={index}>{message}</li>
              ))}
            </ul>
          </div>

          <div>
            <form onSubmit={(event) => handleSubmit(event, message)}>
              <div>
                <input type="text" name={'messageInput'} value={message} onChange={handleMessageChange} />
              </div>
              <input type={'submit'} value='전송' />
            </form>
          </div>
        </div>

      )}
    </div>
  );
};

export default ChatRoomListForm;

