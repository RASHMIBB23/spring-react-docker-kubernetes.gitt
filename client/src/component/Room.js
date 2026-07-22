import { useState, useEffect } from 'react';
import axios from 'axios';
import AddRoom from './Add_Room';
import SearchRooms from './SearchRoom';
import { LogoutButton } from '../auth/LogoutButton';

import { useLocation } from 'react-router-dom';

const apiServer = process.env.REACT_APP_API_SERVER;

const RoomData = () => {
    const {userEmail, userRole } = useLocation().state || {}
    console.log("users info: "+userEmail +" role: "+ userRole);
    const [roomList, setRoomList] = useState([]);
    const [filteredRoomList, setFilteredRoomList] = useState([]);
    const [editRoomId, setEditRoomId] = useState(null);
    const [formData, setFormData] = useState({
        id: '',
        name: '',
        roomNumber: '',
        bedInfo: ''
    });

    const [messages, setMessages] = useState('');

    const [sortConfig, setSortConfig] = useState({ key: 'name', direction: 'asc' });

    const isUserRoleUser = userRole.includes('USER');

    useEffect(() => {
        fetchRoom();
    }, []);

    const fetchRoom = async () => {
        try {
            const response = await axios.get(`${apiServer}/rooms/api/getAll`);
            setRoomList(response.data);
            setFilteredRoomList(response.data); // Set filtered list initially
        } catch (error) {
            console.error('Error fetching rooms:', error);
        }
    };

    const handleEditClick = (room) => {
        setEditRoomId(room.id);
        setFormData({
            id: room.id,
            name: room.name,
            roomNumber: room.roomNumber,
            bedInfo: room.bedInfo
        });
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleCancelClick = () => {
        setEditRoomId(null);
        setFormData({ id: '', name: '', roomNumber: '', bedInfo: '' });
        setMessages('');
    };

    const handleSaveClick = async () => {
        try {
            await axios.put(`${apiServer}/rooms/api/${formData.id}`, formData);
            fetchRoom();
            setEditRoomId(null);
            setMessages(`<font color="green">Record is updated Successfully!</font>`);
        } catch (error) {
            setMessages(`<font color="red">Error saving room: ${error}</font>`);
        }
    };

    const handleDeleteClick = async (roomId) => {
        try {
            await axios.delete(`${apiServer}/rooms/api/${roomId}`);
            setMessages(`<font color="green">Record is Deleted Successfully!</font>`);
            fetchRoom();
        } catch (error) {
            setMessages(`<font color="red">Error saving room: ${error}</font>`);
        }
    };

    const sortRooms = (key) => {
        let direction = "asc";
        if (sortConfig.key === key && sortConfig.direction === 'asc') {
            direction = 'desc';
        }
        setSortConfig({ key, direction });

        const sortedRooms = [...filteredRoomList].sort((a, b) => {
            if (a[key] < b[key]) {
                return direction === 'asc' ? -1 : 1;
            }
            if (a[key] > b[key]) {
                return direction === 'asc' ? 1 : -1;
            }
            return 0;
        });
        setFilteredRoomList(sortedRooms);
    };

    const getSortArrow = (key) => {
        if (sortConfig.key === key) {
            return sortConfig.direction === 'asc' ? ' ▲' : ' ▼';
        }
        return '';
    };

    const handleSearch = (filteredRooms) => {
        setFilteredRoomList(filteredRooms);
    };

    return (
        <>
            <div className="row">
                <div className="col-md-6">{userEmail && <div>{userEmail}</div> }</div>
            </div>
            <LogoutButton />
            <AddRoom fetchRoom={fetchRoom} />

            <div className="container">
                <div className="row border-bottom mb-3">
                    <div className="col-md-8">Explore Room Data: Fetched dynamically using ReactJS and RESTful API integration</div>
                    <div className="col-md-4" dangerouslySetInnerHTML={{ __html: messages }}></div>
                </div>
                <SearchRooms roomList={roomList} onSearch={handleSearch} />
                <div className="table-container">
                    <table className="table">
                        <thead className="sticky-header">
                            <tr>
                                <th role="button" title="Click here to sort data" onClick={() => sortRooms('name')}>Name{getSortArrow('name')}</th>
                                <th role="button" title="Click here to sort data" onClick={() => sortRooms('roomNumber')}>Room Number{getSortArrow('roomNumber')}</th>
                                <th role="button" title="Click here to sort data" onClick={() => sortRooms('bedInfo')}>Bed Info{getSortArrow('bedInfo')}</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredRoomList.map(room => (
                                <tr key={room.id}>
                                    {editRoomId === room.id ? (
                                        <>
                                            <td><input type="text" className="form-control" name="name" value={formData.name} onChange={handleInputChange} /></td>
                                            <td>{room.roomNumber}</td>
                                            <td><input type="text" className="form-control" name="bedInfo" value={formData.bedInfo} onChange={handleInputChange} /></td>
                                            <td>
                                                <button className="btn btn-success btn-sm" onClick={handleSaveClick}>Save</button>
                                                <button className="btn btn-warning btn-sm ml-2" onClick={handleCancelClick}>Cancel</button>
                                                <button className="btn btn-danger btn-sm ml-2" onClick={() => handleDeleteClick(room.id)}>Delete</button>
                                            </td>
                                        </>
                                    ) : (
                                        <>
                                            <td 
                                              className={isUserRoleUser ? '' : 'editRoomBtn'} 
                                              title={isUserRoleUser ? 'Your Role is User and you can\'t edit this row' : "Click here to update/delete this record"} 
                                              onClick={isUserRoleUser ? null : () => handleEditClick(room)}>
                                                {room.name}
                                            </td>
                                            <td>{room.roomNumber}</td>
                                            <td
                                              className={isUserRoleUser ? '' : 'editRoomBtn'} 
                                              title={isUserRoleUser ? 'Your Role is User and you can\'t edit this row' : "Click here to update/delete this record"} 
                                              onClick={isUserRoleUser ? null : () => handleEditClick(room)}>
                                                {room.bedInfo}
                                            </td>
                                            <td></td>
                                        </>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
                <div className="row mt-3"><div className="col-md-12 text-center font-weight-bold">Number of records: {roomList.length}</div></div>
            </div>
        </>
    );
}

export default RoomData;
