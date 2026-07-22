import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

export const PrivateRoute = () => {
  const isAuthenticated = !!localStorage.getItem('authToken'); // Example check, use your actual authentication logic

  return isAuthenticated ? <Outlet /> : <Navigate to="/login" />;
};
