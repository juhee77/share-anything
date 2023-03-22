import {useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from '../../store/auth-context';

interface ChatRoom {
    id: string;
    name: string;
}

type Props = {
    chatRooms: ChatRoom[];
};

const ChatRoomListForm: React.FC<Props> = ({chatRooms}) => {
    const authCtx = useContext(AuthContext);
    const token = authCtx.token;
    const navigate = useNavigate(); // useNavigate hook 사용

    const handleChatRoomClick = (chatRoom: ChatRoom) => {
        console.log("connect chat room");
        if (!token) {
            console.log('token이 없습니다.');
            return;
        }
        // 해당 채팅방으로 들어갈 수 있는지 확인
        console.log(chatRoom.name + "을 클릭함");

        // ChatPage로 이동
        navigate(`/find/chat/${chatRoom.id}/${chatRoom.name}`); // useNavigate hook을 사용하여 ChatPage로 이동
    };

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
        </div>
    );
};

export default ChatRoomListForm;
