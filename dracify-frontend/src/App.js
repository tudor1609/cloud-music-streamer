import React, { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Library from './pages/Library';
import Profile from './pages/Profile';
import Register from './pages/Register';
import Login from './pages/Login';
import './App.css';

const MainApp = () => {
    const { isLoggedIn } = useAuth();
    const [activeTab, setActiveTab] = useState('library');
    const [currentSong, setCurrentSong] = useState(null);
    const [playlist, setPlaylist] = useState([]); // Pentru autoplay
    const [showRegister, setShowRegister] = useState(false);

    if (!isLoggedIn) {
        return showRegister ? (
            <Register onSwitchToLogin={() => setShowRegister(false)} />
        ) : (
            <Login onSwitchToRegister={() => setShowRegister(true)} />
        );
    }

    return (
        <Layout activeTab={activeTab} setActiveTab={setActiveTab} currentSong={currentSong} songs={playlist} onSelectSong={setCurrentSong}>
            {activeTab === 'library' && (
                <Library
                    onSelectSong={setCurrentSong}
                    currentSongId={currentSong?.id}
                    onUpdatePlaylist={setPlaylist}
                />
            )}
            {activeTab === 'profile' && <Profile />}
        </Layout>
    );
};

const App = () => (
    <AuthProvider>
        <MainApp />
    </AuthProvider>
);

export default App;