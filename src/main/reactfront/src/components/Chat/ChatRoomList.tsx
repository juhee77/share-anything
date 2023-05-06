import React, { useContext, useEffect, useState } from 'react';
import AuthContext from 'store/auth-context';
import ChatRoomListForm from './ChatRoomListForm';


type ChatRoom = {
    id: string;
    name: string;
    writer: string;
    number: number;
}

const ChatRoomList: React.FC = () => {
    const authCtx = useContext(AuthContext);
    const [chatRooms, setChatRooms] = useState<ChatRoom[]>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const [error, setError] = useState<string>('');

    useEffect(() => {
        setLoading(true);
        const token = authCtx.token;

        fetch('/chat/room', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        })
            .then((response) => response.json())
            .then((data) => setChatRooms(data))
            .catch((error) => console.error(error));

        setLoading(false);
    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return <ChatRoomListForm chatRooms={chatRooms} />;
};

export default ChatRoomList;
