import { Link } from 'react-router-dom'

const NavLink = () => {
    return(
        <nav className='mt-3 border-bottom mb-3'>
         <ul>
            <li><Link to="/rooms/api">Room</Link></li>
         </ul>
        </nav>
    );
}

export default NavLink;