import React, { useState, useEffect } from 'react';
import api from '../api/axios';

const Player = ({ song, songs, onSelectSong }) => {
    const [audioUrl, setAudioUrl] = useState('');

    const handleNext = () => {
        const idx = songs.findIndex(s => s.id === song.id);
        if (idx !== -1 && idx < songs.length - 1) onSelectSong(songs[idx + 1]);
    };

    useEffect(() => {
        if (!song) return;

        // Sync Activitate către prieteni
        api.post('/auth/users/me/activity', null, {
            params: { title: song.title || song.name, artist: song.artist || "Necunoscut" }
        }).catch(e => console.log("Sync activity failed"));

        // Stream
        api.get(`/music/stream/${song.googleDriveId}`, { responseType: 'blob' })
            .then(res => setAudioUrl(URL.createObjectURL(res.data)));

        return () => { if (audioUrl) URL.revokeObjectURL(audioUrl); };
    }, [song]);

    if (!song) return null;

    return (
        <div className="player-bar">
            <div className="player-info">
                <img
                    src={`https://gateway-production-529a.up.railway.app/music/songs/${song.id}/album-art`}
                    alt=""
                    onError={(e) => e.target.src = 'default-art.png'}
                />
                <div className="meta">
                    <p className="title">{song.title || song.name}</p>
                    <p className="artist">{song.artist || "Necunoscut"}</p>
                </div>
            </div>
            <audio key={audioUrl} controls autoPlay src={audioUrl} onEnded={handleNext} className="drac-audio" />
        </div>
    );
};

export default Player;