import React, { useState, useEffect } from 'react';
import api from '../api/axios';

const Player = ({ song }) => {
    const [audioUrl, setAudioUrl] = useState('');

    useEffect(() => {
        if (!song) return;

        // "Manevra cURL": Cerem piesa prin Axios cu tot cu token
        api.get(`/music/stream/${song.googleDriveId}`, {
            responseType: 'blob' // Important: cerem date brute (binary)
        })
            .then(response => {
                // Creăm un URL temporar în memoria browserului pentru acest fișier
                const url = URL.createObjectURL(response.data);
                setAudioUrl(url);
            })
            .catch(err => {
                console.error("Eroare la descărcare stream:", err);
                alert("N-am putut trage piesa. Vezi Network!");
            });

        // Curățăm memoria când se schimbă piesa
        return () => {
            if (audioUrl) URL.revokeObjectURL(audioUrl);
        };
    }, [song]);

    if (!song) return null;

    return (
        <div className="player-bar">
            <div className="player-info">
                <div className="logo-box mini shadow-glow">D</div>
                <div>
                    <p className="player-song-name">{song.name}</p>
                    <p className="player-tag">BLOB STREAMING ACTIVE</p>
                </div>
            </div>
            <div className="player-controls">
                {audioUrl ? (
                    <audio
                        key={audioUrl}
                        controls
                        autoPlay
                        src={audioUrl}
                        className="drac-audio"
                    />
                ) : (
                    <p>Se încarcă piesa...</p>
                )}
            </div>
        </div>
    );
};

export default Player;