import React, { useState, useEffect } from 'react';
import Sidebar from './Sidebar';
import Player from './Player';
import api from '../api/axios';
import { UserPlus, Search } from 'lucide-react';

const Layout = ({ children, activeTab, setActiveTab, currentSong, songs, onSelectSong }) => {
    const [friendActivity, setFriendActivity] = useState([]);
    const [userSearch, setUserSearch] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    const fetchActivity = async () => {
        try {
            const res = await api.get('/auth/users/friends/activity');
            setFriendActivity(res.data);
        } catch (e) { console.log("Activity fetch failed"); }
    };

    const searchPeople = async (q) => {
        setUserSearch(q);
        if (q.length > 2) {
            const res = await api.get(`/auth/users/search?q=${q}`);
            setSearchResults(res.data);
        } else {
            setSearchResults([]);
        }
    };

    const addFriend = async (friendUsername) => {
        try {
            await api.post(`/auth/users/friends/add?friendUsername=${friendUsername}`);
            alert(`L-ai adăugat pe ${friendUsername}!`);
            setSearchResults([]);
            setUserSearch("");
            fetchActivity();
        } catch (e) { alert("Eroare la adăugare"); }
    };

    useEffect(() => {
        fetchActivity();
        const interval = setInterval(fetchActivity, 30000);
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="main-layout">
            <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
            <main className="content">{children}</main>

            <aside className="social-sidebar">
                <div className="add-friend-section">
                    <h4 style={{display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '15px'}}>
                        <UserPlus size={18}/> Găsește prieteni
                    </h4>
                    <div className="small-search">
                        <Search size={14} color="#b3b3b3" />
                        <input
                            type="text"
                            placeholder="Caută utilizatori..."
                            value={userSearch}
                            onChange={(e) => searchPeople(e.target.value)}
                        />
                    </div>
                    <div className="results">
                        {searchResults.map(u => (
                            <div key={u.id} className="user-result">
                                <span>{u.username}</span>
                                <button className="add-btn" onClick={() => addFriend(u.username)}>ADD</button>
                            </div>
                        ))}
                    </div>
                </div>

                <h3>Activitate Prieteni</h3>
                {friendActivity.map((act, i) => (
                    <div key={i} className="friend-card">
                        <span className="user">{act.username}</span>
                        <span className="status">
                            {act.currentSongTitle ? `🎧 ${act.currentSongTitle}` : 'Offline'}
                        </span>
                    </div>
                ))}
            </aside>
            <Player song={currentSong} songs={songs} onSelectSong={onSelectSong} />
        </div>
    );
};

export default Layout;