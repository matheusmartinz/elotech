import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  TextField,
  Typography,
} from '@mui/material';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { useEffect, useRef, useState } from 'react';
import { maskData, maskISBN } from '../../utils/mask';
import { TEmprestimo } from './emprestimo.model';
import EmprestimoService from './emprestimo.service';
import UsuarioService from '../biblioteca/usuario.service';
import LivroService from '../livro/livro.service';
import { TLivro } from '../livro/livro.model';
import { TUsuario } from '../biblioteca/usuario.model';

const initialState = {
  emprestimos: [] as TEmprestimo[],
  livros: [] as TLivro[],
  usuarios: [] as TUsuario[],
  openDialog: false,
};

const Emprestimo = () => {
  const [stateLocal, setStateLocal] = useState(initialState);
  const emprestimoService = EmprestimoService();
  const usuarioService = UsuarioService();
  const livroService = LivroService();
  const firstRender = useRef<boolean>(true);

  const getEmprestimo = async () => {
    try {
      const { data } = await emprestimoService.getEmprestimo();
      setStateLocal((prev) => ({
        ...prev,
        emprestimos: data,
      }));
    } catch (error: any) {
      console.log(error);
    }
  };

  const getUsuario = async () => {
    try {
      const { data } = await usuarioService.getUsuarios();

      setStateLocal((prev) => ({
        ...prev,
        usuarios: data,
      }));
    } catch (err: any) {
      console.log(err);
    }
  };

  const getLivros = async () => {
    try {
      const { data } = await livroService.getLivro();

      setStateLocal((prev) => ({
        ...prev,
        livros: data,
      }));
    } catch (err: any) {
      console.log(err);
    }
  };

  useEffect(() => {
    if (firstRender.current) {
      getEmprestimo();
      getLivros();
      getUsuario();
    }
    firstRender.current = false;
  }, []);

  const onEditEmprestimo = (event: any, params: any) => {};

  const onExcluirEmprestimo = (event: any, params: any) => {};

  const onCadastroEmprestimo = () => {
    setStateLocal((prev) => ({
      ...prev,
      openDialog: true,
    }));
  };

  const onCloseDialog = () => {
    setStateLocal((prev) => ({
      ...prev,
      openDialog: false,
    }));
  };

  const columns: GridColDef<TEmprestimo>[] = [
    {
      field: 'titulo',
      headerName: 'Título',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
    },
    {
      field: 'autor',
      headerName: 'Autor',
      flex: 2,
      minWidth: 200,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
    },
    {
      field: 'isbn',
      headerName: 'ISBN',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
      renderCell: (params) => maskISBN(params.value),
    },
    {
      field: 'dataPublicacao',
      headerName: 'Data de publicação',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
      renderCell: (params) => maskData(params.value),
    },
    {
      field: 'categoria',
      headerName: 'Categoria',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
    },
    {
      field: 'acoes',
      headerName: '',
      flex: 0.5,
      minWidth: 120,
      align: 'center',
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
      renderCell: (params) => (
        <>
          <IconButton>
            <ModeEditIcon sx={{ marginRight: '5px' }} />
          </IconButton>
          <IconButton>
            <DeleteIcon />
          </IconButton>
        </>
      ),
    },
  ];

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '85vh' }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between',
          margin: 2,
        }}
      >
        <Typography variant="h4">Empréstimos</Typography>

        <Button variant="contained" color="primary" sx={{ borderRadius: '20px' }} onClick={onCadastroEmprestimo}>
          Novo
        </Button>
      </Box>

      <Box sx={{ flex: 1, width: '100%' }}>
        <DataGrid
          getRowId={(row) => row.uuid}
          rows={stateLocal.emprestimos}
          columns={columns}
          initialState={{
            pagination: {
              paginationModel: {
                pageSize: 5,
              },
            },
          }}
          pageSizeOptions={[5]}
          disableRowSelectionOnClick
          checkboxSelection={false}
          sx={{
            '& .MuiDataGrid-cell': {
              whiteSpace: 'normal',
              outline: 'none',
            },
            '& .MuiDataGrid-columnHeader': {
              textOverflow: 'ellipsis',
              overflow: 'hidden',
            },
            '& .MuiDataGrid-cell:focus': {
              outline: 'none',
            },
            '& .MuiDataGrid-columnHeader:focus': {
              outline: 'none',
            },
          }}
          localeText={{
            noRowsLabel: 'Sem dados',
          }}
        />
      </Box>

      <Dialog open={stateLocal.openDialog} onClose={onCloseDialog}>
        <DialogTitle>Subscribe</DialogTitle>
        <DialogContent>
          <DialogContentText>
            To subscribe to this website, please enter your email address here. We will send updates occasionally.
          </DialogContentText>
          <form onSubmit={() => {}} id="subscription-form">
            <TextField
              autoFocus
              required
              margin="dense"
              id="name"
              name="email"
              label="Email Address"
              type="email"
              fullWidth
              variant="standard"
            />
          </form>
        </DialogContent>
        <DialogActions>
          <Button onClick={onCloseDialog}>Cancel</Button>
          <Button type="submit" form="subscription-form">
            Subscribe
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default Emprestimo;
