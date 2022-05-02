package com.msapplicationbyprocess.service.processar;

import java.io.IOException;

import com.msapplicationbyprocess.gateway.json.CompraChaveJson;
import com.msapplicationbyprocess.gateway.json.CompraFinalizadaJson;
import com.msapplicationbyprocess.service.bank.BankService;
import com.msapplicationbyprocess.service.bank.PagamentoRetorno;

import org.apache.logging.log4j.message.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ListenerService {

    @Autowired
    private BankService bank;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${fila.entrada}")
    private String nomeFilaRepublicar;

    @Value("${fila.finalizado}")
    private String nomeFilaFinalizado;

    @HystrixCommand(fallbackMethod = "republicOnMessage")
    @RabbitListener(queues="${fila.entrada}")
    public void onMessage(Message message) throws JsonParseException, JsonMappingException, IOException  {

        String json = new String(message.getBody(), "UTF-8");

        System.out.println("Mensagem recebida:"+json);

        ObjectMapper mapper = new ObjectMapper();
        CompraChaveJson compraChaveJson = mapper.readValue(json, CompraChaveJson.class);

        PagamentoRetorno pg = bank.pagar(compraChaveJson);

        CompraFinalizadaJson compraFinalizadaJson = new CompraFinalizadaJson();
        compraFinalizadaJson.setCompraChaveJson(compraChaveJson);
        compraFinalizadaJson.setPagamentoOK(pg.isPagamentoOK());
        compraFinalizadaJson.setMensagem(pg.getMensagem());

        org.codehaus.jackson.map.ObjectMapper obj = new org.codehaus.jackson.map.ObjectMapper();
        String jsonFinalizado = obj.writeValueAsString(compraFinalizadaJson);

        rabbitTemplate.convertAndSend(nomeFilaFinalizado, jsonFinalizado);
    }

    public void republicOnMessage(Message message) throws JsonParseException, JsonMappingException, IOException  {
        System.out.println("Republicando mensagem...");
        rabbitTemplate.convertAndSend(nomeFilaRepublicar, message);
    }
}
