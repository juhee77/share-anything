import {useContext, useRef, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import AuthContext from '../../store/auth-context';
import './ChatRoomListForm.css';
import {POST} from 'store/fetch-auth-action';

type ChatRoom = {
    id: string;
    name: string;
    writer: string;
}

type Props = {
    chatRooms: ChatRoom[];
};

const ChatRoomListForm: React.FC<Props> = ({chatRooms}) => {
    const authCtx = useContext(AuthContext);
    const token = authCtx.token;
    const navigate = useNavigate(); // useNavigate hook 사용
    const [newRoomName, setNewRoomName] = useState<string>('');
    const [showInputBox, setShowInputBox] = useState<boolean>(false);

    const inputRef = useRef<HTMLInputElement>(null);

    const handleChatRoomClick = (chatRoom: ChatRoom) => {
        console.log('connect chat room');
        if (!token) {
            console.log('token이 없습니다.');
            return;
        }
        console.log(chatRoom.name + '을 클릭함');
        navigate(`/find/chat/${chatRoom.id}/${chatRoom.name}`);
    };


    //새로운 방 생성 인풋박스 동적 생성
    const handleNewRoomClick = () => {
        setShowInputBox(true);
        setTimeout(() => {
            if (inputRef.current) {
                inputRef.current.focus();
            }
        }, 0);
    };


    const handleNewRoomSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log('create new room: ' + newRoomName);
        // 새 방을 생성하고 새 방으로 이동
        // 서버에서 방을 생성
        makeNewRoom(newRoomName);
        setNewRoomName('');
        setShowInputBox(false);
    };

    const makeNewRoom = (name: string) => {
        const URL = '/chat/room/create?name=' + name;
        const data = {name}
        POST(URL, data, createTokenHeader(authCtx.token)).then((result) => {
            if (result !== null) {
                const chatRoom: ChatRoom = result.data;
                console.log("makeRoom: " + chatRoom.id);
                navigate(`/find/chat/${chatRoom.id}/${chatRoom.name}`);
            }
        });
    }

    const createTokenHeader = (token: string) => {
        return {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }
    }

    return (
        <div className="chat-room-list-container">
            <h1>Chat Room List</h1>
            <table className="chat-room-list">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                </tr>
                </thead>
                <tbody>
                {chatRooms.map((chatRoom, index) => (
                    <tr key={chatRoom.id} onClick={() => handleChatRoomClick(chatRoom)}>
                        <td>{index + 1}</td>
                        <td>{chatRoom.name}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            {showInputBox ? (
                <form onSubmit={handleNewRoomSubmit} className="new-room-form">
                    <label>
                        New Room Name:
                        <input type="text" value={newRoomName} onChange={(event) => setNewRoomName(event.target.value)}
                               ref={inputRef}/>
                    </label>
                    <button type="submit">Create Room</button>
                </form>
            ) : (
                <button onClick={handleNewRoomClick}>Create New Chat Room</button>
            )}

        </div>
    );
};

export default ChatRoomListForm;
