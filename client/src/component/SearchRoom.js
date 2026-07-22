import React, { useState, useEffect, useCallback } from "react";
import DownloadButton from "./ExportButtons";

const SearchRooms = ({ roomList, onSearch }) => {
    const [searchInput, setSearchInput] = useState(''); // State to keep track of the search input value
    const [query, setQuery] = useState('');

    const handleSearch = useCallback(() => {
        const filtered = roomList.filter(room =>
            room.name.toLowerCase().includes(query.toLowerCase())
        );
        onSearch(filtered);
    }, [roomList, query, onSearch]);

    useEffect(() => {
        handleSearch();
    }, [query]);

    const handleInputChange = (event) => {
        const value = event.target.value;
        setSearchInput(value); // Update the search input value
        setQuery(value); // Update the query only when the user types
    };

    return (
        <div className="container mb-1">
            <div className="row">
                <div className="col-md-6 form-group">
                   <input className="form-control" type="text" placeholder="Search rooms" value={searchInput} onChange={handleInputChange} />
                </div>
                <DownloadButton />
            </div>
        </div>
    );
};

export default SearchRooms;
