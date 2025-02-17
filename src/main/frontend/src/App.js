import React from 'react';
import AppRoutes from './routes/AppRoutes';
import { UserProvider } from './context/UserContext'; // ✅ UserProvider 추가

function App() {
  return (
    <UserProvider>
      <AppRoutes />
    </UserProvider>
  );
}

export default App;
