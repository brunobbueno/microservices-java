package com.ms.applicationbuyfeedback.gateway.http;

import com.ms.applicationbuyfeedback.domain.CompraRedis;
import com.ms.applicationbuyfeedback.exceptions.NaoFinalizadoException;
import com.ms.applicationbuyfeedback.gateway.repository.CompraRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CompraController {

    @Autowired
    private CompraRedisRepository compraRedisRepository;

    @RequestMapping(path = "/{chave}", method = RequestMethod.GET)
    public CompraRedis status(@PathVariable("chave") String chave){

        Optional<CompraRedis> compra = compraRedisRepository.findById(chave);

        if( !compra.isPresent()){
            throw new NaoFinalizadoException();
        }

        return compra.get();
    }

    @RequestMapping(path = "/meunome", method = RequestMethod.GET)
    public String status(){
        return "Estou na máquina do: Marcelo";
    }
}
