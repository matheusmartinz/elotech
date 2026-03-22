import React from 'react';
import { Box } from '@mui/material';
import { Outlet } from 'react-router-dom';
import SideMenu from '../components/SideMenu';

const Layout = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      <SideMenu />

      <Box sx={{ flexGrow: 1, p: 3 }}>
        <Outlet />
      </Box>
    </Box>
  );
};

export default Layout;
