import axios, { AxiosResponse } from 'axios';
import { TEmprestimo } from './emprestimo.model';

const EmprestimoService = () => {
  const mainPathUsusario = 'http://localhost:8080/emprestimo';

  return {
    postEmprestimo(usuario: TEmprestimo): Promise<AxiosResponse<TEmprestimo>> {
      return axios.post(`${mainPathUsusario}/cadastro`, usuario);
    },

    getEmprestimo(): Promise<AxiosResponse<Array<TEmprestimo>>> {
      return axios.get(`${mainPathUsusario}/listar`);
    },

    updateEmprestimo(emprestimo: TEmprestimo, uuidEmprestimo: string): Promise<AxiosResponse<TEmprestimo>> {
      return axios.put(`${mainPathUsusario}/editar/${uuidEmprestimo}`, emprestimo);
    },

    deleteEmprestimo(uuidEmprestimo: string): Promise<AxiosResponse<string>> {
      return axios.delete(`${mainPathUsusario}/excluir/${uuidEmprestimo}`);
    },
  };
};

export default EmprestimoService;
