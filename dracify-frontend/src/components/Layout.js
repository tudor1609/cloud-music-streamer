import React from 'react';
import Sidebar from './Sidebar';
import Player from './Player';

const Layout = ({ children, activeTab, setActiveTab, currentSong }) => {
    return (
        <div className="dracify-app">
            {/* Navigația rămâne fixă în stânga */}
            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />

            {/* Zona centrală unde se încarcă paginile (Library, Profile etc.) */}
            <main className="main-content">
                {children}
            </main>

            {/* Player-ul rămâne fix jos dacă avem o piesă selectată */}
            <Player song={currentSong} />
        </div>
    );
};

export default Layout;