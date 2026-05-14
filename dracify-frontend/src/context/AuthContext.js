import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem('dracify_token'));
    const [user, setUser] = useState(JSON.parse(localStorage.getItem('dracify_user')));

    const login = (newToken, userData) => {
        localStorage.setItem('dracify_token', newToken);
        localStorage.setItem('dracify_user', JSON.stringify(userData)); // Salvăm și datele userului
        setToken(newToken);
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem('dracify_token');
        localStorage.removeItem('dracify_user');
        setToken(null);
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ token, user, login, logout, isLoggedIn: !!token }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);