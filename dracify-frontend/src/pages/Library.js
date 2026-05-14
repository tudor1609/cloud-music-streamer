import React, { useState, useEffect } from 'react';
import api from '../api/axios';
import { Search, Loader2 } from 'lucide-react';

const Library = ({ onSelectSong, currentSongId, onUpdatePlaylist }) => {
    const [songs, setSongs] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");

    const fetchSongs = async (q = "") => {
        const res = await api.get(q ? `/music/search?q=${q}` : '/music/songs');
        setSongs(res.data);
        if (!q) onUpdatePlaylist(res.data);
    };

    useEffect(() => {
        const delay = setTimeout(() => fetchSongs(searchTerm), 300);
        return () => clearTimeout(delay);
    }, [searchTerm]);

    return (
        <div className="page">
            <div className="top-nav">
                <div className="search-wrapper">
                    <Search size={18} color="#b3b3b3" />
                    <input
                        type="text"
                        placeholder="Ce vrei să asculți?"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            <div className="song-list">
                {songs.map((song, i) => (
                    <div key={song.id} className="song-item" onClick={() => onSelectSong(song)}>
                        <span style={{color: '#b3b3b3'}}>{i + 1}</span>
                        <div className="song-info">
                            <span className="song-title">{song.title || song.name}</span>
                            <span className="song-artist">{song.artist || "Necunoscut"}</span>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Library;