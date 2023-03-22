import React, { useContext } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';

import Layout from './components/Layout/Layout';
import AuthPage from './pages/AuthPage';
import CreateAccountPage from './pages/CreateAccountPage';
import HomePage from './pages/HomePage';
import ProfilePage from './pages/ProfilePage';
import AuthContext from './store/auth-context';
import AddPostPage from 'pages/AddPostPage';
import ChatRoomList from 'components/Chat/ChatRoomList';
import ChatPage from 'components/Chat/ChatPage';

function App() {

    const authCtx = useContext(AuthContext);
    console.log("MAIN");

    return (
        <Layout>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/signup/" element={authCtx.isLoggedIn ? <Navigate to='/' /> : <CreateAccountPage />} />
                <Route path="/login/*"
                    element={authCtx.isLoggedIn ? <Navigate to='/' /> : <AuthPage />}
                />
                <Route path="/profile/" element={!authCtx.isLoggedIn ? <Navigate to='/' /> : <ProfilePage />} />
                <Route path="/post/add" element={!authCtx.isLoggedIn ? <Navigate to='/' /> : <AddPostPage />} />
                <Route path="/chat/all/rooms" element={!authCtx.isLoggedIn ? <Navigate to='/' /> : <ChatRoomList />} />

                <Route path="/find/chat/:roomId/:name" element={<ChatPage id={''} name={''} />} />
            </Routes>

        </Layout>
    );
}

export default App;