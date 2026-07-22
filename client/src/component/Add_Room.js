import React, { useState } from "react";
import axios from 'axios';

const apiServer = process.env.REACT_APP_API_SERVER;

const AddRoom = ({ fetchRoom }) => {
    const [formData, setFormData] = useState({
        name: '',
        roomNumber: '',
        bedInfo: ''
    });

    const [messages, setMessages] = useState('');

    const handleChange = (e) => {
        const { id, value } = e.target;
        setFormData({
            ...formData,
            [id]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try{
            const response = await axios.post(`${apiServer}/rooms/api/add`, formData);
            // handle success response
            setMessages(`<font color="green">Room added Successfully: ${response.data.name}</font>` );
            setFormData({ name: '', roomNumber: '', bedInfo: '' });
            fetchRoom(); // Refresh the room list
        } catch (error) {
            setMessages(`<font color="red">Error adding room: ${error}</font>`);
        }
    };

    return(
        <div className="container mb-2">
        <div className="row mt-2 border-bottom font-weight-bold">
           <div className="col-md-4">Add Room</div>
           <div className="col-md-8" dangerouslySetInnerHTML={{ __html: messages }}></div> 
        </div>
        <form id="addRoomForm" onSubmit = {handleSubmit}>
        <div className="form-row">
                    <div className="form-group col-md-4">
                        <label htmlFor="name">Room Name:</label>
                        <input type="text" className="form-control" id="name" name="name" value={formData.name} onChange={handleChange} />
                    </div>
                    <div className="form-group col-md-3">
                        <label htmlFor="roomNumber">Room Number:</label>
                        <input type="text" className="form-control" id="roomNumber" name="roomNumber" value={formData.roomNumber} onChange={handleChange} />
                    </div>
                    <div className="form-group col-md-3">
                        <label htmlFor="bedInfo">Bed Info:</label>
                        <input type="text" className="form-control" id="bedInfo" name="bedInfo" value={formData.bedInfo} onChange={handleChange} />
                    </div>
                    <div className="form-group col-md-2">
                        <label>&nbsp;</label>
                        <button type="submit" className="btn btn-primary btn-block">Add Room</button>
                    </div>
                </div>
        </form>
    </div>
    );


};

export default AddRoom;