import React from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './routes.js';
import SideMenu from './components/SideMenu.js';

createRoot(document.getElementById('root') as HTMLElement).render(
  <React.StrictMode>
    <SideMenu />
    <RouterProvider router={router} />
  </React.StrictMode>,
);
