package sls.dynamodb.enhancedclient.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class HistoricoDTO {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data-insercao")
    private String dataInsercao;

    @JsonProperty("mensagem")
    private String mensagem;
}
