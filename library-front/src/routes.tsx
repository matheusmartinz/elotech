import { createBrowserRouter, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import Usuario from './screens/biblioteca/Usuario';
import Livro from './screens/livro/Livro';
import Emprestimo from './screens/emprestimo/Emprestimo';

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [{ index: true, element: <Navigate to="/biblioteca" replace /> }],
  },
  {
    path: '/biblioteca',
    element: <Layout />,
    children: [
      { index: true, element: <Usuario /> },
      { path: 'usuario', element: <Usuario /> },
      { path: 'livro', element: <Livro /> },
      { path: 'emprestimo', element: <Emprestimo /> },
    ],
  },
]);
