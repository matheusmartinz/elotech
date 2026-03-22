import { TUsuario } from '../biblioteca/usuario.model';
import { TLivro } from '../livro/livro.model';

export enum Status {
  ATIVO = 'ATIVO',
  INATIVO = 'INATIVO',
}

export type TEmprestimo = {
  uuid: string;
  usuarioId: TUsuario;
  livroId: TLivro;
  dataEmprestimo: string;
  dataDevolucao: string | null;
  status: Status;
};
