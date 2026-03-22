import DeleteIcon from '@mui/icons-material/Delete';
import ModeEditIcon from '@mui/icons-material/ModeEdit';
import {
  Alert,
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
  Snackbar,
  TextField,
  Typography,
} from '@mui/material';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs, { Dayjs } from 'dayjs';
import { useCallback, useEffect, useRef, useState } from 'react';
import { maskData, maskTelefone } from '../../utils/mask';
import { TUsuario } from './usuario.model';
import UsuarioService from './usuario.service';

const initialState = {
  usuarios: [] as TUsuario[],
  openDialog: false,
  usuarioDTO: {
    uuid: null as string | null,
    nome: '',
    email: '',
    telefone: '',
    dataCadastro: '',
  } as TUsuario,
  isEdit: false,
  snackBar: {
    severity: 'success' as 'success' | 'error' | 'info',
    message: '',
    open: false,
  },
};

const Usuario = () => {
  const [stateLocal, setStateLocal] = useState(initialState);
  const usuarioService = UsuarioService();
  const firstRender = useRef<boolean>(true);

  const columns: GridColDef<TUsuario>[] = [
    {
      field: 'nome',
      headerName: 'Nome',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
    },
    {
      field: 'email',
      headerName: 'Email',
      flex: 2,
      minWidth: 200,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
    },
    {
      field: 'telefone',
      headerName: 'Telefone',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
      renderCell: (params) => maskTelefone(params.value),
    },
    {
      field: 'dataCadastro',
      headerName: 'Data de cadastro',
      flex: 1,
      minWidth: 150,
      editable: false,
      sortable: false,
      disableColumnMenu: true,
      resizable: true,
      renderCell: (params) => maskData(params.value),
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
          <IconButton onClick={(event) => onEditUsuario(event, params.row)}>
            <ModeEditIcon sx={{ marginRight: '5px' }} />
          </IconButton>
          <IconButton onClick={(event) => onExcluirUsuario(event, params.row)}>
            <DeleteIcon />
          </IconButton>
        </>
      ),
    },
  ];

  const getUsuarios = async () => {
    try {
      const { data } = await usuarioService.getUsuarios();
      if (data.length > 0) {
        setStateLocal((prev) => ({
          ...prev,
          usuarios: data,
        }));
      }
    } catch (error: any) {
      console.log(error);
    }
  };

  const updateUsuario = async (usuarioDTO: TUsuario, uuid: string) => {
    const usuarioParaEnviar: TUsuario = {
      ...stateLocal.usuarioDTO,
      dataCadastro: stateLocal.usuarioDTO.dataCadastro
        ? dayjs(stateLocal.usuarioDTO.dataCadastro, 'DD/MM/YYYY').format('YYYY-MM-DD')
        : '',
    };

    if (stateLocal.usuarioDTO.telefone.length > 11) {
      setStateLocal((prev) => ({
        ...prev,
        snackBar: {
          ...prev.snackBar,
          open: true,
          message: 'Telefone inválido.',
          severity: 'error',
        },
      }));
    } else {
      try {
        const { data } = await usuarioService.putUsuario(usuarioParaEnviar, uuid);
        if (data) {
          setStateLocal((prev) => ({
            ...prev,
            openDialog: false,
            snackBar: {
              ...prev.snackBar,
              open: true,
              message: 'Usuário atualizado.',
              severity: 'success',
            },
          }));
          getUsuarios();
        }
      } catch (err: unknown) {
        console.log(err);
      }
    }
  };

  const postUsuario = async () => {
    const usuarioParaEnviar: TUsuario = {
      ...stateLocal.usuarioDTO,
      dataCadastro: stateLocal.usuarioDTO.dataCadastro
        ? dayjs(stateLocal.usuarioDTO.dataCadastro, 'DD/MM/YYYY').format('YYYY-MM-DD')
        : '',
    };

    if (stateLocal.usuarioDTO.telefone.length > 11) {
      setStateLocal((prev) => ({
        ...prev,
        snackBar: {
          ...prev.snackBar,
          open: true,
          message: 'Telefone inválido.',
          severity: 'error',
        },
      }));
    } else {
      try {
        const { data } = await usuarioService.postUsuario(usuarioParaEnviar);
        if (data) {
          setStateLocal((prev) => ({
            ...prev,
            openDialog: false,
          }));
          getUsuarios();
        }
      } catch (err: unknown) {
        console.log(err);
      }
    }
  };

  useEffect(() => {
    if (firstRender.current) {
      getUsuarios();
    }
    firstRender.current = false;
  }, []);

  const resetUsuario = useCallback(() => {
    setStateLocal((prev) => ({
      ...prev,
      usuarioDTO: {
        ...prev.usuarioDTO,
        uuid: '',
        dataCadastro: '',
        nome: '',
        email: '',
        telefone: '',
      },
    }));
  }, []);

  const onEditUsuario = useCallback((event: React.MouseEvent<HTMLButtonElement>, usuario: TUsuario) => {
    event.stopPropagation();
    setStateLocal((prevState) => ({
      ...prevState,
      openDialog: true,
      isEdit: true,
      usuarioDTO: { ...usuario },
    }));
  }, []);

  const onExcluirUsuario = async (event: React.MouseEvent<HTMLButtonElement, MouseEvent>, params: TUsuario) => {
    try {
      const { data } = await usuarioService.deleteUsuario(params.uuid);

      if (data) {
        setStateLocal((prev) => ({
          ...prev,
          snackBar: {
            ...prev.snackBar,
            open: true,
            message: data,
            severity: 'success',
          },
        }));
        getUsuarios();
      }
    } catch (error) {
      console.log(error);
    }
  };

  const onCadastroUsuario = useCallback(() => {
    setStateLocal((prev) => ({
      ...prev,
      openDialog: true,
      isEdit: false,
    }));
  }, []);

  const onCloseDialog = useCallback(() => {
    setStateLocal((prev) => ({
      ...prev,
      openDialog: false,
      isEdit: false,
    }));
    resetUsuario();
  }, []);

  const onChangeNome = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    setStateLocal((prev) => ({
      ...prev,
      usuarioDTO: {
        ...prev.usuarioDTO,
        nome: event.target.value,
      },
    }));
  }, []);

  const onChangeEmail = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    setStateLocal((prev) => ({
      ...prev,
      usuarioDTO: {
        ...prev.usuarioDTO,
        email: event.target.value,
      },
    }));
  }, []);

  const onChangeTelefone = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    setStateLocal((prev) => ({
      ...prev,
      usuarioDTO: {
        ...prev.usuarioDTO,
        telefone: event.target.value,
      },
    }));
  }, []);

  const onChangeDataCadastro = (newValue: Dayjs | null) => {
    setStateLocal((prev) => ({
      ...prev,
      usuarioDTO: {
        ...prev.usuarioDTO,
        dataCadastro: newValue ? newValue.format('DD/MM/YYYY') : '',
      },
    }));
  };

  const onCloseSnackBar = () => {
    setStateLocal((prev) => ({
      ...prev,
      snackBar: {
        ...prev.snackBar,
        open: false,
      },
    }));
  };

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
        <Typography variant="h4">Usuários</Typography>

        <Button variant="contained" sx={{ borderRadius: '20px' }} onClick={onCadastroUsuario}>
          Novo
        </Button>
      </Box>

      <Box sx={{ flex: 1, width: '100%' }}>
        <DataGrid
          getRowId={(row) => row.uuid}
          rows={stateLocal.usuarios}
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
        <DialogTitle>{stateLocal.isEdit ? 'Atualizar Usuário' : 'Cadastrar Usuário'}</DialogTitle>
        <DialogContent>
          <DialogContentText>Preencha os dados abaixo para cadastrar um novo usuário.</DialogContentText>

          <form
            id="usuario-form"
            onSubmit={(event) => {
              event.preventDefault();
              if (stateLocal.isEdit && stateLocal.usuarioDTO.uuid) {
                updateUsuario(stateLocal.usuarioDTO, stateLocal.usuarioDTO.uuid);
              } else {
                postUsuario();
              }
            }}
          >
            <TextField
              autoFocus
              margin="dense"
              name="nome"
              label="Nome"
              value={stateLocal.usuarioDTO.nome}
              onChange={onChangeNome}
              fullWidth
              required
            />
            <TextField
              margin="dense"
              name="email"
              label="Email"
              type="email"
              value={stateLocal.usuarioDTO.email}
              onChange={onChangeEmail}
              fullWidth
              required
            />
            <TextField
              margin="dense"
              name="telefone"
              label="Telefone"
              value={stateLocal.usuarioDTO.telefone}
              onChange={onChangeTelefone}
              type="number"
              fullWidth
              required
            />

            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="pt-br">
              <DatePicker
                label="Data de Cadastro"
                value={
                  stateLocal.usuarioDTO.dataCadastro ? dayjs(stateLocal.usuarioDTO.dataCadastro, 'DD/MM/YYYY') : null
                }
                onChange={onChangeDataCadastro}
                slotProps={{
                  textField: {
                    fullWidth: true,
                    margin: 'dense',
                    required: true,
                  },
                }}
                format="DD/MM/YYYY"
              />
            </LocalizationProvider>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={onCloseDialog}>Cancelar</Button>
          <Button type="submit" form="usuario-form" variant="contained" sx={{ borderRadius: '20px' }}>
            Salvar
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        open={stateLocal.snackBar.open}
        onClose={onCloseSnackBar}
        message={stateLocal.snackBar.message}
        autoHideDuration={4000}
      >
        <Alert onClose={onCloseSnackBar} severity={stateLocal.snackBar.severity}>
          {stateLocal.snackBar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Usuario;
