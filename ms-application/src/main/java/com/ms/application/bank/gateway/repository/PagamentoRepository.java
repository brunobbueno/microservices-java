package com.ms.application.bank.gateway.repository;

import com.ms.application.bank.domain.Pagamento;
import org.springframework.data.repository.CrudRepository;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {
}
