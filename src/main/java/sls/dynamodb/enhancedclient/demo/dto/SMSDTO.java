package sls.dynamodb.enhancedclient.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SMSDTO {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("id-cliente")
    private String idCliente;

    @JsonProperty("data-insercao")
    private String dataInsercao;

    @JsonProperty("sigla")
    private String sigla;

    @JsonProperty("historico")
    private List<HistoricoDTO> historico;
}
