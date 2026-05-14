import React, { useState } from 'react';
import api from '../api/axios';
import { useAuth } from '../context/AuthContext';
import { Lock, User as UserIcon } from 'lucide-react';

const Login = ({ onSwitchToRegister }) => {
    const { login } = useAuth();
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const res = await api.post('/auth/login', credentials);

            // LOG DE DEBUG: Vedem exact ce trimite Java
            console.log("Răspuns Backend:", res.data);

            // Verificăm unde e token-ul și username-ul în răspunsul tău
            const token = res.data.token || res.data.accessToken;
            const username = res.data.username || credentials.username;
            const email = res.data.email || 'borfas@dracify.ro';

            if (token) {
                // Trimitem ambele argumente către context
                login(token, { username, email });
            } else {
                setError("Eroare: Backend-ul nu a trimis token-ul!");
            }
        } catch (err) {
            console.error("Eroare Login:", err);
            setError(err.response?.data?.message || 'Serverul Dracify nu răspunde sau date greșite.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-screen">
            <div className="login-card shadow-glow">
                <div className="logo-box">D</div>
                <h1 className="dracify-title">DRACIFY ACCESS</h1>
                <form onSubmit={handleSubmit}>
                    {error && <p className="error-text">{error}</p>}

                    <div className="drac-input">
                        <UserIcon size={18} />
                        <input
                            type="text"
                            placeholder="Username"
                            value={credentials.username}
                            onChange={e => setCredentials({...credentials, username: e.target.value})}
                        />
                    </div>
                    <div className="drac-input">
                        <Lock size={18} />
                        <input
                            type="password"
                            placeholder="Parolă"
                            value={credentials.password}
                            onChange={e => setCredentials({...credentials, password: e.target.value})}
                        />
                    </div>

                    <button type="submit" className="drac-btn" disabled={loading}>
                        {loading ? 'SE VERIFICĂ...' : 'INTRĂ ÎN BRIGADĂ'}
                    </button>
                </form>

                <p className="switch-text" onClick={onSwitchToRegister}>
                    Nu ai cont? <span className="text-red">Înregistrează-te.</span>
                </p>
            </div>
        </div>
    );
};

export default Login;