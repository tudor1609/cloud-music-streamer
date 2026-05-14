import React, { useState } from 'react';
import api from '../api/axios';
import { Lock, User as UserIcon, Mail } from 'lucide-react';

const Register = ({ onSwitchToLogin }) => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: ''
    });
    const [message, setMessage] = useState({ type: '', text: '' });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Trimitem datele către endpoint-ul de register al Auth Service
            await api.post('/auth/register', formData);
            setMessage({ type: 'success', text: 'Cont creat! Acum te poți loga.' });
            setTimeout(() => onSwitchToLogin(), 2000);
        } catch (err) {
            setMessage({ type: 'error', text: 'Eroare! Username-ul sau email-ul e deja luat.' });
        }
    };

    return (
        <div className="login-screen">
            <div className="login-card shadow-glow">
                <div className="logo-box">D</div>
                <h1 className="dracify-title">REGISTER BRIGADĂ</h1>
                <form onSubmit={handleSubmit}>
                    {message.text && (
                        <p className={message.type === 'error' ? 'error-text' : 'success-text'}>
                            {message.text}
                        </p>
                    )}
                    <div className="drac-input">
                        <UserIcon size={18} />
                        <input
                            type="text"
                            placeholder="Username"
                            required
                            onChange={e => setFormData({...formData, username: e.target.value})}
                        />
                    </div>
                    <div className="drac-input">
                        <Mail size={18} />
                        <input
                            type="email"
                            placeholder="Email"
                            required
                            onChange={e => setFormData({...formData, email: e.target.value})}
                        />
                    </div>
                    <div className="drac-input">
                        <Lock size={18} />
                        <input
                            type="password"
                            placeholder="Parolă"
                            required
                            onChange={e => setFormData({...formData, password: e.target.value})}
                        />
                    </div>
                    <button type="submit" className="drac-btn">CREEAZĂ CONT</button>
                </form>
                <p className="switch-text" onClick={onSwitchToLogin}>
                    Ai deja cont? <span className="text-red">Loghează-te aici.</span>
                </p>
            </div>
        </div>
    );
};

export default Register;