import React, { useState, useEffect } from 'react';
import api from '../api/axios';
import SongRow from '../components/SongRow';
import { Loader2 } from 'lucide-react';

const Library = ({ onSelectSong, currentSongId }) => {
    const [songs, setSongs] = useState([]);
    const [isScanning, setIsScanning] = useState(false);

    // 1. Definim funcția de adus piese (ca să poată fi apelată de useEffect)
    const fetchSongs = async () => {
        try {
            const res = await api.get('/music/songs');
            setSongs(res.data);
        } catch (err) {
            console.error("Eroare la încărcarea pieselor:", err);
        }
    };

    // 2. Hook-ul de inițializare (Scanare + Fetch)
    useEffect(() => {
        const initLibrary = async () => {
            setIsScanning(true);
            const folderId = '1mCA8YqjfG_60vPQ1OIpKalznPWD0iiAM';

            try {
                // Încercăm cu GET prima dată (căutăm în params)
                await api.get(`/music/scan`, {
                    params: { folderId: folderId }
                });
                console.log("Scanare pornită cu succes (GET)");
            } catch (err) {
                // Dacă dă 405, încercăm cu POST (căutăm în query string)
                if (err.response?.status === 405) {
                    try {
                        await api.post(`/music/scan?folderId=${folderId}`);
                        console.log("Scanare pornită cu succes (POST)");
                    } catch (postErr) {
                        console.error("Nicio metodă nu e acceptată!");
                    }
                }
            } finally {
                fetchSongs();
                setIsScanning(false);
            }
        };

        initLibrary();
    }, []); // Se execută o singură dată la montare

    return (
        <div className="page">
            <header className="page-header">
                <h1>ARHIVA MUZICALĂ</h1>
                {isScanning && (
                    <div className="scanning-tag">
                        <Loader2 size={14} className="spin" />
                        <span>Sincronizare Drive...</span>
                    </div>
                )}
            </header>

            <div className="song-list">
                {songs.length > 0 ? (
                    songs.map((song, i) => (
                        <SongRow
                            key={song.id || i}
                            index={i + 1}
                            song={song}
                            isActive={currentSongId === song.id}
                            onClick={() => onSelectSong(song)}
                        />
                    ))
                ) : (
                    <div className="empty-state">
                        {!isScanning && <p>Nu s-au găsit piese. Verifică folderul de Drive!</p>}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Library;