import axios, { AxiosResponse } from 'axios';
import { TUsuario } from './usuario.model';

const UsuarioService = () => {
  const mainPathUsusario = 'http://localhost:8080/usuario';

  return {
    postUsuario(usuarioDTO: TUsuario): Promise<AxiosResponse<TUsuario>> {
      return axios.post(`${mainPathUsusario}/cadastro`, usuarioDTO);
    },

    getUsuarios(): Promise<AxiosResponse<Array<TUsuario>>> {
      return axios.get(`${mainPathUsusario}/listar`);
    },

    putUsuario(usuarioDTO: TUsuario, uuidUsuario: string): Promise<AxiosResponse<TUsuario>> {
      return axios.put(`${mainPathUsusario}/editar/${uuidUsuario}`, usuarioDTO);
    },

    deleteUsuario(uuidUsuario: string): Promise<AxiosResponse<string>> {
      return axios.delete(`${mainPathUsusario}/excluir/${uuidUsuario}`);
    },
  };
};

export default UsuarioService;
