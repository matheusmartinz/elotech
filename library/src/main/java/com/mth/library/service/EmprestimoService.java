package com.mth.library.service;

import com.mth.library.dto.AtualizarEmprestimoDTO;
import com.mth.library.dto.EmprestimoDTO;
import com.mth.library.exceptions.FailedConditional;
import com.mth.library.exceptions.IsNull;
import com.mth.library.model.Emprestimo;
import com.mth.library.model.Livro;
import com.mth.library.model.Status;
import com.mth.library.repository.EmprestimoRepository;
import com.mth.library.repository.LivroRepository;
import com.mth.library.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    public EmprestimoDTO createEmprestimo(EmprestimoDTO emprestimoDTO) {
        Livro livro = livroRepository.findByUuid(emprestimoDTO.getLivroDTO().getUuid());
        if (livro == null) {
            throw new IsNull("Livro não encontrado.");
        }

        Optional<Emprestimo> emprestimoAtivo = emprestimoRepository.findByLivroAndStatus(livro, Status.ATIVO);
        if (emprestimoAtivo.isPresent()) {
            throw new FailedConditional("Livro emprestado.");
        }

        if (emprestimoDTO.getDataEmprestimo().isAfter(LocalDate.now())) {
            throw new FailedConditional("Data de empréstimo não pode ser maior que o dia atual.");
        }

        Emprestimo emprestimo = new Emprestimo(emprestimoDTO, usuarioRepository, livroRepository);
        return new EmprestimoDTO(emprestimoRepository.save(emprestimo));
    }


    public List<EmprestimoDTO> getAllEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        return emprestimos.stream().map(EmprestimoDTO::new).toList();
    }

    public EmprestimoDTO updateEmprestimo(AtualizarEmprestimoDTO atualizarEmprestimoDTO, UUID uuidEmprestimo) {
        Emprestimo emprestimo = emprestimoRepository.findByUuid(uuidEmprestimo);
        if (emprestimo == null) {
            throw new IsNull("Empréstimo não encontrado.");
        }
        emprestimo.updateEmprestimo(atualizarEmprestimoDTO);

        return new EmprestimoDTO(emprestimoRepository.save(emprestimo));
    }

    public void deleteEmprestimo(UUID uuidEmprestimo) {
        Emprestimo emprestimo = emprestimoRepository.findByUuid(uuidEmprestimo);
        if (emprestimo == null) {
            throw new IsNull("Empréstimo não encontrado.");
        }
        emprestimoRepository.delete(emprestimo);
    }
}
