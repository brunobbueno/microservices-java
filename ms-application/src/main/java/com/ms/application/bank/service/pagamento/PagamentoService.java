package com.ms.application.bank.service.pagamento;

import javax.transaction.Transactional;

import com.ms.application.bank.domain.Pagamento;
import com.ms.application.bank.exceptions.PagamentoException;
import com.ms.application.bank.gateway.json.PagamentoJson;
import com.ms.application.bank.gateway.repository.PagamentoRepository;
import com.ms.application.bank.service.cartao.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private CartaoService cartaoService;

    @Transactional
    public void pagamento(PagamentoJson pagamentoJson){

        if( !cartaoService.isValido(pagamentoJson.getCodigoSegurancaCartao(), pagamentoJson.getNroCartao()) ){
            throw new PagamentoException("Cartão inválido.");
        }

        if( !cartaoService.isSaldoSuficiente(pagamentoJson.getCodigoSegurancaCartao(), pagamentoJson.getNroCartao(), pagamentoJson.getValorCompra()) ){
            throw new PagamentoException("Cartão não possui saldo suficiente.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setValorCompra(pagamentoJson.getValorCompra());
        pagamento.setCartao(cartaoService.getCartao(pagamentoJson.getCodigoSegurancaCartao(), pagamentoJson.getNroCartao()));

        pagamentoRepository.save(pagamento);

        cartaoService.atualizarSaldo(pagamentoJson.getCodigoSegurancaCartao(), pagamentoJson.getNroCartao(), pagamentoJson.getValorCompra());
    }
}
