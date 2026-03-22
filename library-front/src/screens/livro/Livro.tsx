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
import { maskData, maskISBN } from '../../utils/mask';
import { TLivro } from './livro.model';
import LivroService from './livro.service';

const initialState = {
  livros: [] as TLivro[],
  openDialog: false,
  livroDTO: {
    uuid: null as string | null,
    titulo: '',
    autor: '',
    isbn: '',
    dataPublicacao: '',
    categoria: '',
  } as TLivro,
  isEdit: false,
  snackBar: {
    severity: 'success' as 'success' | 'error' | 'info',
    message: '',
    open: false,
  },
};

const Livro = () => {
  const [stateLocal, setStateLocal] = useState(initialState);
  const livroService = LivroService();
  const firstRender = useRef<boolean>(true);

  const columns: GridColDef<TLivro>[] = [
    {
      field: 'titulo',
      headerName: 'Título',
      flex: 1,
      minWidth: 150,
    },
    {
      field: 'autor',
      headerName: 'Autor',
      flex: 2,
      minWidth: 200,
    },
    {
      field: 'isbn',
      headerName: 'ISBN',
      flex: 1,
      minWidth: 150,
      renderCell: (params) => maskISBN(params.value),
    },
    {
      field: 'dataPublicacao',
      headerName: 'Data de publicação',
      flex: 1,
      minWidth: 150,
      renderCell: (params) => maskData(params.value),
    },
    {
      field: 'categoria',
      headerName: 'Categoria',
      flex: 1,
      minWidth: 150,
    },
    {
      field: 'acoes',
      headerName: '',
      flex: 0.5,
      minWidth: 120,
      align: 'center',
      renderCell: (params) => (
        <>
          <IconButton onClick={(event) => onEditLivro(event, params.row)}>
            <ModeEditIcon sx={{ marginRight: '5px' }} />
          </IconButton>
          <IconButton onClick={(event) => onExcluirLivro(event, params.row)}>
            <DeleteIcon />
          </IconButton>
        </>
      ),
    },
  ];

  const getLivros = async () => {
    try {
      const { data } = await livroService.getLivro();
      setStateLocal((prev) => ({
        ...prev,
        livros: data,
      }));
    } catch (error) {
      console.log(error);
    }
  };

  const resetLivro = useCallback(() => {
    setStateLocal((prev) => ({
      ...prev,
      livroDTO: {
        uuid: '',
        titulo: '',
        autor: '',
        isbn: '',
        dataPublicacao: '',
        categoria: '',
      },
    }));
  }, []);

  const postLivro = async () => {
    const livroParaEnviar: TLivro = {
      ...stateLocal.livroDTO,
      dataPublicacao: stateLocal.livroDTO.dataPublicacao
        ? dayjs(stateLocal.livroDTO.dataPublicacao, 'DD/MM/YYYY').format('YYYY-MM-DD')
        : '',
    };

    try {
      const { data } = await livroService.postLivro(livroParaEnviar);
      if (data) {
        setStateLocal((prev) => ({
          ...prev,
          openDialog: false,
          snackBar: {
            ...prev.snackBar,
            open: true,
            message: 'Livro cadastrado!',
            severity: 'success',
          },
        }));
        getLivros();
      }
    } catch (error) {
      console.log(error);
      setStateLocal((prev) => ({
        ...prev,
        snackBar: {
          ...prev.snackBar,
          open: true,
          message: 'Erro ao cadastrar livro.',
          severity: 'error',
        },
      }));
    }
  };

  const updateLivro = async (livroDTO: TLivro, uuid: string) => {
    const livroParaEnviar: TLivro = {
      ...livroDTO,
      dataPublicacao: livroDTO.dataPublicacao ? dayjs(livroDTO.dataPublicacao, 'DD/MM/YYYY').format('YYYY-MM-DD') : '',
    };

    try {
      const { data } = await livroService.putLivro(livroParaEnviar, uuid);
      if (data) {
        setStateLocal((prev) => ({
          ...prev,
          openDialog: false,
          snackBar: {
            ...prev.snackBar,
            open: true,
            message: 'Livro atualizado!',
            severity: 'success',
          },
        }));
        getLivros();
      }
    } catch (error) {
      console.log(error);
      setStateLocal((prev) => ({
        ...prev,
        snackBar: {
          ...prev.snackBar,
          open: true,
          message: 'Erro ao atualizar livro.',
          severity: 'error',
        },
      }));
    }
  };

  const deleteLivro = async (uuid: string) => {
    try {
      const { data } = await livroService.deleteLivro(uuid);
      if (data) {
        setStateLocal((prev) => ({
          ...prev,
          snackBar: {
            ...prev.snackBar,
            open: true,
            message: 'Livro deletado!',
            severity: 'success',
          },
        }));
        getLivros();
      }
    } catch (error) {
      console.log(error);
      setStateLocal((prev) => ({
        ...prev,
        snackBar: {
          ...prev.snackBar,
          open: true,
          message: 'Erro ao deletar livro.',
          severity: 'error',
        },
      }));
    }
  };

  useEffect(() => {
    if (firstRender.current) {
      getLivros();
    }
    firstRender.current = false;
  }, []);

  const onEditLivro = useCallback((event: any, livro: TLivro) => {
    event.stopPropagation();
    setStateLocal((prev) => ({
      ...prev,
      openDialog: true,
      isEdit: true,
      livroDTO: { ...livro },
    }));
  }, []);

  const onExcluirLivro = useCallback((event: any, livro: TLivro) => {
    event.stopPropagation();
    if (livro.uuid) deleteLivro(livro.uuid);
  }, []);

  const onCadastroLivro = useCallback(() => {
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
    resetLivro();
  }, []);

  const onChangeField = (field: keyof TLivro, value: string) => {
    setStateLocal((prev) => ({
      ...prev,
      livroDTO: {
        ...prev.livroDTO,
        [field]: value,
      },
    }));
  };

  const onChangeDataPublicacao = (newValue: Dayjs | null) => {
    onChangeField('dataPublicacao', newValue ? newValue.format('DD/MM/YYYY') : '');
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
        sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', margin: 2 }}
      >
        <Typography variant="h4">Livros</Typography>
        <Button variant="contained" sx={{ borderRadius: '20px' }} onClick={onCadastroLivro}>
          Novo
        </Button>
      </Box>

      <Box sx={{ flex: 1, width: '100%' }}>
        <DataGrid
          getRowId={(row) => row.uuid}
          rows={stateLocal.livros}
          columns={columns}
          initialState={{
            pagination: { paginationModel: { pageSize: 5 } },
          }}
          pageSizeOptions={[5]}
          disableRowSelectionOnClick
          checkboxSelection={false}
          sx={{
            '& .MuiDataGrid-cell': { whiteSpace: 'normal', outline: 'none' },
            '& .MuiDataGrid-columnHeader': { textOverflow: 'ellipsis', overflow: 'hidden' },
          }}
          localeText={{ noRowsLabel: 'Sem dados' }}
        />
      </Box>

      <Dialog open={stateLocal.openDialog} onClose={onCloseDialog}>
        <DialogTitle>{stateLocal.isEdit ? 'Atualizar Livro' : 'Cadastrar Livro'}</DialogTitle>
        <DialogContent>
          <DialogContentText>Preencha os dados abaixo para cadastrar ou atualizar um livro.</DialogContentText>

          <form
            id="livro-form"
            onSubmit={(event) => {
              event.preventDefault();
              if (stateLocal.isEdit && stateLocal.livroDTO.uuid) {
                updateLivro(stateLocal.livroDTO, stateLocal.livroDTO.uuid);
              } else {
                postLivro();
              }
            }}
          >
            <TextField
              autoFocus
              margin="dense"
              label="Título"
              fullWidth
              required
              value={stateLocal.livroDTO.titulo}
              onChange={(e) => onChangeField('titulo', e.target.value)}
            />
            <TextField
              margin="dense"
              label="Autor"
              fullWidth
              required
              value={stateLocal.livroDTO.autor}
              onChange={(e) => onChangeField('autor', e.target.value)}
            />
            <TextField
              margin="dense"
              label="ISBN"
              fullWidth
              type="number"
              required
              value={stateLocal.livroDTO.isbn}
              onChange={(e) => onChangeField('isbn', e.target.value)}
            />
            <TextField
              margin="dense"
              label="Categoria"
              fullWidth
              required
              value={stateLocal.livroDTO.categoria}
              onChange={(e) => onChangeField('categoria', e.target.value)}
            />

            <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="pt-br">
              <DatePicker
                label="Data de Publicação"
                value={
                  stateLocal.livroDTO.dataPublicacao ? dayjs(stateLocal.livroDTO.dataPublicacao, 'DD/MM/YYYY') : null
                }
                onChange={onChangeDataPublicacao}
                slotProps={{ textField: { fullWidth: true, margin: 'dense', required: true } }}
                format="DD/MM/YYYY"
              />
            </LocalizationProvider>
          </form>
        </DialogContent>

        <DialogActions>
          <Button onClick={onCloseDialog}>Cancelar</Button>
          <Button type="submit" form="livro-form" variant="contained" sx={{ borderRadius: '20px' }}>
            Salvar
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        open={stateLocal.snackBar.open}
        onClose={onCloseSnackBar}
        autoHideDuration={4000}
      >
        <Alert onClose={onCloseSnackBar} severity={stateLocal.snackBar.severity}>
          {stateLocal.snackBar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default Livro;
