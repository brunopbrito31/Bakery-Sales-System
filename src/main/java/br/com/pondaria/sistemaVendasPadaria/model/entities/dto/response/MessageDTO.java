package br.com.pondaria.sistemaVendasPadaria.model.entities.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {

    private String msg;

    public MessageDTO(String msg) {
        this.msg = msg;
    }
}
