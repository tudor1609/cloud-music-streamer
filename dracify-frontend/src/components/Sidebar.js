import React from 'react';
import { Home, User, LogOut, Disc } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const Sidebar = ({ activeTab, setActiveTab }) => {
    const { logout } = useAuth();

    return (
        <aside className="sidebar">
            <div className="sidebar-brand">
                <div className="logo-box">D</div>
                <span>DRACIFY</span>
            </div>
            <nav className="sidebar-nav">
                <button className={activeTab === 'library' ? 'active' : ''} onClick={() => setActiveTab('library')}>
                    <Home size={20} /> <span>Bibliotecă</span>
                </button>
                <button className={activeTab === 'profile' ? 'active' : ''} onClick={() => setActiveTab('profile')}>
                    <User size={20} /> <span>Profil Borfaș</span>
                </button>
            </nav>
            <button className="logout-btn" onClick={logout}>
                <LogOut size={18} /> <span>Ieșire</span>
            </button>
        </aside>
    );
};

export default Sidebar;