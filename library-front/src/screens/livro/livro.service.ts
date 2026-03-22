import axios, { AxiosResponse } from 'axios';
import { TLivro } from './livro.model';

const LivroService = () => {
  const mainPathLivro = 'http://localhost:8080/livro';

  return {
    postLivro(livro: TLivro): Promise<AxiosResponse<TLivro>> {
      return axios.post(`${mainPathLivro}/cadastro`, livro);
    },

    getLivro(): Promise<AxiosResponse<Array<TLivro>>> {
      return axios.get(`${mainPathLivro}/listar`);
    },

    putLivro(livro: TLivro, uuidLivro: string): Promise<AxiosResponse<TLivro>> {
      return axios.put(`${mainPathLivro}/editar/${uuidLivro}`, livro);
    },

    deleteLivro(uuidLivro: string): Promise<AxiosResponse<string>> {
      return axios.delete(`${mainPathLivro}/excluir/${uuidLivro}`);
    },
  };
};

export default LivroService;
