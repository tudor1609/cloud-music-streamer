import React from 'react';
import { Play, Music } from 'lucide-react';

const SongRow = ({ song, index, isActive, onClick }) => {
    return (
        <div className={`song-row ${isActive ? 'active' : ''}`} onClick={onClick}>
            <span className="row-index">{index}</span>
            <div className="row-content">
                <Music size={16} className={isActive ? 'text-red' : ''} />
                <span className="song-title">{song.name}</span>
            </div>
            <Play size={18} fill={isActive ? "#ff0000" : "transparent"} className="row-play" />
        </div>
    );
};

export default SongRow;