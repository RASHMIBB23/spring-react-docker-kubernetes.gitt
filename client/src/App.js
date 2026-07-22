import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import RoomData from "./component/Room";
import { PrivateRoute } from "./auth/PrivateRoute";
import './App.css';
import { LoginPage } from "./component/LoginPage";
import { SignUpPage } from "./component/SignUp";

function App() {
  return (
    <Router>
      
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/sign-up" element={<SignUpPage />} />
        <Route path="/rooms" element={<PrivateRoute />}>
          <Route path="api" element={<RoomData />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
