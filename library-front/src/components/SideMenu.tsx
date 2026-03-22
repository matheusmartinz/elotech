import { Box, Drawer, List, ListItemButton, ListItemText, Typography } from '@mui/material';
import { Link, useLocation } from 'react-router-dom';

const drawerWidth = 180;

const menuItems = [
  { label: 'Usuários', path: '/' },
  { label: 'Livros', path: '/biblioteca/livro' },
  { label: 'Empréstimos', path: '/biblioteca/emprestimo' },
];

const SideMenu = () => {
  const location = useLocation();

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          bgcolor: '#1E5F86',
          color: '#fff',
          border: 'none',
        },
      }}
    >
      <Box sx={{ p: 3 }}>
        <Typography variant="h6" fontWeight="bold">
          Library System
        </Typography>
      </Box>

      <List>
        {menuItems.map((item, index) => {
          const active = location.pathname === item.path;

          return (
            <ListItemButton
              key={index}
              component={Link}
              to={item.path}
              sx={{
                px: 3,
                py: 1.5,
                bgcolor: active ? '#2E7DA6' : 'transparent',
                '&:hover': {
                  bgcolor: '#2E7DA6',
                },
              }}
            >
              <ListItemText primary={item.label} />
            </ListItemButton>
          );
        })}
      </List>
    </Drawer>
  );
};

export default SideMenu;
