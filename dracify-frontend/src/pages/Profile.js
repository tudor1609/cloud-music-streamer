import React from 'react';
import { useAuth } from '../context/AuthContext';
import { User, Mail, ShieldCheck } from 'lucide-react';

const Profile = () => {
    const { user } = useAuth(); // Luăm userul logat din context

    return (
        <div className="page">
            <header className="page-header">
                <h1>PROFILUL TĂU</h1>
            </header>

            <div className="profile-card">
                <div className="avatar">
                    {user?.username ? user.username.charAt(0).toUpperCase() : 'D'}
                </div>

                <div className="info-group">
                    <div className="info-item">
                        <User size={18} color="#ff0000" />
                        <span><strong>Nume Utilizator:</strong> {user?.username || "Oaspete"}</span>
                    </div>

                    <div className="info-item">
                        <Mail size={18} color="#ff0000" />
                        <span><strong>Email:</strong> {user?.email || "nespecificat@dracify.com"}</span>
                    </div>

                    <div className="info-item status-badge">
                        <ShieldCheck size={18} color="#00ff00" />
                        <span><strong>Statut:</strong> BORFAȘ AUTENTIFICAT</span>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Profile;